package kr.co.skcc.base.com.common.domain.menu;

import jakarta.persistence.*;
import kr.co.skcc.base.com.common.api.dto.domainDto.MenuDto;
import kr.co.skcc.base.com.common.jpa.Apiable;
import kr.co.skcc.base.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="OCO10210", catalog = "OCO")
public class Menu extends BaseEntity implements Apiable<MenuDto> {

    @Id
    @Column(name="MENU_ID", nullable=false)
    private String menuId;

    @Column(name="CHRG_TASK_GROUP_CD", length=3)
    private String chrgTaskGroupCd;

    @Column(name="MENU_TYPE_CD", length=3)
    private String menuTypeCd;

    @Column(name="SCREN_EXCUT_CL_CD", length=1)
    private String screnExcutClCd;

    @Column(name="MENU_NM", length=100, nullable=false)
    private String menuNm;

    @Column(name="MENU_DESC", length=200, nullable=false)
    private String menuDesc;

    @Column(name="MENU_STEP_VAL")
    private int menuStepVal;

    @Column(name="SORT_SEQN")
    private int sortSeqn;

    @Column(name="USE_YN", length=1, nullable=false)
    private String useYn;

    @Column(name="MENU_EXPSE_YN", length=1)
    private String menuExpseYn;

    @Column(name="SUPER_MENU_ID")
    private String superMenuId;

    @Column(name="SCREN_ID")
    private String screnId;

    @Column(name="LINKA_SYSTM_TAG_CNTNT")
    private String linkaSystmTagCntnt;

    @Override
    public MenuDto toApi() {
        MenuDto dto = new MenuDto();

        BeanUtils.copyProperties(this,dto);

        return dto;
    }
}
