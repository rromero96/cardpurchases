package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends MongoRepository<Promotion, String> {

    // Búsqueda por código
    Optional<Promotion> findByCode(String code);

    // Obtener promociones de un banco
    List<Promotion> findByBankId(String bankId);

    // Obtener promociones disponibles de un local entre dos fechas
    @Query("{ 'cuitStore': ?0, 'validityStartDate': { $lte: ?2 }, 'validityEndDate': { $gte: ?1 } }")
    List<Promotion> findAvailablePromotionsByStoreBetweenDates(
            String cuitStore, LocalDate startDate, LocalDate endDate
    );

    // Obtener promociones activas en una fecha específica
    @Query("{ 'validityStartDate': { $lte: ?0 }, 'validityEndDate': { $gte: ?0 } }")
    List<Promotion> findActivePromotionsByDate(LocalDate date);
}
