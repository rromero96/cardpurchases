package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    // Búsqueda por código
    Optional<Promotion> findByCode(String code);

    // Obtener promociones de un banco
    List<Promotion> findByBankId(Long bankId);

    // Obtener promociones disponibles de un local entre dos fechas
    @Query(value = "SELECT p.* FROM promotions p WHERE p.cuit_store = :cuitStore AND p.validity_start_date <= :endDate AND p.validity_end_date >= :startDate", nativeQuery = true)
    List<Promotion> findAvailablePromotionsByStoreBetweenDates(
            @Param("cuitStore") String cuitStore,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Obtener promociones activas en una fecha específica
    @Query(value = "SELECT p.* FROM promotions p WHERE p.validity_start_date <= :date AND p.validity_end_date >= :date", nativeQuery = true)
    List<Promotion> findActivePromotionsByDate(@Param("date") LocalDate date);

    // Obtener promociones aplicadas a una compra
    @Query(value = "SELECT pr.* FROM promotions pr INNER JOIN purchase_promotion pp ON pr.id = pp.promotion_id WHERE pp.purchase_id = :purchaseId", nativeQuery = true)
    List<Promotion> findPromotionsByPurchaseId(@Param("purchaseId") Long purchaseId);
}
