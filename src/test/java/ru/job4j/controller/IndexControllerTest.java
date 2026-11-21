package ru.job4j.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class IndexControllerTest {
    private IndexController indexController;

    @BeforeEach
    void setUp() {
        indexController = new IndexController();
    }

    @Test
    public void whenIndexThenRedirectToPosts() {
        String view = indexController.index();
        assertThat(view).isEqualTo("redirect:/posts");
    }
}