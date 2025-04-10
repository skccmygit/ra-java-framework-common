package com.skcc.ra.account.domain.hist;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.UserAuthReqHisDto;
import com.skcc.ra.account.domain.hist.pk.UserAuthReqHisPK;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OCO10132", catalog = "OCO")
@Entity
@IdClass(UserAuthReqHisPK.class)
public class UserAuthReqHis extends BaseEntity implements Apiable<UserAuthReqHisDto> {

    //권한신청순번
    @Id
    @Column(name = "ATHRTY_REQST_SEQ", nullable = false)
    private Integer athrtyReqstSeq;

    //권한신청내역순번
    @Id
    @Column(name = "ATHRTY_REQST_DTL_SEQ", nullable = false)
    private int athrtyReqstDtlSeq;

    //사용자역할ID
    @Column(name = "USER_ROLE_ID", length = 4)
    private String userRoleId;

    //메뉴ID
    @Column(name = "MENU_ID")
    private String menuId;

    //화면ID
    @Column(name = "SCREN_ID", length = 10)
    private String screnId;

    //버튼ID
    @Column(name = "BTTN_ID", length = 10)
    private String bttnId;

    //권한추가여부
    @Column(name = "ATHRTY_ADDN_YN", length = 1)
    private String athrtyAddnYn;

    @Override
    public UserAuthReqHisDto toApi() {
        UserAuthReqHisDto userAuthReqHisDto = new UserAuthReqHisDto();
        BeanUtils.copyProperties(this, userAuthReqHisDto);
        return userAuthReqHisDto;
    }
}