package com.example.ElastumTestTask.service;


import com.example.ElastumTestTask.service.coinbase.CoinbaseRepoInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class InitRepos {


    private static Logger log = LoggerFactory.getLogger(InitRepos.class);

    CoinbaseRepoInit coinbaseRepoInit;

    @Autowired
    public InitRepos(CoinbaseRepoInit coinbaseRepoInit) {
        this.coinbaseRepoInit = coinbaseRepoInit;
    }

}
