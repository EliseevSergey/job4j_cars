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

        Brand brand;
        Model model;
        Fuel fuel;
        Engine engine;
        Color color;

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            brand = new Brand(null, "Lada", List.of());
            model = new Model(null, "Niva", brand);
            fuel = new Fuel(null, "Diesel");
            engine = new Engine(null, 2.0, fuel, 100);
            color = new Color(null, "Green");

            session.persist(brand);
            session.persist(model);
            session.persist(fuel);
            session.persist(engine);
            session.persist(color);

            tx.commit();
        }

        carToUpdate.setVin("newVIN");
        carToUpdate.setBrand(brand);
        carToUpdate.setModel(model);
        carToUpdate.setEngine(engine);
        carToUpdate.setColor(color);

        carRepository.update(carToUpdate);

        try (Session session = sessionFactory.openSession()) {

            Car dBCar = session.get(Car.class, id);

            assertThat(dBCar).isNotNull();
            assertThat(dBCar.getVin()).isEqualTo("newVIN");
            assertThat(dBCar.getId()).isEqualTo(id);

            assertThat(dBCar.getBrand().getName()).isEqualTo("Lada");
            assertThat(dBCar.getModel().getName()).isEqualTo("Niva");
            assertThat(dBCar.getEngine().getFuel().getName()).isEqualTo("Diesel");
            assertThat(dBCar.getColor().getName()).isEqualTo("Green");
        }
    }

    @Test
    void whenDeleteThenReturnTrueAndCarIsGone() {
        Car savedCar = carRepository.create(car);
        Integer id = savedCar.getId();

        try (Session session = sessionFactory.openSession()) {
            Car dbCar = session.get(Car.class, id);
            assertThat(dbCar).isNotNull();
        }

        assertThat(carRepository.delete(id)).isTrue();

        try (Session session = sessionFactory.openSession()) {
            assertThat(session.get(Car.class, id)).isNull();
        }
    }

    @Test
    public void whenDeleteNonExistentCarThenReturnFalse() {
        assertThat(carRepository.delete(666)).isFalse();
    }

    @Test
    public void whenFindByLikeModelNameThenReceiveCarList() {
        String key = "aMr";
        Car dbCar = carRepository.create(car);

        Model model = new Model(null, "RAV4", car.getBrand());

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(model);
            tx.commit();
        }
        Car other = new Car(null, "VIN2", car.getBrand(), model,
                car.getEngine(), car.getColor(), null);


        carRepository.create(other);
        List<Car> rsl = carRepository.findByLikeModelName(key);
        assertThat(rsl).hasSize(1);
        assertThat(rsl).containsExactly(dbCar);
        assertThat(rsl.get(0).getModel().getName()).isEqualTo("Camry");
        assertThat(rsl.get(0).getVin()).isEqualTo("VIN0");

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