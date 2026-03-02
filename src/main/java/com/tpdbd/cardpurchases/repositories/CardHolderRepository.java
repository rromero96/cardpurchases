package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.CardHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardHolderRepository extends JpaRepository<CardHolder, Long> {

    // Búsqueda por nombre completo
    List<CardHolder> findByCompleteNameContainingIgnoreCase(String completeName);

    // Obtener top 10 titulares con mayor monto total en compras
    @Query(value = "SELECT ch.*, SUM(p.final_amount) as total_spent FROM cardholders ch LEFT JOIN cards c ON ch.id = c.cardholder_id LEFT JOIN purchases p ON c.id = p.card_id WHERE p.id IS NOT NULL GROUP BY ch.id ORDER BY total_spent DESC LIMIT 10", nativeQuery = true)
    List<CardHolder> findTop10ByTotalSpent();
}
