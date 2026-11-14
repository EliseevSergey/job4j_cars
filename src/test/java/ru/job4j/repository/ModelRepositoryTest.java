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

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class ModelRepositoryTest {
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private Model model1;
    private Model model2;
    private Model model3;
    private Model model4;

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
            brand1 = new Brand(null, "Toyota", null);
            brand2 = new Brand(null, "BMW", null);

            model1 = new Model(null, "Camry", brand1);
            model2 = new Model(null, "Land Cruiser", brand1);

            model3 = new Model(null, "X1", brand2);
            model4 = new Model(null, "X5", brand2);

            session.persist(brand1);
            session.persist(brand2);
            session.persist(model1);
            session.persist(model2);
            session.persist(model3);
            session.persist(model4);

            tx.commit();
        }
    }

    @Test
    public void whenFindAllThenReceiveAllModels() {
        List<Model> rsl = modelRepository.findAll();
        assertThat(rsl).hasSize(4).containsExactlyInAnyOrder(model1, model2, model3, model4);

        assertThat(rsl).extracting(m -> m.getBrand().getName()).containsOnly("Toyota", "BMW");
    }

    @Test
    public void whenFindByIdThenReceiveModelWithThisId() {
        Optional<Model> rsl = modelRepository.findById(model2.getId());
        assertThat(rsl).isPresent().get().isEqualTo(model2);
        assertThat(rsl)
                .isPresent()
                .get().extracting(Model::getName, m -> m.getBrand().getName())
                        .containsExactly("Land Cruiser", "Toyota");
    }

    @Test
    public void whenFindByIdWithNonExistentIdThenReturnEmpty() {
        assertThat(modelRepository.findById(666)).isEmpty();
    }

    @Test
    public void whenFindByBrandIdThenReceiveModelList() {
        List<Model> rsl1 = modelRepository.findByBrandId(brand1.getId());
        assertThat(rsl1).hasSize(2).containsExactlyInAnyOrder(model1, model2);


        List<Model> rsl2 = modelRepository.findByBrandId(brand2.getId());
        assertThat(rsl2).hasSize(2).containsExactlyInAnyOrder(model3, model4);
    }
}