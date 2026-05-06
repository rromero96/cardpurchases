package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.*;
import com.tpdbd.cardpurchases.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final QuotaRepository quotaRepository;
    private final CashPaymentRepository cashPurchaseRepository;
    private final CardRepository cardRepository;


    @Autowired
    public PaymentService(PaymentRepository paymentRepository, QuotaRepository quotaRepository, CashPaymentRepository cashPurchaseRepository, CardRepository cardRepository) {
        this.paymentRepository = paymentRepository;
        this.quotaRepository = quotaRepository;
        this.cashPurchaseRepository = cashPurchaseRepository;
        this.cardRepository = cardRepository;
    }
    /**
     * Crear un nuevo pago
     */
    @Transactional
    public Payment createPayment(String code, String month, String year,
                                 LocalDate firstExpiration, LocalDate secondExpiration,
                                 Float surcharge, Float totalPrice) {
        Payment payment = new Payment();
        payment.setCode(code);
        payment.setMonth(month);
        payment.setYear(year);
        payment.setFirstExpiration(firstExpiration);
        payment.setSecondExpiration(secondExpiration);
        payment.setSurcharge(surcharge);
        payment.setTotalPrice(totalPrice);
        payment.setQuotas(new ArrayList<>());
        payment.setCashPayments(new ArrayList<>());
        return paymentRepository.save(payment);
    }

    /**
     * Editar las fechas de vencimiento de un pago
     */
    @Transactional
    public Payment editPaymentDueDates(String paymentCode, LocalDate newFirstExpiration,
                                       LocalDate newSecondExpiration,
                                       Float surcharge, Float totalPrice) {

        Payment payment = paymentRepository.findByCode(paymentCode)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));

        payment.setFirstExpiration(newFirstExpiration);
        payment.setSecondExpiration(newSecondExpiration);
        payment.setSurcharge(surcharge);
        payment.setTotalPrice(totalPrice);

        return paymentRepository.save(payment);
    }

    /**
     * Generar el total de pago de un mes dado
     * Incluye cuotas y compras al contado
     */
    public PaymentDetails generateMonthlyPayment(int year, String month) {
        PaymentDetails details = new PaymentDetails();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Sumar cuotas del mes
        List<Quota> quotas = quotaRepository.findQuotasByMonthAndYear(String.valueOf(year), month);
        for (Quota quota : quotas) {
            totalAmount = totalAmount.add(BigDecimal.valueOf(quota.getPrice()));
            details.addQuotaItem(quota);
        }

        // Sumar compras al contado del mes
        List<CashPayment> cashPurchases = cashPurchaseRepository.findByPurchaseYearAndPurchaseMonth(
                String.valueOf(year), month);
        for (CashPayment cp : cashPurchases) {
            totalAmount = totalAmount.add(BigDecimal.valueOf(cp.getFinalAmount()));
            details.addCashPaymentItem(cp);
        }

        details.setTotalAmount(totalAmount);
        details.setYear(year);
        details.setMonth(month);

        return details;
    }

    /**
     * Clase interna para detallar un pago
     */
    public static class PaymentDetails {
        private int year;
        private String month;
        private BigDecimal totalAmount;
        private List<Quota> quotaItems = new ArrayList<>();
        private List<CashPayment> cashPurchaseItems = new ArrayList<>();

        public void addQuotaItem(Quota quota) {
            this.quotaItems.add(quota);
        }

        public void addCashPaymentItem(CashPayment cashPurchase) {
            this.cashPurchaseItems.add(cashPurchase);
        }

        // Getters y setters
        public int getYear() { return year; }
        public void setYear(int year) { this.year = year; }

        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

        public List<Quota> getQuotaItems() { return quotaItems; }
        public List<CashPayment> getCashPaymentItems() { return cashPurchaseItems; }
    }
}
