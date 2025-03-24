package kr.co.skcc.base.com.common.api.dto.excelDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ExcelHeaderDto", description = "엑셀 헤더")
public class ExcelHeaderDto {
    /***************************************
    * 타이틀
    ***************************************/
    @Schema(description = "타이틀명")
    private String title;

    @Schema(description = "이미지 시작 Row")
    private int imgStartRow;

    @Schema(description = "이미지 시작 Col")
    private int imgStartCol;

    @Schema(description = "이미지 종료 Row")
    private int imgEndRow;

    @Schema(description = "이미지 종료 Col")
    private int imgEndCol;

    @Schema(description = "이미지명")
    private boolean imgTitle;

    @Schema(description = "타이틀이미지경로")
    private String titleImgPath;

    /***************************************
     * 헤더
     ***************************************/
    @Schema(description = "헤더명")
    private List<String[]> headerNm;

    @Schema(description = "헤더 Merge 기준")
    private List<ExcelMergeDto> headerMerge;

    @Schema(description = "헤더 Row 개수")
    private int stRownum;

    @Schema(description = "헤더 Col 개수")
    private int hLength;

    /***************************************
     * Cell
     ***************************************/
    @Schema(description = "cellType")
    private List<ExcelHeaderCellType> cellType;

}
