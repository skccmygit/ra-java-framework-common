package kr.co.skcc.oss.com.common.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CdDto", description = "코드")
public class CdDto {

    @Schema(description = "코드")
    private String code;

    @Schema(description = "코드명")
    private String name;
}
