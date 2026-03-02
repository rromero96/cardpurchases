package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {

    // Búsqueda por número de tarjeta
    Optional<Card> findByNumber(String number);

    // Obtener todas las tarjetas de un titular
    List<Card> findByCardholderId(String cardholderId);

    // Obtener todas las tarjetas de un banco
    List<Card> findByBankId(String bankId);

    // Obtener tarjetas emitidas hace más de 5 años
    // Compara fecha 'since' con 5 años atrás desde hoy
    List<Card> findCardsBySinceBefore(LocalDate fiveYearsAgo);

    // Obtener tarjetas próximas a vencer (en los próximos 3 meses)
    // Compara expirationDate entre hoy y hoy + 3 meses
    List<Card> findCardsByExpirationDateBetween(LocalDate startDate, LocalDate endDate);
}
