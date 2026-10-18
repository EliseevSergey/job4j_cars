package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.model.Model;
import ru.job4j.service.ModelService;
import java.util.List;

@RestController
@RequestMapping("/api/models")
@AllArgsConstructor
public class ModelRestController {
    private final ModelService modelService;

    @GetMapping
    public List<Model> getByBrand(@RequestParam ("brandId") Integer brandId) {
        return modelService.findByBrandId(brandId);
    }
}
