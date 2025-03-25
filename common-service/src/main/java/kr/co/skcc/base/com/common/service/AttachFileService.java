package kr.co.skcc.base.com.common.service;

import kr.co.skcc.base.com.common.api.dto.domainDto.AttachFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachFileService {

    List<AttachFileDto> findFileNoList(List<Long> fileNoList);
    List<AttachFileDto> fileUpload(List<MultipartFile> files, String taskGroup) throws IOException;
    void fileDownload(Long fileNoList) throws IOException;
    List<Long> removeFileSoft(List<Long> fileNoList);
    List<Long> removeFileHard(List<Long> fileNoList);

}
