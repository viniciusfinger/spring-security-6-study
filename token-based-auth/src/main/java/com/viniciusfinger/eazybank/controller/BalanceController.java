package com.viniciusfinger.eazybank.controller;

import com.viniciusfinger.eazybank.model.AccountTransaction;
import com.viniciusfinger.eazybank.repository.AccountTransactionRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balances")
public class BalanceController {

    private final AccountTransactionRepository accountTransactionRepository;

    public BalanceController(AccountTransactionRepository accountTransactionRepository){
        this.accountTransactionRepository = accountTransactionRepository;
    }

    @GetMapping("/accounts/{id}")
    public List<AccountTransaction> getMyBalance(@PathVariable Integer id){
        return this.accountTransactionRepository.findByCustomerIdOrderByTransactionDtDesc(id);
    }
}
