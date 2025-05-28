package com.skcc.ra.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BoardLookUpDto", description = "공지사항조회이력")
public class BoardLookUpDto {

    @Schema(description = "공지번호")
    private Long annceNo;

    @Schema(description = "사용자ID")
    private String userid;
}
