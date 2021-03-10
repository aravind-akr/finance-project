package com.aravind.finance.services;

import com.aravind.finance.exceptions.UserIdException;
import com.aravind.finance.models.PurchaseModel;
import com.aravind.finance.repositories.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.RequestDispatcher;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    RestTemplate restTemplate = new RestTemplate();

    public PurchaseModel saveOrUpdatePurchase(PurchaseModel itemModel){
        try{
            return purchaseRepository.save(itemModel);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public PurchaseModel getSinglePurchase(String userId){
        PurchaseModel purchaseModel = purchaseRepository.findByUserId(userId);;
        if (purchaseModel == null) {
                throw new UserIdException("User Id '" + userId + "' does not exist");
        }
        return purchaseModel;
    }

    public Iterable<PurchaseModel> getAllPurchases(String userId){
        return purchaseRepository.findAllByUserId(userId);

    }
}
