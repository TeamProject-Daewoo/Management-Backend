    package com.example.backend.announcement;

    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.data.domain.Sort;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;

    import java.util.List;

    @RestController
    @RequestMapping("/api/admin/notices")
    @RequiredArgsConstructor
    public class NoticeController {

        private final NoticeService noticeService;

      
@GetMapping("/paged")
public ResponseEntity<Page<NoticeDTO>> getNoticesPaged(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam(defaultValue = "DESC") String sortDir // 추가
) {
    Page<NoticeDTO> pagedResult = noticeService.getNoticesPaged(page, size, sortDir);
    return ResponseEntity.ok(pagedResult);
}

        @GetMapping("/{id}")
        public ResponseEntity<NoticeDTO> getNotice(@PathVariable Long id) {
            return ResponseEntity.ok(noticeService.getNotice(id));
        }

        @PostMapping
        public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO dto) {
            return ResponseEntity.ok(noticeService.createNotice(dto));
        }

        @PutMapping("/{id}")
        public ResponseEntity<NoticeDTO> updateNotice(@PathVariable Long id, @RequestBody NoticeDTO dto) {
            return ResponseEntity.ok(noticeService.updateNotice(id, dto));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
            noticeService.deleteNotice(id);
            return ResponseEntity.noContent().build();
        }
    }
