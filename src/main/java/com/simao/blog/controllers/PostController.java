package com.simao.blog.controllers;

import com.simao.blog.dto.PostPaginationDto;
import com.simao.blog.dto.PostRequestDto;
import com.simao.blog.dto.PostResponseDto;
import com.simao.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequestDto));
    }

    @GetMapping
    public ResponseEntity<PostPaginationDto<PostResponseDto>> getAllPost(@RequestParam(name = "title", required = false) String title, Pageable p) {
        PostPaginationDto<PostResponseDto> posts = title != null ? postService.findPostByTitle(title, p) : postService.getAll(p);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable("postId") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@RequestBody PostRequestDto postRequestDto, @PathVariable("postId") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(postRequestDto, id));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long id) {
        postService.deletePost(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
