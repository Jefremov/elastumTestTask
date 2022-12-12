package com.example.ElastumTestTask.service;

import com.example.ElastumTestTask.entity.CoinbasePair;
import com.example.ElastumTestTask.entity.ExchangeRate;
import com.example.ElastumTestTask.repository.CoinbaseExchangeRatesRepo;
import com.example.ElastumTestTask.repository.CoinbaseRepository;
import com.example.ElastumTestTask.service.coinbase.CoinbaseRatesInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Component
public class ApiService {

    private final int CONNECTION_TIMEOUT = 540;


    CoinbaseRatesInit coinbaseRatesInit;
    CoinbaseRepository coinbaseRepository;
    CoinbaseExchangeRatesRepo coinbaseExchangeRatesRepo;

    @Autowired
    public ApiService(CoinbaseRatesInit coinbaseRatesInit, CoinbaseRepository coinbaseRepository, CoinbaseExchangeRatesRepo coinbaseExchangeRatesRepo) {
        this.coinbaseRatesInit = coinbaseRatesInit;
        this.coinbaseRepository = coinbaseRepository;
        this.coinbaseExchangeRatesRepo = coinbaseExchangeRatesRepo;
    }

    public String getPrice(String currency, String amount) throws IOException {


        List<ExchangeRate> exchangeRateList = coinbaseExchangeRatesRepo.findAllByCurrency(currency);
        BigDecimal amountDecimal = new BigDecimal(amount);
        return null;

    }

    public List<CoinbasePair> test() {
        return coinbaseRepository.findAll();
    }
}