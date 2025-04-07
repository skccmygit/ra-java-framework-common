package com.skcc.ra.common.api.dto.excelDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ExcelFormDto", description = "엑셀 Form 다운로드 시 데이터")
public class ExcelFormDto {

    @Schema(description = "row")
    int row;

    @Schema(description = "col")
    int col;

    @Schema(description = "value")
    String value;
}
