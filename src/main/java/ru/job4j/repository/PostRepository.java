package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepository {
    private final CrudRepository crudRepository;

    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    public List<Post> findAll() {
        return crudRepository.query("FROM Post p "
                + "JOIN FETCH p.car c "
                + "JOIN FETCH c.brand "
                + "JOIN FETCH c.model "
                + "JOIN FETCH c.engine e "
                + "JOIN FETCH e.fuel "
                + "JOIN FETCH c.color "
                + "JOIN FETCH p.user "
                + "ORDER BY p.created DESC", Post.class);
    }

    public Optional<Post> findById(Integer postId) {
        return crudRepository.optional("FROM Post p "
                + "JOIN FETCH p.car c "
                + "JOIN FETCH c.brand "
                + "JOIN FETCH c.model "
                + "JOIN FETCH c.engine e "
                + "JOIN FETCH e.fuel "
                + "JOIN FETCH c.color "
                + "JOIN FETCH p.user "
                + "WHERE p.id = :fId", Post.class, Map.of("fId", postId));
    }

    public List<Post> findLastDay() {
        return crudRepository.query("FROM Post p "
                        + "JOIN FETCH p.car c "
                        + "JOIN FETCH c.brand "
                        + "JOIN FETCH c.model "
                        + "JOIN FETCH c.engine e "
                        + "JOIN FETCH e.fuel "
                        + "JOIN FETCH c.color "
                        + "JOIN FETCH p.user "
                        + "WHERE p.created >= :fDate ORDER BY p.created DESC",
                Post.class,
                Map.of("fDate", LocalDateTime.now().minusDays(1)));
    }

    public List<Post> findWithPhoto() {
        return crudRepository.query("FROM Post p "
                + "JOIN FETCH p.car c "
                + "JOIN FETCH c.brand "
                + "JOIN FETCH c.model "
                + "JOIN FETCH c.engine e "
                + "JOIN FETCH e.fuel "
                + "JOIN FETCH c.color "
                + "JOIN FETCH p.user "
                + "WHERE p.photo IS NOT NULL ORDER BY p.created DESC", Post.class);
    }

    public List<Post> findByBrand(String brand) {
        return crudRepository.query("FROM Post p "
                        + "JOIN FETCH p.car c "
                        + "JOIN FETCH c.brand "
                        + "JOIN FETCH c.model "
                        + "JOIN FETCH c.engine e "
                        + "JOIN FETCH e.fuel "
                        + "JOIN FETCH c.color "
                        + "JOIN FETCH p.user "
                        + "WHERE LOWER(c.brand.name) = LOWER(:fBrand) ORDER BY p.created DESC",
                Post.class, Map.of("fBrand", brand));
    }

    public List<Post> findByModel(String model) {
        return crudRepository.query("FROM Post p "
                        + "JOIN FETCH p.car c "
                        + "JOIN FETCH c.brand "
                        + "JOIN FETCH c.model "
                        + "JOIN FETCH c.engine e "
                        + "JOIN FETCH e.fuel "
                        + "JOIN FETCH c.color "
                        + "JOIN FETCH p.user "
                        + "WHERE LOWER(c.model.name) = LOWER(:fModel) ORDER BY p.created DESC",
                Post.class, Map.of("fModel", model));
    }

    public boolean setSold(Integer postId, boolean sold) {
        return crudRepository.execute("UPDATE Post p SET sold = :fSold WHERE p.id = :fPostId",
                Map.of("fPostId", postId, "fSold", sold)) > 0;
    }

    /*@Deprecated
    public boolean update(Post post) {
        return crudRepository.execute("UPDATE Post p SET "
                + "p.description = :fDescription, "
                + "p.car = :fCar, "
                + "p.photo = :fPhoto, "
                + "p.sold = :fSold "
                + "WHERE p.id = :fId ", Map.of(
                "fId", post.getId(),
                "fDescription", post.getDescription(),
                "fCar", post.getCar(),
                "fPhoto", post.getPhoto(),
                "fSold", post.isSold())) > 0;
    }*/

    public Post update(Post post) {
        crudRepository.tx(session -> session.merge(post));
        return post;
    }
}