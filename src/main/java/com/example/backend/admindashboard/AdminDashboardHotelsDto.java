package com.example.backend.admindashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardHotelsDto {
    private String contentId;
    private String title;
    private String area;
    private String businessNumber;
}
