package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.model.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private User user;
    private Car car;

    @BeforeEach
    public void setUp() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM Post").executeUpdate();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.createQuery("DELETE FROM Fuel").executeUpdate();
            session.createQuery("DELETE FROM Model").executeUpdate();
            session.createQuery("DELETE FROM Brand").executeUpdate();
            session.createQuery("DELETE FROM Color").executeUpdate();
            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Brand brand = new Brand(null, "Toyota", List.of());
            Model model = new Model(null, "name", brand);
            Fuel fuel = new Fuel(null, "Petrol");
            Engine engine = new Engine(null, 2.5, fuel, 180);
            Color color = new Color(null, "Black");
            user = new User(null, "login@user", "pass", "Test User", "UTC", List.of());

            car = new Car(null, "VIN0", brand, model, engine, color, null);
            Car car1 = new Car(null, "VIN1", brand, model, engine, color, null);
            Car car2 = new Car(null, "VIN2", brand, model, engine, color, null);

            session.persist(brand);
            session.persist(model);
            session.persist(fuel);
            session.persist(engine);
            session.persist(color);
            session.persist(user);

            session.persist(car);
            session.persist(car1);
            session.persist(car2);

            Post postWithPhoto = new Post();
            postWithPhoto.setCar(car1);
            postWithPhoto.setDescription("С фото");
            postWithPhoto.setPhoto("/photo.jpg");
            postWithPhoto.setSold(false);
            postWithPhoto.setCar(car1);
            postWithPhoto.setUser(user);
            postWithPhoto.setCreated(LocalDateTime.now());
            session.persist(postWithPhoto);

            Post postWithoutPhoto = new Post();
            postWithoutPhoto = new Post();
            postWithoutPhoto.setDescription("Без фото");
            postWithoutPhoto.setPhoto(null);
            postWithoutPhoto.setSold(false);
            postWithoutPhoto.setCar(car2);
            postWithoutPhoto.setUser(user);
            postWithoutPhoto.setCreated(LocalDateTime.now());
            session.persist(postWithoutPhoto);

            tx.commit();
        }
    }

    @Test
    public void whenCreatePostThenReceiveSavedPost() {
        Post post = new Post();
        post.setUser(user);
        post.setCar(car);
        post.setDescription("description");
        post.setPhoto("/photo.jpg");

        Post savedPost = postRepository.create(post);

        assertThat(savedPost.getId()).isGreaterThan(0);
        assertThat(savedPost.getDescription()).isEqualTo("description");
        assertThat(savedPost.getPhoto()).isEqualTo("/photo.jpg");
        assertThat(savedPost.getCar().getVin()).isEqualTo("VIN0");
        assertThat(savedPost.getUser().getLogin()).isEqualTo("login@user");
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findLastDay() {
    }

    @Test
    public void whenFindWithPhotoThenReceiveOnlyWithPhoto() {
        List<Post> rsl = postRepository.findWithPhoto();
        assertThat(rsl).hasSize(1);

        assertThat(rsl.get(0).getDescription()).isEqualTo("С фото");
        assertThat(rsl.get(0).getPhoto()).isEqualTo("/photo.jpg");
    }

    @Test
    void findByBrand() {
    }

    @Test
    void findByModel() {
    }

    @Test
    void setSold() {
    }

    @Test
    void update() {
    }
}