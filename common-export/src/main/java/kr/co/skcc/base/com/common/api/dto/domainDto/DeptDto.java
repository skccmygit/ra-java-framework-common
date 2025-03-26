package kr.co.skcc.base.com.common.api.dto.domainDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.base.com.common.domain.dept.Dept;
import kr.co.skcc.base.com.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "DeptDto", description = "DeptDto")
public class DeptDto implements Entitiable<Dept> {

    @Schema(description = "BSSMACD", example = "본부코드")
    private String bssmacd;

    @Schema(description = "BSSMACD_NM", example = "본부명")
    private String bssmacdNm;

    @Schema(description = "DEPTCD", example = "부서코드")
    private String deptcd;

    @Schema(description = "DEPT_NM", example = "부서명")
    private String deptNm;

    @Schema(description = "COM_CORP_NM", example = "부서명")
    private String comCorpNm;

    @Schema(description = "PHNO", example = "부서명")
    private String phno;

    @Schema(description = "FAX_NO", example = "부서명")
    private String faxNo;

    @Schema(description = "BZNO", example = "부서명")
    private String bzno;

    @Schema(description = "ZIPCD", example = "부서명")
    private String zipcd;

    @Schema(description = "BASIC_ADDR", example = "부서명")
    private String basicAddr;

    @Schema(description = "DETIL_ADDR", example = "부서명")
    private String detilAddr;

    @Schema(description = "USE_YN", example = "사용여부")
    private String useYn;

    @Schema(description = "MGMT_DEPTCD", example = "")
    private String mgmtDeptcd;

    @Schema(description = "ENGSH_DEPT_NM", example = "")
    private String engshDeptNm;

    @Schema(description = "ENGSH_DEPT_ABBR_NM", example = "")
    private String engshDeptAbbrNm;

    @Schema(description = "SUPER_DEPTCD", example = "")
    private String superDeptcd;

    @Schema(description = "DEPT_GRADE_CD", example = "")
    private String deptGradeCd;

    @Schema(description = "DEPT_CRAT_DT", example = "")
    private String deptCratDt;

    @Schema(description = "DEPT_ABOL_DT", example = "")
    private String deptAbolDt;

    @Schema(description = "FEMP_DEPTCD", example = "")
    private String fempDeptcd;

    @Schema(description = "WHTAX_BZPL_CD", example = "")
    private String whtaxBzplCd;

    @Schema(description = "SALS_DEPTCD", example = "")
    private String salsDeptcd;

    @Schema(description = "INV_DEPTCD", example = "")
    private String invDeptcd;

    @Schema(description = "INVNT_WRHUS_CD", example = "")
    private String invntWrhusCd;

    @Schema(description = "VAT_BZPL_CD", example = "")
    private String vatBzplCd;

    @Schema(description = "BZMAN_ACQ_DT", example = "")
    private String bzmanAcqDt;

    @Schema(description = "TXOFC_CD", example = "")
    private String txofcCd;

    @Schema(description = "TXOFC_NM", example = "")
    private String txofcNm;

    @Schema(description = "COM_BSCND_NM", example = "")
    private String comBscndNm;

    @Schema(description = "COM_ITM_NM", example = "")
    private String comItmNm;

    @Schema(description = "CCNTR_CD", example = "")
    private String ccntrCd;

    @Schema(description = "EXTSN_NO", example = "")
    private String extsnNo;

    @Schema(description = "LAST_CHNGR_ID", example = "")
    private String lastChngrId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "LAST_CHNG_DTMD", example = "")
    private LocalDateTime lastChngDtmd;

    //대표자명
    @Schema(description = "RPRTT_NM", example = "대표자명")
    private String rprttNm;

    @Schema(description = "하위부서")
    private List<DeptDto> depts;

    @Override
    public Dept toEntity() {
        Dept entity = new Dept();
        BeanUtils.copyProperties(this, entity);

        return entity;
    }

    public DeptDto(Dept dept){
        BeanUtils.copyProperties(dept, this);
        this.phno = dept.getPhno().replaceAll( "^(01\\d{1}|02|0505|0502|0506|0\\d{1,2}|\\*\\*\\*\\*)-?(\\d{3,4})-?(\\d{4}|\\*\\*\\*\\*)", "$1-$2-$3");
        this.faxNo = dept.getFaxNo().replaceAll("^(01\\d{1}|02|0505|0502|0506|0\\d{1,2}|\\*\\*\\*\\*)-?(\\d{3,4})-?(\\d{4}|\\*\\*\\*\\*)", "$1-$2-$3");
        this.bzno = dept.getBzno().replaceAll("(\\d{3})(\\d{2})(\\d{5})", "$1-$2-$3");
        this.extsnNo = StringUtils.isEmpty(dept.getExtsnNo()) ? "" : "7" + dept.getExtsnNo();
    }

    public DeptDto(Dept dept, String bssmacdNm){
        this(dept);
        this.bssmacdNm = bssmacdNm;
    }
}

