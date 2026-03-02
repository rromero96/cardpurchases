package com.tpdbd.cardpurchases.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "promotions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "promotion_type")
public abstract class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String promotionTitle;

    @Column(nullable = false)
    private String nameStore;

    @Column(nullable = false)
    private String cuitStore;

    @Column(nullable = false)
    private LocalDate validityStartDate;

    @Column(nullable = false)
    private LocalDate validityEndDate;

    @Column
    private String comments;

    // Relación muchos a uno: Muchas promociones pertenecen a UN banco
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    @JsonIgnore
    private Bank bank;

    // Relación muchos a muchos (inverso): Una promoción aplica a muchas compras
    @ManyToMany(mappedBy = "validPromotion")
    @JsonIgnore
    private List<Purchase> purchases;


    public Promotion() {}
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
    public String getPromotionTitle() {
        return this.promotionTitle;
    }
    public void setPromotionTitle(String promotionTitle) {
        this.promotionTitle = promotionTitle;
    }
    public String getNameStore() {
        return this.nameStore;
    }
    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }
    public String getCuitStore() {
        return this.cuitStore;
    }
    public void setCuitStore(String cuitStore) {
        this.cuitStore = cuitStore;
    }
    public LocalDate getValidityStartDate() {
        return this.validityStartDate;
    }
    public void setValidityStartDate(LocalDate validityStartDate) {
        this.validityStartDate = validityStartDate;
    }
    public LocalDate getValidityEndDate() {
        return this.validityEndDate;
    }
    public void setValidityEndDate(LocalDate validityEndDate) {
        this.validityEndDate = validityEndDate;
    }
    public String getComments() {
        return this.comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public Bank getBank() {
        return this.bank;
    }
    public void setBank(Bank bank) {
        this.bank = bank;
    }
    public List<Purchase> getPurchases() {
        return this.purchases;
    }
    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }
}
