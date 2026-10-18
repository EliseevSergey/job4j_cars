package ru.job4j.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.model.Brand;
import ru.job4j.model.Model;
import ru.job4j.repository.ModelRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimpleModelServiceTest {
    @Mock
    private ModelRepository modelRepository;
    @InjectMocks
    private SimpleModelService modelService;

    @Test
    public void whenFindAllThenReturnAllModels() {
        Brand brand = new Brand(1, "brand", List.of());
        Model model1 = new Model(1, "model1", brand);
        Model model2 = new Model(2, "model2", brand);
        List<Model> expected = List.of(model1, model2);
        when(modelRepository.findAll()).thenReturn(expected);
        assertThat(modelService.findAll()).isEqualTo(expected);
    }

    @Test
    public void whenFindByIdThenReturnModel() {
        Brand brand = new Brand(1, "brand", List.of());
        Model model = new Model(1, "model", brand);
        when(modelRepository.findById(1)).thenReturn(Optional.of(model));
        assertThat(modelService.findById(1)).isPresent().contains(model);
    }

    @Test
    public void whenFindByBrandIdThenReturnModelList() {
        Brand brand = new Brand(1, "brand", List.of());
        Model model1 = new Model(1, "model1", brand);
        Model model2 = new Model(2, "model2", brand);
        List<Model> expected = List.of(model1, model2);
        when(modelRepository.findByBrandId(1)).thenReturn(expected);
        assertThat(modelService.findByBrandId(1)).isEqualTo(expected);
    }

    @Test
    public void whenFindByIdThenReturnEmptyIfNotFind() {
        when(modelRepository.findById(666)).thenReturn(Optional.empty());
        assertThat(modelService.findById(666)).isEmpty();
    }

    @Test
    void whenFindByBrandIdThenReturnEmptyListIfNoModels() {
        when(modelRepository.findByBrandId(777)).thenReturn(List.of());
        assertThat(modelService.findByBrandId(777)).isEmpty();
    }
}