package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.CardHolder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardHolderRepository extends MongoRepository<CardHolder, String> {

    // Búsqueda por nombre completo
    List<CardHolder> findByCompleteNameContainingIgnoreCase(String completeName);
}
