package kr.co.skcc.base.com.common.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.skcc.base.com.common.api.dto.domainDto.AttachFileDto;
import kr.co.skcc.base.com.common.service.AttachFileService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[기타] 파일 관리(AttachFileResource)", description = "첨부 파일 관리를 위한 API")
@RestController
@RequestMapping("/v1/com/common/attachFile")
@Slf4j
public class AttachFileResource {
    @Autowired
    private AttachFileService attachFileService;

    @Operation(summary = "파일 조회 - 파일NO 기준")
    @GetMapping("/fileNo")
    public ResponseEntity<List<AttachFileDto>> findAllFileList(@RequestParam List<Long> fileNoList){
        return new ResponseEntity<>(attachFileService.findFileNoList(fileNoList), HttpStatus.OK);
    }

    @Operation(summary = "파일 업로드")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<AttachFileDto>> fileUpload(@RequestPart List<MultipartFile> files,
                                                          @RequestParam String taskGroup) throws IOException {
        return new ResponseEntity<>(attachFileService.fileUpload(files, taskGroup), HttpStatus.OK);
    }

    @Operation(summary = "파일 다운로드")
    @GetMapping("/download")
    public ResponseEntity<HttpStatus> fileDownload(@RequestParam Long fileNo) throws IOException {
        attachFileService.fileDownload(fileNo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "파일 삭제 - DB flag만 변경")
    @PutMapping("/remove/soft")
    public ResponseEntity<List<Long>> removeFileSoft(@RequestParam List<Long> fileNoList){
        return new ResponseEntity<>(attachFileService.removeFileSoft(fileNoList), HttpStatus.OK);
    }

    @Operation(summary = "파일 삭제 - 물리 파일까지 삭제")
    @PutMapping("/remove/hard")
    public ResponseEntity<List<Long>> removeFileHard(@RequestParam List<Long> fileNoList) {
        return new ResponseEntity<>(attachFileService.removeFileHard(fileNoList), HttpStatus.OK);
    }
}
