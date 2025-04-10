package com.skcc.ra.account.domain.hist;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.UserAuthReqProcHisDto;
import com.skcc.ra.account.domain.hist.pk.UserAuthReqProcHisPK;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OCO10131", catalog = "OCO")
@Entity
@IdClass(UserAuthReqProcHisPK.class)
public class UserAuthReqProcHis extends BaseEntity implements Apiable<UserAuthReqProcHisDto> {

    //권한신청순번
    @Id
    @Column(name = "ATHRTY_REQST_SEQ", nullable = false)
    private Integer athrtyReqstSeq;

    //권한신청처리일시
    @Id
    @Column(name = "ATHRTY_REQST_OP_DTM", length = 15, nullable = false)
    private String athrtyReqstOpDtm;

    //권한신청상태코드
    @Id
    @Column(name = "ATHRTY_REQST_STS_CD", length = 1)
    private String athrtyReqstStsCd;

    //결재사용자ID
    @Column(name = "SETTL_USERID", length = 10)
    private String settlUserid;

    //결제사용자ID
    @Column(name = "SETTL_USER_IPADDR", length = 16)
    private String settlUserIpaddr;

    //반려사유내용
    @Column(name = "GVBK_RESON_CNTNT", length = 1000)
    private String gvbkResonCntnt;

    @Override
    public UserAuthReqProcHisDto toApi() {
        UserAuthReqProcHisDto userAuthReqProcHisDto = new UserAuthReqProcHisDto();
        BeanUtils.copyProperties(this, userAuthReqProcHisDto);
        return userAuthReqProcHisDto;
    }
}