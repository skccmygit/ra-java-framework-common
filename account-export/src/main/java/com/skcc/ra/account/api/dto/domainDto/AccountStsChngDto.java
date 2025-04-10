package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.hist.AccountStsChng;
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
@Schema(name = "AccountStsChngDto", description = "계정관리")
public class AccountStsChngDto implements Entitiable<AccountStsChng> {

    @Schema(description = "사용자ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userid;

    @Schema(description = "변경영어명", requiredMode = Schema.RequiredMode.REQUIRED)
    private String chngColEngshNm;

    @Schema(description = "변경일시", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String chngDtm;

    @Schema(description = "변경값")
    private String chngColVal;


    public AccountStsChng toEntity() {
        AccountStsChng accountStsChng = new AccountStsChng();
        BeanUtils.copyProperties(this, accountStsChng);
        return accountStsChng;
    }
}
