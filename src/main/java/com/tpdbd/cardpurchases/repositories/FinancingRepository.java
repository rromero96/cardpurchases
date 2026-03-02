package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Financing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancingRepository extends MongoRepository<Financing, String> {

    // Obtener financiamientos de un banco
    List<Financing> findByBankId(String bankId);

    // Obtener financiamientos según número de cuotas
    @Query("{ 'promotionType': 'FINANCING', 'numberOfQuotas': ?0 }")
    List<Financing> findByNumberOfQuotas(@Param("numberOfQuotas") int numberOfQuotas);

    // Obtener financiamientos con baja tasa de interés (< 5%)
    @Query("{ 'promotionType': 'FINANCING', 'interestRate': { $lt: 5 } }")
    List<Financing> findLowInterestRateFinancings();
}
