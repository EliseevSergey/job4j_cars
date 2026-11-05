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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SessionFactory sessionFactory;
    private User user;
    private Car car;
    private Post postWithPhoto;
    private Post postWithoutPhoto;

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
            Brand brand1 = new Brand(null, "Toyota", List.of());
            Brand brand2 = new Brand(null, "BMW", List.of());
            Model model1 = new Model(null, "Camry", brand1);
            Model model2 = new Model(null, "X5", brand2);
            Fuel fuel = new Fuel(null, "Petrol");
            Engine engine = new Engine(null, 2.5, fuel, 180);
            Color color = new Color(null, "Black");
            user = new User(null, "login@user", "pass", "Test User", "UTC", List.of());

            car = new Car(null, "VIN0", brand1, model1, engine, color, null);
            Car car1 = new Car(null, "VIN1", brand1, model1, engine, color, null);
            Car car2 = new Car(null, "VIN2", brand2, model2, engine, color, null);
            Car car3 = new Car(null, "VIN3", brand2, model2, engine, color, null);

            session.persist(brand1);
            session.persist(brand2);
            session.persist(model1);
            session.persist(model2);
            session.persist(fuel);
            session.persist(engine);
            session.persist(color);
            session.persist(user);

            session.persist(car);
            session.persist(car1);
            session.persist(car2);
            session.persist(car3);

            postWithPhoto = new Post();
            postWithPhoto.setCar(car1);
            postWithPhoto.setDescription("С фото");
            postWithPhoto.setPhoto("/photo.jpg");
            postWithPhoto.setSold(false);
            postWithPhoto.setUser(user);
            postWithPhoto.setCreated(LocalDateTime.now());
            session.persist(postWithPhoto);

            postWithoutPhoto = new Post();
            postWithoutPhoto.setDescription("Без фото");
            postWithoutPhoto.setPhoto(null);
            postWithoutPhoto.setSold(false);
            postWithoutPhoto.setCar(car2);
            postWithoutPhoto.setUser(user);
            postWithoutPhoto.setCreated(LocalDateTime.now());

            session.persist(postWithoutPhoto);

            Post outOfLastDayPost = new Post();
            outOfLastDayPost.setDescription("Старый пост");
            outOfLastDayPost.setPhoto(null);
            outOfLastDayPost.setSold(false);
            outOfLastDayPost.setCar(car3);
            outOfLastDayPost.setUser(user);

            outOfLastDayPost.setCreated(LocalDateTime.of(2000, 1, 1, 1, 1));
            session.persist(outOfLastDayPost);

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
    public void whenFindAllThenReceiveAll() {
        List<Post> saved = postRepository.findAll();
        assertThat(saved).hasSize(3);
    }

    @Test
    public void whenFindByIdThenReceivedPost() {
        Optional<Post> rsl = postRepository.findById(postWithPhoto.getId());
        assertThat(rsl).isPresent();
        assertThat(postWithPhoto.getId()).isEqualTo(rsl.get().getId());
        assertThat(rsl.get().getDescription()).isEqualTo("С фото");
    }

    @Test
    public void whenFindLastDayWhenReceiveLastDay() {
        List<Post> rsl = postRepository.findLastDay();
        assertThat(rsl).hasSize(2);

        List<String> descriptions = rsl.stream().map(post -> post.getDescription()).toList();
        assertThat(descriptions).doesNotContain("Старый пост");
        assertThat(descriptions).containsExactlyInAnyOrder("Без фото", "С фото");
    }

    @Test
    public void whenFindWithPhotoThenReceiveOnlyWithPhoto() {
        List<Post> rsl = postRepository.findWithPhoto();
        assertThat(rsl).hasSize(1);
        assertThat(rsl.get(0).getDescription()).isEqualTo("С фото");
        assertThat(rsl.get(0).getPhoto()).isEqualTo("/photo.jpg");
    }

    @Test
    public void whenFindByBrandThenReceiveWithThisBrand() {
        List<Post> rsl = postRepository.findByBrand("bmw");
        assertThat(rsl).hasSize(2);
    }

    @Test
    public void whenFindByModelThenReceiveWithThisModel() {
        List<Post> rsl = postRepository.findByModel("camry");
        assertThat(rsl).hasSize(1);
    }

    @Test
    public void whenSetSoldThenChangeStatusSold() {
        boolean rsl = postRepository.setSold(postWithPhoto.getId(), true);

        try (Session session = sessionFactory.openSession()) {
            Post updatedPost = session.get(Post.class, postWithPhoto.getId());
            assertThat(updatedPost.isSold()).isTrue();
        }
    }

    @Test
    public void whenUpdateThenPostIsUpdated() {
        postWithPhoto.setDescription("Новое описание");
        postRepository.update(postWithPhoto);

        try (Session session = sessionFactory.openSession()) {
            Post updatedPost = session.get(Post.class, postWithPhoto.getId());
            assertThat(updatedPost.getDescription()).isEqualTo("Новое описание");
        }
    }
}