package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.job4j.model.User;

import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final SessionFactory sf;

    public UserRepository(SessionFactory sf) {
        this.sf = sf;
    }

    public User create(User user) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(user);
                transaction.commit();
                return user;
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Failed to save user", e);
            }
        }
    }

    public void update(User user) {
        try (Session session = sf.openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.createQuery("UPDATE User SET password = :newPassword WHERE id = :id")
                        .setParameter("newPassword", user.getPassword())
                        .setParameter("id", user.getId())
                        .executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Failed to update user", e);
            }
        }
    }

    public void delete(Integer userId) {
        try (Session session = sf.openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.createQuery("DELETE FROM User WHERE id = :id")
                        .setParameter("id", userId)
                        .executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Failed to delete user", e);
            }
        }
    }

    public List<User> findAllOrderById() {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM User ORDER BY id", User.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve from database", e);
        }
    }

    public Optional<User> findById(Integer userId) {
        try (Session session = sf.openSession()) {
            return Optional.ofNullable(session.get(User.class, userId));
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve from database", e);
        }
    }

    public List<User> findByLikeLogin(String key) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM User WHERE LOWER(login) LIKE LOWER(:key)", User.class)
                    .setParameter("key", "%" + key + "%")
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve from database", e);
        }
    }

    public Optional<User> findByLogin(String login) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM User WHERE login =:login", User.class)
                    .setParameter("login", login)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve from database", e);
        }
    }
}
