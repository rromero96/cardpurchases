package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Financing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancingRepository extends MongoRepository<Financing, String> {

    List<Financing> findByBankId(String bankId);
}
