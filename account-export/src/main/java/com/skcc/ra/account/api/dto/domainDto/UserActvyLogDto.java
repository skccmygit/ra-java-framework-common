package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.hist.UserActvyLog;
import com.skcc.ra.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "UserActvyLogDto", description = "사용자활동로그")
public class UserActvyLogDto implements Entitiable<UserActvyLog> {

    @Schema(description = "사용자활동순번", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userActvySeq;

    @Schema(description = "사용자활동일시", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userActvyDtm;

    @Schema(description = "부서명")
    private String deptNm;

    @Schema(description = "직급명")
    private String clofpNm;

    @Schema(description = "직능명")
    private String vctnNm;

    @Schema(description = "사용자ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userid;

    @Schema(description = "최종변경자ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastChngrId;

    @Schema(description = "사용자활동유형코드", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userActvyTypeCd;

    @Schema(description = "접속IP주소")
    private String connIpaddr;

    @Schema(description = "메뉴ID")
    private String menuId;

    @Schema(description = "화면ID")
    private String screnId;

    @Schema(description = "시스템분류코드")
    private String systmCtgryCd;

    @Schema(description = "메뉴명")
    private String menuNm;

    @Schema(description = "화면명")
    private String screnNm;

    @Schema(description = "시스템분류명")
    private String systmCtgryNm;

    @Schema(description = "메뉴업무구분")
    private String chrgTaskGroupNm;

    @Schema(description = "사용자활동유형명")
    private String userActvyTypeNm;

    @Schema(description = "사용자명")
    private String userNm;

    @Schema(description = "다운로드사유(개인정보포함)")
    private String dwnldResonCntnt;

    public UserActvyLogDto(UserActvyLog t, String chrgTaskGroupNm, String menuNm,
                            String screnNm, String systmCtgryNm, String userActvyTypeNm, String userNm,
                            String clofpNm, String vctnNm, String deptNm) throws ParseException {
        this.userActvySeq = t.getUserActvySeq();
        this.userActvyDtm = formatter(t.getUserActvyDtm());
        this.userid = t.getUserid();
        this.lastChngrId = t.getLastChngrId();
        this.userActvyTypeCd = t.getUserActvyTypeCd();
        this.connIpaddr = t.getConnIpaddr();
        this.screnId = t.getScrenId();
        this.systmCtgryCd = t.getSystmCtgryCd();
        this.menuNm = menuNm;
        this.screnNm = screnNm;
        this.systmCtgryNm = systmCtgryNm;
        this.chrgTaskGroupNm = chrgTaskGroupNm;
        this.userActvyTypeNm = userActvyTypeNm;
        this.userNm = userNm;
        this.clofpNm = clofpNm;
        this.vctnNm = vctnNm;
        this.deptNm = deptNm;
        this.dwnldResonCntnt = t.getDwnldResonCntnt();
        // this.filePathNm = t.getFilePathNm();
    }

    // 로그인/로그아웃 시 호출하는 형태
    public UserActvyLogDto(String userid, String userActvyTypeCd) {
        this.userid = userid;
        this.userActvyTypeCd = userActvyTypeCd;
    }

    //메뉴사용통계 상세에 사용
    public UserActvyLogDto(UserActvyLog userActvyLog, String userNm, String menuId, String menuNm){
        this.userid = userActvyLog.getUserid();
        this.userNm = userNm;
        this.userActvyDtm = userActvyLog.getUserActvyDtm();
        this.connIpaddr = userActvyLog.getConnIpaddr();
        this.menuId = menuId;
        this.menuNm = menuNm;
    }
    
    public String formatter(String str) throws ParseException {
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date formatDate = dtFormat.parse(str);

        return newDtFormat.format(formatDate);
    }

    public UserActvyLog toEntity() {
        UserActvyLog entity = new UserActvyLog();
        BeanUtils.copyProperties(this, entity);
        return entity;
    }
}
