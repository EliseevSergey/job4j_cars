package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private Car car1;
    private Car car2;
    private Car car3;

    private User user1;
    private User user2;
    private Owner owner;

    @BeforeEach
    public void setUp() {
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
            Brand brand1 = new Brand(null, "Toyota", List.of());
            Brand brand2 = new Brand(null, "BMW", List.of());

            Model model1 = new Model(null, "Camry", brand1);
            Model model2 = new Model(null, "X5", brand2);
            Model model3 = new Model(null, "LandCruiser", brand1);

            Fuel fuel1 = new Fuel(null, "Petrol");
            Fuel fuel2 = new Fuel(null, "Diesel");

            Engine engine1 = new Engine(null, 2.5, fuel1, 180);
            Engine engine2 = new Engine(null, 1.6, fuel2, 180);

            Color color1 = new Color(null, "Black");
            Color color2 = new Color(null, "Green");
            Color color3 = new Color(null, "Yellow");

            user1 = new User(null, "login1@user", "pass1", "Test User1", "UTC", List.of());
            user2 = new User(null, "login2@user", "pass2", "Test User2", "UTC", List.of());

            owner = new Owner(null, "Owner1", user1);


            car1 = new Car(null, "VIN1", brand1, model1, engine1, color1, null);
            car2 = new Car(null, "VIN2", brand2, model2, engine2, color2, null);
            car3 = new Car(null, "VIN3", brand1, model3, engine1, color3, null);


            session.persist(user1);
            session.persist(user2);

            session.persist(owner);

            session.persist(brand1);
            session.persist(brand2);

            session.persist(model1);
            session.persist(model2);
            session.persist(model3);

            session.persist(fuel1);
            session.persist(fuel2);

            session.persist(engine1);
            session.persist(engine2);

            session.persist(color1);
            session.persist(color2);
            session.persist(color3);

            session.persist(car1);
            session.persist(car2);
            session.persist(car3);

            tx.commit();
        }
    }

    @Test
    public void whenCreateThanReceiveSavedCar() {
        Car newCar = new Car(null, "VIN_NEW", car1.getBrand(), car1.getModel(), car1.getEngine(),
                car1.getColor(), new HashSet<>());

        Car savedCar = carRepository.create(newCar);

        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getId()).isNotNull();

        try (Session session = sessionFactory.openSession()) {
            Car dBCar = session.get(Car.class, savedCar.getId());

            assertThat(dBCar.getVin()).isEqualTo("VIN_NEW");
            assertThat(dBCar.getBrand().getName()).isEqualTo("Toyota");
            assertThat(dBCar.getEngine().getVolume()).isEqualTo(2.5);
        }
    }

    @Test
    public void whenFindByIdThenReceivedCarWithThisId() {
        Integer id = car1.getId();

        Optional<Car> rsl = carRepository.findById(id);

        assertThat(rsl).isPresent();
        assertThat(rsl.get().getId()).isEqualTo(id);

        assertThat(rsl).isPresent().get()
                .extracting(Car::getVin,
                        c -> c.getBrand().getName(),
                        c -> c.getModel().getName(),
                        c -> c.getColor().getName(),
                        c -> c.getEngine().getVolume(),
                        c -> c.getEngine().getFuel().getName())
                .containsExactly("VIN1", "Toyota", "Camry", "Black", 2.5, "Petrol");
    }


    @Test
    public void whenUpdateThenReceiveUpdatedCar() {
        Integer id = car1.getId();

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
            color = new Color(null, "Red");

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
            assertThat(dBCar.getColor().getName()).isEqualTo("Red");
        }
    }

    @Test
    void whenDeleteThenReturnTrueAndCarIsGone() {
        Integer id = car1.getId();

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
        List<Car> rsl = carRepository.findByLikeModelName("aMr");

        assertThat(rsl).hasSize(1).containsExactly(car1);

        assertThat(rsl).hasSize(1).extracting(
                Car::getVin,
                c -> c.getBrand().getName(),
                c -> c.getModel().getName(),
                c -> c.getEngine().getFuel().getName(),
                c -> c.getColor().getName())
                .containsExactly(tuple("VIN1", "Toyota", "Camry", "Petrol", "Black"));
    }

    @Test
    public void whenFindByLikeBrandNameThenReceiveCarList() {
        List<Car> rsl = carRepository.findByLikeBrandName("YoTa");

        assertThat(rsl).hasSize(2)
                .containsExactlyInAnyOrder(car1, car3);

        assertThat(rsl).hasSize(2).extracting(
                        Car::getVin,
                        c -> c.getBrand().getName(),
                        c -> c.getModel().getName(),
                        c -> c.getEngine().getFuel().getName(),
                        c -> c.getColor().getName())
                .containsExactly(
                        tuple("VIN1", "Toyota", "Camry", "Petrol", "Black"),
                        tuple("VIN3", "Toyota", "LandCruiser", "Petrol", "Yellow"));
    }

    @Test
    public void whenFindAllThenReceiveAll() {
        List<Car> rsl = carRepository.findAll();

        assertThat(rsl).hasSize(3)
                .containsExactlyInAnyOrder(car1, car2, car3);

        assertThat(rsl).hasSize(3)
                .extracting(Car::getVin,
                        c -> c.getBrand().getName(),
                        c -> c.getModel().getName(),
                        c -> c.getEngine().getVolume(),
                        c -> c.getEngine().getFuel().getName(),
                        c -> c.getColor().getName())
                .containsExactly(
                        tuple("VIN2", "BMW", "X5", 1.6, "Diesel", "Green"),
                        tuple("VIN1", "Toyota", "Camry", 2.5, "Petrol", "Black"),
                        tuple("VIN3", "Toyota", "LandCruiser", 2.5, "Petrol", "Yellow")
                );
    }

    @Test
    public void whenAddOwnerThenReceiveCarWithOwner() {
        Integer id = car1.getId();

        try (Session session = sessionFactory.openSession()) {
            Car dBCar = session.get(Car.class, id);
            assertThat(dBCar.getOwners()).isEmpty();
        }

        assertTrue(carRepository.addOwner(id, owner.getId()));

        try (Session session = sessionFactory.openSession()) {
            Car dbCar = session.get(Car.class, id);
            Set<Owner> owners = dbCar.getOwners();
            assertThat(owners).hasSize(1)
                    .extracting(Owner::getName)
                    .containsOnly("Owner1");
        }
    }

    @Test
    public void whenRemoveOwnerThenReceiveEmptyOwner() {
        Integer id = car1.getId();

        try (Session session = sessionFactory.openSession()) {
            Car dBCar = session.get(Car.class, id);
            assertThat(dBCar.getOwners()).isEmpty();
        }

        assertTrue(carRepository.addOwner(id, owner.getId()));

        try (Session session = sessionFactory.openSession()) {
            Car dbCar = session.get(Car.class, id);
            Set<Owner> owners = dbCar.getOwners();
            assertThat(owners).hasSize(1)
                    .extracting(Owner::getName)
                    .containsOnly("Owner1");
        }

        assertTrue(carRepository.removeOwner(id, owner.getId()));

        try (Session session = sessionFactory.openSession()) {
            Car dBCar = session.get(Car.class, id);
            assertThat(dBCar.getOwners()).isEmpty();
        }
    }
}