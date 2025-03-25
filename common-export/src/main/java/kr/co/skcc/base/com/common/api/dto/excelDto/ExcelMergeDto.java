package kr.co.skcc.base.com.common.api.dto.excelDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ExcelMergeDto", description = "엑셀 헤더 Merge 기준")
public class ExcelMergeDto {

    @Schema(description = "merge row 시작점")
    int rowStart;

    @Schema(description = "merge row 종료점")
    int rowEnd;

    @Schema(description = "merge col 시작점")
    int colStart;

    @Schema(description = "merge col 종료점")
    int colEnd;
}
