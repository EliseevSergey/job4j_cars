package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.model.Post;
import ru.job4j.repository.PostRepository;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class SimplePostService implements PostService {
    private final PostRepository postRepository;

    @Override
    public Post create(Post post) {
        return postRepository.create(post);
    }

    @Override
    public Optional<Post> findById(Integer postId) {
        return postRepository.findById(postId);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findLastDay() {
        return postRepository.findLastDay();
    }

    @Override
    public List<Post> findWithPhoto() {
        return postRepository.findWithPhoto();
    }

    @Override
    public List<Post> findByBrand(String brand) {
        return postRepository.findByBrand(brand);
    }

    @Override
    public boolean setSold(Integer postId, boolean sold) {
        return postRepository.setSold(postId, sold);
    }

    @Override
    public Post update(Post post) {
        return postRepository.update(post);
    }
}
