package ru.job4j.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.model.Post;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public interface PostService {
    Post create(Post post);

    List<Post> findAll();

    Optional<Post> findById(Integer postId);

    List<Post> findLastDay();
    List<Post> findWithPhoto();
    List<Post> findByBrand(String brand);
    Post update(Post post);
    boolean setSold(Integer postId, boolean sold);
}
