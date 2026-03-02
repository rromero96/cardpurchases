package com.tpdbd.cardpurchases.model;
import java.math.BigDecimal;
import jakarta.persistence.*;
@Entity
@Table(name = "quotas")
public class Quota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer number;
    @Column(nullable = false)
    private Float price;
    @Column(nullable = false)
    private String month;
    @Column(nullable = false)
    private String year;
    // Relación muchos a uno: Muchas cuotas pertenecen a UNA compra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    public Quota() {}
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getNumber() {
        return this.number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public Float getPrice() {
        return this.price;
    }
    public void setPrice(Float price) {
        this.price = price;
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
    public Purchase getPurchase() {
        return this.purchase;
    }
    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
}