package com.tpdbd.cardpurchases.model;
import org.springframework.data.mongodb.core.mapping.*;

@Document(collection = "promotions")
public class Discount extends Promotion {
    
    private Float discountPercentage;
    
    private Float priceCap;
    
    private Boolean onlyCash;

    public Discount() {}
    public Float getDiscountPercentage() {
        return this.discountPercentage;
    }
    public void setDiscountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    public Float getPriceCap() {
        return this.priceCap;
    }
    public void setPriceCap(Float priceCap) {
        this.priceCap = priceCap;
    }
    public Boolean getOnlyCash() {
        return this.onlyCash;
    }
    public void setOnlyCash(Boolean onlyCash) {
        this.onlyCash = onlyCash;
    }
}