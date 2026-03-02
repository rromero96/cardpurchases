package com.tpdbd.cardpurchases.model;
import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
@Entity
@DiscriminatorValue("DISCOUNT")
public class Discount extends Promotion {
    @Column
    private Float discountPercentage;
    @Column
    private Float priceCap;
    @Column
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