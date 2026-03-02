package com.tpdbd.cardpurchases.model;
import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
@Entity
@DiscriminatorValue("FINANCING")
public class Financing extends Promotion {
    @Column
    private Integer numberOfQuotas;
    @Column
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