package ru.job4j.repository;

import lombok.AllArgsConstructor;
import ru.job4j.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final CrudRepository crudRepository;

    public User create(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
    }

    public Optional<User> findById(Integer userId) {
        return crudRepository.optional(
                "FROM User WHERE id = :fId", User.class, Map.of("fId", userId));
    }

    public boolean updatePassword(Integer userId, String newPassword) {
        return crudRepository.execute("UPDATE User SET password = :newPassword WHERE id = :fId",
                Map.of("newPassword", newPassword, "fId", userId)) > 0;
    }

    public boolean delete(Integer userId) {
        return crudRepository.execute("DELETE FROM User WHERE id = :fId", Map.of("fId", userId)) > 0;
    }

    public List<User> findAll() {
        return crudRepository.query("FROM User ORDER BY id ASC", User.class);
    }

    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "FROM User WHERE LOWER(login) LIKE LOWER(:fKey)", User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    public Optional<User> findByLogin(String login) {
        return crudRepository.optional("FROM User WHERE login =:login", User.class,
                Map.of("login", login));
    }
}

