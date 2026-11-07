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

        Brand brand2;
        Model model2;
        Fuel fuel2;
        Engine engine2;
        Color color2;

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            brand2 = new Brand(null, "Lada", List.of());
            model2 = new Model(null, "Niva", brand2);
            fuel2 = new Fuel(null, "Diesel");
            engine2 = new Engine(null, 2.0, fuel2, 100);
            color2 = new Color(null, "Green");

            session.persist(brand2);
            session.persist(model2);
            session.persist(fuel2);
            session.persist(engine2);
            session.persist(color2);

            tx.commit();
        }

        carToUpdate.setVin("newVIN");
        carToUpdate.setBrand(brand2);
        carToUpdate.setModel(model2);
        carToUpdate.setEngine(engine2);
        carToUpdate.setColor(color2);


        carRepository.update(carToUpdate);

        try (Session session = sessionFactory.openSession()) {
            //Transaction tx = session.beginTransaction();

            Car dBCar = session.get(Car.class, id);

            assertThat(dBCar.getVin()).isEqualTo("newVIN");
            assertThat(dBCar.getId()).isEqualTo(id);

            assertThat(dBCar.getBrand().getName()).isEqualTo("Lada");
            assertThat(dBCar.getModel().getName()).isEqualTo("Niva");
            assertThat(dBCar.getEngine().getFuel().getName()).isEqualTo("Diesel");
            assertThat(dBCar.getColor().getName()).isEqualTo("Green");
            //tx.commit();
        }
    }

    @Test
    public void whenUpdateThenCarUpdatedInDatabase() {
        // 1. Создаём машину
        Car savedCar = carRepository.create(car);
        Integer id = savedCar.getId();

        // 2. Отвязываем объект от сессии (гарантируем detached)
        try (Session session = sessionFactory.openSession()) {
            Car detached = session.get(Car.class, id);
            session.evict(detached); // ← важно!
            // detached теперь "отвязан"
        }

        // 3. Создаём новые справочники в отдельной транзакции
        Brand newBrand;
        Model newModel;
        Engine newEngine;
        Color newColor;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            newBrand = new Brand(null, "Lada", List.of());
            newModel = new Model(null, "Niva", newBrand);
            Fuel newFuel = new Fuel(null, "Diesel");
            newEngine = new Engine(null, 2.0, newFuel, 100);
            newColor = new Color(null, "Green");
            session.persist(newBrand);
            session.persist(newModel);
            session.persist(newFuel);
            session.persist(newEngine);
            session.persist(newColor);
            tx.commit();
        }

        // 4. Изменяем detached объект вручную (не в контексте Hibernate!)
        Car toUpdate = new Car();
        toUpdate.setId(id);
        toUpdate.setVin("newVIN");
        toUpdate.setBrand(newBrand);
        toUpdate.setModel(newModel);
        toUpdate.setEngine(newEngine);
        toUpdate.setColor(newColor);

        // 5. Вызываем тестируемый метод
        carRepository.update(toUpdate);

        // 6. Проверяем в БД
        try (Session session = sessionFactory.openSession()) {
            Car dbCar = session.get(Car.class, id);
            assertThat(dbCar.getVin()).isEqualTo("newVIN");
            assertThat(dbCar.getBrand().getName()).isEqualTo("Lada");
            assertThat(dbCar.getModel().getName()).isEqualTo("Niva");
            assertThat(dbCar.getEngine().getFuel().getName()).isEqualTo("Diesel");
            assertThat(dbCar.getColor().getName()).isEqualTo("Green");
        }
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