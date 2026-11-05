package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.model.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private Car car;
    private User user;

    @BeforeEach
    public void setUp() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Post").executeUpdate();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.createQuery("DELETE FROM Fuel").executeUpdate();
            session.createQuery("DELETE FROM Model").executeUpdate();
            session.createQuery("DELETE FROM Brand").executeUpdate();
            session.createQuery("DELETE FROM Color").executeUpdate();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            tx.commit();
        }



        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Brand brand1 = new Brand(null, "Toyota", List.of());
            Model model1 = new Model(null, "Camry", brand1);
            Fuel fuel = new Fuel(null, "Petrol");
            Engine engine = new Engine(null, 2.5, fuel, 180);
            Color color = new Color(null, "Black");
            user = new User(null, "login@user", "pass", "Test User", "UTC", List.of());
            car = new Car(null, "VIN0", brand1, model1, engine, color, null);
            session.persist(user);
            session.persist(brand1);
            session.persist(model1);
            session.persist(fuel);
            session.persist(engine);
            session.persist(color);
            tx.commit();
        }
    }

    @Test
    public void whenCreateThanReceiveSavedCar() {
        Car savedCar = carRepository.create(car);
        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getId()).isNotNull();

        try (Session session = sessionFactory.openSession()) {
            Car dBCar = session.get(Car.class, savedCar.getId());
            assertThat(dBCar.getVin()).isEqualTo("VIN0");
            assertThat(dBCar.getBrand().getName()).isEqualTo("Toyota");
        }
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findByLikeModelName() {
    }

    @Test
    void findByLikeBrandName() {
    }

    @Test
    void findAll() {
    }

    @Test
    void addOwner() {
    }

    @Test
    void removeOwner() {
    }
}