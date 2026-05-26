package com.simao.blog.mapper;

import com.simao.blog.dto.PaginationDto;
import com.simao.blog.dto.PostRequestDto;
import com.simao.blog.dto.PostResponseDto;
import com.simao.blog.model.Post;
import org.springframework.data.domain.Page;

public class PostMapper {

    public static Post convertPostDtoToEntity(PostRequestDto postRequestDto) {
        return new Post(
                postRequestDto.title(),
                postRequestDto.content()
        );
    }

    public static PostResponseDto convertEntityToDtoResponse(Post post) {
        return new PostResponseDto(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }

    public static <T> PaginationDto<T> buildPaginatedResponse(Page<T> page) {
        return new PaginationDto<>(
            page.getContent(),
            page.getNumber(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.getSize(),
            page.hasNext(),
            page.hasPrevious()
        );

    }

}
