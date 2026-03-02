package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    // Método gratis: findById(Long), findAll(), save(), delete(), etc.

    // Búsqueda por nombre
    Optional<Bank> findByName(String name);

    // Búsqueda por CUIT (Código de Identidad del banco)
    Optional<Bank> findByCuit(String cuit);

    // Query personalizada: Obtener banco con más compras
    @Query(value = "SELECT b.*, COUNT(p.id) as purchase_count FROM banks b LEFT JOIN cards c ON b.id = c.bank_id LEFT JOIN purchases p ON c.id = p.card_id GROUP BY b.id ORDER BY purchase_count DESC LIMIT 1", nativeQuery = true)
    Optional<Bank> findBankWithMostPurchases();

    // Contar clientes por banco
    @Query(value = "SELECT COUNT(DISTINCT ch.id) FROM banks b LEFT JOIN cards c ON b.id = c.bank_id LEFT JOIN cardholders ch ON c.cardholder_id = ch.id WHERE b.id = :bankId", nativeQuery = true)
    Long countCardholdersByBank(@Param("bankId") Long bankId);
}
