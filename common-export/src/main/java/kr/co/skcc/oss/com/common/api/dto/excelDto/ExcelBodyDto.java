package kr.co.skcc.oss.com.common.api.dto.excelDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ExcelBodyDto", description = "엑셀 바디")
public class ExcelBodyDto<T> {

    // body 데이터 영문명
    @Schema(description = "Body영문명")
    private List<String> bodyColNm;

    // body 데이터
    @Schema(description = "Body")
    private List<T> body;

    // colNm, Celltype, width
    @Schema(description = "cellType")
    private List<ExcelCellType> cellType;

}
