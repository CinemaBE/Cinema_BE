package com.cinema.project.dto;

import com.cinema.project.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private String content;
    private String writer;
    private LocalDateTime createdAt;

    public ReviewResponseDto(Review review) {
        this.content = review.getContent();
        this.writer = review.getWriter();
        this.createdAt = review.getCreatedAt();
    }
}
