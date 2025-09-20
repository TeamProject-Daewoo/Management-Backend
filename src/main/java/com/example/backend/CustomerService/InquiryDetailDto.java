package com.example.backend.CustomerService;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InquiryDetailDto {

    private Long id;
    private String category;
    private String title;
    private String content;
    private List<InquiryFileDto> inquiryFiles;  // 첨부파일 리스트로 변경
    private LocalDateTime createdAt;
    private String status;

    private String answerContent;
    private LocalDateTime answeredAt;
    private String adminUsername;

    // 첨부파일 DTO (파일명, 경로, id)
    @Getter
    public static class InquiryFileDto {
        private Long id;
        private String fileName;
        private String filePath;

        public InquiryFileDto(Long id, String fileName, String filePath) {
            this.id = id;
            this.fileName = fileName;
            this.filePath = filePath;
        }
    }

    // 엔티티 -> DTO 변환 메서드
    public static InquiryDetailDto from(Inquiry inquiry, InquiryAnswer answer) {
        InquiryDetailDto dto = new InquiryDetailDto();
        dto.id = inquiry.getId();
        dto.category = inquiry.getCategory();
        dto.title = inquiry.getTitle();
        dto.content = inquiry.getContent();

        // InquiryFile 리스트를 InquiryFileDto 리스트로 변환
        dto.inquiryFiles = inquiry.getInquiryFiles()
                .stream()
                .map(f -> new InquiryFileDto(f.getId(), f.getFileName(), f.getFilePath()))
                .collect(Collectors.toList());

        dto.createdAt = inquiry.getCreatedAt();
        dto.status = inquiry.getStatus().name();

        if (answer != null) {
            dto.answerContent = answer.getAnswerContent();
            dto.answeredAt = answer.getAnsweredAt();
            dto.adminUsername = answer.getAdminUser().getUsername();
        }

        return dto;
    }
}
