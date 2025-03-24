package kr.co.skcc.oss.com.common.domain.dept;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.skcc.oss.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//MIS 통해서 들어오는 부서 정보

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OIF21200", catalog = "OIF")
@Entity
public class DeptTemp extends BaseEntity {
    @Id
    @Column(name = "DEPTCD", length = 8, nullable = false)
    private String deptcd;

    @Column(name = "MGMT_DEPTCD", length = 6)
    private String mgmtDeptcd;

    @Column(name = "DEPT_NM", length = 50)
    private String deptNm;

    @Column(name = "ENGSH_DEPT_NM", length = 50)
    private String engshDeptNm;

    @Column(name = "ENGSH_DEPT_ABBR_NM", length = 50)
    private String engshDeptAbbrNm;

    @Column(name = "BSSMACD", length = 2)
    private String bssmacd;

    @Column(name = "SUPER_DEPTCD", length = 6)
    private String superDeptcd;

    @Column(name = "DEPT_GRADE_CD", length = 1)
    private String deptGradeCd;

    @Column(name = "DEPT_CRAT_DT", length = 8)
    private String deptCratDt;

    @Column(name = "DEPT_ABOL_DT", length = 8)
    private String deptAbolDt;

    @Column(name = "FEMP_DEPTCD", length = 6)
    private String fempDeptcd;

    @Column(name = "WHTAX_BZPL_CD", length = 6)
    private String whtaxBzplCd;

    @Column(name = "SALS_DEPTCD", length = 6)
    private String salsDeptcd;

    @Column(name = "INV_DEPTCD", length = 6)
    private String invDeptcd;

    @Column(name = "INVNT_WRHUS_CD", length = 6)
    private String invntWrhusCd;

    @Column(name = "VAT_BZPL_CD", length = 6)
    private String vatBzplCd;

    @Column(name = "ZIPCD", length = 6)
    private String zipcd;

    @Column(name = "BASIC_ADDR", length = 200)
    private String basicAddr;

    @Column(name = "DETIL_ADDR", length = 200)
    private String detilAddr;

    @Column(name = "PHNO", length = 14)
    private String phno;

    @Column(name = "FAX_NO", length = 14)
    private String faxNo;

    @Column(name = "BZNO", length = 10)
    private String bzno;

    @Column(name = "BZMAN_ACQ_DT", length = 8)
    private String bzmanAcqDt;

    @Column(name = "TXOFC_CD", length = 3)
    private String txofcCd;

    @Column(name = "TXOFC_NM", length = 20)
    private String txofcNm;

    @Column(name = "COM_BSCND_NM", length = 30)
    private String comBscndNm;

    @Column(name = "COM_ITM_NM", length = 30)
    private String comItmNm;

    @Column(name = "COM_CORP_NM", length = 50)
    private String comCorpNm;

    @Column(name = "USE_YN", length = 1)
    private String useYn;

    @Column(name = "CCNTR_CD", length = 10)
    private String ccntrCd;

    @Column(name = "RPRTT_NM", length = 30)
    private String rprttNm;

    @Column(name = "EXTSN_NO", length = 4)
    private String extsnNo;
}
