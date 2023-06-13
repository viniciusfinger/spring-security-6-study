package com.viniciusfinger.eazybank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balances")
public class BalanceController {


    @GetMapping("/me")
    public String getMyBalance(){
        return "My balance mock";
    }
}
