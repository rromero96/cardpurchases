package com.tpdbd.cardpurchases.model;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "cardholders")
public class CardHolder {
    @Id
    private String id;

    
    private String completeName;

    
    private String dni;

    
    private String cuil;

    
    private String address;

    
    private String telephone;

    
    private LocalDate entryDate;

    // Relación uno a muchos: Un titular tiene muchas tarjetas
    @DBRef
    @JsonIgnore
    private List<Card> cards;

    public CardHolder() {}
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompleteName() {
        return this.completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getDni() {
        return this.dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCuil() {
        return this.cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
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

    public LocalDate getEntryDate() {
        return this.entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}