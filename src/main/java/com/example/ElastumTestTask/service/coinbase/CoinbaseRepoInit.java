package com.example.ElastumTestTask.service.coinbase;

import com.example.ElastumTestTask.entity.CoinbasePair;
import com.example.ElastumTestTask.entity.ExchangeRate;
import com.example.ElastumTestTask.repository.CoinbaseExchangeRatesRepo;
import com.example.ElastumTestTask.repository.CoinbaseRepository;
import com.example.ElastumTestTask.service.InitRepos;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CoinbaseRepoInit {

    private static Logger log = LoggerFactory.getLogger(CoinbaseRepoInit.class);
    private CoinbaseRepository coinbaseRepository;
    private CoinbaseExchangeRatesRepo exchangeRatesRepo;
    private CoinbaseRepoInit coinbaseRepoInit;

    @Autowired
    public CoinbaseRepoInit(CoinbaseRepository coinbaseRepository, CoinbaseExchangeRatesRepo exchangeRatesRepo) {
        this.coinbaseRepository = coinbaseRepository;
        this.exchangeRatesRepo = exchangeRatesRepo;
    }

    private final int CONNECTION_TIMEOUT = 10000;

    @Scheduled(fixedDelayString = "${reposInitInterval}")
    @Async
    public void initCoinbaseRepo() {
        try {
            Properties prop = new Properties();
            prop.load(InitRepos.class.getClassLoader().getResourceAsStream("coinbase.properties"));
            final URL url = new URL(prop.getProperty("products.address"));
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setReadTimeout(CONNECTION_TIMEOUT);
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                final StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                JSONArray array = new JSONArray(content.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    if (object.getString("quote_currency").equals("USD") && object.getString("status").equals("online")) {
                        CoinbasePair pair = new CoinbasePair();
                        pair.setPairId(object.getString("id"));
                        pair.setBaseCurrency(object.getString("base_currency"));
                        pair.setQuoteCurrency(object.getString("quote_currency"));
                        pair.setPostOnly(Boolean.parseBoolean(String.valueOf(object.getBoolean("post_only"))));
                        pair.setLimitOnly(Boolean.parseBoolean(String.valueOf(object.getBoolean("limit_only"))));
                        pair.setCancelOnly(Boolean.parseBoolean(String.valueOf(object.getBoolean("cancel_only"))));
                        pair.setTradingDisabled(Boolean.parseBoolean(String.valueOf(object.getBoolean("trading_disabled"))));
                        pair.setAuctionMode(Boolean.parseBoolean(String.valueOf(object.getBoolean("auction_mode"))));
                        coinbaseRepository.save(pair);
                    }
                }
                log.info("Coinbase repo initialized at " + LocalDateTime.now());

            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
        }
    }


}
