package com.dev.agregador_investimento.service;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dev.agregador_investimento.dto.AccountResponseDTO;
import com.dev.agregador_investimento.dto.CreateAccountDTO;
import com.dev.agregador_investimento.dto.CreateUserDTO;
import com.dev.agregador_investimento.dto.UpdateUserDTO;
import com.dev.agregador_investimento.entity.Account;
import com.dev.agregador_investimento.entity.BillingAddress;
import com.dev.agregador_investimento.entity.User;
import com.dev.agregador_investimento.repository.AccountRepository;
import com.dev.agregador_investimento.repository.BillingAddressRepository;
import com.dev.agregador_investimento.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository,
            BillingAddressRepository billingAddressRepository) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    public UUID createUser(CreateUserDTO createUserDTO) {
        // DTO to Entity
        var entity = new User(
                null,
                createUserDTO.username(),
                createUserDTO.email(),
                createUserDTO.password(),
                Instant.now(),
                null);

        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void updateUserById(String userId, UpdateUserDTO updateUserDTO) {
        var id = UUID.fromString(userId);

        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();

            if (updateUserDTO.username() != null) {
                user.setUsername(updateUserDTO.username());
            }

            if (updateUserDTO.password() != null) {
                user.setPassword(updateUserDTO.password());
            }

            userRepository.save(user);
        }

    }

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);

        var userExists = userRepository.existsById(id);

        if (userExists) {
            userRepository.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDTO createAccountDTO) {

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não existe"));

        if (isNull(user.getAccounts())) {
            user.setAccounts(new ArrayList<>());
        }

        // DTO to Entity
        var account = new Account(
                null,
                user,
                null,
                createAccountDTO.description(),
                new ArrayList<>());

        var accountCreated = accountRepository.save(account);
        // System.out.println("AccountID" + account.getAccountId());
        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                account,
                createAccountDTO.street(),
                createAccountDTO.number());

        billingAddressRepository.save(billingAddress);
    }

    public List<AccountResponseDTO> listAccounts(String userId) {

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var accounts = user.getAccounts().stream()
                .map(ac -> new AccountResponseDTO(ac.getAccountId().toString(), ac.getDescription()))
                .toList();

        return accounts;

    }
}
