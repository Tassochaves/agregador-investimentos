package com.dev.agregador_investimento.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dev.agregador_investimento.dto.CreateUserDTO;
import com.dev.agregador_investimento.entity.User;
import com.dev.agregador_investimento.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
