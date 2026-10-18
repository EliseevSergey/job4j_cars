package ru.job4j.service;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.model.*;
import ru.job4j.repository.BrandRepository;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@NoArgsConstructor
@ExtendWith(MockitoExtension.class)
public class SimpleBrandServiceTest {
    @Mock
    private BrandRepository brandRepository;
    @InjectMocks
    private SimpleBrandService brandService;

    @Test
    public void whenFindAllThenReturnAllBrands() {
        List<Model> models1 = new ArrayList<>();
        Brand brand1 = new Brand(1, "brand1", models1);
        Model model1 = new Model(1, "model1", brand1);
        Model model2 = new Model(2, "model2", brand1);
        models1 = List.of(model1, model2);

        List<Model> models2 = new ArrayList<>();

        Brand brand2 = new Brand(2, "brand2", models2);
        Model model3 = new Model(3, "model1", brand2);
        Model model4 = new Model(4, "model2", brand2);

        List<Brand> brands1 = List.of(brand1, brand2);

        when(brandRepository.findAll()).thenReturn(brands1);

        assertThat(brandService.findAll()).isEqualTo(brands1);
    }

    /**
     * Проверяет, что сервис возвращает все бренды из репозитория.
     * сервис элементарный - проверяет что делериует в верный репозиторий
     * Сценарий: репозиторий вернул список из двух брендов —
     * сервис должен вернуть тот же список без изменений.
     */


    @Test
    void whenFindByIdThenReturnBrand() {
        Brand brand = new Brand(1, "brand", List.of());
        when(brandRepository.findById(1)).thenReturn(Optional.of(brand));
        assertThat(brandService.findById(1)).isPresent().contains(brand);
    }

    @Test
    void whenFindByIdThenReturnEmptyIfNotFound() {
        when(brandRepository.findById(666)).thenReturn(Optional.empty());
        assertThat(brandService.findById(666)).isEmpty();
    }


    /* List<Model> models1 = new ArrayList<>();

        Brand brand1 = new Brand(1, "brand1", models1);

        Model model1 = new Model(1, "model1", brand1);
        Model model2 = new Model(2, "model2", brand1);
        models1 = List.of(model1, model2);

        Fuel fuel = new Fuel(1, "Diesel");
        Engine engine1 = new Engine(1, 1.5D, fuel, 100);
        Color color1 = new Color(1, "Green");

        List<Post> posts1 = new ArrayList<>();

        User user1 = new User(1, "login1", "pas", "name", "UTC", posts1);

        Set<Owner> owners1 = new HashSet<>();
        Owner owner1 = new Owner(1, "Owner1", user1);
        owners1.add(owner1);

        Car car1 = new Car(1, "vin45678901234567", brand1, model1, engine1, color1, owners1);

        List<Post> posts2 = new ArrayList<>();

        List<Model> models2 = new ArrayList<>();

        Brand brand2 = new Brand(2, "brand2", models2);
        Model model3 = new Model(3, "model1", brand2);
        Model model4 = new Model(4, "model2", brand2);

        Set<Owner> owners2 = new HashSet<>();

        User user2 = new User(2, "login2", "pas", "name", "UTC", posts2);
        Owner owner2 = new Owner(2, "Owner2", user2);
        owners2.add(owner2);

        Car car2 = new Car(2, "vin45678901234567", brand1, model1, engine1, color1, owners2);
        List<Brand> brands1 = List.of(brand1, brand2);*/
}