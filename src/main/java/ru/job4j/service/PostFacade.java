package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.dto.PostForm;
import ru.job4j.model.Car;
import ru.job4j.model.Engine;
import ru.job4j.model.Post;
import ru.job4j.model.User;

@Service
@AllArgsConstructor
public class PostFacade {
    private final CarCreationService carCreationService;
    private final PostService postService;
    private final FileService fileService;
    private final CarService carService;
    private final EngineService engineService;

    @Transactional
    public Post createFromForm(PostForm form, User user) {
        Car car = carCreationService.createFromForm(
                form.getVin(),
                form.getBrandId(),
                form.getModelId(),
                form.getColorId(),
                form.getFuelId(),
                form.getVolume(),
                form.getHorsePower()
        );

        Post post = new Post();
        post.setDescription(form.getDescription());
        post.setSold(false);
        post.setCar(car);
        post.setPhoto(fileService.save(form.getPhoto()));
        post.setUser(user);

        return postService.create(post);
    }

    @Transactional
    public Post update(Post post, Car car, Engine engine) {
        engineService.update(engine);
        carService.update(car);
        postService.update(post);
        return post;
    }
}
