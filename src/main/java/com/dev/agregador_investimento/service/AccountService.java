package com.dev.agregador_investimento.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dev.agregador_investimento.client.BrapiClient;
import com.dev.agregador_investimento.dto.AccountStockResponseDTO;
import com.dev.agregador_investimento.dto.AssociateAccountDTO;
import com.dev.agregador_investimento.entity.AccountStock;
import com.dev.agregador_investimento.entity.AccountStockId;
import com.dev.agregador_investimento.repository.AccountRepository;
import com.dev.agregador_investimento.repository.AccountStockRepository;
import com.dev.agregador_investimento.repository.StockRepository;

@Service
public class AccountService {

        @Value("${environment.TOKEN}")
        private String TOKEN;

        private AccountRepository accountRepository;
        private StockRepository stockRepository;
        private AccountStockRepository accountStockRepository;
        private BrapiClient brapiClient;

        public AccountService(AccountRepository accountRepository, StockRepository stockRepository,
                        AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
                this.accountRepository = accountRepository;
                this.stockRepository = stockRepository;
                this.accountStockRepository = accountStockRepository;
                this.brapiClient = brapiClient;
        }

        public void associateStock(String accountId, AssociateAccountDTO associateAccountDTO) {
                var account = accountRepository.findById(UUID.fromString(accountId))
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

                var stock = stockRepository.findById(associateAccountDTO.stockId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

                // DTO to Entity
                var id = new AccountStockId(account.getAccountId(), stock.getStockId());

                var entity = new AccountStock(
                                id,
                                account,
                                stock,
                                associateAccountDTO.quantity());

                accountStockRepository.save(entity);
        }

        public List<AccountStockResponseDTO> listStocks(String accountId) {

                var account = accountRepository.findById(UUID.fromString(accountId))
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

                return account.getAccountStocks()
                                .stream()
                                .map(as -> new AccountStockResponseDTO(as.getStock().getStockId(), as.getQuantity(),
                                                getTotal(as.getQuantity(), as.getStock().getStockId())))
                                .toList();
        }

        private double getTotal(Integer quantity, String stockId) {

                var response = brapiClient.getQuote(TOKEN, stockId);

                var price = response.results().getFirst().regularMarketPrice();

                return quantity * price;
        }
}
