package com.dev.agregador_investimento.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.agregador_investimento.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

}
