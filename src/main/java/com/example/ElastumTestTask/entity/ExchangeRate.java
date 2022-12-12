package com.example.ElastumTestTask.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String currency;

    @ManyToOne(fetch=FetchType.LAZY,
            cascade=CascadeType.ALL)
    @JoinColumn (name="pair_id")
    private CoinbasePair coinbasePair;

    private BigDecimal price;

    private BigDecimal amount;

    private LocalDateTime time;
}
