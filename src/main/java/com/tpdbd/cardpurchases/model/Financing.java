package com.tpdbd.cardpurchases.model;
import org.springframework.data.mongodb.core.mapping.*;

@Document(collection = "promotions")
public class Financing extends Promotion {
    
    private Integer numberOfQuotas;
    
    private Float interest;

    public Financing() {}
    public Integer getNumberOfQuotas() {
        return this.numberOfQuotas;
    }
    public void setNumberOfQuotas(Integer numberOfQuotas) {
        this.numberOfQuotas = numberOfQuotas;
    }
    public Float getInterest() {
        return this.interest;
    }
    public void setInterest(Float interest) {
        this.interest = interest;
    }
}