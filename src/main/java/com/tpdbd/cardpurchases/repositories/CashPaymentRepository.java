package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.CashPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashPaymentRepository extends MongoRepository<CashPayment, String> {

    // Obtener compras al contado de un mes y año
    @Query("{ 'purchaseType': 'CASH', 'month': ?0, 'year': ?1 }")
    List<CashPayment> findCashPaymentsByMonthAndYear(@Param("year") String year, @Param("month") String month);

    // Obtener compras al contado con descuento
    @Query("{ 'purchaseType': 'CASH', 'storeDiscount': { $gt: 0 } }")
    List<CashPayment> findCashPaymentsWithDiscount();
}
