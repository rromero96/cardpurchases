package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, String> {

    List<Purchase> findByCardId(String cardId);

    List<Purchase> findByCuitStore(String cuitStore);

    // Purchases que tienen esta promoción en su lista (para limpiar @DBRef antes de borrar)
    @Query("{ 'validPromotion.$id': { $oid: '?0' } }")
    List<Purchase> findByValidPromotionId(String promotionId);

}
