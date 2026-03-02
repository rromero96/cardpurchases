package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    // Obtener compras de una tarjeta
    List<Purchase> findByCardId(Long cardId);

    // Obtener compras de un local (por CUIT)
    List<Purchase> findByCuitStore(String cuitStore);

    // Obtener local con más compras registradas
    @Query(value = "SELECT p.store FROM purchases p GROUP BY p.store ORDER BY COUNT(p.id) DESC LIMIT 1", nativeQuery = true)
    String findStoreWithMostPurchases();

    // Obtener información completa de una compra (con sus cuotas si las tiene)
    @Query(value = "SELECT p.* FROM purchases p WHERE p.id = :purchaseId", nativeQuery = true)
    Optional<Purchase> findPurchaseWithDetails(@Param("purchaseId") Long purchaseId);
}
