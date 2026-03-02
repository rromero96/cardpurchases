package com.tpdbd.cardpurchases.controllers;

import com.tpdbd.cardpurchases.model.CashPayment;
import com.tpdbd.cardpurchases.model.MonthlyPayments;
import com.tpdbd.cardpurchases.model.Purchase;
import com.tpdbd.cardpurchases.services.PurchaseService;
import com.tpdbd.cardpurchases.services.PurchaseService.PurchaseDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }
    /**
     * POST /api/purchases/cash
     * Crear una compra al contado
     */
    @PostMapping("/cash")
    public ResponseEntity<CashPayment> createCashPayment(
            @RequestParam String cardId,
            @RequestParam String store,
            @RequestParam String cuitStore,
            @RequestParam Float amount,
            @RequestParam Float storeDiscount) {

        CashPayment purchase = purchaseService.createCashPayment(
                cardId, store, cuitStore, amount, storeDiscount
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }

    /**
     * POST /api/purchases/monthly
     * Crear una compra en cuotas
     */
    @PostMapping("/monthly")
    public ResponseEntity<MonthlyPayments> createMonthlyPurchase(
            @RequestParam String cardId,
            @RequestParam String store,
            @RequestParam String cuitStore,
            @RequestParam Float amount,
            @RequestParam Integer numberOfQuotas,
            @RequestParam Float interest,
            @RequestParam(required = false) String paymentVoucher) {

        MonthlyPayments purchase = purchaseService.createMonthlyPurchase(
                cardId, store, cuitStore, amount, numberOfQuotas, interest, paymentVoucher
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }

    /**
     * GET /api/purchases/{purchaseId}
     * Obtener información completa de una compra
     * Incluye cuotas si las tiene y promociones aplicadas
     */
    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDetails> getPurchaseDetails(
            @PathVariable String purchaseId) {

        PurchaseDetails details = purchaseService.getPurchaseDetails(purchaseId);
        return ResponseEntity.ok(details);
    }

    /**
     * GET /api/purchases/store-most-purchases
     * Obtener local con mayor cantidad de compras
     */
    @GetMapping("/store-most-purchases")
    public ResponseEntity<?> getStoreWithMostPurchases() {
        String store = purchaseService.getStoreWithMostPurchases();
        if (store == null || store.isEmpty()) {
            return ResponseEntity.ok(java.util.Map.of("store", "No hay compras registradas"));
        }
        return ResponseEntity.ok(java.util.Map.of("store", store));
    }

}
