package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.model.Owner;
import ru.job4j.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class OwnerRepositoryTest {
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private Owner owner1;
    private Owner owner2;
    private Owner owner3;

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
            User user1 = new User(null, "login@user1", "pass", "User1", "UTC", List.of());
            session.persist(user1);
            owner1 = new Owner(null, "Owner1", user1);
            session.persist(owner1);

            User user2 = new User(null, "login@user2", "pass", "User2", "UTC", List.of());
            session.persist(user2);
            owner2 = new Owner(null, "Owner2", user2);
            session.persist(owner2);

            User user3 = new User(null, "login@user3", "pass", "AnotherName", "UTC", List.of());
            session.persist(user3);
            owner3 = new Owner(null, "AnotherName", user3);
            session.persist(owner3);

            tx.commit();
        }
    }

    @Test
    public void whenCreateThenReturnSavedOwner() {
        User newUser =
                new User(null, "login@newUser", "pass", "newUser", "UTC", List.of());
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(newUser);
            tx.commit();
        }

        Owner newOwner = new Owner(null, "New Owner", newUser);

        Owner savedOwner = ownerRepository.create(newOwner);

        assertThat(savedOwner).isNotNull();
        Integer id = savedOwner.getId();

        try (Session session = sessionFactory.openSession()) {
            Owner ownerDb = session.get(Owner.class, id);

            assertThat(ownerDb).isNotNull()
                    .extracting(Owner::getId, Owner::getName, o -> o.getUser().getLogin())
                    .containsExactly(id, "New Owner", "login@newUser");
        }
    }

    @Test
    public void whenDeleteThenReceiveTrueAndOwnerGone() {
        Integer id = owner1.getId();
        assertThat(id).isNotNull();

        assertThat(ownerRepository.delete(id)).isTrue();

        try (Session session = sessionFactory.openSession()) {
            Owner removedOwner = session.get(Owner.class, id);
            assertThat(removedOwner).isNull();
        }
    }

    @Test
    public void whenFindByIdThenReceiveWithThisId() {
        Integer id = owner1.getId();
        assertThat(id).isNotNull();

        Optional<Owner> ownerDb = ownerRepository.findById(id);

        assertThat(ownerDb).isPresent().get()
                .extracting(Owner::getId, o -> o.getUser().getLogin())
                .containsExactly(id, "login@user1");
    }

    @Test
    public void whenFindByIdThenReturnEmpty() {
        assertThat(ownerRepository.findById(666)).isEmpty();
    }

    @Test
    public void whenFindByLikeNameThenReceiveList() {
        List<Owner> rsl = ownerRepository.findByLikeName("own");

        assertThat(rsl).hasSize(2)
                .extracting(Owner::getName, o-> o.getUser().getName())

                .containsExactly(tuple("Owner1", "User1"),
                tuple("Owner2", "User2"));
    }

    @Test
    public void whenFindAllThenReceiveListWithAll() {
        List<Owner> rsl = ownerRepository.findAll();

        assertThat(rsl).hasSize(3).extracting(Owner::getName, o -> o.getUser().getName())
                .containsExactlyInAnyOrder(
                        tuple("Owner1", "User1"),
                        tuple("Owner2", "User2"),
                        tuple("AnotherName", "AnotherName"));
    }
}