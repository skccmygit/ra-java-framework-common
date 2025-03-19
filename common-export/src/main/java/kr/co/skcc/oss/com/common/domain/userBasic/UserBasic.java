package kr.co.skcc.oss.com.common.domain.userBasic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.skcc.oss.com.common.api.dto.domainDto.UserBasicDto;
import kr.co.skcc.oss.com.common.jpa.Apiable;
import kr.co.skcc.oss.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

//MIS 통해서 들어오는 정직원 정보
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OCO50100", catalog = "OCO")
@Entity
public class UserBasic extends BaseEntity implements Apiable<UserBasicDto> {

    //사원번호
    @Id
    @Column(name = "EMPNO", length = 8, nullable = false)
    private String empno;

    //사원한글명
    @Column(name = "EMP_KRN_NM", length = 30, nullable = false)
    private String empKrnNm;

    //사원영문성
    @Column(name = "EMP_ENGLNM", length = 20, nullable = false)
    private String empEnglnm;

    //사원영문이름
    @Column(name = "EMP_ENGFNM", length = 20)
    private String empEngfnm;

    //부서코드
    @Column(name = "DEPTCD", length = 6)
    private String deptcd;

    //생년월일
    @Column(name = "BTHDY", length = 8)
    private String bthdy;

    //자택전화번호
    @Column(name = "OWHM_PHNO", length = 14)
    private String owgmPhno;

    //휴대전화번호
    @Column(name = "MPHNO", length = 14)
    private String mphno;

    //그룹입사일자
    @Column(name = "GROUP_ENTCP_DT", length = 8)
    private String groupEntcpDt;

    //당사입사일자
    @Column(name = "TCOM_ENTCP_DT", length = 8)
    private String tcomEntcpDt;

    //현직발령일자
    @Column(name = "CURTP_OFORD_DT", length = 8)
    private String curtpOfordDt;

    //현직발령코드
    @Column(name = "CURTP_OFORD_CD", length = 3)
    private String curtpOfordCd;

    //퇴사일자
    @Column(name = "RESG_DT", length = 8)
    private String resgDt;

    //회사구분코드
    @Column(name = "COM_CL_CD", length = 1)
    private String comClCd;

    //직급코드
    @Column(name = "CLOFP_CD", length = 3)
    private String clofpCd;

    //직군코드
    @Column(name = "REOFO_CD", length = 3)
    private String reofoCd;

    //직군코드
    @Column(name = "JGP_CD", length = 1)
    private String jgpCd;

    //직능코드
    @Column(name = "VCTN_CD", length = 3)
    private String vctnCd;

    //팀원구분코드
    @Column(name = "TEAMBR_CL_CD", length = 3)
    private String teambrClCd;

    //직급명
    @Column(name = "CLOFP_NM", length = 20)
    private String clofpNm;

    //직군명
    @Column(name = "JGP_NM", length = 20)
    private String jgpNm;

    //직능명
    @Column(name = "VCTN_NM", length = 20)
    private String vctnNm;

    //이메일주소
    @Column(name = "EMAILADDR", length = 100)
    private String emailaddr;

    //이전사원번호
    @Column(name = "BEFO_EMPNO", length = 8)
    private String befoEmpno;

    @Override
    public UserBasicDto toApi() {
        UserBasicDto userBasicDto = new UserBasicDto();
        BeanUtils.copyProperties(this,userBasicDto);

        return userBasicDto;
    }

}