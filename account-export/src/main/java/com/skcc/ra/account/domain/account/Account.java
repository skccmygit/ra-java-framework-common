package com.skcc.ra.account.domain.account;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OCO10100", catalog = "OCO")
@Entity
public class Account extends BaseEntity implements Apiable<AccountDto>  {

    /*
    1. 최초 로그인시 비밀번호 변경
    2. 패스워드 자동 저장 기능 금지
    3. 패스워드 변경 주기 90일
    4. 이전에 사용한 패스워드 동일 패스워드 사용 제한
    5. 패스워드 복잡도: 영어/숫자/특수문자 3조합 8자리
    6. 데이터 삭제 없음 -> 삭제여부 변경 && 데이터 중 개인정보 삭제
    7. 시스템 접속 5회 이내 연속 실패 시 계정 잠김
    8. 연속 숫자, 생일, 전화번호, 아이디 주민번호 등 추측 쉬운 정보 안됨
    */

    //사용자ID, Key
    @Id
    @Column(name = "USERID", length = 10, nullable = false)
    private String userid;

    //사용자명
    @Column(name = "USER_NM", length = 30)
    private String userNm;

    //접속비밀번호, To-Be 암호화 필요
    @Column(name = "CONN_PSSWD", length = 256, nullable = false)
    private String connPsswd;

    //접속비밀번호 만료일자
    @Column(name = "PSSWD_EXPIR_DT", length = 8, nullable = false)
    private String psswdExpirDt;

    //사용자연락전화번호
    @Column(name = "USER_CONT_PHNO", length = 14, nullable = false)
    private String userContPhno;

    //사용자이메일주소
    @Column(name = "USER_EMAILADDR", length = 100)
    private String userEmailaddr;

    //부서코드
    @Column(name = "DEPTCD", length = 6)
    private String deptcd;

    //직책코드
    @Column(name = "REOFO_CD", length = 3)
    private String reofoCd;

    //직능코드
    @Column(name = "VCTN_CD", length = 3)
    private String vctnCd;

    //사용자그룹코드
    @Column(name = "USER_GROUP_CD", length = 1)
    private String userGroupCd;

    //내부사용자구분코드
    //0: 해당없음, 1: 상담사, 2: 관제, 3: BP, 4: TSE, 9: 기타(임시)
    @Column(name = "INNER_USER_CL_CD", length = 1)
    private String innerUserClCd;


    //사용자식별번호
    @Column(name = "USER_IDENT_NO", length = 10)
    private String userIdentNo;

    //사용자ID 생성일자
    @Column(name = "FST_REG_DTMD")
    private LocalDateTime fstRegDtmd;

    //비밀번호오류횟수
    @Column(name = "PSSWD_ERR_FRQY")
    private Integer psswdErrFrqy;

    //사용자ID상태코드
    //  W:승인대기          Waiting
    //  O:정상             On progress
    //  R:반려             Reject
    //  L:잠김             Lock
    //  D:삭제             Delete
    //  P:비밀번호 변경 필요 Password change
    //  T:임시비밀번호 부여  Temp password
    @Column(name = "USERID_STS_CD", length = 1, nullable = false)
    private String useridStsCd;

    @Column(name = "USER_IPADDR", length = 16)
    private String userIpaddr;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(	name = "OCO10101",
//            catalog = "OCO",
//            joinColumns = @JoinColumn(name = "USERID"),
//            inverseJoinColumns = @JoinColumn(name = "USER_ROLE_ID"))
//    private Set<Role> roles = new HashSet<>();
//
//    //사용자ID
//    public Set<Role> getRoles() { return roles; }
//    public void setRoles(Set<Role> roles) { this.roles = roles; }

    @Override
    public AccountDto toApi() {
        AccountDto accountDto = new AccountDto();
        BeanUtils.copyProperties(this, accountDto);
        return accountDto;
    }
}