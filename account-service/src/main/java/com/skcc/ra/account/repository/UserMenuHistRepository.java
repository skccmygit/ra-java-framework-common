package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.responseDto.ifDto.RoleHistDto;
import com.skcc.ra.account.domain.hist.UserMenuHist;
import com.skcc.ra.account.domain.hist.pk.UserMenuHistPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserMenuHistRepository extends JpaRepository<UserMenuHist, UserMenuHistPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    void deleteByUserid(String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT C.DEPT_NM AS deptNm       \n" +
            "            ,A.USERID AS userid        \n" +
            "            ,B.USER_NM AS userNm       \n" +
            "            ,E.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd \n" +
            "            ,J.CMMN_CD_VAL_NM AS chrgTaskGroupNm \n" +
            "            ,F.MENU_NM AS superMenuNm  \n" +
            "            ,E.MENU_NM AS menuNm       \n" +
            "            ,G.CMMN_CD_VAL_NM  AS crudClCd \n" +
            "            ,A.LAST_CHNGR_ID AS lastChngrId \n" +
            "            ,H.USER_NM AS lastChngrNm  \n" +
            "            ,DATE_FORMAT(STR_TO_DATE(A.CHNG_DTM,'%Y%m%d%H%i%s'), '%Y-%m-%d %H:%i:%s') AS chngrDtm\n" +
            "            ,A.CHNG_USER_IPADDR AS ipAddr  \n" +
            "            ,A.ATHRTY_REQST_SEQ AS athrtyReqstSeq  \n" +
            "            ,I.GVBK_RESON_CNTNT AS apprvResonCntnt  \n" +
            "            ,A.CHNG_RESON_CNTNT AS chngResonCntnt  \n" +
            "  FROM OCO.OCO10116 A \n" +
            "  LEFT OUTER JOIN OCO.OCO10100 B ON (B.USERID = A.USERID)    \n" +
            "  LEFT OUTER JOIN OCO.OCO50200 C ON (C.DEPTCD = B.DEPTCD)    \n" +
            "  LEFT OUTER JOIN OCO.OCO10210 E ON (E.MENU_ID = A.MENU_ID)  \n" +
            "  LEFT OUTER JOIN OCO.OCO10210 F ON (F.MENU_ID = E.SUPER_MENU_ID)    \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 G ON (G.CMMN_CD = 'CRUD_CL_CD' AND G.CMMN_CD_VAL = A.CRUD_CL_CD)\n" +
            "  LEFT OUTER JOIN OCO.OCO10100 H ON (H.USERID = A.LAST_CHNGR_ID)     \n" +
            "  LEFT OUTER JOIN OCO.OCO10131 I ON (I.ATHRTY_REQST_SEQ = A.ATHRTY_REQST_SEQ AND I.ATHRTY_REQST_STS_CD = 'A')\n" +
            "  LEFT OUTER JOIN OCO.OCO20101 J ON (J.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND J.CMMN_CD_VAL = E.CHRG_TASK_GROUP_CD) \n" +
            " WHERE A.CHNG_DTM BETWEEN :startDt AND :endDt      \n" +
            "   AND (A.USERID LIKE CONCAT('%',:userNm,'%') or B.USER_NM LIKE CONCAT('%',:userNm,'%'))   \n" +
            "   AND ('' =  :chrgTaskGroupCd OR E.CHRG_TASK_GROUP_CD = :chrgTaskGroupCd)                 \n" +
            "   AND (A.LAST_CHNGR_ID LIKE CONCAT('%',:chngrNm,'%') or H.USER_NM LIKE CONCAT('%',:chngrNm,'%'))  \n" +
            "   AND (E.MENU_NM LIKE CONCAT('%',:menuNm,'%'))    \n" +
            " ORDER BY A.CHNG_DTM DESC, A.USERID   \n "
            ,countQuery = "SELECT COUNT(*)\n" +
            "                FROM OCO.OCO10116 A \n" +
            "                LEFT OUTER JOIN OCO.OCO10100 B ON (B.USERID = A.USERID)    \n" +
            "                LEFT OUTER JOIN OCO.OCO10210 E ON (E.MENU_ID = A.MENU_ID)  \n" +
            "                LEFT OUTER JOIN OCO.OCO10100 H ON (H.USERID = A.LAST_CHNGR_ID)     \n" +
            "               WHERE A.CHNG_DTM BETWEEN :startDt AND :endDt      \n" +
            "                 AND (A.USERID LIKE CONCAT('%',:userNm,'%') or B.USER_NM LIKE CONCAT('%',:userNm,'%'))   \n" +
            "                 AND ('' =  :chrgTaskGroupCd OR E.CHRG_TASK_GROUP_CD = :chrgTaskGroupCd)                 \n" +
            "                 AND (A.LAST_CHNGR_ID LIKE CONCAT('%',:chngrNm,'%') or H.USER_NM LIKE CONCAT('%',:chngrNm,'%'))  \n" +
            "                 AND (E.MENU_NM LIKE CONCAT('%',:menuNm,'%')) "
            ,nativeQuery = true)
    Page<RoleHistDto> searchAddAuthHistoryMenu(String userNm, String menuNm, String chrgTaskGroupCd, String startDt, String endDt, String chngrNm, Pageable pageable);
}
