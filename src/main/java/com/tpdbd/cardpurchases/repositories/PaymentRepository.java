package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Búsqueda por código
    Optional<Payment> findByCode(String code);

    // Obtener pagos de un mes específico
    @Query("SELECT p FROM Payment p WHERE p.year = :year AND p.month = :month")
    List<Payment> findPaymentsByMonthAndYear(@Param("year") String year, @Param("month") String month);

    // Obtener total de pago de un mes (incluyendo cuotas y compras al contado)
    @Query("SELECT COALESCE(SUM(q.price), 0) + COALESCE(SUM(cp.amount), 0) FROM Payment p LEFT JOIN p.quotas q LEFT JOIN p.cashPurchases cp WHERE p.year = :year AND p.month = :month")
    Optional<Double> getTotalPaymentAmount(@Param("year") String year, @Param("month") String month);

    // Obtener detalles del pago (cuotas + compras al contado)
    @Query("SELECT p FROM Payment p WHERE p.id = :paymentId")
    Optional<Payment> getPaymentDetails(@Param("paymentId") Long paymentId);
}
