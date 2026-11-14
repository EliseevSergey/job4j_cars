package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.model.Fuel;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FuelRepositoryTest {
    @Autowired
    private FuelRepository fuelRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private Fuel fuel1;
    private Fuel fuel2;

    @BeforeEach
    void setUp() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Post").executeUpdate();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.createQuery("DELETE FROM Fuel").executeUpdate();
            tx.commit();
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            fuel1 = new Fuel(null, "Petrol");
            fuel2 = new Fuel(null, "Diesel");

            session.persist(fuel1);
            session.persist(fuel2);

            tx.commit();
        }
    }

    @Test
    public void whenFindAllThenReceiveList() {
        List<Fuel> rsl = fuelRepository.findAll();
        assertThat(rsl)
                .hasSize(2)
                .extracting(Fuel::getName).containsExactlyInAnyOrder("Petrol", "Diesel");

        assertThat(rsl)
                .containsExactlyInAnyOrder(fuel1, fuel2);
    }

    @Test
    public void whenFindByIdThenReceiveFuelWithThisId() {
        assertThat(fuelRepository.findById(fuel1.getId()))
                .isPresent().contains(fuel1);

        assertThat(fuelRepository.findById(fuel2.getId()))
                .isPresent().contains(fuel2);

        assertThat(fuelRepository.findById(fuel2.getId()))
                .get().extracting(Fuel::getName).isEqualTo("Diesel");
    }

    @Test
    public void whenFindByIdWithNonExistentIdThenReturnEmpty () {
        assertThat(fuelRepository.findById(666)).isEmpty();
    }
}