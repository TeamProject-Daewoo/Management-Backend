package com.example.backend.CustomerService;

import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;

import java.util.List;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/admin/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final AdminInquiryService inquiryService;
    private final InquiryAnswerService answerService;
    private final InquiryFileRepository inquiryFileRepository;  // 추가
    private final InquiryFileService inquiryFileService;


    @Value("${file.upload-dir-default}")
    private String uploadDir;

    @GetMapping
    public Page<InquiryDetailDto> listInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) InquiryStatus status) {

        Page<Inquiry> inquiries = inquiryService.getInquiries(PageRequest.of(page, size), status);
        return inquiries.map(inquiry -> inquiryService.getInquiryDetail(inquiry.getId()));
    }

    @GetMapping("/{id}")
    public InquiryDetailDto getInquiryDetail(@PathVariable Long id) {
        return inquiryService.getInquiryDetail(id);
    }

    @PostMapping("/{id}/answer")
    public void answerInquiry(@PathVariable Long id, @RequestBody AnswerRequestDto answerDto) {
        answerService.saveAnswer(id, answerDto.getAnswerContent(), answerDto.getAdminUsername());
    }

    // ——— 여러 파일 리스트 조회 API ———
    @GetMapping("/{id}/files")
    public List<InquiryFileDto> getInquiryFiles(@PathVariable Long id) {
        List<InquiryFile> files = inquiryFileRepository.findByInquiryId(id);
        return files.stream()
                .map(file -> new InquiryFileDto(file.getId(), file.getFileName(), file.getFilePath()))
                .toList();
    }

   @GetMapping("/{inquiryId}/download/{fileId}")
public ResponseEntity<Resource> downloadAttachment(
        @PathVariable Long inquiryId,
        @PathVariable Long fileId) throws IOException {

    Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
    InquiryFile inquiryFile = inquiryFileService.getById(fileId); // 새로 만들어야 할 서비스 메서드

    // inquiryFile이 inquiry와 연관되어 있는지 확인 (보안)
    if (!inquiryFile.getInquiry().getId().equals(inquiry.getId())) {
        throw new RuntimeException("Invalid file for the inquiry");
    }

    Path filePath = Paths.get(uploadDir).resolve(inquiryFile.getFilePath());
    if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
        throw new FileNotFoundException("파일을 찾을 수 없습니다: " + filePath.toString());
    }

    Resource resource = new UrlResource(filePath.toUri());
    String contentType = Files.probeContentType(filePath);
    if (contentType == null) {
        contentType = "application/octet-stream";
    }

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + inquiryFile.getFileName() + "\"")
            .body(resource);
}

}
