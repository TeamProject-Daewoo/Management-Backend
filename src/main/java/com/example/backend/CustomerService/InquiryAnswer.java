package com.example.backend.CustomerService;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "inquiry_answer")
public class InquiryAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String answerContent;

    private LocalDateTime answeredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_username", referencedColumnName = "user_name", nullable = false)
    private UserInquiryDto adminUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", unique = true)
    private Inquiry inquiry;

    // setter 추가
    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

    public void setAdminUser(UserInquiryDto adminUser) {
        this.adminUser = adminUser;
    }

    public static InquiryAnswer create(Inquiry inquiry, String answerContent, UserInquiryDto adminUser) {
        return InquiryAnswer.builder()
                .inquiry(inquiry)
                .answerContent(answerContent)
                .answeredAt(LocalDateTime.now())
                .adminUser(adminUser)
                .build();
    }
}
