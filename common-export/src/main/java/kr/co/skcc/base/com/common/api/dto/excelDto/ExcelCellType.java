package kr.co.skcc.base.com.common.api.dto.excelDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ExcelCellType", description = "엑셀 Cell 타입, width")
public class ExcelCellType {

    @Schema(description = "colNm")
    private String colNm;

    @Schema(description = "colCellType")
    private String colCellType;

    @Schema(description = "colWidth")
    private int colWidth;

}
