package com.example.ElastumTestTask;

import com.example.ElastumTestTask.service.coinbase.CoinbaseRatesInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElastumTestTaskApplication {

	@Autowired
    CoinbaseRatesInit coinbaseRatesInit;
	public static void main(String[] args) {


		SpringApplication.run(ElastumTestTaskApplication.class, args);
	}
}
