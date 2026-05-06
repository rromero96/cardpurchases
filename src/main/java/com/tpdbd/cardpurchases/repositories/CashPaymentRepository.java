package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.CashPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashPaymentRepository extends JpaRepository<CashPayment, Long> {

    // Obtener compras al contado de un mes y año
    List<CashPayment> findByPurchaseYearAndPurchaseMonth(String purchaseYear, String purchaseMonth);

    // Obtener compras al contado con descuento
    @Query(value = "SELECT cp.* FROM purchases cp WHERE cp.purchase_type = 'CASH' AND cp.store_discount > 0", nativeQuery = true)
    List<CashPayment> findCashPaymentsWithDiscount();
}
