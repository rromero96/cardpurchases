package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Quota;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotaRepository extends MongoRepository<Quota, String> {

    // Obtener cuotas de una compra
    List<Quota> findByPurchaseId(String purchaseId);

    // Obtener cuotas vencidas en un mes específico
    @Query("{ 'year': ?0, 'month': ?1 }")
    List<Quota> findQuotasByMonthAndYear(@Param("year") String year, @Param("month") String month);
}
