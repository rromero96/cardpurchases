package com.tpdbd.cardpurchases.model;
import org.springframework.data.mongodb.core.mapping.*;

@Document(collection = "purchases")
public class MonthlyPayments extends Purchase {
    
    private Float interest;
    
    private Integer numberOfQuotas;

    public MonthlyPayments() {}
    public Float getInterest() {
        return this.interest;
    }
    public void setInterest(Float interest) {
        this.interest = interest;
    }
    public Integer getNumberOfQuotas() {
        return this.numberOfQuotas;
    }
    public void setNumberOfQuotas(Integer numberOfQuotas) {
        this.numberOfQuotas = numberOfQuotas;
    }
}