package ru.job4j.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.model.Brand;
import ru.job4j.model.Car;
import ru.job4j.repository.CarRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimpleCarServiceTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private SimpleCarService simpleCarService;

    @Test
    public void whenCreateCarThenReturnSavedCar() {
        Car car = new Car(1, "vin45678901234567", null, null, null, null, Set.of());
        when(carRepository.create(car)).thenReturn(car);
        assertThat(simpleCarService.create(car)).isEqualTo(car);
    }

    @Test
    public void whenFindByIdThenReturnCar() {
        Car car = new Car(1, "vin45678901234567", null, null, null, null, Set.of());
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        assertThat(simpleCarService.findById(1)).isPresent().contains(car);
    }

    @Test
    public void whenUpdateThenReturnUpdatedCar() {
        Car car = new Car(1, "vin45678901234567", null, null, null, null, Set.of());
        when(carRepository.update(car)).thenReturn(car);
        assertThat(simpleCarService.update(car)).isEqualTo(car);
    }

    @Test
    public void whenDeleteThenReturnTrue() {
        Car car = new Car(1, "vin45678901234567", null, null, null, null, Set.of());
        when(carRepository.delete(1)).thenReturn(true);
        assertThat(simpleCarService.delete(1)).isTrue();
    }

    @Test
    public void findByLikeModelName() {
        Car car = new Car(1, "vin45678901234567", null, null, null, null, Set.of());
        when(carRepository.findByLikeModelName("Camry")).thenReturn(List.of(car));
        assertThat(simpleCarService.findByLikeModelName("Camry")).isEqualTo(List.of(car));
    }

    @Test
    public void findByLikeBrandName() {
        Car car = new Car(1, "vin45678901234567", null, null, null, null, Set.of());
        when(carRepository.findByLikeBrandName("bmw")).thenReturn(List.of(car));
        assertThat(simpleCarService.findByLikeBrandName("bmw")).isEqualTo(List.of(car));
    }

    @Test
    public void findAll() {
        Car car1 = new Car(1, "vin15678901234567", null, null, null, null, Set.of());
        Car car2 = new Car(2, "vin25678901234567", null, null, null, null, Set.of());
        when(carRepository.findAll()).thenReturn(List.of(car1, car2));
        assertThat(simpleCarService.findAll()).isEqualTo(List.of(car1, car2));
    }

    @Test
    public void addOwner() {
    }

    @Test
    public void removeOwner() {
    }
}