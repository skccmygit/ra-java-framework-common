package com.skcc.ra.account.domain.account;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.AccountReqMgmtDto;
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
@Entity
@Table(name = "OCO10109", catalog = "OCO") //사용자ID신청목록
public class AccountReqMgmt extends BaseEntity implements Apiable<AccountReqMgmtDto>  {

    //사용자ID신청순번
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERID_REQST_SEQ")
    private Integer useridReqstSeq;

    //사용자ID
    @Column(name = "USERID", length = 10, nullable = false)
    private String userid;

    //사용자ID신청일자
    @Column(name = "USERID_REQST_DT", length = 8, nullable = false)
    private String useridReqstDt;

    //사용자ID신청상태코드
    //Q:신청  	reQuest
    //A:승인		Approve
    //C:검토		Confirm
    //R:반려    	Reject
    @Column(name = "USERID_REQST_STS_CD", length = 1, nullable = false)
    private String useridReqstStsCd;


    //검토사용자ID
    @Column(name = "RVW_USERID", length = 10)
    private String rvwUserid;

    //검토일자
    @Column(name = "RVW_DT", length = 8)
    private String rvwDt;

    //승인사용자ID
    @Column(name = "AUTHZ_USERID", length = 10)
    private String authzUserid;

    //승인사용자IP
    @Column(name = "AUTHZ_USER_IPADDR", length = 16)
    private String authzUserIpaddr;

    //승인일자
    @Column(name = "AUTHZ_DT", length = 8)
    private String authzDt;

    //반려사유내용
    @Column(name = "GVBK_RESON_CNTNT")
    private String gvbkResonCntnt;

    //신청사유내용
    @Column(name = "REQST_RESON_CNTNT")
    private String reqstResonCntnt;

    @Override
    public AccountReqMgmtDto toApi() {
        AccountReqMgmtDto accountReqMgmtDto = new AccountReqMgmtDto();
        BeanUtils.copyProperties(this, accountReqMgmtDto);
        return accountReqMgmtDto;
    }
}