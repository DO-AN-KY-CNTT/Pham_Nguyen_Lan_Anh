package com.nutribudget.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestMenuRequest {
    private Long userId;
    private LocalDate ngayApDung;
    private Integer soBua; // 3 hoac 4
    private String tuyChon; // HOM_NAY, NGAY_MAI, TUAN
}
