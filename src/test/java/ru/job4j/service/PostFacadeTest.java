package ru.job4j.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dto.PostForm;
import ru.job4j.model.Car;
import ru.job4j.model.User;
import ru.job4j.model.Post;
import ru.job4j.dto.PostForm;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostFacadeTest {
    @InjectMocks
    private PostFacade postFacade;
    @Mock
    private CarCreationService carCreationService;
    @Mock
    private PostService postService;
    @Mock
    private FileService fileService;
    @Mock
    private CarService carService;
    @Mock
    private EngineService engineService;

    private Car car = new Car();
    private User user = new User();
    private PostForm postForm = new PostForm();
    private Post post = new Post();
    private MultipartFile photo;

    @Test
    void whenCreateFromFormThenSavedPost() {
        postForm.setDescription("description");
        postForm.setPhoto(photo);
        postForm.setVin("vin");
        postForm.setBrandId(1);
        postForm.setFuelId(1);
        postForm.setColorId(1);
        postForm.setVolume(1D);
        postForm.setModelId(1);
        postForm.setHorsePower(1);
        user.setId(1);
        when(carCreationService.createFromForm(
                "vin", 1, 1, 1, 1, 1D, 1)).thenReturn(car);
        when(fileService.save(photo)).thenReturn("/photo.jpg");

        Post result = postFacade.createFromForm(postForm, user);

        assertThat(result).isEqualTo(post);
        assertThat(result.getDescription()).isEqualTo("description");
        assertThat(result.getCar()).isEqualTo(car);
        assertThat(result.getPhoto()).isEqualTo("/photo.jpg");

    }

    @Test
    void whenUpdateThenReturnUpdatedPost() {
    }
}