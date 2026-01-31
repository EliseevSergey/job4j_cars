package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    public void delete(Integer userId) {
        crudRepository.run("DELETE FROM User WHERE id = :fId", Map.of("fId", userId)
        );
    }

    public List<User> findAllOrderById() {
        return crudRepository.query("FROM User ORDER BY id ASC", User.class);
    }

    public Optional<User> findById(Integer userId) {
        return crudRepository.optional(
                "FROM User WHERE id = :fid", User.class, Map.of("fId", userId));
    }

    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "FROM User WHERE LOWER(login) LIKE LOWER(:key)", User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    public Optional<User> findByLogin(String login) {
        return crudRepository.optional("FROM User WHERE login =:login", User.class,
                Map.of("login", login));
    }

}
