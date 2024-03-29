package com.aravind.finance.controller;

import com.aravind.finance.exceptions.ExpenseException;
import com.aravind.finance.exceptions.UserIdException;
import com.aravind.finance.models.ExpenseModel;
import com.aravind.finance.repositories.ExpenseRepository;
import com.aravind.finance.services.MapValidationErrorService;
import com.aravind.finance.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/finance/expenses")
public class ExpenseItemController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @PostMapping("/add")
    public ResponseEntity<?> createNewExpense(@Valid @RequestBody ExpenseModel expenseModel,
                                              BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationErrorService(result);
        if(errorMap!=null) return errorMap;

        ExpenseModel expenseModel1 = expenseService.saveOrUpdateExpense(expenseModel);
        return new ResponseEntity<>(expenseModel1, HttpStatus.CREATED);
    }

    @GetMapping("/user-expense/{userId}")
    public RedirectView getExpensesOfUser(@PathVariable String userId, HttpServletResponse response) throws IOException {
        long userCount = expenseRepository.countByUserId(userId);
        RedirectView view = new RedirectView();
        view.setContextRelative(true);
        if(userCount==0){
            throw new UserIdException(userId+" Not Found");
        }
        else if(userCount==1){
            view.setUrl("/finance/expenses/user-single-expense/"+userId);
        }
        else{
            view.setUrl("/finance/expenses/user-expenses/"+userId);
        }
        return view;
    }

    @GetMapping("/user-single-expense/{userId}")
    public ResponseEntity<?> getSingleExpensesOfUser(@PathVariable String userId){
        ExpenseModel expenseModel = expenseService.getSingleExpenseOfUser(userId);
        return new ResponseEntity<>(expenseModel,HttpStatus.OK);
    }

    /**
     * If the userId has more than one purchases
     */
    @GetMapping("/user-expenses/{userId}")
    public Iterable<ExpenseModel> getAllExpensesOfUser(@PathVariable String userId){
        return expenseService.getAllExpensesOfUser(userId);
    }
    
    @GetMapping("/category-expense/{category}")
    public Iterable<ExpenseModel> getAllExpensesByCategory(@PathVariable String category){
        return expenseService.getAllExpensesByCategory(category);
    }

    @GetMapping("/subCategory-expense/{subCategory}")
    public Iterable<ExpenseModel> getAllExpensesBySubCategory(@PathVariable String subCategory){
        return expenseService.getAllExpensesBySubCategory(subCategory);
    }

    @GetMapping("/mode-expense/{mode}")
    public Iterable<ExpenseModel> getAllExpensesByPaymentMode(@PathVariable String mode){
        return expenseService.getAllExpensesByMode(mode);
    }

    @GetMapping("/get/{expenseId}")
    public ExpenseModel getExpenseById(@PathVariable int expenseId){
        ExpenseModel expenseByID = expenseService.getExpenseByID(expenseId);
        if(expenseByID == null){
            throw new ExpenseException("No expenses found");
        }
        return expenseByID;
    }

    //Delete the expense with its id
    @DeleteMapping("/deleteEx/{expenseId}")
    public ResponseEntity<?> deleteExpenseById(@PathVariable int expenseId){
        expenseService.deleteExpenseByID(expenseId);
        return new ResponseEntity<>("Expense with ID "+ expenseId +" is deleted", HttpStatus.OK);
    }

    //Delete the expenses of the user.
    @DeleteMapping("/deleteUs/{userId}")
    public ResponseEntity<?> deleteUserExpenses(@PathVariable String userId){
        List<Integer> expenses = expenseService.deleteUserExpenses(userId);
        return new ResponseEntity<>(String.format("User with ID %s expenses %s are deleted", userId, expenses),HttpStatus.OK);
    }
}
