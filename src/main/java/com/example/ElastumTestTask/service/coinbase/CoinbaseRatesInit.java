package com.example.ElastumTestTask.service.coinbase;

import com.example.ElastumTestTask.entity.ExchangeRate;
import com.example.ElastumTestTask.repository.CoinbaseExchangeRatesRepo;
import com.example.ElastumTestTask.repository.CoinbaseRepository;
import com.example.ElastumTestTask.service.InitRepos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;


@Component
public class CoinbaseRatesInit {

    private final int CONNECTION_TIMEOUT = 10000;
    private static Logger log = LoggerFactory.getLogger(CoinbaseRatesInit.class);

    private CoinbaseExchangeRatesRepo exchangeRatesRepo;
    private CoinbaseRepository coinbaseRepository;
    private CoinbaseRepoInit coinbaseRepoInit;

    @Autowired
    public CoinbaseRatesInit(CoinbaseExchangeRatesRepo exchangeRatesRepo, CoinbaseRepository coinbaseRepository, CoinbaseRepoInit coinbaseRepoInit) {
        this.exchangeRatesRepo = exchangeRatesRepo;
        this.coinbaseRepository = coinbaseRepository;
        this.coinbaseRepoInit = coinbaseRepoInit;
    }

//    @Scheduled(fixedDelayString = "${currencyGettingInterval}")
//    @Async
//    public void getRates() {
//
//        int threadsCount = 2;
//        List<CoinbasePair> pairList = coinbaseRepository.findAll();
//        ExecutorService pool = Executors.newFixedThreadPool(threadsCount);
//        final BlockingQueue<CoinbasePair> queue = new ArrayBlockingQueue<>(pairList.size());
//        queue.addAll(pairList);
//        for (int i = 1; i <= threadsCount; i++) {
//            Runnable r = () -> {
//                String currency;
//                while ((currency = Objects.requireNonNull(queue.poll()).getBaseCurrency()) != null) {
//                    getRatesByCurrency(currency);
//                    log.info("Getting rates for " + currency);
//                }
//            };
//            pool.execute(r);
//        }
//        log.info("Exchange rates received at " + LocalDateTime.now());
//    }

    @Scheduled(fixedDelayString = "${currencyGettingInterval}")
    @Async
    @Transactional
    public void getRates() {
        Long start = System.currentTimeMillis();
        List<String> currencys = new ArrayList<>();
        currencys.add("BTC");
//        currencys.add("ETH");
//        currencys.add("USDT");
        currencys.forEach(currency -> {
            //   new Thread(() -> getRatesByCurrency(currency)).start();
            getRatesByCurrency(currency);
            log.info("Getting rates for " + currency);
        });

        log.info("Exchange rates received at " + LocalDateTime.now());
        Long finish = System.currentTimeMillis();
        log.info("getRates: " + (finish - start));
    }

    public void getRatesByCurrency(String currency) {
        try {
            Long start = System.currentTimeMillis();
            Properties prop = new Properties();
            prop.load(InitRepos.class.getClassLoader().getResourceAsStream("coinbase.properties"));
            StringBuilder urlString = new StringBuilder(prop.getProperty("products.address")).append(currency).append("-USD/book?level=2");
            final URL url = new URL(urlString.toString());
            log.info("Connecting to " + url);
            Long connectionStart = System.currentTimeMillis();
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setReadTimeout(CONNECTION_TIMEOUT);
            Long connectionFinish = System.currentTimeMillis();
            if (exchangeRatesRepo.existsByCurrency(currency)) {
                exchangeRatesRepo.removeByCurrency(currency);
            }
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                final StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                String asks = content.toString().split("asks\":\\[\\[")[1];
                asks = asks.substring(0, asks.indexOf("]],"));
                List<String> asksList = Arrays.asList(asks.split("],\\["));
                asksList.forEach((ask) -> {
                    ask = ask.replace("\"", "");
                    BigDecimal price = new BigDecimal(ask.split(",")[0]);
                    BigDecimal amount = new BigDecimal(ask.split(",")[1]);
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setCurrency(currency);
                    exchangeRate.setAmount(amount);
                    exchangeRate.setPrice(price);
                    exchangeRatesRepo.save(exchangeRate);
                });
                Long finish = System.currentTimeMillis();
                log.info("getRatesByCurrency all time: " + (finish - start));
                log.info("getRatesByCurrency connection: " + (connectionFinish - connectionStart));
                log.info("getRatesByCurrency repo init: " + (finish - connectionFinish));

            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            log.error("Connection error");
            log.error(e.getMessage());
            log.error(e.toString());
        }
    }

}
