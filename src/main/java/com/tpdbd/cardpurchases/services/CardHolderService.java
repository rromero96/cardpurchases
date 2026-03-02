package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.CardHolder;
import com.tpdbd.cardpurchases.repositories.CardHolderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardHolderService {

    private final CardHolderRepository cardHolderRepository;


    @Autowired
    public CardHolderService(CardHolderRepository cardHolderRepository) {
        this.cardHolderRepository = cardHolderRepository;
    }
    /**
     * Crear un nuevo titular de tarjeta
     */
    @Transactional
    public CardHolder createCardHolder(String completeName, String dni, String cuil,
                                       String address, String telephone, LocalDate entryDate) {

        CardHolder cardHolder = new CardHolder();
        cardHolder.setCompleteName(completeName);
        cardHolder.setDni(dni);
        cardHolder.setCuil(cuil);
        cardHolder.setAddress(address);
        cardHolder.setTelephone(telephone);
        cardHolder.setEntryDate(entryDate);

        return cardHolderRepository.save(cardHolder);
    }

    /**
     * Obtener top 10 titulares con mayor monto total en compras
     */
    public List<CardHolder> getTop10CardholdersBySpending() {
        return cardHolderRepository.findTop10ByTotalSpent();
    }
}
