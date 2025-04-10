package com.skcc.ra.account.api.dto.domainDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import com.skcc.ra.account.domain.account.Account;
import com.skcc.ra.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AccountDto", description = "계정관리")
public class AccountDto implements Entitiable<Account> {

    @Schema(description = "사용자ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userid;

    @Schema(description = "사용자명", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userNm;

    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{10,20}",
             message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 10자 ~ 20자의 비밀번호여야 합니다.")
    @Schema(description = "접속비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String connPsswd;

    @Schema(description = "접속비밀번호만료일자", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime psswdExpirDt;

    @Schema(description = "사용자연락전화번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userContPhno;

    @Schema(description = "사용자이메일주소", example = "adtcaps@caps.com")
    private String userEmailaddr;

    @Schema(description = "부서코드")
    private String deptcd;

    @Schema(description = " 부서명")
    private String deptNm;

    @Schema(description = "직책코드")
    private String reofoCd;

    @Schema(description = "직능코드")
    private String vctnCd;

    @Schema(description = "사용자그룹코드")
    private String userGroupCd;

    @Schema(description = "내부사용자구분코드")
    private String innerUserClCd;

    @Schema(description = "직책명", example = "직책")
    private String reofoNm;

    @Schema(description = "직급명", example = "직급")
    private String clofpNm;

    @Schema(description = "직능명", example = "직능")
    private String vctnNm;

    @Schema(description = "사용자식별번호", example = "K9000111")
    private String userIdentNo;

    @Schema(description = "사용자ID상태코드")
    private String useridStsCd;

    @Schema(description = "사용자ID상태명")
    private String useridStsNm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "사용자ID생성일자")
    private LocalDateTime fstRegDtmd;

    @Schema(description = "비밀번호오류횟수")
    private Integer psswdErrFrqy;

    @Schema(description = "사용자 IP주소")
    private String userIpaddr;

    @Schema(description = "시스템 카테고리 구분")
    private String systmCtgryCd;

    @Schema(description = "신청사유내용")
    private String reqstResonCntnt;

    @Schema(description = "역할")
    private Set<String> roles;

    public Account toEntity() {
        Account account = new Account();
        BeanUtils.copyProperties(this, account);
        return account;
    }

    public AccountDto(Account account){
        BeanUtils.copyProperties(account, this );
    }
    public AccountDto(Account account, List<String> roles){
        this(account);
        this.setRoles(new HashSet<>(roles));
    }
    public AccountDto(Account account, String deptNm, String useridStsNm, String clofpNm, String vctnNm, String reofoNm) {
        this(account);
        this.deptNm = deptNm;
        this.useridStsNm = useridStsNm;
        this.clofpNm = clofpNm;
        this.vctnNm = vctnNm;
        this.reofoNm = reofoNm;
    }
    public AccountDto(String userid, String deptcd, String deptNm) {
        this.userid = userid;
        this.deptcd = deptcd;
        this.deptNm = deptNm;
    }
}
