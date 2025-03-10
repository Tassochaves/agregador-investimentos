package com.dev.agregador_investimento.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dev.agregador_investimento.dto.CreateUserDTO;
import com.dev.agregador_investimento.dto.UpdateUserDTO;
import com.dev.agregador_investimento.entity.User;
import com.dev.agregador_investimento.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUserWithSuccess() {

            // Arrange
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password123", Instant.now(), null);

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            var input = new CreateUserDTO("username", "email@email.com", "password123");

            // Act
            var output = userService.createUser(input);

            // Assert
            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName("Should trhow when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {
            doThrow(new RuntimeException()).when(userRepository).save(any());

            var input = new CreateUserDTO("username", "email@email.com", "password123");

            // Act & Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));

        }
    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("Should get user by id with success when optional is present")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {

            // Arrange
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password123", Instant.now(), null);
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(user.getUserId().toString());

            // Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

        }

        @Test
        @DisplayName("Should get user by id with success when optional is empty")
        void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {

            // Arrange
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(userId.toString());

            // Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());

        }
    }

    @Nested
    class listUsers {

        @Test
        @DisplayName("Should return all users with success")
        void shouldReturnAllUsersWithSuccess() {

            // Arrange
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password123", Instant.now(), null);

            var userList = List.of(user);
            doReturn(userList).when(userRepository).findAll();

            // Act
            var output = userService.listUsers();

            // Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class deleteById {

        @Test
        @DisplayName("Should dele user with success when user exists")
        void shouldDeleteUserWithSuccess() {

            // Arrange
            doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());
            doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert
            var idList = uuidArgumentCaptor.getAllValues();

            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).existsById(idList.get(1));

        }

        @Test
        @DisplayName("Should not delete user when user not exists")
        void shouldNotDeleteUserWhenNotExists() {

            // Arrange
            doReturn(false).when(userRepository).existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());

        }
    }

    @Nested
    class updateUserById {

        @Test
        @DisplayName("Should update user by id when user exists and username and password is filled")
        void shouldUpdateUserByIdWhenUserExistsAndUsernameAndPasswordIsFilled() {

            // Arrange
            var updateUserDTO = new UpdateUserDTO("newUsername", "newPassword");
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password123", Instant.now(), null);
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            // Act
            userService.updateUserById(user.getUserId().toString(), updateUserDTO);

            var userCaptured = userArgumentCaptor.getValue();

            // Assert
            assertEquals(updateUserDTO.username(), userCaptured.getUsername());
            assertEquals(updateUserDTO.password(), userCaptured.getPassword());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(1)).save(user);

        }

        @Test
        @DisplayName("Should not update user when user not exists")
        void shouldNotUpdateUserWhenUserNotExists() {

            // Arrange
            var updateUserDTO = new UpdateUserDTO("newUsername", "newPassword");
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            // Act
            userService.updateUserById(userId.toString(), updateUserDTO);

            // Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).save(any());

        }
    }
}
