package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.CashPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashPaymentRepository extends JpaRepository<CashPayment, Long> {

    // Obtener compras al contado de un mes y año
    @Query(value = "SELECT cp.* FROM purchases cp WHERE cp.purchase_type = 'CASH' AND cp.month = :month AND cp.year = :year", nativeQuery = true)
    List<CashPayment> findCashPaymentsByMonthAndYear(@Param("year") String year, @Param("month") String month);

    // Obtener compras al contado con descuento
    @Query(value = "SELECT cp.* FROM purchases cp WHERE cp.purchase_type = 'CASH' AND cp.store_discount > 0", nativeQuery = true)
    List<CashPayment> findCashPaymentsWithDiscount();
}
