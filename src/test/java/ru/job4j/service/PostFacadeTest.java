package ru.job4j.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dto.PostForm;
import ru.job4j.model.Car;
import ru.job4j.model.Engine;
import ru.job4j.model.User;
import ru.job4j.model.Post;
import ru.job4j.dto.PostForm;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private MultipartFile photo = mock(MultipartFile.class);

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

        when(postService.create(any(Post.class))).then(inv -> inv.getArgument(0));

        postFacade.createFromForm(postForm, user);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postService).create(captor.capture());
        Post captured = captor.getValue();

        assertThat(captured.getDescription()).isEqualTo("description");
        assertThat(captured.getCar()).isEqualTo(car);
        assertThat(captured.getPhoto()).isEqualTo("/photo.jpg");
        assertThat(captured.getUser()).isEqualTo(user);
        assertThat(captured.isSold()).isFalse();
    }

    @Test
    void whenUpdateThenReturnUpdatedPost() {
        Engine engine = new Engine();
        Car car = new Car();
        Post post = new Post();

        when(engineService.update(engine)).thenReturn(engine);
        when(carService.update(car)).thenReturn(car);
        when(postService.update(post)).thenReturn(post);

        assertThat(postFacade.update(post, car, engine)).isEqualTo(post);

        InOrder inOrder = inOrder(engineService, carService, postService);
        inOrder.verify(postService).update(post);
        inOrder.verify(carService).update(car);
        inOrder.verify(engineService).update(engine);
    }
}