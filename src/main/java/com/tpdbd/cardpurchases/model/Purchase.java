package com.tpdbd.cardpurchases.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "purchases")
public abstract class Purchase {

    @Id
    private String id;

    
    private String paymentVoucher;

    
    private String store;

    
    private String cuitStore;

    
    private Float amount;

    
    private Float finalAmount;

    // Relación muchos a uno: Muchas compras usan UNA tarjeta
    @DBRef
    @JsonIgnore
    private Card card;

    // Relación muchos a muchos: Una compra puede tener muchas promociones
    @DBRef
    @JsonIgnore
    private List<Promotion> validPromotion;

    // Relación uno a muchos: Una compra tiene muchas cuotas
    @DBRef
    @JsonIgnore
    private List<Quota> quotas;

    public Purchase() {}
    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    public String getPaymentVoucher() { return this.paymentVoucher; }
    public void setPaymentVoucher(String paymentVoucher) { this.paymentVoucher = paymentVoucher; }
    public String getStore() { return this.store; }
    public void setStore(String store) { this.store = store; }
    public String getCuitStore() { return this.cuitStore; }
    public void setCuitStore(String cuitStore) { this.cuitStore = cuitStore; }
    public Float getAmount() { return this.amount; }
    public void setAmount(Float amount) { this.amount = amount; }
    public Float getFinalAmount() { return this.finalAmount; }
    public void setFinalAmount(Float finalAmount) { this.finalAmount = finalAmount; }
    public Card getCard() { return this.card; }
    public void setCard(Card card) { this.card = card; }
    public List<Promotion> getValidPromotion() { return this.validPromotion; }
    public void setValidPromotion(List<Promotion> validPromotion) { this.validPromotion = validPromotion; }
    public List<Quota> getQuotas() { return this.quotas; }
    public void setQuotas(List<Quota> quotas) { this.quotas = quotas; }
}
