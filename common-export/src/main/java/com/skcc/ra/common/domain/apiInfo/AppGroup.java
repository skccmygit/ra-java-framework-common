package com.skcc.ra.common.domain.apiInfo;

import jakarta.persistence.*;
import com.skcc.ra.common.api.dto.domainDto.AppGroupDto;
import com.skcc.ra.common.domain.apiInfo.type.BizTask;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OCO40100", catalog = "OCO")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AppGroup extends BaseEntity implements Apiable<AppGroupDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APRO_GROUP_ID")
    private int aproGroupId;

    @Column(name = "APRO_TYPE_CL_CD", length = 1)
    private String aproTypeClCd;

    @Column(name = "APRO_TASK_CL_CD", length = 3)
    @Enumerated(EnumType.STRING)
    private BizTask aproTaskClCd;

    @Column(name = "APRO_GROUP_CL_NM", length = 30)
    private String aproGroupClNm;

    @Column(name = "APRO_GROUP_DESC", length = 1000)
    private String aproGroupDesc;

    @OneToMany(mappedBy = "aproGroupId", orphanRemoval = true)
    private List<ApiInfo> apiInfos = new ArrayList<>();

    @Override
    public AppGroupDto toApi() {
        AppGroupDto appGroupDto = new AppGroupDto();
        BeanUtils.copyProperties(this, appGroupDto);
        if (this.aproTaskClCd != null) appGroupDto.setAproTaskClCdNm(this.aproTaskClCd.getName());
        return appGroupDto;
    }
}
