package com.viniciusfinger.springsecuritybasic.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @PostMapping
    public void saveContact(){
        //mock save
    }
}
