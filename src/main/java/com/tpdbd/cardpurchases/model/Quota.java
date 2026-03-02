package com.tpdbd.cardpurchases.model;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;
@Document(collection = "quotas")
public class Quota {
    @Id
    private String id;
    
    private Integer number;
    
    private Float price;
    
    private String month;
    
    private String year;
    // Relación muchos a uno: Muchas cuotas pertenecen a UNA compra
    @DBRef
    private Purchase purchase;

    public Quota() {}
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
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