package com.example.backend.announcement;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDTO {
    private Long id;
    private String category;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
