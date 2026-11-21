package ru.job4j.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.model.Brand;
import ru.job4j.model.Model;
import ru.job4j.service.ModelService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelRestControllerTest {
    private ModelRestController modelRestController;
    private ModelService modelService;
    private Model model1;
    private Model model2;
    private Brand brand1;
    private Brand brand2;

    @BeforeEach
    void setUp() {
        modelService = mock(ModelService.class);
        modelRestController = new ModelRestController(modelService);

        brand1 = new Brand(1, "Toyota", List.of());
        model1 = new Model(0, "Camry", brand1);
        model2 = new Model(0, "Rav4", brand1);
        brand1.setModels(List.of(model1, model2));
    }

    @Test
    public void whenGetByBrandThenReceiveModelList() {
        when(modelService.findByBrandId(brand1.getId())).thenReturn(List.of(model1, model2));

        List<Model> rsl = modelRestController.getByBrand(brand1.getId());
        assertThat(rsl).hasSize(2)
                .extracting(Model::getName)
                .containsExactly("Camry", "Rav4");
    }

    @Test
    void whenGetByBrandWithNoModelsThenReturnEmptyList() {
        when(modelService.findByBrandId(666)).thenReturn(List.of());
        List<Model> rsl = modelRestController.getByBrand(666);
        assertThat(rsl).isEmpty();
    }
}