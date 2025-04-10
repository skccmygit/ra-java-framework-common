package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.hist.UserAuthReqHis;
import com.skcc.ra.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserAuthReqHisDto", description = "사용자권한신청내역")
public class UserAuthReqHisDto implements Entitiable<UserAuthReqHis> {

    @Schema(description = "권한신청순번", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer athrtyReqstSeq;

    @Schema(description = "권한신청내역순번")
    private int athrtyReqstDtlSeq;

    @Schema(description = "사용자역할ID")
    private String userRoleId;

    @Schema(description = "메뉴ID")
    private String menuId;

    @Schema(description = "화면ID")
    private String screnId;

    @Schema(description = "버튼ID")
    private String bttnId;

    @Schema(description = "권한추가여부")
    private String athrtyAddnYn;

    public UserAuthReqHis toEntity() {
        UserAuthReqHis userAuthReqHis = new UserAuthReqHis();
        BeanUtils.copyProperties(this, userAuthReqHis);
        return userAuthReqHis;
    }
}
