package com.tpdbd.cardpurchases.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "purchases")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "purchase_type")
public abstract class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentVoucher;

    @Column(nullable = false)
    private String store;

    @Column(nullable = false)
    private String cuitStore;

    @Column(nullable = false)
    private Float amount;

    @Column(nullable = false)
    private Float finalAmount;

    // Relación muchos a uno: Muchas compras usan UNA tarjeta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    @JsonIgnore
    private Card card;

    // Relación muchos a muchos: Una compra puede tener muchas promociones
    @ManyToMany
    @JoinTable(
            name = "purchase_promotion",
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    @JsonIgnore
    private List<Promotion> validPromotion;

    // Relación uno a muchos: Una compra tiene muchas cuotas
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Quota> quotas;


    public Purchase() {}
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
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
