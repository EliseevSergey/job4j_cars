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
import java.util.Optional;

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
    public void whenFindByIdThenReceivedCarWithThisId() {
        Car savedCar = carRepository.create(car);
        Integer id = savedCar.getId();

        Optional<Car> rsl = carRepository.findById(id);

        assertThat(rsl).isPresent();
        assertThat(rsl.get().getId()).isEqualTo(id);
        assertThat(rsl.get().getVin()).isEqualTo("VIN0");
        assertThat(rsl.get().getBrand().getName()).isEqualTo("Toyota");
    }

    @Test
    public void whenUpdateThenReceiveUpdatedCar() {
        Car savedCar = carRepository.create(car);
        Integer id = savedCar.getId();
        Car carToUpdate = carRepository.findById(id).get();

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Brand brand2 = new Brand(null, "Lada", List.of());
            Model model2 = new Model(null, "Niva", brand2);
            Fuel fuel2 = new Fuel(null, "Diesel");
            Engine engine2 = new Engine(null, 2.0, fuel2, 100);
            Color color2 = new Color(null, "Green");

            session.persist(brand2);
            session.persist(model2);
            session.persist(fuel2);
            session.persist(engine2);
            session.persist(color2);

            carToUpdate.setVin("newVIN");

            carToUpdate.setBrand(brand2);
            carToUpdate.setModel(model2);
            carToUpdate.setEngine(engine2);
            carToUpdate.setColor(color2);

            tx.commit();
        }

        Car afterUpdate = carRepository.update(carToUpdate);

        assertThat(afterUpdate.getVin()).isEqualTo("newVIN");
        assertThat(afterUpdate.getId()).isEqualTo(id);

        assertThat(afterUpdate.getBrand().getName()).isEqualTo("Lada");
        assertThat(afterUpdate.getModel().getName()).isEqualTo("Niva");
        assertThat(afterUpdate.getEngine().getFuel().getName()).isEqualTo("Diesel");
        assertThat(afterUpdate.getColor().getName()).isEqualTo("Green");
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