package com.simao.blog.dto;

import jakarta.validation.constraints.NotBlank;

public record PostRequestDto(@NotBlank String title, @NotBlank String content) {
}
