package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.model.*;

@Service
@AllArgsConstructor
public class CarCreationService {
    private final CarService carService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final EngineService engineService;
    private final FuelService fuelService;
    private final ColorService colorService;

    @Transactional
    public Car createFromForm(String vin, Integer brandId, Integer modelId,
                              Integer colorId, Integer fuelId, Double volume, Integer horsePower) {

        Fuel fuel = fuelService.findById(fuelId).orElseThrow(
                () -> new IllegalArgumentException("Fuel not found " + fuelId));
        Engine engine = new Engine();
        engine.setFuel(fuel);
        engine.setVolume(volume);
        engine.setHorsePower(horsePower);
        engineService.create(engine);

        Brand brand = brandService.findById(brandId).orElseThrow(
                () -> new IllegalArgumentException("Brand not found " + brandId));

        Model model = modelService.findById(modelId).orElseThrow(
                () -> new IllegalArgumentException("Model not found " + modelId));

        Color color = colorService.findById(colorId).orElseThrow(
                () -> new IllegalArgumentException("Color not found " + colorId));

        Car car = new Car();
        car.setVin(vin);
        car.setBrand(brand);
        car.setModel(model);
        car.setEngine(engine);
        car.setColor(color);

        return carService.create(car);
    }
}
