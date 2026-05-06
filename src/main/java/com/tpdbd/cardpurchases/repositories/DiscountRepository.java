package com.tpdbd.cardpurchases.repositories;

import com.tpdbd.cardpurchases.model.Discount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends MongoRepository<Discount, String> {

    List<Discount> findByBankId(String bankId);
}
