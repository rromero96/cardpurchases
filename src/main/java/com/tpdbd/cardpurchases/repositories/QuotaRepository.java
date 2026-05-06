package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Quota;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotaRepository extends MongoRepository<Quota, String> {

    // Obtener cuotas de un mes específico
    @Query("{ 'year': ?0, 'month': ?1 }")
    List<Quota> findQuotasByMonthAndYear(String year, String month);
}
