package com.tpdbd.cardpurchases.model;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "banks")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String cuit;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String telephone;
    @Column(nullable = false)
    private String direction;
    // Relación uno a muchos: Un banco tiene muchas tarjetas
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Card> cards;
    // Relación uno a muchos: Un banco tiene muchas promociones
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Promotion> promotions;

    public Bank() {}
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
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