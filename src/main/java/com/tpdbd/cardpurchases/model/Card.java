package com.tpdbd.cardpurchases.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false)
    private String ccv;
    @Column(nullable = false)
    private String cardholderNameInCard;
    @Column(nullable = false)
    private LocalDate since;
    @Column(nullable = false)
    private LocalDate expirationDate;
    // Relación muchos a uno: Muchas tarjetas pertenecen a UN titular
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardholder_id", nullable = false)
    private CardHolder cardholder;
    // Relación muchos a uno: Muchas tarjetas pertenecen a UN banco
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;
    // Relación uno a muchos: Una tarjeta puede tener muchas compras
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;

    public Card() {}
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getCcv() {
        return this.ccv;
    }
    public void setCcv(String ccv) {
        this.ccv = ccv;
    }
    public String getCardholderNameInCard() {
        return this.cardholderNameInCard;
    }
    public void setCardholderNameInCard(String cardholderNameInCard) {
        this.cardholderNameInCard = cardholderNameInCard;
    }
    public LocalDate getSince() {
        return this.since;
    }
    public void setSince(LocalDate since) {
        this.since = since;
    }
    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
    public CardHolder getCardholder() {
        return this.cardholder;
    }
    public void setCardholder(CardHolder cardholder) {
        this.cardholder = cardholder;
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