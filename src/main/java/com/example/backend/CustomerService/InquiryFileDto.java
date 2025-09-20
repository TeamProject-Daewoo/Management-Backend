package com.example.backend.CustomerService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InquiryFileDto {
    private Long id;
    private String fileName;
    private String filePath;
}
