package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.CardHolder;
import com.tpdbd.cardpurchases.repositories.CardHolderRepository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardHolderService {

    private final CardHolderRepository cardHolderRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CardHolderService(CardHolderRepository cardHolderRepository, MongoTemplate mongoTemplate) {
        this.cardHolderRepository = cardHolderRepository;
        this.mongoTemplate = mongoTemplate;
    }

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
     * Top 10 titulares por gasto total usando MongoDB aggregation pipeline.
     * Evita traer todas las compras a memoria (purchaseRepository.findAll()).
     */
    public List<CardHolder> getTop10CardholdersBySpending() {
        Aggregation agg = Aggregation.newAggregation(
            Aggregation.lookup("cards", "card.$id", "_id", "cardDoc"),
            Aggregation.unwind("cardDoc"),
            Aggregation.group("cardDoc.cardholder.$id").sum("finalAmount").as("totalSpent"),
            Aggregation.sort(Sort.Direction.DESC, "totalSpent"),
            Aggregation.limit(10)
        );

        List<String> ids = mongoTemplate
            .aggregate(agg, "purchases", Document.class)
            .getMappedResults()
            .stream()
            .map(d -> d.get("_id").toString())
            .collect(Collectors.toList());

        return ids.stream()
            .map(cardHolderRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
}
