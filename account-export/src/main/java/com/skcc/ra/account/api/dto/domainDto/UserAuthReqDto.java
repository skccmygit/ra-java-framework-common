package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.addAuth.UserAuthReq;
import com.skcc.ra.account.domain.hist.UserAuthReqHis;
import com.skcc.ra.account.domain.hist.UserAuthReqProcHis;
import com.skcc.ra.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserAuthReqDto", description = "사용자권한신청기본")
public class UserAuthReqDto implements Entitiable<UserAuthReq> {

    @Schema(description = "권한신청순번", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer athrtyReqstSeq;

    @Schema(description = "권한신청상태코드")
    private String athrtyReqstStsCd;

    @Schema(description = "권한신청상태명")
    private String athrtyReqstStsNm;

    @Schema(description = "사용자ID")
    private String userid;

    @Schema(description = "신청사유")
    private String reqstResonCntnt;

    @Schema(description = "승인사유")
    private String apprvResonCntnt;

    @Schema(description = "개인정보여부")
    private String indivInfoYn;

    @Schema(description = "RPA계정여부")
    private String rpaUserYn;

    @Schema(description = "신청자 IP")
    private String reqstUserIpaddr;

    @Schema(description = "변경사유")
    private String chngResonCntnt;

    @Schema(description = "사용자권한신청처리내역")
    private Set<UserAuthReqProcHis> userAuthReqProcHiss;

    public Set<UserAuthReqProcHis> getUserAuthReqProcHiss() {
        return userAuthReqProcHiss;
    }

    public void setUserAuthReqProcHiss(Set<UserAuthReqProcHis> userAuthReqProcHiss) {
        this.userAuthReqProcHiss = new HashSet<>();
        if(userAuthReqProcHiss != null && !userAuthReqProcHiss.isEmpty()) {
            this.userAuthReqProcHiss.addAll(userAuthReqProcHiss);
        }
    }

    @Schema(description = "사용자권한신청내역")
    private Set<UserAuthReqHis> userAuthReqHiss;

    public Set<UserAuthReqHis> getUserAuthReqHiss() {
        return userAuthReqHiss;
    }

    public void setUserAuthReqHiss(Set<UserAuthReqHis> userAuthReqHiss) {
        this.userAuthReqHiss = new HashSet<>();
        if(userAuthReqHiss != null && !userAuthReqHiss.isEmpty()) {
            this.userAuthReqHiss.addAll(userAuthReqHiss);
        }
    }

    public UserAuthReqDto(UserAuthReq userAuthReq, String athrtyReqstStsNm) {
        this.athrtyReqstSeq = userAuthReq.getAthrtyReqstSeq();
        this.athrtyReqstStsCd = userAuthReq.getAthrtyReqstStsCd();
        this.athrtyReqstStsNm = athrtyReqstStsNm;
        this.userid = userAuthReq.getUserid();
        this.setUserAuthReqHiss(userAuthReq.getUserAuthReqHiss());
        this.setUserAuthReqProcHiss(userAuthReq.getUserAuthReqProcHiss());
    }

    public UserAuthReq toEntity() {
        UserAuthReq userAuthReq = new UserAuthReq();
        BeanUtils.copyProperties(this, userAuthReq);
        if(userAuthReq.getUserAuthReqHiss() != null && !userAuthReq.getUserAuthReqHiss().isEmpty()) {
            this.setUserAuthReqHiss(userAuthReq.getUserAuthReqHiss());
        }
        if(userAuthReq.getUserAuthReqProcHiss() != null && !userAuthReq.getUserAuthReqProcHiss().isEmpty()) {
            this.setUserAuthReqProcHiss(userAuthReq.getUserAuthReqProcHiss());
        }

        return userAuthReq;
    }
}
