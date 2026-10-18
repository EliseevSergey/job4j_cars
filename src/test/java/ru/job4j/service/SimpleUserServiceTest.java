package ru.job4j.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimpleUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SimpleUserService userService;


    @Test
    public void whenCreateThenReturnSavedUser() {
        User user = new User(1, "login", "pas", "name", "UTC", List.of());
        when(userRepository.create(user)).thenReturn(user);
        assertThat(userService.create(user)).isEqualTo(user);
    }

    @Test
    public void whenFindByLoginAndPasswordThenReturnUser() {
        User user = new User(1, "login", "pas", "name", "UTC", List.of());
        when(userRepository.findByLoginAndPassword("login", "pas")).thenReturn(Optional.of(user));
        assertThat(userService.findByLoginAndPassword("login", "pas")).isPresent().contains(user);
    }

    @Test
    public void whenFindByLoginAndPasswordThenEmptyIfNotFound() {
        when(userRepository.findByLoginAndPassword("unknown", "wrongPas")).thenReturn(Optional.empty());
        assertThat(userService.findByLoginAndPassword("unknown", "wrongPas")).isEmpty();
    }
}