package com.tpdbd.cardpurchases.model;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "promotions")
public abstract class Promotion {

    @Id
    private String id;

    
    private String code;

    
    private String promotionTitle;

    
    private String nameStore;

    
    private String cuitStore;

    
    private LocalDate validityStartDate;

    
    private LocalDate validityEndDate;

    
    private String comments;

    // Relación muchos a uno: Muchas promociones pertenecen a UN banco
    @DBRef
    @JsonIgnore
    private Bank bank;


    public Promotion() {}
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

}
