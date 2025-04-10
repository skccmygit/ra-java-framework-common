package com.skcc.ra.account.domain.addAuth;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.UserAuthReqDto;
import com.skcc.ra.account.domain.hist.UserAuthReqHis;
import com.skcc.ra.account.domain.hist.UserAuthReqProcHis;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
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
@Entity
@Table(name = "OCO10130", catalog = "OCO")
public class UserAuthReq extends BaseEntity implements Apiable<UserAuthReqDto> {

    //권한신청순번
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATHRTY_REQST_SEQ", nullable = false)
    private Integer athrtyReqstSeq;

    //권한신청상태코드
    //Q:신청  	reQuest
    //U:신청취소 Unrequest
    //A:승인		Approve
    //C:검토		Confirm
    //R:반려    	Reject
    @Column(name = "ATHRTY_REQST_STS_CD", length = 1)
    private String athrtyReqstStsCd;

    //사용자ID
    @Column(name = "USERID", length = 10)
    private String userid;

    //사용자ID
    @Column(name = "REQST_RESON_CNTNT", length = 1000)
    private String reqstResonCntnt;

    @Column(name = "INDIV_INFO_YN", length = 1)
    private String indivInfoYn;

    @Column(name = "RPA_USER_YN", length = 1)
    private String rpaUserYn;

    @Column(name = "REQST_USER_IPADDR", length = 16)
    private String reqstUserIpaddr;

    //사용자권한신청처리내역 테이블
    @OneToMany(mappedBy = "athrtyReqstSeq", targetEntity = UserAuthReqProcHis.class)
    private Set<UserAuthReqProcHis> userAuthReqProcHiss = new HashSet<>();
    public Set<UserAuthReqProcHis> getUserAuthReqProcHiss() { return userAuthReqProcHiss; }
    public void setUserAuthReqProcHiss(Set<UserAuthReqProcHis> userAuthReqProcHiss) { this.userAuthReqProcHiss = userAuthReqProcHiss; }

    //사용자권한신청내역 테이블
    @OneToMany(mappedBy = "athrtyReqstSeq",targetEntity = UserAuthReqHis.class)
    private Set<UserAuthReqHis> userAuthReqHiss = new HashSet<>();
    public Set<UserAuthReqHis> getUserAuthReqHiss() { return userAuthReqHiss; }
    public void setUserAuthReqHiss(Set<UserAuthReqHis> userAuthReqHiss) { this.userAuthReqHiss = userAuthReqHiss; }

    @Override
    public UserAuthReqDto toApi() {
        UserAuthReqDto userAuthReqDto = new UserAuthReqDto();
        BeanUtils.copyProperties(this, userAuthReqDto);
        return userAuthReqDto;
    }
}