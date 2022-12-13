package com.example.ElastumTestTask.service;

import com.example.ElastumTestTask.dto.RequestDto;
import com.example.ElastumTestTask.entity.ExchangeRate;
import com.example.ElastumTestTask.repository.CoinbaseExchangeRatesRepo;
import com.example.ElastumTestTask.repository.CoinbaseRepository;
import com.example.ElastumTestTask.service.coinbase.CoinbaseRatesInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public String getPrice(RequestDto requestDto) throws IOException {

        String currency = requestDto.getCurrency();
        String amount = requestDto.getAmount();
        List<ExchangeRate> exchangeRateList = coinbaseExchangeRatesRepo.findAllByCurrency(currency);
        BigDecimal amountDecimal = new BigDecimal(amount);
        return priceCalculating(exchangeRateList, amountDecimal);

    }
    private String priceCalculating(List<ExchangeRate> exchangeRateList, BigDecimal amountDecimal){

        BigDecimal calculatedAmount = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);
        for (ExchangeRate excR: exchangeRateList
             ) {
            BigDecimal amountValue = excR.getAmount();
            BigDecimal priceValue = excR.getPrice();
            calculatedAmount = calculatedAmount.add(amountValue);
            if(amountDecimal.compareTo(calculatedAmount) > 0){
                totalPrice = totalPrice.add(amountValue.multiply(priceValue));
            } else{
                calculatedAmount = calculatedAmount.subtract(amountValue);
                BigDecimal decimal = amountDecimal.subtract(calculatedAmount);
                totalPrice = totalPrice.add(decimal.multiply(priceValue));
                break;
            }
        }
        totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);
        return totalPrice.toString();
    }
}