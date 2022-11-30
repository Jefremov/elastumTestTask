package com.example.ElastumTestTask.controller;

import com.example.ElastumTestTask.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    ApiService apiService;

    @GetMapping("/best-price")
//    public String getPrice(@PathVariable("currency") String currency, @PathVariable("amount") String amount) throws IOException {
    public String getPrice() throws IOException {
        try {
//            return apiService.getPrice(currency, amount);
            return apiService.getPrice();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}