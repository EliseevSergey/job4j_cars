package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Post").executeUpdate();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.createQuery("DELETE FROM Fuel").executeUpdate();
            session.createQuery("DELETE FROM Model").executeUpdate();
            session.createQuery("DELETE FROM Brand").executeUpdate();
            session.createQuery("DELETE FROM Color").executeUpdate();
            tx.commit();
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            user1 = new User(null, "login@user1", "pass", "User1", "UTC", List.of());
            session.persist(user1);

            user2 = new User(null, "login@user2", "pass", "User2", "UTC", List.of());
            session.persist(user2);

            user3 = new User(null, "lgn@user3", "pass", "AnotherName", "UTC", List.of());
            session.persist(user3);

            tx.commit();
        }

    }

    @Test
    public void whenCreateNewUserThenReceiveSavedOne() {
        User newUser =
                new User(null, "login@NewUser", "pass", "newUser", "UTC", List.of());

        User savedUser = userRepository.create(newUser);

        assertThat(savedUser).isNotNull();

        Integer id = savedUser.getId();
        assertThat(id).isNotNull();

        try (Session session = sessionFactory.openSession()) {
            User userDb = session.get(User.class, id);

            assertThat(userDb).isNotNull()
                    .extracting(User::getLogin, User::getPassword, User::getName, User::getTimezone)
                    .containsExactly("login@NewUser", "pass", "newUser", "UTC");
        }
    }

    @Test
    public void whenFindByLoginAndPasswordThenReceiveUser() {
        Optional<User> rsl = userRepository.findByLoginAndPassword("login@user1", "pass");
        assertThat(rsl).isPresent();

        assertThat(rsl.get()).extracting(User::getId, User::getLogin, User::getPassword)
                .containsExactly(user1.getId(), "login@user1", "pass");
    }

    @Test
    public void whenFindByIdThenReceiveUserWithId() {
        Optional<User> rsl = userRepository.findById(user1.getId());
        assertThat(rsl).isPresent();

        assertThat(rsl.get()).extracting(User::getId, User::getLogin, User::getPassword)
                .containsExactly(user1.getId(), "login@user1", "pass");
    }

    @Test
    public void whenFindByIdWithNonExistedIdThenReceiveEmpty() {
        Optional<User> rsl = userRepository.findById(666);
        assertThat(rsl).isEmpty();
    }

    @Test
    public void whenUpdatePasswordThenReceiveUserWithUpdatedPass() {
        Integer id = user1.getId();

        assertThat(userRepository.updatePassword(id, "newPass")).isTrue();

        try (Session session = sessionFactory.openSession()) {
            User updatedUser = session.get(User.class, id);
            assertThat(updatedUser).isNotNull()
                    .extracting(User::getId, User::getPassword)
                    .containsExactly(id, "newPass");
        }
    }

    @Test
    public void whenDeleteThenTrueAndUserGone() {
        Integer id = user1.getId();

        assertThat(userRepository.delete(id)).isTrue();

        try (Session session = sessionFactory.openSession()) {
            User deleted = session.get(User.class, id);
            assertThat(deleted).isNull();
        }
    }

    @Test
    public void whenFindAllThenReceiveListWithAll() {
        List<User> rsl = userRepository.findAll();

        assertThat(rsl).hasSize(3).extracting(User::getId, User::getLogin)
                .containsExactly(
                        tuple(user1.getId(), "login@user1"),
                        tuple(user2.getId(), "login@user2"),
                        tuple(user3.getId(), "lgn@user3"));
    }

    @Test
    public void whenFindByLikeLoginThenReceiveList() {
        List<User> rsl = userRepository.findByLikeLogin("ogi");

        assertThat(rsl).hasSize(2).extracting(User::getId, User::getLogin)
                .containsExactly(
                        tuple(user1.getId(), "login@user1"),
                        tuple(user2.getId(), "login@user2"));
    }

    @Test
    public void whenFindByLoginThenReceiveWithThisLogin() {
        Optional<User> rsl = userRepository.findByLogin(user1.getLogin());
        assertThat(rsl).isPresent();

        assertThat(rsl.get()).extracting(User::getId, User::getLogin)
                .containsExactly(user1.getId(), "login@user1");
    }
}