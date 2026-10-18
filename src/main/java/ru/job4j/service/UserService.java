package ru.job4j.service;

import ru.job4j.model.User;

import java.util.Optional;

public interface UserService {
    User create(User user);
    Optional<User> findByLoginAndPassword(String login, String password);

}
