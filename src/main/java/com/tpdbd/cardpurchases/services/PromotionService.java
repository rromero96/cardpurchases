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
    private final BankRepository bankRepository;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository, DiscountRepository discountRepository, BankRepository bankRepository) {
        this.promotionRepository = promotionRepository;
        this.discountRepository = discountRepository;
        this.bankRepository = bankRepository;
    }
    /**
     * Agregar una nueva promoción de descuento a un banco
     */
    @Transactional
    public Discount addDiscountPromotion(String bankId, String code, String promotionTitle,
                                         String nameStore, String cuitStore,
                                         LocalDate validityStartDate, LocalDate validityEndDate,
                                         Float discountPercentage, Float priceCap, Boolean onlyCash) {

        // Buscar el banco
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new IllegalArgumentException("Banco no encontrado"));

        // Crear el descuento
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

    /**
     * Obtener promociones disponibles de un local entre dos fechas
     */
    public List<Promotion> getAvailablePromotions(String cuitStore, LocalDate startDate, LocalDate endDate) {
        return promotionRepository.findAvailablePromotionsByStoreBetweenDates(cuitStore, startDate, endDate);
    }

    /**
     * Eliminar una promoción por código
     */
    @Transactional
    public void deletePromotionByCode(String code) {
        Promotion promotion = promotionRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Promoción no encontrada"));

        promotionRepository.deleteById(promotion.getId());
    }

}
