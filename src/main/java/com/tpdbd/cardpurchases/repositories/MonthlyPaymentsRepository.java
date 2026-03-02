package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.MonthlyPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyPaymentsRepository extends JpaRepository<MonthlyPayments, Long> {

    // Obtener compras en cuotas de un mes
    @Query(value = "SELECT mp.*" + "FROM purchases mp" + "WHERE mp.purchase_type = 'MONTHLY'" + "AND YEAR(mp.purchase_date) = :year" + "AND MONTH(mp.purchase_date) = :month", nativeQuery = true)
    List<MonthlyPayments> findMonthlyPaymentsByMonthAndYear(
            @Param("year") int year,
            @Param("month") int month
    );

    // Obtener compras con cuotas según cantidad
    @Query(value = "SELECT mp.*" + "FROM purchases mp" + "WHERE mp.purchase_type = 'MONTHLY'" + "AND mp.number_of_quotas = :numberOfQuotas", nativeQuery = true)
    List<MonthlyPayments> findByNumberOfQuotas(@Param("numberOfQuotas") int numberOfQuotas);

    // Obtener compras en cuotas con interés
    @Query(value = "SELECT mp.*" + "FROM purchases mp" + "WHERE mp.purchase_type = 'MONTHLY'" + "AND mp.interest > 0", nativeQuery = true)
    List<MonthlyPayments> findMonthlyPaymentsWithInterest();
}
