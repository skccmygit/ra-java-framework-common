package kr.co.skcc.base.com.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.UserBasicScrenIDto;
import kr.co.skcc.base.com.common.domain.userBasic.UserBasic;
import kr.co.skcc.base.com.common.jpa.Entitiable;
import kr.co.skcc.base.com.common.util.MaskingUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserBasicDto", description = "사원정보")
public class UserBasicDto implements Entitiable<UserBasic> {

    @Schema(description = "사원번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "K12345")
    private String empno;

    @Schema(description = "사원한글명", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동")
    private String empKrnNm;

    @Schema(description = "사원영문성", requiredMode = Schema.RequiredMode.REQUIRED, example = "hong")
    private String empEnglnm;

    @Schema(description = "사원영문이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "kildong")
    private String empEngfnm;

    @Schema(description = "본부코드")
    private String bssmacd;

    @Schema(description = "본부명")
    private String bssmacdNm;
    @Schema(description = "부서코드")
    private String deptcd;

    @Schema(description = "부서명")
    private String deptNm;

    @Schema(description = "생년월일")
    private String bthdy;

    @Schema(description = "자택전화번호")
    private String owgmPhno;

    @Schema(description = "휴대전화번호")
    private String mphno;

    @Schema(description = "그룹입사일자")
    private String groupEntcpDt;

    @Schema(description = "당사입사일자")
    private String tcomEntcpDt;

    @Schema(description = "현직발령일자")
    private String curtpOfordDt;

    @Schema(description = "현직발령코드")
    private String curtpOfordCd;

    @Schema(description = "퇴사일자")
    private String resgDt;

    @Schema(description = "회사구분코드")
    private String comClCd;

    @Schema(description = "직급코드")
    private String clofpCd;

    @Schema(description = "직급명")
    private String clofpNm;

    @Schema(description = "직책코드")
    private String reofoCd;

    @Schema(description = "직책코드명")
    private String reofoNm;

    @Schema(description = "직군코드")
    private String jgpCd;

    @Schema(description = "직군명")
    private String jgpNm;

    @Schema(description = "직능코드")
    private String vctnCd;

    @Schema(description = "직능명")
    private String vctnNm;

    @Schema(description = "팀원구분코드")
    private String teambrClCd;

    @Schema(description = "이메일주소")
    private String emailaddr;

    @Schema(description = "이전사원번호")
    private String befoEmpno;

    public UserBasicDto(UserBasic userBasic, String deptNm) {
        this(userBasic.toApi());
        this.deptNm = deptNm;
    }

    public UserBasicDto(UserBasic userBasic, String deptNm, String bssHqNm) {
        this(userBasic.toApi());
        this.deptNm = deptNm;
        this.bssmacdNm = bssHqNm;
    }

    public UserBasicDto(UserBasic userBasic, String deptNm, String reofoNm, String bssmacd, String bssmacdNm) {
        this(userBasic.toApi());
        this.deptNm = deptNm;
        this.tcomEntcpDt = userBasic.getTcomEntcpDt().replaceAll("(\\d{4})(\\d{2})(\\d{2})", "$1-$2-$3");
        this.reofoNm = reofoNm;
        this.bssmacd = bssmacd;
        this.bssmacdNm = bssmacdNm;

        // 사원조회 화면에서 임원 이상은 전화번호 안나오도록 조치
        // 직책이 120 보다 작거나 직급이 38보다 작을 경우
        if(!("".equals(this.reofoCd.replace(" ","")) || this.reofoCd == null)) {
            if (Integer.parseInt(this.reofoCd) <= 120) {
                this.mphno = "";
            }
        }
        if(!("".equals(this.clofpCd.replace(" ","")) || this.clofpCd == null)) {
            if (Integer.parseInt(this.clofpCd) <= 38) {
                this.mphno = "";
            }
        }
        // 전화번호 마스킹 처리
        if(!"".equals(this.mphno)){
            this.mphno = MaskingUtil.getMaskedId(this.mphno,"phone");
        }
    }

    public UserBasicDto(UserBasicDto userBasicDto) {
        BeanUtils.copyProperties(userBasicDto, this);
    }

    public UserBasicDto(UserBasic c) {
        BeanUtils.copyProperties(c, this);
    }

    public UserBasicDto(UserBasicScrenIDto UserBasicScrenIDto, String maskingYn, String formatYn) {
        BeanUtils.copyProperties(UserBasicScrenIDto, this);

        String tcomEntcpDt = UserBasicScrenIDto.getTcomEntcpDt();
        String mphno = UserBasicScrenIDto.getMphno();

        if (tcomEntcpDt != null || "".equals(tcomEntcpDt)) {
            this.setTcomEntcpDt(tcomEntcpDt.trim().replaceAll("(\\d{4})(\\d{2})(\\d{2})", "$1-$2-$3"));
        }

        if ("Y".equals(maskingYn)) {
            if (mphno != null || "".equals(mphno)) {
                this.setMphno(MaskingUtil.getMaskedId(mphno.trim(), "phone"));
            }
        } else {
            if ("Y".equals(formatYn)) {
                if (mphno != null || "".equals(mphno)) {
                    this.setMphno(MaskingUtil.getFormatPhoneNum(mphno.trim()));
                }
            } else {
                if (mphno != null || "".equals(mphno)) {
                    this.setMphno(MaskingUtil.getFormatPhoneNum(mphno.trim()));
                }
            }
        }
    }

    public UserBasic toEntity() {
        UserBasic userBasic = new UserBasic();
        BeanUtils.copyProperties(this, userBasic);
        return userBasic;
    }
}
