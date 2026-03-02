package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Financing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancingRepository extends JpaRepository<Financing, Long> {

    // Obtener financiamientos de un banco
    List<Financing> findByBankId(Long bankId);

    // Obtener financiamientos según número de cuotas
    @Query(value = "SELECT f.*" + "FROM promotions f" + "WHERE f.promotion_type = 'FINANCING'" + "AND f.number_of_quotas = :numberOfQuotas", nativeQuery = true)
    List<Financing> findByNumberOfQuotas(@Param("numberOfQuotas") int numberOfQuotas);

    // Obtener financiamientos con baja tasa de interés (< 5%)
    @Query(value = "SELECT f.*" + "FROM promotions f" + "WHERE f.promotion_type = 'FINANCING'" + "AND f.interest_rate < 5", nativeQuery = true)
    List<Financing> findLowInterestRateFinancings();
}
