package com.aravind.finance.controller;

import com.aravind.finance.models.PurchaseModel;
import com.aravind.finance.services.MapValidationErrorService;
import com.aravind.finance.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/finance")
public class PurchaseItemController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewPurchase(@Valid @RequestBody PurchaseModel purchaseModel, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationErrorService(result);
        if(errorMap!=null) return errorMap;

        PurchaseModel purchaseModel1 = purchaseService.saveOrUpdatePurchase(purchaseModel);
        return new ResponseEntity<>(purchaseModel1, HttpStatus.CREATED);
    }

    /**
     * If the userId has only one purchase
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getSinglePurchaseByUserId(@PathVariable String userId){
        PurchaseModel purchaseModel = purchaseService.getSinglePurchase(userId);
        return new ResponseEntity<>(purchaseModel,HttpStatus.OK);
    }

    /**
     * If the userId has more than one purchases
     */
    @GetMapping("/all/{userId}")
    public Iterable<PurchaseModel> getAllPurchasesByUserId(@PathVariable String userId){
        return purchaseService.getAllPurchases(userId);
    }
}
