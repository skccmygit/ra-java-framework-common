package kr.co.skcc.oss.com.common.api.dto.emailDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "EmailContent", description = "EmailContent")
public class EmailContent {

    @Schema(description = "제목")
    private String subject;

    @Schema(description = "메일 내용 set")
    private String content;

    @Schema(description = "첨부파일 set")
    private String[] attFilePath;

}
