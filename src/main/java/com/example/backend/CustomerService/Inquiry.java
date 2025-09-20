package com.example.backend.CustomerService;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "inquiry")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "user_name", nullable = false)
    private UserInquiryDto user;

    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InquiryFile> inquiryFiles = new ArrayList<>();

    // 생성자 헬퍼 (첨부파일 관련 파라미터는 따로 처리)
    public static Inquiry create(String category, String title, String content, UserInquiryDto user) {
        return Inquiry.builder()
                .category(category)
                .title(title)
                .content(content)
                .status(InquiryStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }

    public void markAsAnswered() {
        this.status = InquiryStatus.ANSWERED;
    }
}
