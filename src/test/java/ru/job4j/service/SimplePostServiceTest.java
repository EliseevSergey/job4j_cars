package ru.job4j.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.model.Post;
import ru.job4j.repository.PostRepository;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimplePostServiceTest {
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private SimplePostService simplePostService;
    private Post post = new Post();

    @Test
    public void whenCreateThenReturnSavedPost() {
        when(postRepository.create(post)).thenReturn(post);
        assertThat(simplePostService.create(post)).isEqualTo(post);
    }

    @Test
    public void whenFindByIdThenReturnPost() {
        post.setId(1);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        assertThat(simplePostService.findById(1)).isPresent().contains(post);
    }

    @Test
    public void whenFindByIdThenReturnEmptyIfNotFound() {
        when(postRepository.findById(666)).thenReturn(Optional.empty());
        assertThat(simplePostService.findById(666)).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnAllPosts() {
        Post post1 = new Post();
        Post post2 = new Post();
        when(postRepository.findAll()).thenReturn(List.of(post1, post2));
        assertThat(simplePostService.findAll()).isEqualTo(List.of(post1, post2));
    }

    @Test
    public void whenFindLastDayThenReturnPosts() {
        when(postRepository.findLastDay()).thenReturn(List.of(post));
        assertThat(simplePostService.findLastDay()).hasSize(1).contains(post);
    }

    @Test
    public void whenFindWithPhotoThenReturnPosts() {
        when(postRepository.findWithPhoto()).thenReturn(List.of(post));
        assertThat(simplePostService.findWithPhoto()).hasSize(1).contains(post);
    }

    @Test
    public void whenFindWithPhotoThenReturnEmpty() {
        when(postRepository.findWithPhoto()).thenReturn(List.of());
        assertThat(simplePostService.findWithPhoto()).isEmpty();
    }

    @Test
    public void findByBrand() {
        when(postRepository.findByBrand("Brand")).thenReturn(List.of(post));
        assertThat(simplePostService.findByBrand("Brand")).hasSize(1).contains(post);
    }

    @Test
    public void whenSetSoldThenReturnTrueIfUpdated() {
        when(postRepository.setSold(1, true)).thenReturn(true);
        assertThat(simplePostService.setSold(1, true)).isTrue();
    }

    @Test
    public void whenSetSoldThenReturnFalseIfNotFound() {
        when(postRepository.setSold(999, true)).thenReturn(false);
        assertThat(simplePostService.setSold(999, true)).isFalse();
    }

    @Test
    public void whenUpdateThenReturnUpdatedPost() {
        when(postRepository.update(post)).thenReturn(post);
        assertThat(simplePostService.update(post)).isEqualTo(post);
    }
}