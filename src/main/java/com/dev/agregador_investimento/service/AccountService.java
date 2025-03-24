package com.dev.agregador_investimento.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dev.agregador_investimento.dto.AssociateAccountDTO;
import com.dev.agregador_investimento.entity.AccountStock;
import com.dev.agregador_investimento.entity.AccountStockId;
import com.dev.agregador_investimento.repository.AccountRepository;
import com.dev.agregador_investimento.repository.AccountStockRepository;
import com.dev.agregador_investimento.repository.StockRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository,
            AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
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

}
