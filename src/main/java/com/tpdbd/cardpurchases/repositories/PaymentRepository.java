package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    // Búsqueda por código
    Optional<Payment> findByCode(String code);

    // Obtener pagos de un mes específico
    @Query("{ 'year': ?0, 'month': ?1 }")
    List<Payment> findPaymentsByMonthAndYear(@Param("year") String year, @Param("month") String month);

    // Obtener detalles del pago (cuotas + compras al contado)
    @Query("{ '_id': ?0 }")
    Optional<Payment> getPaymentDetails(@Param("paymentId") String paymentId);
}
