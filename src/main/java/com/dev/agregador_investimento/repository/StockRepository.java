package com.dev.agregador_investimento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dev.agregador_investimento.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {

}
