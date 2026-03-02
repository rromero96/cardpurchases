package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.Bank;
import com.tpdbd.cardpurchases.model.Card;
import com.tpdbd.cardpurchases.repositories.BankRepository;
import com.tpdbd.cardpurchases.repositories.CardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BankService {

    private final BankRepository bankRepository;
    private final CardRepository cardRepository;

    @Autowired
    public BankService(BankRepository bankRepository, CardRepository cardRepository) {
        this.bankRepository = bankRepository;
        this.cardRepository = cardRepository;
    }
    /**
     * Crear un nuevo banco
     */
    @Transactional
    public Bank createBank(String name, String cuit, String address, String telephone, String direction) {
        Bank bank = new Bank();
        bank.setName(name);
        bank.setCuit(cuit);
        bank.setAddress(address);
        bank.setTelephone(telephone);
        bank.setDirection(direction);
        bank.setCards(new ArrayList<>());
        bank.setPromotions(new ArrayList<>());

        return bankRepository.save(bank);
    }

    /**
     * Obtener banco con mayor cantidad de compras
     */
    public Bank getBankWithMostPurchases() {
        // Obtener todas las tarjetas y agrupar por banco
        List<Card> allCards = cardRepository.findAll();

        return allCards.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Card::getBank,
                        java.util.stream.Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElseThrow(() -> new IllegalArgumentException("No hay bancos registrados"));
    }

    /**
     * Obtener número de clientes de cada banco
     */
    public Map<String, Long> getClientCountByBank() {
        Map<String, Long> result = new HashMap<>();

        // Obtener todas las tarjetas
        List<Card> allCards = cardRepository.findAll();

        // Agrupar por banco y contar cardholders únicos
        allCards.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Card::getBank,
                        java.util.stream.Collectors.mapping(
                                card -> card.getCardholder(),
                                java.util.stream.Collectors.toSet()
                        )
                ))
                .forEach((bank, cardholders) -> {
                    result.put(bank.getName(), (long) cardholders.size());
                });

        return result;
    }

}
