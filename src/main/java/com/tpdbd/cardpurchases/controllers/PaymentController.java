package com.tpdbd.cardpurchases.controllers;

import com.tpdbd.cardpurchases.model.Payment;
import com.tpdbd.cardpurchases.services.PaymentService;
import com.tpdbd.cardpurchases.services.PaymentService.PaymentDetails;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    /**
     * PUT /api/payments/{code}/due-dates
     * Editar las fechas de vencimiento de un pago
     */
    @PutMapping("/{code}/due-dates")
    public ResponseEntity<Payment> editPaymentDueDates(
            @PathVariable String code,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newFirstExpiration,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newSecondExpiration,
            @RequestParam Float surcharge,
            @RequestParam Float totalPrice) {

        Payment payment = paymentService.editPaymentDueDates(
                code, newFirstExpiration, newSecondExpiration, surcharge, totalPrice
        );

        return ResponseEntity.ok(payment);
    }

    /**
     * GET /api/payments/month/{year}/{month}
     * Generar el total de pago de un mes dado
     * Retorna cuotas + compras al contado
     */
    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<PaymentDetails> getMonthlyPaymentDetails(
            @PathVariable int year,
            @PathVariable String month) {

        PaymentDetails details = paymentService.generateMonthlyPayment(year, month);
        return ResponseEntity.ok(details);
    }

}
