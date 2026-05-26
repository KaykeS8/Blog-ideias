package com.simao.blog.service;

import com.simao.blog.dto.PaginationDto;
import com.simao.blog.dto.PostRequestDto;
import com.simao.blog.dto.PostResponseDto;
import com.simao.blog.exceptions.PostNotFound;
import com.simao.blog.mapper.PostMapper;
import com.simao.blog.model.Post;
import com.simao.blog.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final Logger log = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        log.info("Creating post");
        Post post = postRepository.save(PostMapper.convertPostDtoToEntity(postRequestDto));
        return PostMapper.convertEntityToDtoResponse(post);
    }

    public PaginationDto<PostResponseDto> getAll(Pageable p) {
        log.info("List all posts");
        Page<PostResponseDto> posts = postRepository.findAll(p).map(PostMapper::convertEntityToDtoResponse);
        return PostMapper.buildPaginatedResponse(posts);
    }

    public PaginationDto<PostResponseDto> findPostByTitle(String title, Pageable p) {
        log.info("Get post by title: {}", title);
        Page<PostResponseDto> posts = postRepository.findByTitleContainingIgnoreCase(title, p).map(PostMapper::convertEntityToDtoResponse);
        if (posts.getContent().isEmpty()) throw new PostNotFound("Post not found with title: " + title);
        return PostMapper.buildPaginatedResponse(posts);
    }

    public PostResponseDto getPostById(Long id) {
        log.info("Get post by ID: {}", id);
        return postRepository.findById(id)
                .map(PostMapper::convertEntityToDtoResponse)
                .orElseThrow(() -> new PostNotFound("Post not found with ID: " + id));
    }

    public PostResponseDto updatePost(PostRequestDto postRequestDto, Long id) {
        log.info("Update post with ID: {}", id);
        return postRepository.findById(id)
                .map(post -> {
                    if(postRequestDto.title() != null) post.setTitle(postRequestDto.title());
                    if(postRequestDto.content() != null) post.setContent(postRequestDto.content());
                    return PostMapper.convertEntityToDtoResponse(postRepository.save(post));
                }).orElseThrow(() -> new PostNotFound("Post not found with ID: " + id));
    }

    public void deletePost(Long id) {
        log.info("Delete post with ID: {}", id);
        if (!postRepository.existsById(id)) throw new PostNotFound("Post not found with ID: " + id);
        postRepository.deleteById(id);
    }
}
