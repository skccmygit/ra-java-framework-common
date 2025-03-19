package kr.co.skcc.oss.com.common.api.dto.excelDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ExcelHeaderCellType", description = "엑셀 헤더용 Cell 타입")
public class ExcelHeaderCellType {

    @Schema(description = "row")
    private int row;

    @Schema(description = "row")
    private int col;

    @Schema(description = "cellType")
    private int cellType;

}
