package com.dev.agregador_investimento.service;

import org.springframework.stereotype.Service;

import com.dev.agregador_investimento.dto.CreateStockDTO;
import com.dev.agregador_investimento.entity.Stock;
import com.dev.agregador_investimento.repository.StockRepository;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDTO createStockDTO) {
        // DTO to Entity
        var stock = new Stock(
                createStockDTO.stockId(),
                createStockDTO.description());

        stockRepository.save(stock);
    }
}
