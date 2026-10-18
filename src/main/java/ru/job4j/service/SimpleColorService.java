package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.Color;
import ru.job4j.repository.ColorRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleColorService implements ColorService {
    private final ColorRepository colorRepository;

    @Override
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    public Optional<Color> findById(Integer colorId) {
        return colorRepository.findById(colorId);
    }
}
