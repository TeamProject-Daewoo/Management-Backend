package com.example.backend.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<NoticeDTO> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public NoticeDTO getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        return entityToDto(notice);
    }

    public NoticeDTO createNotice(NoticeDTO dto) {
        Notice notice = Notice.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        Notice saved = noticeRepository.save(notice);
        return entityToDto(saved);
    }

    public NoticeDTO updateNotice(Long id, NoticeDTO dto) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        notice.setCategory(dto.getCategory());
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        Notice updated = noticeRepository.save(notice);
        return entityToDto(updated);
    }

    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    private NoticeDTO entityToDto(Notice notice) {
        return NoticeDTO.builder()
                .id(notice.getId())
                .category(notice.getCategory())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}