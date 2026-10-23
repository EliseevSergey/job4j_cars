package ru.job4j.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.model.Post;
import ru.job4j.repository.PostRepository;

import java.time.LocalDateTime;
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

    @Test
    public void whenCreateThanReturnSavedPost() {
        Post post = new Post();
        when(postRepository.create(post)).thenReturn(post);
        assertThat(simplePostService.create(post)).isEqualTo(post);
    }

    @Test
    public void whenFindByIdThanReturnPost() {
        Post post = new Post();
        post.setId(1);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        assertThat(simplePostService.create(post)).isEqualTo(post);
    }

    @Test
    public void whenFindByIdThanReturnEmptyIfNotFound(){
        when(postRepository.findById(666)).thenReturn(Optional.empty());
        assertThat(simplePostService.findById(666).isEmpty());
    }

    @Test
    public void whenFindAllThanReturnAllPosts() {
        Post post1 = new Post();
        Post post2 = new Post();
        when(postRepository.findAll()).thenReturn(List.of(post1, post2));
        assertThat(simplePostService.findAll()).isEqualTo(List.of(post1, post2));
    }

    @Test
    public void whenFindLastDayThanReturnPosts() {
        Post post1 = new Post();
        when(postRepository.findLastDay()).thenReturn(List.of(post1));
        assertThat(simplePostService.findLastDay()).hasSize(1).contains(post1);
    }

    @Test
    public void findWithPhoto() {
    }

    @Test
    public void findByBrand() {
    }

    @Test
    public void whenSetSoldThanReturnTrueIfUpdated() {
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
        Post post = new Post();
        when(postRepository.update(post)).thenReturn(post);
        assertThat(simplePostService.update(post)).isEqualTo(post);
    }
}