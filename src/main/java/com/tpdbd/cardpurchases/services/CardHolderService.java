package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.CardHolder;
import com.tpdbd.cardpurchases.model.Card;
import com.tpdbd.cardpurchases.model.Purchase;
import com.tpdbd.cardpurchases.repositories.CardHolderRepository;
import com.tpdbd.cardpurchases.repositories.CardRepository;
import com.tpdbd.cardpurchases.repositories.PurchaseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardHolderService {

    private final CardHolderRepository cardHolderRepository;
    private final CardRepository cardRepository;
    private final PurchaseRepository purchaseRepository;


    @Autowired
    public CardHolderService(CardHolderRepository cardHolderRepository,
                             CardRepository cardRepository,
                             PurchaseRepository purchaseRepository) {
        this.cardHolderRepository = cardHolderRepository;
        this.cardRepository = cardRepository;
        this.purchaseRepository = purchaseRepository;
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
        // Obtener todas las compras
        List<Purchase> allPurchases = purchaseRepository.findAll();

        // Agrupar por cardholder y calcular suma total
        return allPurchases.stream()
                .collect(Collectors.groupingBy(
                        purchase -> purchase.getCard().getCardholder(),
                        Collectors.summingDouble(Purchase::getAmount)
                ))
                .entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }
}
