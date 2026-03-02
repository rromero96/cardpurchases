package com.tpdbd.cardpurchases.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;
@Document(collection = "cards")
public class Card {
    @Id
    private String id;
    
    private String number;
    
    private String ccv;
    
    private String cardholderNameInCard;
    
    private LocalDate since;
    
    private LocalDate expirationDate;
    // Relación muchos a uno: Muchas tarjetas pertenecen a UN titular
    @DBRef
    private CardHolder cardholder;
    // Relación muchos a uno: Muchas tarjetas pertenecen a UN banco
    @DBRef
    private Bank bank;
    // Relación uno a muchos: Una tarjeta puede tener muchas compras
    @DBRef
    private List<Purchase> purchases;

    public Card() {}
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
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