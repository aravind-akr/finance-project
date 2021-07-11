package com.aravind.finance.controller;

import com.aravind.finance.models.ExpenseModel;
import com.aravind.finance.services.MapValidationErrorService;
import com.aravind.finance.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/finance/expenses")
public class ExpenseItemController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/add")
    public ResponseEntity<?> createNewPurchase(@Valid @RequestBody ExpenseModel expenseModel,
                                               BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationErrorService(result);
        if(errorMap!=null) return errorMap;

        ExpenseModel expenseModel1 = expenseService.saveOrUpdateExpense(expenseModel);
        return new ResponseEntity<>(expenseModel1, HttpStatus.CREATED);
    }

    /**
     * If the userId has only one purchase
     * @param userId
     * @return
     */
    @GetMapping("/user-single-expense/{userId}")
    public ResponseEntity<?> getSinglePurchaseByUserId(@PathVariable String userId){
        ExpenseModel expenseModel = expenseService.getSingleExpenseOfUser(userId);
        return new ResponseEntity<>(expenseModel,HttpStatus.OK);
    }

    /**
     * If the userId has more than one purchases
     */
    @GetMapping("/user-expenses/{userId}")
    public Iterable<ExpenseModel> getAllPurchasesByUserId(@PathVariable String userId){
        return expenseService.getAllExpensesOfUser(userId);
    }
    
    @GetMapping("/category-expense/{category}")
    public Iterable<ExpenseModel> getAllPurchasesByCategory(@PathVariable String category){
        return expenseService.getAllExpensesByCategory(category);
    }

    @GetMapping("/subCategory-expense/{subCategory}")
    public Iterable<ExpenseModel> getAllPurchasesBySubCategory(@PathVariable String subCategory){
        return expenseService.getAllExpensesBySubCategory(subCategory);
    }

    @GetMapping("/mode-expense/{mode}")
    public Iterable<ExpenseModel> getAllPurchasesByPaymentMode(@PathVariable String mode){
        return expenseService.getAllExpensesByMode(mode);
    }
}
