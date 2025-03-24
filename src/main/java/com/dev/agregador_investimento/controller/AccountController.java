package com.dev.agregador_investimento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.agregador_investimento.dto.AssociateAccountDTO;
import com.dev.agregador_investimento.service.AccountService;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable String accountId,
            @RequestBody AssociateAccountDTO associateAccountDTO) {

        accountService.associateStock(accountId, associateAccountDTO);

        return ResponseEntity.ok().build();
    }
}
