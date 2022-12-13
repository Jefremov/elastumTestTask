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
    public String getPrice(@RequestBody RequestDto requestDto){
        try {
            return apiService.getPrice(requestDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}