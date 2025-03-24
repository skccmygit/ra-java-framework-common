package kr.co.skcc.base.com.common.api.dto.excelDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ExcelDto", description = "엑셀 업로드/다운로드")
public class ExcelDto {

    // 시트명
    @Schema(description = "페이지이름")
    private String sheetName;

    // 헤더 정보 포함 DTO
    @Schema(description = "헤더")
    private ExcelHeaderDto header;

    // 바디 정보 포함 DTO
    @Schema(description = "바디")
    private ExcelBodyDto body;
}