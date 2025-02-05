package com.dev.agregador_investimento.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.agregador_investimento.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}
