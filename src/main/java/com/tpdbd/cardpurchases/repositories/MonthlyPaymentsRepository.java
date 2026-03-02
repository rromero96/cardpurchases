package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.MonthlyPayments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyPaymentsRepository extends MongoRepository<MonthlyPayments, String> {

    // Obtener compras en cuotas de un mes
    // Requiere que el servicio extraiga año y mes de purchaseDate
    @Query("{ 'purchaseType': 'MONTHLY', 'year': ?0, 'month': ?1 }")
    List<MonthlyPayments> findMonthlyPaymentsByMonthAndYear(
            @Param("year") int year,
            @Param("month") int month
    );

    // Obtener compras con cuotas según cantidad
    @Query("{ 'purchaseType': 'MONTHLY', 'numberOfQuotas': ?0 }")
    List<MonthlyPayments> findByNumberOfQuotas(@Param("numberOfQuotas") int numberOfQuotas);

    // Obtener compras en cuotas con interés
    @Query("{ 'purchaseType': 'MONTHLY', 'interest': { $gt: 0 } }")
    List<MonthlyPayments> findMonthlyPaymentsWithInterest();
}
