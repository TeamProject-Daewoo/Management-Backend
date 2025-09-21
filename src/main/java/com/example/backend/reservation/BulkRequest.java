// BulkRequest.java
package com.example.backend.reservation;

import lombok.Data;
import java.util.List;

@Data
public class BulkRequest {
    private String contentid;
    private List<Long> ids;
    private String action;
}
