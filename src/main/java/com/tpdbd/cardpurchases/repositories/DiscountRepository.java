package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Discount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends MongoRepository<Discount, String> {

    // Obtener descuentos de un banco
    List<Discount> findByBankId(String bankId);

    // Obtener descuentos con tope máximo
    @Query("{ 'promotionType': 'DISCOUNT', 'maxDiscountAmount': { $exists: true, $ne: null } }")
    List<Discount> findDiscountsWithMaxAmount();

    // Obtener descuentos sin tope
    @Query("{ 'promotionType': 'DISCOUNT', 'maxDiscountAmount': { $exists: false } }")
    List<Discount> findDiscountsWithoutMaxAmount();
}
