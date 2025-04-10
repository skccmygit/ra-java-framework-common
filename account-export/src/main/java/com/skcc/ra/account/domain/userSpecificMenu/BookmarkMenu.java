package com.skcc.ra.account.domain.userSpecificMenu;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.BookmarkMenuDto;
import com.skcc.ra.account.domain.auth.pk.UserMenuPK;
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
@Entity
@IdClass(UserMenuPK.class)
@Table(name="OCO10104", catalog = "OCO")
public class BookmarkMenu extends BaseEntity implements Apiable<BookmarkMenuDto> {

    @Id
    @Column(name="USERID", length=10, nullable=false)
    private String userid;

    @Id
    @Column(name="MENU_ID", nullable=false)
    private String menuId;

    @Column(name="SORT_SEQN")
    private int sortSeqn;

    @Override
    public BookmarkMenuDto toApi() {
        BookmarkMenuDto bookmarkMenuDto = new BookmarkMenuDto();
        BeanUtils.copyProperties(this, bookmarkMenuDto);
        return bookmarkMenuDto;
    }
}
