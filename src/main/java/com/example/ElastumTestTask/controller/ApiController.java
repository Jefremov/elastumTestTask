package com.example.ElastumTestTask.controller;

import com.example.ElastumTestTask.dto.RequestDto;
import com.example.ElastumTestTask.entity.CoinbasePair;
import com.example.ElastumTestTask.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    ApiService apiService;

    @GetMapping("/best-price")
//    public String getPrice(@RequestParam(name = "currency", required = false)  String currency, @RequestParam(name = "amount", required = false)  String amount) throws IOException {
    public String getPrice(@RequestBody RequestDto requestDto) throws IOException {
        try {
            System.out.println("Controller: ");
            System.out.println("currency: " + requestDto.getCurrency());
            System.out.println("amount: " + requestDto.getAmount());
            return apiService.getPrice(requestDto.getCurrency(), requestDto.getAmount());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/test")
    public List<CoinbasePair> test(){
        return apiService.test();
    }



}