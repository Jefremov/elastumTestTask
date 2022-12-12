package com.example.ElastumTestTask.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
//@Table(name = "coinbasePair", uniqueConstraints = {
//        @UniqueConstraint(columnNames = "pair_id")
//})
@Table(name = "coinbase_Pair")
public class CoinbasePair {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id")
//    private Long id;
    @Id
    @Column(name = "pair_id")
    private String pairId;
    @Column(name = "base_currency")
    private String baseCurrency;
    @Column(name = "quote_currency")
    private String quoteCurrency;
    @Column(name = "post_only")
    private boolean postOnly;
    @Column(name = "limit_only")
    private boolean limitOnly;
    @Column(name = "cancel_only")
    private boolean cancelOnly;
    @Column(name = "trading_disabled")
    private boolean tradingDisabled;
    @Column(name = "auction_mode")
    private boolean auctionMode;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "coinbasePair",
            cascade = CascadeType.ALL)
    private List<ExchangeRate> exchangeRateList;


}
