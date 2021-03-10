package com.aravind.finance.repositories;

import com.aravind.finance.models.PurchaseModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends CrudRepository<PurchaseModel,Long> {

    Iterable<PurchaseModel> findAllByUserId(String userId);

    PurchaseModel findByUserId(String userId);
    long countByUserId(String userId);
}
