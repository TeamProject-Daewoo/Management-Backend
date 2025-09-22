package com.example.backend.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

  

    public Page<NoticeDTO> getNoticesPaged(int page, int size, String sortDir) {
    Sort.Direction direction = Sort.Direction.fromString(sortDir);
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
    Page<Notice> noticePage = noticeRepository.findAll(pageable);
    return noticePage.map(this::entityToDto);
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
