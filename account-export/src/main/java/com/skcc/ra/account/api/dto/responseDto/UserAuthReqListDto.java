package com.skcc.ra.account.api.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserAuthReqListDto", description = "사용자권한신청리스트")
public class UserAuthReqListDto {

    @Schema(description = "신청서ID")
    private Integer athrtyReqstSeq;

    @Schema(description = "부서코드")
    private String deptcd;

    @Schema(description = "부서명")
    private String deptNm;

    @Schema(description = "신청자직책")
    private String reofoNm;

    @Schema(description = "신청자직급")
    private String clofpNm;

    @Schema(description = "신청자직능")
    private String vctnNm;

    @Schema(description = "신청자ID")
    private String userid;

    @Schema(description = "신청자명")
    private String userNm;

    @Schema(description = "신청사유")
    private String athrtyReqstResonCntnt;

    @Schema(description = "신청일시")
    private String athrtyReqstOpDtm;

    @Schema(description = "신청상태코드")
    private String athrtyReqstStsCd;

    @Schema(description = "신청상태명")
    private String athrtyReqstStsNm;

    @Schema(description = "검토자ID")
    private String athrtyConfirmOpUserid;

    @Schema(description = "검토자명")
    private String athrtyConfirmOpUserNm;

    @Schema(description = "검토사유")
    private String athrtyConfirmResonCntnt;

    @Schema(description = "검토일시")
    private String athrtyConfirmOpDtm;

    @Schema(description = "승인자ID")
    private String athrtyApproveOpUserid;

    @Schema(description = "승인자명")
    private String athrtyApproveOpUserNm;

    @Schema(description = "승인자IP")
    private String athrtyApproveIpaddr;

    @Schema(description = "승인일시")
    private String athrtyApproveOpDtm;

    @Schema(description = "반려자ID")
    private String athrtyRejectOpUserid;

    @Schema(description = "반려자명")
    private String athrtyRejectOpUserNm;

    @Schema(description = "반려사유")
    private String gvbkResonCntnt;

    @Schema(description = "반려일시")
    private String gvbkResonOpDtm;

    @Schema(description = "개인정보검토자ID")
    private String athrtyIndivOpUserid;

    @Schema(description = "개인정보검토자명")
    private String athrtyIndivOpUserNm;

    @Schema(description = "개인정보검토자 검토사유")
    private String athrtyIndivResonCntnt;

    @Schema(description = "검토일시")
    private String athrtyIndivConfirmOpDtm;

    @Schema(description = "개인정보여부")
    private String indivInfoYn;

    @Schema(description = "RPA계정여부")
    private String rpaUserYn;

    @Schema(description = "신청자 IP주소")
    private String reqstUserIpaddr;
}
