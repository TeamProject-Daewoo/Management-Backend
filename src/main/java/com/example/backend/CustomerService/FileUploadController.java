package com.example.backend.CustomerService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Value("${file.upload-dir-default}")
    private String defaultUploadDir;

    @Value("${file.upload-dir-alt}")
    private String altUploadDir;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 없습니다.");
        }

        try {
            // 두 경로 검사: 존재하고 쓰기 가능한 쪽 선택
            File dirDefault = new File(defaultUploadDir);
            File dirAlt     = new File(altUploadDir);

            String uploadDirToUse;

            if (dirDefault.exists() && dirDefault.canWrite()) {
                uploadDirToUse = defaultUploadDir;
            } else if (dirAlt.exists() && dirAlt.canWrite()) {
                uploadDirToUse = altUploadDir;
            } else {
                // 기본 경로 생성 시도
                if (!dirDefault.exists()) {
                    dirDefault.mkdirs();
                }
                if (dirDefault.exists() && dirDefault.canWrite()) {
                    uploadDirToUse = defaultUploadDir;
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                         .body("업로드 가능한 디렉토리가 없습니다.");
                }
            }

            // 업로드 디렉토리가 없으면 생성
            File uploadFolder = new File(uploadDirToUse);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + (originalFilename != null ? originalFilename : "file");

            File destinationFile = new File(uploadFolder, fileName);
            file.transferTo(destinationFile);

            // 클라이언트에 반환할 URL 경로
            String fileUrl = "/uploads/" + fileName;

            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("파일 업로드 실패: " + e.getMessage());
        }
    }
}

