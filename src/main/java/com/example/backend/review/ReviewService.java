package com.example.backend.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByHotel(String hotelId) {
        return reviewRepository.findByHotelContentid(hotelId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByUser(String username) {
        return reviewRepository.findByUserUsernameAndIsDeletedFalseOrderByCreatedAtDesc(username).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private ReviewResponseDto convertToDto(Review review) {
        long visitCount = reviewRepository.countReservationsByUserAndHotelBeforeDate(
            review.getUser().getUsername(),
            review.getHotel().getContentid(),
            review.getReservation().getCheckInDate()
        );

        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .hotelId(review.getHotel().getContentid())
                .hotelName(review.getHotel().getTitle())
                .userName(review.getUser().getName())
                .reviewText(review.getContent())
                .rating(review.getRating())
                .reviewDate(review.getCreatedAt())
                .imageUrl(review.getImageUrl())
                .visitCount(visitCount) // 계산된 방문 횟수 추가
                .build();
    }

    public List<AdminReviewResponseDto> getReviewList(boolean show, String searchTerm) {
        List<Review> reviews = reviewRepository.findAllViewable(show, searchTerm);
        return reviews.stream()
            .map(AdminReviewResponseDto::new)
            .collect(Collectors.toList());
    }

    public Integer deleteReviewsById(String id, boolean isDelete) {
        return reviewRepository.softDeleteById(id, isDelete);
    }
     public Integer deleteReviewsByIds(List<String> id, boolean isDelete) {
        return reviewRepository.softDeleteAllByIds(id, isDelete);
    }

     public List<BussinessReviewResponseDto> findByHotelId(String hotelId, String searchTerm) {
        List<Review> reviews = reviewRepository.findByContentIdWithKeyword(hotelId, searchTerm);
        return reviews.stream()
            .map(BussinessReviewResponseDto::new)
            .collect(Collectors.toList());
     }

     public Integer reportReviewsById(String id, boolean isReport) {
        return reviewRepository.reportById(id, isReport);
     }
     public Integer reportReviewByIds(List<String> ids, boolean isReport) {
        return reviewRepository.reportAllByIds(ids, isReport);
     }

     public List<BussinessReviewResponseDto> getReportedReviewList(String searchTerm) {
        return reviewRepository.findAllReportedList(searchTerm).stream()
            .map(BussinessReviewResponseDto::new)
            .collect(Collectors.toList());
     }
}