package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.hist.UserAuthReqProcHis;
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
@Schema(name = "UserAuthReqProcHisDto", description = "사용자권한신청처리내역")
public class UserAuthReqProcHisDto implements Entitiable<UserAuthReqProcHis> {

    @Schema(description = "권한신청순번", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer athrtyReqstSeq;

    @Schema(description = "권한신청처리일시", requiredMode = Schema.RequiredMode.REQUIRED)
    private String athrtyReqstOpDtm;

    @Schema(description = "권한신청상태코드")
    private String athrtyReqstStsCd;

    @Schema(description = "결제사용자ID")
    private String settlUserid;

    @Schema(description = "결제사용자IP")
    private String settlUserIpaddr;

    @Schema(description = "반려사유내용")
    private String gvbkResonCntnt;

    public UserAuthReqProcHis toEntity() {
        UserAuthReqProcHis userAuthReqProcHis = new UserAuthReqProcHis();
        BeanUtils.copyProperties(this, userAuthReqProcHis);
        return userAuthReqProcHis;
    }

    public UserAuthReqProcHisDto(UserAuthReqProcHisDto userAuthReqProcHisDto) {
        this.athrtyReqstSeq = userAuthReqProcHisDto.getAthrtyReqstSeq();
        this.athrtyReqstOpDtm = userAuthReqProcHisDto.getAthrtyReqstOpDtm();
        this.athrtyReqstStsCd = userAuthReqProcHisDto.getAthrtyReqstStsCd();
        this.settlUserid = userAuthReqProcHisDto.getSettlUserid();
        this.gvbkResonCntnt = userAuthReqProcHisDto.getGvbkResonCntnt();
    }

}
