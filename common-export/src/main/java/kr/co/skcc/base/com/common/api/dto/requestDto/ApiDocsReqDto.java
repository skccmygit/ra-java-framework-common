package kr.co.skcc.base.com.common.api.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ApiDocsReqDto", description = "API Documentation 저장")
public class ApiDocsReqDto {

    @Schema(description = "API Documentation uri")
    @NotNull
    private String apiDocsUri;

    @Schema(description = "응용프로그램그룹ID")
    @NotNull
    private int aproGroupId;

}
