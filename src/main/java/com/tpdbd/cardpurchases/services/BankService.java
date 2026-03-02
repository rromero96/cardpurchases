package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.Bank;
import com.tpdbd.cardpurchases.repositories.BankRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BankService {

    private final BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
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
        return bankRepository.findBankWithMostPurchases()
                .orElseThrow(() -> new IllegalArgumentException("No hay bancos registrados"));
    }

    /**
     * Obtener número de clientes de cada banco
     */
    public Map<String, Long> getClientCountByBank() {
        Map<String, Long> result = new HashMap<>();

        List<Bank> allBanks = bankRepository.findAll();
        for (Bank bank : allBanks) {
            Long clientCount = bankRepository.countCardholdersByBank(bank.getId());
            result.put(bank.getName(), clientCount);
        }

        return result;
    }

}
