package kr.co.skcc.oss.com.common.domain.attachFile;

import jakarta.persistence.*;
import kr.co.skcc.oss.com.common.api.dto.domainDto.AttachFileDto;
import kr.co.skcc.oss.com.common.jpa.Apiable;
import kr.co.skcc.oss.com.common.jpa.BaseEntity;
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
@Table(name="OCO30900", catalog = "OCO")
public class AttachFile extends BaseEntity implements Apiable<AttachFileDto> {
    @Id
    @Column(name="ATAC_FILE_NO", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long atacFileNo;

    @Column(name="ATAC_FILE_TYPE_CD")
    private String atacFileTypeCd;

    @Column(name="ATAC_FILE_TASK_CL_CD")
    private String atacFileTaskClCd;

    @Column(name="ATAC_FILE_NM")
    private String atacFileNm;

    @Column(name="ATAC_FILE_SIZE")
    private int atacFileSize;

    @Column(name="ATAC_FILE_PATH_NM")
    private String atacFilePathNm;

    @Column(name="ATAC_FILE_LOC_URLADDR")
    private String atacFileLocUrladdr;

    @Column(name="ATAC_FILE_STS_CD")
    private String atacFileStsCd;

    @Override
    public AttachFileDto toApi() {

        AttachFileDto dto = new AttachFileDto();
        BeanUtils.copyProperties(this,dto);

        return dto;
    }
}
