package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Quota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotaRepository extends JpaRepository<Quota, Long> {

    // Obtener cuotas de una compra
    List<Quota> findByPurchaseId(Long purchaseId);

    // Obtener cuotas vencidas en un mes específico
    @Query(value = "SELECT q.* FROM quotas q WHERE q.year = :year AND q.month = :month", nativeQuery = true)
    List<Quota> findQuotasByMonthAndYear(@Param("year") String year, @Param("month") String month);

    // Obtener cantidad total de cuotas pendientes
    @Query(value = "SELECT COUNT(q.id) FROM quotas q", nativeQuery = true)
    Long countPendingQuotas();
}
