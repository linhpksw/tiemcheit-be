package com.tiemcheit.tiemcheitbe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @PostMapping("/add")
    public ResponseEntity<String> getAllOrder() {
        // just for test
        return new ResponseEntity<>("Hello world", HttpStatus.CREATED);
    }

}
