package com.viniciusfinger.eazybank.controller;

import com.viniciusfinger.eazybank.model.Notice;
import com.viniciusfinger.eazybank.repository.NoticeRepository;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeRepository noticeRepository;

    public NoticeController(NoticeRepository noticeRepository){
        this.noticeRepository = noticeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices(){
        List<Notice> activeNotices = noticeRepository.findAllActiveNotices();

        if (activeNotices != null){
           return ResponseEntity
                   .ok()
                   //Evita que o front refaça a mesma chamada dentro de 60s.
                   .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                   .body(activeNotices);
        }

        return ResponseEntity.noContent().build();
    }

}
