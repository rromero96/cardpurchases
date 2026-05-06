package com.tpdbd.cardpurchases.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "banks")
public class Bank {
    @Id
    private String id;
    private String name;
    private String cuit;
    private String address;
    private String telephone;
    private String direction;

    public Bank() {}
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCuit() {
        return this.cuit;
    }
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getTelephone() {
        return this.telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getDirection() {
        return this.direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

}
