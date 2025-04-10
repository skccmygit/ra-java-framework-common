package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.account.AccountReqMgmt;
import com.skcc.ra.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AccountReqMgmtDto", description = "사용자ID신청목록")
public class AccountReqMgmtDto implements Entitiable<AccountReqMgmt> {

    @Schema(description = "사용자ID신청순번", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer useridReqstSeq;

    @Schema(description = "직책명")
    private String reofoNm;

    @Schema(description = "직급명")
    private String clofpNm;

    @Schema(description = "직능명")
    private String vctnNm;

    @Schema(description = "사용자ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userid;

    @Schema(description = "사용자명")
    private String userNm;

    @Schema(description = "사용자ID신청일자")
    private String useridReqstDt;

    @Schema(description = "사용자ID신청상태코드", requiredMode = Schema.RequiredMode.REQUIRED)
    private String useridReqstStsCd;

    @Schema(description = "사용자ID신청상태명")
    private String useridReqstStsNm;

    @Schema(description = "검토자명")
    private String rvwUserNm;

    @Schema(description = "검토자ID")
    private String rvwUserid;

    @Schema(description = "검토일자")
    private String rvwDt;

    @Schema(description = "승인자명")
    private String authzUserNm;

    @Schema(description = "승인자ID")
    private String authzUserid;

    @Schema(description = "승인사용자IP")
    private String authzUserIpaddr;

    @Schema(description = "승인일자")
    private String authzDt;

    @Schema(description = "반려사유내용")
    private String gvbkResonCntnt;

    @Schema(description = "부서코드")
    private String deptcd;

    @Schema(description = "부서명")
    private String deptNm;

    @Schema(description = "신청사유내용")
    private String reqstResonCntnt;

    public AccountReqMgmt toEntity() {
        AccountReqMgmt accountReqMgmt = new AccountReqMgmt();
        BeanUtils.copyProperties(this, accountReqMgmt);
        return accountReqMgmt;
    }

    public AccountReqMgmtDto(AccountReqMgmt accountReqMgmt, String userNm, String deptcd, String deptNm, String useridReqstStsNm,
                             String rvwUserNm, String authzUserNm, String clofpNm,String vctnNm, String reofoNm) {
        this.useridReqstSeq = accountReqMgmt.getUseridReqstSeq();
        this.clofpNm = clofpNm;
        this.vctnNm = vctnNm;
        this.userNm = userNm;
        this.userid = accountReqMgmt.getUserid();
        this.deptcd = deptcd;
        this.deptNm = deptNm;
        if(accountReqMgmt.getUseridReqstDt() != null)
            this.useridReqstDt = dateFormat(accountReqMgmt.getUseridReqstDt());
        this.useridReqstStsNm = useridReqstStsNm;
        this.useridReqstStsCd = accountReqMgmt.getUseridReqstStsCd();
        this.rvwUserNm = rvwUserNm;
        this.rvwUserid = accountReqMgmt.getRvwUserid();
        if(accountReqMgmt.getRvwDt() != null)
            this.rvwDt = dateFormat(accountReqMgmt.getRvwDt());
        this.authzUserNm = authzUserNm;
        this.authzUserid = accountReqMgmt.getAuthzUserid();
        this.authzUserIpaddr = accountReqMgmt.getAuthzUserIpaddr();
        if(accountReqMgmt.getAuthzDt() != null)
            this.authzDt = dateFormat(accountReqMgmt.getAuthzDt());

        this.gvbkResonCntnt = accountReqMgmt.getGvbkResonCntnt();

        this.reofoNm = reofoNm;
        this.reqstResonCntnt = accountReqMgmt.getReqstResonCntnt();

    }

    public String dateFormat(String date){
        return new StringBuilder()
                .append(date, 0, 4)
                .append("-")
                .append(date, 4, 6)
                .append("-")
                .append(date, 6, 8).toString();
    }

}
