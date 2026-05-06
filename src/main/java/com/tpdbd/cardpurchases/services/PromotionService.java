package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.*;
import com.tpdbd.cardpurchases.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final DiscountRepository discountRepository;
    private final FinancingRepository financingRepository;
    private final BankRepository bankRepository;
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository, DiscountRepository discountRepository,
                            FinancingRepository financingRepository, BankRepository bankRepository,
                            PurchaseRepository purchaseRepository) {
        this.promotionRepository = promotionRepository;
        this.discountRepository = discountRepository;
        this.financingRepository = financingRepository;
        this.bankRepository = bankRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional
    public Discount addDiscountPromotion(String bankId, String code, String promotionTitle,
                                         String nameStore, String cuitStore,
                                         LocalDate validityStartDate, LocalDate validityEndDate,
                                         Float discountPercentage, Float priceCap, Boolean onlyCash) {

        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new IllegalArgumentException("Banco no encontrado"));

        Discount discount = new Discount();
        discount.setCode(code);
        discount.setPromotionTitle(promotionTitle);
        discount.setNameStore(nameStore);
        discount.setCuitStore(cuitStore);
        discount.setValidityStartDate(validityStartDate);
        discount.setValidityEndDate(validityEndDate);
        discount.setBank(bank);
        discount.setDiscountPercentage(discountPercentage);
        discount.setPriceCap(priceCap);
        discount.setOnlyCash(onlyCash);

        return discountRepository.save(discount);
    }

    @Transactional
    public Financing addFinancingPromotion(String bankId, String code, String promotionTitle,
                                           String nameStore, String cuitStore,
                                           LocalDate validityStartDate, LocalDate validityEndDate,
                                           Integer numberOfQuotas, Float interest) {

        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new IllegalArgumentException("Banco no encontrado"));

        Financing financing = new Financing();
        financing.setCode(code);
        financing.setPromotionTitle(promotionTitle);
        financing.setNameStore(nameStore);
        financing.setCuitStore(cuitStore);
        financing.setValidityStartDate(validityStartDate);
        financing.setValidityEndDate(validityEndDate);
        financing.setBank(bank);
        financing.setNumberOfQuotas(numberOfQuotas);
        financing.setInterest(interest);

        return financingRepository.save(financing);
    }

    public List<Promotion> getAvailablePromotions(String cuitStore, LocalDate startDate, LocalDate endDate) {
        return promotionRepository.findAvailablePromotionsByStoreBetweenDates(cuitStore, startDate, endDate);
    }

    /**
     * Eliminar una promoción limpiando los @DBRef huérfanos en purchases antes de borrar.
     */
    @Transactional
    public void deletePromotionByCode(String code) {
        Promotion promotion = promotionRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Promoción no encontrada"));

        // Remover la referencia de todas las purchases que la tengan
        List<Purchase> affected = purchaseRepository.findByValidPromotionId(promotion.getId());
        for (Purchase purchase : affected) {
            purchase.getValidPromotion().removeIf(p -> p.getId().equals(promotion.getId()));
            purchaseRepository.save(purchase);
        }

        promotionRepository.delete(promotion);
    }
}
