package com.viniciusfinger.eazybank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
public class CardController {

    @GetMapping("/me")
    public String getMyCards(){
        return "My cards mock";
    }
}
