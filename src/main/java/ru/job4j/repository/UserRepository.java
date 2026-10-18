package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {
    private final CrudRepository crudRepository;

    public User create(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional("FROM User u WHERE u.login = :fLogin AND u.password = :fPassword",
                User.class, Map.of("fLogin", login, "fPassword", password));
    }

    public Optional<User> findById(Integer userId) {
        return crudRepository.optional(
                "FROM User u WHERE u.id = :fId", User.class, Map.of("fId", userId));
    }

    public boolean updatePassword(Integer userId, String newPassword) {
        return crudRepository.execute("UPDATE User u SET password = :newPassword WHERE u.id = :fId",
                Map.of("newPassword", newPassword, "fId", userId)) > 0;
    }

    public boolean delete(Integer userId) {
        return crudRepository.execute("DELETE FROM User u WHERE u.id = :fId", Map.of("fId", userId)) > 0;
    }

    public List<User> findAll() {
        return crudRepository.query("FROM User u ORDER BY u.id ASC", User.class);
    }

    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "FROM User u WHERE LOWER(u.login) LIKE LOWER(:fKey)", User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    public Optional<User> findByLogin(String login) {
        return crudRepository.optional("FROM User u WHERE u.login = :login", User.class,
                Map.of("login", login));
    }
}

