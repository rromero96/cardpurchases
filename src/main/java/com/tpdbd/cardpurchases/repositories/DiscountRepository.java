package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    // Obtener descuentos de un banco
    List<Discount> findByBankId(Long bankId);

    // Obtener descuentos con tope máximo
    @Query(value = "SELECT d.*" + "FROM promotions d" + "WHERE d.promotion_type = 'DISCOUNT'" + "AND d.max_discount_amount IS NOT NULL", nativeQuery = true)
    List<Discount> findDiscountsWithMaxAmount();

    // Obtener descuentos sin tope
    @Query(value = "SELECT d.*" + "FROM promotions d" + "WHERE d.promotion_type = 'DISCOUNT'" + "AND d.max_discount_amount IS NULL", nativeQuery = true)
    List<Discount> findDiscountsWithoutMaxAmount();
}
