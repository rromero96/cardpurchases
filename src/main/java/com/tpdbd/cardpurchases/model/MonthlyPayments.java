package com.tpdbd.cardpurchases.model;
import jakarta.persistence.*;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
@Entity
@DiscriminatorValue("MONTHLY")
public class MonthlyPayments extends Purchase {
    @Column
    private Float interest;
    @Column
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