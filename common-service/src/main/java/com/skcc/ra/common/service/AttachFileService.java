package com.skcc.ra.common.service;

import com.skcc.ra.common.api.dto.domainDto.AttachFileDto;
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
