package com.simao.blog.service;

import com.simao.blog.dto.PaginationDto;
import com.simao.blog.dto.PostRequestDto;
import com.simao.blog.dto.PostResponseDto;
import com.simao.blog.exceptions.PostNotFound;
import com.simao.blog.model.Post;
import com.simao.blog.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

@DisplayName("PostService - Unit test")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    // Helpers object
    private Post postSalvo;
    private PostRequestDto requestDto;
    private Pageable pageable;

    @BeforeEach
    void setup() {
        pageable = PageRequest.of(0, 10);
        postSalvo = new Post("Test with spring boot", "This is gonna be cool, just persist you can don't give up");
        postSalvo.setId(1L);
        postSalvo.setCreatedAt(LocalDateTime.now());
        postSalvo.setUpdatedAt(LocalDateTime.now());

        requestDto = new PostRequestDto("Test with spring boot", "This is gonna be cool, just persist you can don't give up");
    }

    @Nested
    @DisplayName("CreatePost()")
    class CreatePost {

        @Test
        @DisplayName("Should create and return the correctly data of post")
        void shouldCreatePost() {
            // Arrange
            when(postRepository.save(any(Post.class))).thenReturn(postSalvo);

            // Act
            PostResponseDto response = postService.createPost(requestDto);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("Test with spring boot");
            assertThat(response.content()).isEqualTo("This is gonna be cool, just persist you can don't give up");

            // Verify
            verify(postRepository, times(1)).save(any(Post.class));
        }
    }

    @Nested
    @DisplayName("GetAll()")
    class GetAll {

        @Test
        @DisplayName("Should return all posts when exist")
        void shouldReturnAllPosts() {
            // Arrange
            Page<Post> paginaMock = new PageImpl<>(List.of(postSalvo), pageable, 1);
            when(postRepository.findAll(pageable)).thenReturn(paginaMock);

            // Act
            PaginationDto<PostResponseDto> result = postService.getAll(pageable);

            // Assert
            assertThat(result.getData()).hasSize(1);
            assertThat(result.getTotalItems()).isEqualTo(1);
            assertThat(result.getCurrentPage()).isEqualTo(0);
            assertThat(result.isHasNext()).isFalse();
            assertThat(result.isHasPrevious()).isFalse();

        }

        @Test
        @DisplayName("Should return empty page when not has posts")
        void shouldReturnEmptyPage() {
            // Arrange
            Page<Post> emptyPage = new PageImpl<>(List.of(), pageable, 0);
            when(postRepository.findAll(pageable)).thenReturn(emptyPage);

            // Act
            PaginationDto<PostResponseDto> result = postService.getAll(pageable);

            // Assert
            assertThat(result.getData()).isEmpty();
            assertThat(result.getTotalItems()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("GetPostById()")
    class GetPostById {

        @Test
        @DisplayName("Should return post when exist")
        void shouldReturnPostWhenExist() {
            // Arrange
            when(postRepository.findById(1L)).thenReturn(Optional.of(postSalvo));

            // Act
            PostResponseDto response = postService.getPostById(1L);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("Test with spring boot");
        }

        @Test
        @DisplayName("Throw exception when post do not exist")
        void throwExceptionWhenNotExistPost() {
            // Arrange
            when(postRepository.findById(0L)).thenReturn(Optional.empty());

            // act
            assertThatThrownBy(() -> postService.getPostById(0L))
                    .isInstanceOf(PostNotFound.class)
                    .hasMessageContaining("0");
        }
    }

    @Nested
    @DisplayName("UpdatePost()")
    class UpdatePost {
        @Test
        @DisplayName("Should update title and content when both are provided")
        void shouldUpdateTitleAndContent() {
            // Arrange
            PostRequestDto newData = new PostRequestDto("New title", "New content");
            when(postRepository.findById(1L)).thenReturn(Optional.of(postSalvo));
            when(postRepository.save(any(Post.class))).thenReturn(postSalvo);

            // act
            postService.updatePost(newData, 1L);

            // Assert
            assertThat(postSalvo.getTitle()).isEqualTo("New title");
            assertThat(postSalvo.getContent()).isEqualTo("New content");
        }

        @Test
        @DisplayName("Should throw exception when not found post by ID")
        void shouldThrowExceptionWhenPostNotFoundById() {
            // Arrange
            when(postRepository.findById(0L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.updatePost(requestDto, 0L))
                    .isInstanceOf(PostNotFound.class)
                    .hasMessageContaining("0");
        }
    }

    @Nested
    @DisplayName("DeletePost()")
    class DeletePost {

        @Test
        @DisplayName("Should delete post when ID exist")
        void shouldDeletePostById() {
            //Arrange
            when(postRepository.existsById(1L)).thenReturn(true);

            doNothing().when(postRepository).deleteById(1L);

            postService.deletePost(1L);
            verify(postRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw PostNotFound when try delete ID non-existent")
        void shouldThrowExceptionWhenIdNonExist() {
            when(postRepository.existsById(0L)).thenReturn(false);

            assertThatThrownBy(() -> postService.deletePost(0L))
                    .isInstanceOf(PostNotFound.class)
                    .hasMessageContaining("0");

            verify(postRepository, never()).deleteById(anyLong());
        }
    }
}