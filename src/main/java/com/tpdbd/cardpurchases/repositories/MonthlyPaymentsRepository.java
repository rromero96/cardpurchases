package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.MonthlyPayments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyPaymentsRepository extends MongoRepository<MonthlyPayments, String> {

}
