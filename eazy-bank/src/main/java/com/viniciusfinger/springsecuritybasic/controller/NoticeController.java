package com.viniciusfinger.springsecuritybasic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    @GetMapping("/me")
    public String getMyNotices(){
        return "My notices mock.";
    }
}
