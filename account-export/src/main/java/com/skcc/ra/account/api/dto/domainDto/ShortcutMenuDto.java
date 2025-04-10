package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.userSpecificMenu.ShortcutMenu;
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
@Schema(name = "ShortcutMenuDto", description = "바로가기메뉴")
public class ShortcutMenuDto implements Entitiable<ShortcutMenu> {

    @Schema(description = "사용자ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userid;

    @Schema(description = "메뉴ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuId;

    @Schema(description = "순번", requiredMode = Schema.RequiredMode.REQUIRED)
    private int sortSeqn;

    public ShortcutMenu toEntity() {
        ShortcutMenu shortcutMenu = new ShortcutMenu();
        BeanUtils.copyProperties(this, shortcutMenu);
        return shortcutMenu;
    }
}
