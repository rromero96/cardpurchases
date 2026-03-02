package com.tpdbd.cardpurchases.model;
import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
@Entity
@DiscriminatorValue("MONTHLY")
public class MonthlyPayments extends Purchase {
    @Column(nullable = false)
    private Float interest;
    @Column(nullable = false)
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