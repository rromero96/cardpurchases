package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.*;
import com.tpdbd.cardpurchases.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public CashPayment createCashPayment(String cardId, String store, String cuitStore,
                                            Float amount, Float storeDiscount) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

        CashPayment purchase = new CashPayment();
        purchase.setCard(card);
        purchase.setStore(store);
        purchase.setCuitStore(cuitStore);
        purchase.setAmount(amount);
        purchase.setStoreDiscount(storeDiscount);

        // Calcular monto final: inicial - descuento
        Float finalAmount = amount - (amount * storeDiscount / 100);
        purchase.setFinalAmount(finalAmount);
        purchase.setValidPromotion(new ArrayList<>());

        return cashPurchaseRepository.save(purchase);
    }

    /**
     * Crear una compra en cuotas
     */
    @Transactional
    public MonthlyPayments createMonthlyPurchase(String cardId, String store, String cuitStore,
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
        purchase.setPaymentVoucher(paymentVoucher);
        // Calcular monto final: inicial + interés
        Float finalAmount = amount + (amount * interest / 100);
        purchase.setFinalAmount(finalAmount);
        purchase.setQuotas(new ArrayList<>());
        purchase.setValidPromotion(new ArrayList<>());

        return monthlyPaymentsRepository.save(purchase);
    }

    /**
     * Obtener información completa de una compra (incluyendo cuotas si las tiene)
     */
    public PurchaseDetails getPurchaseDetails(String purchaseId) {
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
        // Obtener todas las compras y agrupar por tienda
        List<Purchase> allPurchases = purchaseRepository.findAll();

        return allPurchases.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Purchase::getStore,
                        java.util.stream.Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse(null);
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
