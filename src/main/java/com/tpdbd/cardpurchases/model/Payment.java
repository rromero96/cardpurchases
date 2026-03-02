package com.tpdbd.cardpurchases.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    
    private String code;
    
    private String month;
    
    private String year;
    
    private LocalDate firstExpiration;
    
    private LocalDate secondExpiration;
    
    private Float surcharge;
    
    private Float totalPrice;
    // Relación muchos a muchos: Un pago agrupa muchas cuotas
    @DBRef
    private List<Quota> quotas;
    // Relación muchos a muchos: Un pago agrupa muchas compras al contado
    @DBRef
    private List<CashPayment> cashPurchases;

    public Payment() {}
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
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