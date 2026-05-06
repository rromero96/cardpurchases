package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.*;
import com.tpdbd.cardpurchases.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CashPaymentRepository cashPurchaseRepository;
    private final MonthlyPaymentsRepository monthlyPaymentsRepository;
    private final QuotaRepository quotaRepository;
    private final CardRepository cardRepository;
    private final PromotionRepository promotionRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository,
                          CashPaymentRepository cashPurchaseRepository,
                          MonthlyPaymentsRepository monthlyPaymentsRepository,
                          QuotaRepository quotaRepository,
                          CardRepository cardRepository,
                          PromotionRepository promotionRepository) {
        this.purchaseRepository = purchaseRepository;
        this.cashPurchaseRepository = cashPurchaseRepository;
        this.monthlyPaymentsRepository = monthlyPaymentsRepository;
        this.quotaRepository = quotaRepository;
        this.cardRepository = cardRepository;
        this.promotionRepository = promotionRepository;
    }

    /**
     * Crear una compra al contado
     */
    @Transactional
    public CashPayment createCashPayment(Long cardId, String store, String cuitStore,
                                            Float amount, Float storeDiscount,
                                            String purchaseMonth, String purchaseYear) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

        CashPayment purchase = new CashPayment();
        purchase.setCard(card);
        purchase.setStore(store);
        purchase.setCuitStore(cuitStore);
        purchase.setAmount(amount);
        purchase.setStoreDiscount(storeDiscount);
        purchase.setPaymentVoucher(UUID.randomUUID().toString());

        LocalDate now = LocalDate.now();
        purchase.setPurchaseMonth(purchaseMonth != null ? purchaseMonth : String.format("%02d", now.getMonthValue()));
        purchase.setPurchaseYear(purchaseYear != null ? purchaseYear : String.valueOf(now.getYear()));

        Float finalAmount = amount - (amount * storeDiscount / 100);
        purchase.setFinalAmount(finalAmount);

        // Solo aplican Discounts al contado — Financing es exclusiva de compras en cuotas
        List<Promotion> allPromos = promotionRepository.findAvailablePromotionsByStoreBetweenDates(cuitStore, now, now);
        List<Promotion> applicable = allPromos != null
                ? allPromos.stream().filter(p -> !(p instanceof Financing)).collect(Collectors.toList())
                : new ArrayList<>();
        purchase.setValidPromotion(applicable);

        return cashPurchaseRepository.save(purchase);
    }

    /**
     * Crear una compra en cuotas
     */
    @Transactional
    public MonthlyPayments createMonthlyPurchase(Long cardId, String store, String cuitStore,
                                                  Float amount, Integer numberOfQuotas,
                                                  Float interest, String paymentVoucher) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

        MonthlyPayments purchase = new MonthlyPayments();
        purchase.setCard(card);
        purchase.setStore(store);
        purchase.setCuitStore(cuitStore);
        purchase.setAmount(amount);
        purchase.setNumberOfQuotas(numberOfQuotas);
        purchase.setInterest(interest);
        purchase.setPaymentVoucher(paymentVoucher != null ? paymentVoucher : UUID.randomUUID().toString());

        Float finalAmount = amount + (amount * interest / 100);
        purchase.setFinalAmount(finalAmount);

        LocalDate now = LocalDate.now();
        purchase.setPurchaseMonth(String.format("%02d", now.getMonthValue()));
        purchase.setPurchaseYear(String.valueOf(now.getYear()));

        // Discounts con onlyCash=false + máximo una Financing
        List<Promotion> allPromos2 = promotionRepository.findAvailablePromotionsByStoreBetweenDates(cuitStore, now, now);
        List<Promotion> applicable2 = new ArrayList<>();
        if (allPromos2 != null) {
            allPromos2.stream()
                    .filter(p -> p instanceof Discount && !((Discount) p).getOnlyCash())
                    .forEach(applicable2::add);
            allPromos2.stream()
                    .filter(p -> p instanceof Financing)
                    .findFirst()
                    .ifPresent(applicable2::add);
        }
        purchase.setValidPromotion(applicable2);
        purchase.setQuotas(new ArrayList<>());

        MonthlyPayments saved = monthlyPaymentsRepository.save(purchase);

        // Generar cuotas
        float quotaPrice = finalAmount / numberOfQuotas;
        LocalDate quotaDate = now;
        for (int i = 1; i <= numberOfQuotas; i++) {
            Quota quota = new Quota();
            quota.setNumber(i);
            quota.setPrice(quotaPrice);
            quota.setMonth(String.format("%02d", quotaDate.getMonthValue()));
            quota.setYear(String.valueOf(quotaDate.getYear()));
            quota.setPurchase(saved);
            quotaRepository.save(quota);
            quotaDate = quotaDate.plusMonths(1);
        }

        return saved;
    }

    /**
     * Obtener información completa de una compra (incluyendo cuotas si las tiene)
     */
    public PurchaseDetails getPurchaseDetails(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));

        PurchaseDetails details = new PurchaseDetails();
        details.setPurchase(purchase);

        // Si es compra en cuotas, obtener cuotas
        if (purchase instanceof MonthlyPayments) {
            MonthlyPayments mp = (MonthlyPayments) purchase;
            List<Quota> quotas = quotaRepository.findByPurchaseId(purchaseId);
            details.setQuotas(quotas);
        }

        // Obtener promociones aplicadas
        List<Promotion> promotions = promotionRepository.findPromotionsByPurchaseId(purchaseId);
        details.setAppliedPromotions(promotions);

        return details;
    }

    /**
     * Obtener local con mayor cantidad de compras
     */
    public String getStoreWithMostPurchases() {
        return purchaseRepository.findStoreWithMostPurchases();
    }

    /**
     * Clase interna para detallar una compra
     */
    public static class PurchaseDetails {
        private Purchase purchase;
        private List<Quota> quotas = new ArrayList<>();
        private List<Promotion> appliedPromotions = new ArrayList<>();

        public Purchase getPurchase() { return purchase; }
        public void setPurchase(Purchase purchase) { this.purchase = purchase; }

        public List<Quota> getQuotas() { return quotas; }
        public void setQuotas(List<Quota> quotas) { this.quotas = quotas; }

        public List<Promotion> getAppliedPromotions() { return appliedPromotions; }
        public void setAppliedPromotions(List<Promotion> appliedPromotions) {
            this.appliedPromotions = appliedPromotions;
        }
    }
}
