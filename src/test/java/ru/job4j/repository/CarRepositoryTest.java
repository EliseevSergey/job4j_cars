package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.model.Car;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private Car car;

    @BeforeEach
    public void setUp() {
        try (Session session = sessionFactory.openSession()) {
            car = new Car();
            car.setVin("VIN");
            session.persist(car);
        }
    }

    @Test
    public void whenCreateThanReceiveSavedCar() {
        carRepository.create(car);

        try (Session session = sessionFactory.openSession()) {
            Car savedCar = session.get(Car.class, car.getId());
            assertThat(savedCar.getVin()).isEqualTo(car.getVin());
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