package ru.job4j.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.model.Car;
import ru.job4j.service.CarService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarControllerTest {
    private CarService carService;
    private CarController carController;
    private Car car1;
    private Car car2;

    @BeforeEach
    void setUp() {
        carService = mock(CarService.class);
        carController = new CarController(carService);

        car1 = new Car();
        car1.setId(1);
        car1.setVin("VIN1");
        car2 = new Car();
        car2.setId(2);
        car2.setVin("VIN2");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void whenAddListThanGetAll() {
        List<Car> expected = List.of(car1, car2);
        when(carService.findAll()).thenReturn(expected);
        ConcurrentModel model = new ConcurrentModel();

        String view = carController.getAll(model);

        assertThat(view).isEqualTo("car/list");
        assertThat(model.getAttribute("cars")).isEqualTo(expected);
    }
}