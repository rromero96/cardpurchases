package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.Bank;
import com.tpdbd.cardpurchases.repositories.BankRepository;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BankService {

    private final BankRepository bankRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public BankService(BankRepository bankRepository, MongoTemplate mongoTemplate) {
        this.bankRepository = bankRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional
    public Bank createBank(String name, String cuit, String address, String telephone, String direction) {
        Bank bank = new Bank();
        bank.setName(name);
        bank.setCuit(cuit);
        bank.setAddress(address);
        bank.setTelephone(telephone);
        bank.setDirection(direction);
        return bankRepository.save(bank);
    }

    /**
     * Obtener banco con mayor cantidad de compras usando aggregation pipeline en MongoDB.
     * Joins purchases → cards → agrupa por bank.$id → ordena desc → toma 1.
     */
    public Bank getBankWithMostPurchases() {
        Aggregation agg = Aggregation.newAggregation(
            Aggregation.lookup("cards", "card.$id", "_id", "cardDoc"),
            Aggregation.unwind("cardDoc"),
            Aggregation.group("cardDoc.bank.$id").count().as("purchaseCount"),
            Aggregation.sort(Sort.Direction.DESC, "purchaseCount"),
            Aggregation.limit(1)
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(agg, "purchases", Document.class);
        Document result = results.getUniqueMappedResult();
        if (result == null) {
            throw new IllegalArgumentException("No hay bancos registrados");
        }
        String bankId = result.get("_id").toString();
        return bankRepository.findById(bankId)
                .orElseThrow(() -> new IllegalArgumentException("Banco no encontrado"));
    }

    /**
     * Obtener número de clientes de cada banco usando aggregation MongoDB.
     * Por cada banco, cuenta cardholders únicos de sus tarjetas.
     */
    public Map<String, Long> getClientCountByBank() {
        Map<String, Long> result = new HashMap<>();
        List<Bank> banks = bankRepository.findAll();

        for (Bank bank : banks) {
            Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("bank.$id").is(new ObjectId(bank.getId()))),
                Aggregation.group("cardholder.$id")
            );
            long count = mongoTemplate.aggregate(agg, "cards", Document.class)
                    .getMappedResults().size();
            result.put(bank.getName(), count);
        }

        return result;
    }
}
