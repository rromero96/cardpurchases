package com.tpdbd.cardpurchases.controllers;

import com.tpdbd.cardpurchases.model.Discount;
import com.tpdbd.cardpurchases.model.Promotion;
import com.tpdbd.cardpurchases.services.PromotionService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }
    /**
     * POST /api/promotions/discount
     * Agregar una nueva promoción de tipo descuento
     */
    @PostMapping("/discount")
    public ResponseEntity<Discount> addDiscountPromotion(
            @RequestParam String bankId,
            @RequestParam String code,
            @RequestParam String promotionTitle,
            @RequestParam String nameStore,
            @RequestParam String cuitStore,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityStartDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityEndDate,
            @RequestParam Float discountPercentage,
            @RequestParam(required = false) Float priceCap,
            @RequestParam(required = false) Boolean onlyCash) {

        Discount discount = promotionService.addDiscountPromotion(
                bankId, code, promotionTitle, nameStore, cuitStore,
                validityStartDate, validityEndDate,
                discountPercentage, priceCap, onlyCash != null ? onlyCash : false
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(discount);
    }

    /**
     * GET /api/promotions/available
     * Obtener promociones disponibles de un local entre dos fechas
     */
    @GetMapping("/available")
    public ResponseEntity<List<Promotion>> getAvailablePromotions(
            @RequestParam String cuitStore,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Promotion> promotions = promotionService.getAvailablePromotions(cuitStore, startDate, endDate);
        return ResponseEntity.ok(promotions);
    }

    /**
     * DELETE /api/promotions/{code}
     * Eliminar una promoción por código
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deletePromotionByCode(@PathVariable String code) {
        promotionService.deletePromotionByCode(code);
        return ResponseEntity.noContent().build();
    }

}
