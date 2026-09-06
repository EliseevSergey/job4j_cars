package ru.job4j.repository;

import lombok.AllArgsConstructor;
import ru.job4j.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PostRepository {
    private final CrudRepository crudRepository;

    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    public List<Post> findLastDay() {
        return crudRepository.query("FROM Post p WHERE p.created >= :fDate ORDER BY p.created DESC",
                Post.class,
                Map.of("fDate", LocalDateTime.now().minusDays(1)));
    }

    public List<Post> findWithPhoto() {
        return crudRepository.query("FROM Post p WHERE p.photo IS NOT NULL ORDER BY p.created DESC", Post.class);
    }

    public List<Post> findByBrand(String brand) {
        return crudRepository.query("FROM Post p WHERE LOWER(p.car.name) = LOWER(:fBrand) ORDER BY p.created DESC",
                Post.class, Map.of("fBrand", brand));
    }
}
