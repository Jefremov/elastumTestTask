package com.example.ElastumTestTask.service.coinbase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class coinbaseApi {


    private final int CONNECTION_TIMEOUT = 540;


    private void initCoinbaseRepo() throws IOException {

        final URL url = new URL("https://api.exchange.coinbase.com/products/RLY-GBP/book");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        con.setReadTimeout(CONNECTION_TIMEOUT);

    }

}
