package kr.co.skcc.oss.com.common.api.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ifDto.InnerCallInfoIDto;
import kr.co.skcc.oss.com.common.util.MaskingUtil;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "InnerCallInfoDto", description = "전화인입 시 내부사용자 조회")
public class InnerCallInfoDto {

    @Schema(description = "본부코드")
    String bssmacd;

    @Schema(description = "본부명")
    String bssmacdNm;

    @Schema(description = "부서코드")
    String deptcd;

    @Schema(description = "부서명")
    String deptNm;

    @Schema(description = "사번")
    String empno;

    @Schema(description = "사원명")
    String empKrnNm;

    @Schema(description = "직급코드")
    String reofoCd;

    @Schema(description = "직급명")
    String reofoNm;

    @Schema(description = "직책코드")
    String clofpCd;

    @Schema(description = "직책명")
    String clofpNm;

    @Schema(description = "직능코드")
    String vctnCd;

    @Schema(description = "직능명")
    String vctnNm;

    @Schema(description = "전화번호")
    String mphno;

    public InnerCallInfoDto(InnerCallInfoIDto innerCallInfoIDto) {
        BeanUtils.copyProperties(innerCallInfoIDto, this);

        this.setMphno(MaskingUtil.getFormatPhoneNum(innerCallInfoIDto.getMphno()));
    }
}
