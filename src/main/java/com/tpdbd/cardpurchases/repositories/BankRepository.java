package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends MongoRepository<Bank, String> {

    // Método gratis: findById(Long), findAll(), save(), delete(), etc.

    // Búsqueda por nombre
    Optional<Bank> findByName(String name);

    // Búsqueda por CUIT (Código de Identidad del banco)
    Optional<Bank> findByCuit(String cuit);
}
