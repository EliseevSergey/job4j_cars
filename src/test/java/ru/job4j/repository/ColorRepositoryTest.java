package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.model.Color;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class ColorRepositoryTest {
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private Color color1;
    private Color color2;

    @BeforeEach
    void setUp() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Post").executeUpdate();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Color").executeUpdate();
            tx.commit();
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            color1 = new Color(null, "White");
            color2 = new Color(null, "Black");
            session.persist(color1);
            session.persist(color2);
            tx.commit();
        }
    }

    @Test
    public void whenFindAllThenReceiveAll() {
        List<Color> rsl = colorRepository.findAll();

        assertThat(rsl).hasSize(2)
                .extracting(Color::getName)
                .containsExactlyInAnyOrder("White", "Black");
    }

    @Test
    public void whenFindByIdThenReceiveColorWithId() {
        assertThat(colorRepository.findById(color1.getId())).
                isPresent()
                .contains(color1);

        assertThat(colorRepository.findById(color2.getId())).
                isPresent()
                .contains(color2);

        assertThat(colorRepository.findById(color1.getId()))
                .get()
                .extracting(Color::getName).isEqualTo("White");
    }

    @Test
    public void whenFindByIdWithNonExistentIdThenReturnEmpty() {
        assertThat(colorRepository.findById(666)).isEmpty();
    }
}