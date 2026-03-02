package com.tpdbd.cardpurchases.model;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "banks")
public class Bank {
    @Id
    private String id;
    private String name;
    private String cuit;
    private String address;
    private String telephone;
    private String direction;
    // Relación uno a muchos: Un banco tiene muchas tarjetas
    @DBRef
    @JsonIgnore
    private List<Card> cards;
    // Relación uno a muchos: Un banco tiene muchas promociones
    @DBRef
    @JsonIgnore
    private List<Promotion> promotions;

    public Bank() {}
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCuit() {
        return this.cuit;
    }
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getTelephone() {
        return this.telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getDirection() {
        return this.direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<Card> getCards() {
        return this.cards;
    }
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    public List<Promotion> getPromotions() {
        return this.promotions;
    }
    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }
}
