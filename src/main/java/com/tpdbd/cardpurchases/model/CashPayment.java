package com.tpdbd.cardpurchases.model;

import org.springframework.data.mongodb.core.mapping.*;

@Document(collection = "purchases")
public class CashPayment extends Purchase {


    private Float storeDiscount;

    public CashPayment() {}
    public Float getStoreDiscount() {
        return storeDiscount;
    }

    public void setStoreDiscount(Float storeDiscount) {
        this.storeDiscount = storeDiscount;
    }
}
