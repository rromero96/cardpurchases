package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, String> {

    // Obtener compras de una tarjeta
    List<Purchase> findByCardId(String cardId);

    // Obtener compras de un local (por CUIT)
    List<Purchase> findByCuitStore(String cuitStore);

    // Obtener información completa de una compra (con sus cuotas si las tiene)
    @Query("{ '_id': ?0 }")
    Optional<Purchase> findPurchaseWithDetails(@Param("purchaseId") String purchaseId);
}
