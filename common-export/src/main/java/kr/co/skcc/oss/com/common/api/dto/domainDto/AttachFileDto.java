package kr.co.skcc.oss.com.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.skcc.oss.com.common.domain.attachFile.AttachFile;
import kr.co.skcc.oss.com.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AttachFileDto", description = "파일첨부")
public class AttachFileDto implements Entitiable<AttachFile> {

    @NotNull
    @Schema(description = "첨부파일번호")
    private Long atacFileNo;

    @Schema(description = "첨부파일유형코드")
    private String atacFileTypeCd;

    @Schema(description = "첨부파일유형")
    private String atacFileTaskClCd;

    @Schema(description = "첨부파일명")
    private String atacFileNm;

    @Schema(description = "첨부파일크기")
    private int atacFileSize;

    @Schema(description = "첨부파일경로명")
    private String atacFilePathNm;

    @Schema(description = "첨부파일URL주소")
    private String atacFileLocUrladdr;

    @Schema(description = "첨부파일상태코드")
    private String atacFileStsCd;

    @Override
    public AttachFile toEntity() {
        AttachFile entity = new AttachFile();
        BeanUtils.copyProperties(this, entity);

        return entity;
    }
}
