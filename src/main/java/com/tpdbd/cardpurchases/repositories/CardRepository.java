package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    // Búsqueda por número de tarjeta
    Optional<Card> findByNumber(String number);

    // Obtener todas las tarjetas de un titular
    List<Card> findByCardholderId(Long cardholderId);

    // Obtener todas las tarjetas de un banco
    List<Card> findByBankId(Long bankId);

    // Obtener tarjetas emitidas hace más de 5 años
    @Query(value = "SELECT c.* FROM cards c WHERE c.since < DATE_SUB(CURDATE(), INTERVAL 5 YEAR)", nativeQuery = true)
    List<Card> findCardsIssuedMoreThan5YearsAgo();

    // Obtener tarjetas próximas a vencer (en los próximos 3 meses)
    @Query(value = "SELECT c.* FROM cards c WHERE c.expiration_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 MONTH)", nativeQuery = true)
    List<Card> findCardsExpiringIn3Months();
}
