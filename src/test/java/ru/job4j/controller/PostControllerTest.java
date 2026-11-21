package ru.job4j.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.model.*;
import ru.job4j.service.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostControllerTest {
    private PostController postController;
    private PostFacade postFacade;
    private PostService postService;
    private BrandService brandService;
    private ColorService colorService;
    private FuelService fuelService;
    private ModelService modelService;
    private FileService fileService;

    private User user;
    private Car car;
    private Post postWithPhoto;
    private Post postWithoutPhoto;
    private Post outOfLastDayPost;

    @BeforeEach
    void setUp() {
        postFacade = mock(PostFacade.class);
        postService = mock(PostService.class);
        brandService = mock(BrandService.class);
        colorService = mock(ColorService.class);
        fuelService = mock(FuelService.class);
        modelService = mock(ModelService.class);
        fileService = mock(FileService.class);

        postController =
                new PostController(
                        postFacade, postService, brandService, colorService, fuelService, modelService, fileService);

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

        postWithPhoto = new Post();
        postWithPhoto.setCar(car1);
        postWithPhoto.setDescription("С фото");
        postWithPhoto.setPhoto("/photo.jpg");
        postWithPhoto.setSold(false);
        postWithPhoto.setUser(user);
        postWithPhoto.setCreated(LocalDateTime.now());

        postWithoutPhoto = new Post();
        postWithoutPhoto.setDescription("Без фото");
        postWithoutPhoto.setPhoto(null);
        postWithoutPhoto.setSold(false);
        postWithoutPhoto.setCar(car2);
        postWithoutPhoto.setUser(user);
        postWithoutPhoto.setCreated(LocalDateTime.now());

        outOfLastDayPost = new Post();
        outOfLastDayPost.setDescription("Старый пост");
        outOfLastDayPost.setPhoto(null);
        outOfLastDayPost.setSold(false);
        outOfLastDayPost.setCar(car3);
        outOfLastDayPost.setUser(user);

        outOfLastDayPost.setCreated(LocalDateTime.of(2000, 1, 1, 1, 1));
    }

    @Test
    public void whenGetAllThenReceiveAllList() {
        List<Post> expected = List.of(postWithPhoto, postWithoutPhoto, outOfLastDayPost);
        when(postService.findAll()).thenReturn(expected);
        ConcurrentModel model = new ConcurrentModel();

        String view = postController.getAll(model);
        assertThat(view).isEqualTo("posts/list");

        List<Post> actualPost = (List<Post>) model.getAttribute("posts");

        assertThat(actualPost).hasSize(3).extracting(Post::getDescription)
                .containsExactly("С фото", "Без фото", "Старый пост");
    }

    @Test
    void createForm() {
    }

    @Test
    void create() {
    }

    @Test
    void getViewPageById() {
    }

    @Test
    void markAsSold() {
    }

    @Test
    void markAsUnsold() {
    }

    @Test
    void getEditPageById() {
    }

    @Test
    void edit() {
    }

    @Test
    void getLastDayList() {
    }

    @Test
    void getWithPhoto() {
    }
}