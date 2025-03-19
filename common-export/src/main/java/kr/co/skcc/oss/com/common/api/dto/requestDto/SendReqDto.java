package kr.co.skcc.oss.com.common.api.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "SendReqDto", description = "S/L/MMS 발송 요청")
public class SendReqDto {

    @Schema(description = "SMS발신일시")
    private String smsSndgDtm;

    @Schema(description = "SMS알림톡구분코드", required = true)
    private String smsNotitClCd;

    @Schema(description = "업무", required = true)
    private String chrgTaskGroupCd;

    @Schema(description = "SMS메시지형식ID")
    private String smsMsgFormId;

    @Schema(description = "발신자식별번호")
    private String dsprIdentNo;

    @Schema(description = "발신자전화번호(Callback)")
    private String dsprPhno;

    @Schema(description = "SMS제목내용")
    private String smsTitleCntnt;

    @Schema(description = "SMS메시지내용")
    private String smsMsgCntnt;

    @Schema(description = "수신자전화번호", required = true)
    private String rcverPhno;

    @Schema(description = "계약번호")
    private String cntrtno;

    @Schema(description = "관제관리번호")
    private String ctlMgmtNo;

    @Schema(description = "신호ID")
    private String signlOpId;

    @Schema(description = "등록일시")
    private String regDtm;
    // 만료 시간
    @Schema(description = "발송만료시간", example = "86400")
    private int expiretime;

    @Schema(description = "첨부파일Cnt", example = "1")
    private int filecnt;

    @Schema(description = "첨부파일경로", example = "/app/sms_agent/contents/image_1.jpg")
    private String fileloc1;

    @Schema(description = "등록구분(업무구분)", example = "\"72\"")
    @Size(max = 2, message = "ADT_GUBN error, MAX LENGTH : 2 ")
    private String adtGubn;


    @Schema(description = "EXT_COL1", example = "p")
    @Size(max = 64, message = "EXT_COL1 error, MAX LENGTH : 64 ")
    private String extCol1;

    @Schema(description = "DEPT ID", example = "DEPT_ID")
    @Size(max = 15, message = "DEPT_ID error, MAX LENGTH : 15 ")
    private String deptId;

    @Schema(description = "TEMPLATE CODE", example = "33A01")
    @Size(max = 30, message = "TEMPLATE_CODE error, MAX LENGTH : 30 ")
    private String templateCode;

    @Schema(description = "버튼타입", example = "\"2\"")
    @Size(max = 1, message = "KKO_BTN_TYPE error, MAX LENGTH : 1 ")
    private String kkoBtnType;

    @Schema(description = "버튼URL", example = "url")
    @Size(max = 4000, message = "KKO_BTN_INFO error, MAX LENGTH : 4000 ")
    private String kkoBtnInfo;

    @Schema(description = "메시지 형식 param", example = "string")
    private List<String> params;

    @Schema(description = "MMS발송순번(모바일)")
    private Long mmsSndSeq;

    @Schema(description = "수신자명(모바일)")
    private String rcverNm;

    @Schema(description = "HTML Page 타입")
    private String htmlTalkType;

    @Schema(description = "API 요청 param")
    private HashMap<String, String> htmlParams;
}

