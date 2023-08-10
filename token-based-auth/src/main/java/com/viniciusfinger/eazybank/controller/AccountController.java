package com.viniciusfinger.eazybank.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @GetMapping
    public String getMyAccount(@RequestParam int id){
        System.out.println("ok");
        return "ok";
    }
}
