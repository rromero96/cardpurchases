package com.tpdbd.cardpurchases.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CASH")
public class CashPayment extends Purchase {

    @Column
    private Float storeDiscount;

    public CashPayment() {
        super();
    }

    public Float getStoreDiscount() {
        return storeDiscount;
    }

    public void setStoreDiscount(Float storeDiscount) {
        this.storeDiscount = storeDiscount;
    }
}
