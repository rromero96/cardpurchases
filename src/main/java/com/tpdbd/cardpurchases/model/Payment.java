package com.tpdbd.cardpurchases.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String month;
    @Column(nullable = false)
    private String year;
    @Column(nullable = false)
    private LocalDate firstExpiration;
    @Column(nullable = false)
    private LocalDate secondExpiration;
    @Column(nullable = false)
    private Float surcharge;
    @Column(nullable = false)
    private Float totalPrice;
    // Relación muchos a muchos: Un pago agrupa muchas cuotas
    @ManyToMany
    @JoinTable(
            name = "payment_quota",
            joinColumns = @JoinColumn(name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "quota_id")
    )
    private List<Quota> quotas;
    // Relación muchos a muchos: Un pago agrupa muchas compras al contado
    @ManyToMany
    @JoinTable(
            name = "payment_cash",
            joinColumns = @JoinColumn(name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "purchase_id")
    )
    private List<CashPayment> cashPurchases;

    public Payment() {}
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMonth() {
        return this.month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public String getYear() {
        return this.year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public LocalDate getFirstExpiration() {
        return this.firstExpiration;
    }
    public void setFirstExpiration(LocalDate firstExpiration) {
        this.firstExpiration = firstExpiration;
    }
    public LocalDate getSecondExpiration() {
        return this.secondExpiration;
    }
    public void setSecondExpiration(LocalDate secondExpiration) {
        this.secondExpiration = secondExpiration;
    }
    public Float getSurcharge() {
        return this.surcharge;
    }
    public void setSurcharge(Float surcharge) {
        this.surcharge = surcharge;
    }
    public Float getTotalPrice() {
        return this.totalPrice;
    }
    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Quota> getQuotas() {
        return this.quotas;
    }
    public void setQuotas(List<Quota> quotas) {
        this.quotas = quotas;
    }
    public List<CashPayment> getCashPayments() {
        return this.cashPurchases;
    }
    public void setCashPayments(List<CashPayment> cashPurchases) {
        this.cashPurchases = cashPurchases;
    }
}