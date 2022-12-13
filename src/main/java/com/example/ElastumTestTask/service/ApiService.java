package com.example.ElastumTestTask.service;

import com.example.ElastumTestTask.dto.RequestDto;
import com.example.ElastumTestTask.entity.ExchangeRate;
import com.example.ElastumTestTask.repository.CoinbaseExchangeRatesRepo;
import com.example.ElastumTestTask.repository.CoinbaseRepository;
import com.example.ElastumTestTask.service.coinbase.CoinbaseRatesInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class ApiService {

    private final int CONNECTION_TIMEOUT = 540;

    private static Logger log = LoggerFactory.getLogger(ApiService.class);
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

    private String priceCalculating(List<ExchangeRate> exchangeRateList, BigDecimal amountDecimal) {

        Long start = System.currentTimeMillis();
        BigDecimal calculatedAmount = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);
        boolean calculated = false;
        for (ExchangeRate excR : exchangeRateList
        ) {
            BigDecimal amountValue = excR.getAmount();
            BigDecimal priceValue = excR.getPrice();
            calculatedAmount = calculatedAmount.add(amountValue);
            if (amountDecimal.compareTo(calculatedAmount) > 0) {
                totalPrice = totalPrice.add(amountValue.multiply(priceValue));
            } else {
                calculatedAmount = calculatedAmount.subtract(amountValue);
                BigDecimal decimal = amountDecimal.subtract(calculatedAmount);
                totalPrice = totalPrice.add(decimal.multiply(priceValue));
                calculated = true;
                break;
            }
        }
        if (calculated) {
            totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);
            Long finish = System.currentTimeMillis();
            log.info("Calculating time: " + (finish - start));
            return totalPrice.toString();
        } else {
            return "Error";
        }
    }
}