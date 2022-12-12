package com.example.ElastumTestTask.repository;

import com.example.ElastumTestTask.entity.CoinbasePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinbaseRepository extends JpaRepository<CoinbasePair, Long> {

    public String getAllBy();

    public List<CoinbasePair> findAll();



}
