package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.model.Brand;
import ru.job4j.model.Model;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class BrandRepositoryTest {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private Brand brand1;
    private Brand brand2;

    @BeforeEach
    void setUp() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Post").executeUpdate();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Model").executeUpdate();
            session.createQuery("DELETE FROM Brand").executeUpdate();
            tx.commit();
        }


        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            brand1 = new Brand(null, "Toyota", List.of());
            Model model1 = new Model(null, "Camry", brand1);
            Model model2 = new Model(null, "LandCruiser", brand1);
            brand1.setModels(List.of(model1, model2));

            brand2 = new Brand(null, "BMW", List.of());
            Model model3 = new Model(null, "X5", brand2);
            Model model4 = new Model(null, "X3", brand2);
            brand2.setModels(List.of(model3, model4));

            session.persist(brand1);
            session.persist(brand2);

            tx.commit();
        }
    }

    @Test
    public void whenFindAllBrandsThenReceiveAllBrands() {
        List<Brand> rsl = brandRepository.findAll();
        assertThat(rsl).hasSize(2).containsExactly(brand2, brand1);
    }

    @Test
    public void whenFindAllBrandsWithModelsThenReceiveAllBrandsWithFetchedModels() {
        List<Brand> rsl = brandRepository.findAllWithModels();
        assertThat(rsl).hasSize(2).containsExactly(brand2, brand1)
                .extracting(Brand::getName, b -> b.getModels().stream().map(Model::getName).toList())
                .containsExactlyInAnyOrder(
                        tuple("BMW", List.of("X3", "X5")),
                        tuple("Toyota", List.of("Camry", "LandCruiser")));
    }

    @Test
    public void whenFindByIdThenReceiveBrandWithId() {
        Optional<Brand> rsl = brandRepository.findById(brand1.getId());
        assertThat(rsl).isPresent().get().isEqualTo(brand1);

        assertThat(rsl).isPresent().get()
                .extracting(Brand::getName, b -> b.getModels().stream().map(Model::getName).toList())
                .containsExactly("Toyota", List.of("Camry", "LandCruiser"));

        assertThat(brandRepository.findById(brand2.getId())).isPresent().get().extracting(Brand::getName)
                .isEqualTo("BMW");
    }

    @Test
    public void whenFindByNotExistedIdThenReceiveEmpty() {
        assertThat(brandRepository.findById(666)).isEmpty();
    }
}