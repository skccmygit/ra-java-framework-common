package com.skcc.ra.common.service.impl;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.common.api.dto.domainDto.AttachFileDto;
import com.skcc.ra.common.domain.attachFile.AttachFile;
import com.skcc.ra.common.domain.cmmnCd.CmmnCdDtl;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.repository.AttachFileRepository;
import com.skcc.ra.common.repository.CmmnCdDtlRepository;
import com.skcc.ra.common.service.AttachFileService;
import com.skcc.ra.common.util.RequestUtil;
import com.skcc.ra.common.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AttachFileServiceImpl implements AttachFileService {

    @Autowired
    private AttachFileRepository attachFileRepository;

    @Autowired
    private CmmnCdDtlRepository cmmnCdDtlRepository;

    @Value("${spring.servlet.multipart.location}")
    private String savePath;

    @Value("${spring.profiles.active}")
    private String env;

    private static final String ILLEGAL_CHARS = "[<>:\"|?*]";

    private String[] allowedExtensionType = {
        "xlsx","xls","doc","docx","ppt","pptx", //"potx","ppsx","xltx","dotx",
        "pdf",
        "png","jpg","jpeg","gif","bmp",
        "zip",
        "txt",
        "bin","s","dat" // 통합GW 요청으로 추가
    };

    private String[] allowedMIMETypesEquals = {
        "application/zip",    // .zip
        "application/pdf",    // .pdf
        "application/x-tika-msoffice", // .doc,
        "application/x-tika-ooxml",  // .xlsx, .pptx, .docx
        "application/octet-stream"
    };

    private String[] allowedMIMETypesStartsWith = {
        "image",    // .png, .jpg, .jpeg, .gif, .bmp
        "text",     // .txt, .html 등
        "application/vnd.ms-word",          // .docx 등 워드 관련
        "application/vnd.ms-excel",         // .xls 등 엑셀 관련
        "application/vnd.ms-powerpoint",    // .ppt 등 파워포인트 관련// .docx, .dotx, .xlsx, .xltx, .pptx, .potx, .ppsx
        "application/vnd.openxmlformats-officedocument",
        "application/vnd.hancom"     // .hwp 관련

    };

    @Override
    public List<AttachFileDto> findFileNoList(List<Long> fileNoList){
        List<AttachFile> attachFiles = new ArrayList<>();
        for (Long fileNo : fileNoList) {
            if (attachFileRepository.existsById(fileNo)) {
                attachFiles.add(findFile(fileNo));
            }
        }

        if (attachFiles.isEmpty()) throw new ServiceException("COM.I0013");

        return attachFiles.stream().map(AttachFile::toApi).collect(Collectors.toList());
    }

    public AttachFile findFile(Long fileNo) {
        Optional<AttachFile> optional = attachFileRepository.findById(fileNo);
        if (optional.isPresent()) return optional.get();
        else return null;
    }

    @Override
    public List<AttachFileDto> fileUpload(List<MultipartFile> files, String taskGroup) {
        List<AttachFileDto> fileList = new ArrayList<>();

        CmmnCdDtl cmmnCdDtl = cmmnCdDtlRepository.findByCmmnCdAndCmmnCdVal("ATAC_FILE_TASK_CL_CD", taskGroup);
        String saveFilePath = savePath + "/ATACFILE/ETC";
        if (!(cmmnCdDtl == null || "".equals(cmmnCdDtl.getRefrnAttrVal1()))) {
            saveFilePath = savePath + cmmnCdDtl.getRefrnAttrVal1();
        }

        // saveFilePath = /절대경로/업무구분/업로드년월
        String saveDir = "yyyyMM";
        if (cmmnCdDtl != null && !"".equals(cmmnCdDtl.getRefrnAttrVal2())) {
            saveDir = cmmnCdDtl.getRefrnAttrVal2();
        }
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern(saveDir));
        saveFilePath += "/" + today;

        for (MultipartFile file : files) {
            fileList.add(upload(file, taskGroup, saveFilePath));
        }
        return fileList;
    }

    public AttachFileDto upload(MultipartFile file, String taskGroup, String saveFilePath) {
        AttachFileDto attachFileDto = null;
        try {
            //원래 파일명
            String origFilename = file.getOriginalFilename();
            //저장될 파일명 생성
            String filename = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(origFilename);
            //파일저장 경로(경로 + 저장될 파일명)
            String filePath = saveFilePath + "/" + filename;

            //파일사이즈
            long fileSize = file.getSize();

            //파일 종류(1:파일 / 2:이미지)
            String extension  = FilenameUtils.getExtension(origFilename).toLowerCase();
            //파일 종류(1:파일 / 2:이미지)
            String fileType = "1";
            //파일타입
            String mimeType = new Tika().detect(file.getInputStream());
            if ("image".equals(mimeType)) {
                fileType = "2";
            } else if ("png".equals(extension) || "jpg".equals(extension) || "jpeg".equals(extension)
                    || "gif".equals(extension) || "bmp".equals(extension) ){
                fileType = "2";
            }

            log.info("===================================================" );
            log.info("origFilename : " + origFilename);
            log.info("filePath : " + filePath);
            log.info("fileSize : " + fileSize);
            log.info("fileType : " + fileType);
            log.info("taskGroup : " + taskGroup);
            log.info("mimeType : " + mimeType);
            log.info("env : " + env);
            log.info("extension : " + extension);
            log.info("===================================================" );

            //MIME Type + Extension 체크  - WFM 은 예외처리
            boolean chkFileType = chkFileType(mimeType, extension);
            if (!chkFileType && !"WFM".equals(taskGroup)) {
                throw new ServiceException("COM.I0014");
            } else {
                if (!isValidPath(saveFilePath)) {
                    saveFilePath = repairPath(saveFilePath);
                }
                Path pathFile = Paths.get(saveFilePath);
                Path parentDir = pathFile.getParent();
                if (parentDir != null && !Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                }
                if (!new File(saveFilePath).exists()){
                    new File(saveFilePath).mkdir();
                }
                try {
                    file.transferTo(new File(filePath));

                    //업로드 성공한 파일의 fileDTO 리턴
                    attachFileDto = attachFileRepository.save(AttachFileDto.builder()
                                                                    .atacFileNm(origFilename)
                                                                    .atacFilePathNm(filePath)
                                                                    .atacFileSize((int)fileSize)
                                                                    .atacFileTypeCd(fileType)
                                                                    .atacFileTaskClCd(taskGroup)
                                                                    .atacFileStsCd("1")
                                                                    .build().toEntity()).toApi();
                    return attachFileDto;
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new ServiceException("COM.I0017");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("COM.I0017");
        }
    }

    public static boolean isValidPath(String pathStr) {
        try {
            Paths.get(pathStr);
            return true;
        } catch (InvalidPathException ex) {
            return false;
        }
    }

    public static String repairPath(String original) {
        // 1) Normalize all slashes to the platform default
        String sep = File.separator;
        String normalized = original.replace("\\", sep).replace("/", sep);

        // 2) Split into segments, stripping illegal characters from each
        String[] rawSegments = normalized.split(sep.equals("\\") ? "\\\\" : sep);
        List<String> cleanSegments = new ArrayList<>();
        for (String seg : rawSegments) {
            if (seg.isEmpty()) continue;                // skip empty
            // remove any illegal characters
            String cleaned = seg.replaceAll(ILLEGAL_CHARS, "");
            if (!cleaned.isEmpty()) {
                cleanSegments.add(cleaned);
            }
        }

        // 3) Rebuild the path, preserving a leading drive letter or root if present
        StringBuilder rebuilt = new StringBuilder();
        // Detect Windows drive letter (e.g. "C:")
        if (cleanSegments.size() > 0 && cleanSegments.get(0).matches("^[A-Za-z]:$")) {
            rebuilt.append(cleanSegments.remove(0))
                    .append(sep);
        } else if (normalized.startsWith(sep)) {
            // absolute on Unix or UNC root on Windows
            rebuilt.append(sep);
        }

        // join the rest
        for (int i = 0; i < cleanSegments.size(); i++) {
            rebuilt.append(cleanSegments.get(i));
            if (i < cleanSegments.size() - 1) {
                rebuilt.append(sep);
            }
        }

        return rebuilt.toString();
    }

    public boolean chkFileType(String mimeType, String fileType) {
        if (mimeType == null || mimeType.equals("")) return false;

        int extensionCnt = 0;
        int mimeCnt = 0;
        log.debug("fileType : " + fileType + "  mimeType : " + mimeType);
        for (int i=0; i<allowedExtensionType.length; i++) {
            if (fileType.startsWith(allowedExtensionType[i])) {
                extensionCnt++;
                break;
            }
        }
        for (int i=0; i<allowedMIMETypesEquals.length; i++) {
            if (mimeType.equals(allowedMIMETypesEquals[i])) {
                mimeCnt++;
                break;
            }
        }
        for (int i=0; i<allowedMIMETypesStartsWith.length; i++) {
            if (mimeType.startsWith(allowedMIMETypesStartsWith[i])) {
                mimeCnt++;
                break;
            }
        }
        if (extensionCnt > 0 && mimeCnt > 0) return true;
        return false;
    }


    @Override
    public void fileDownload(Long fileNo) throws IOException {
        AttachFile downFile;
        downFile = findFile(fileNo);
        fileDown(downFile);
    }

    public void fileDown(AttachFile downFile) throws IOException {
        File file;
        OutputStream out;
        String mimeType;

        String path = downFile.getAtacFilePathNm();

        file = new File(path);
        if (!file.exists()) throw new ServiceException("COM.I0013");
        String downfileName =URLEncoder.encode(downFile.getAtacFileNm(),"UTF-8").replaceAll("\\+", " ");

        try (InputStream in = new FileInputStream(file)) {
            ServletContext context = RequestUtil.getHttpServletRequest().getServletContext();
            mimeType = context.getMimeType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            HttpServletResponse res = ResponseUtil.getHttpServletResponse();

            res.setCharacterEncoding("UTF-8");
            res.setContentType(mimeType);
            res.setHeader("Content-Disposition", "attachment;filename=" + downfileName + ";");
            res.setHeader("Content-Transfer-Encoding", "binary");

            res.setContentLength((int)file.length());
            out = res.getOutputStream();

            byte[] buf = new byte[1024];
            int len = 0;

            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Long> removeFileSoft(List<Long> fileNoList){
        List<AttachFile> deleteFileList = attachFileRepository.findFileList(fileNoList);
        List<Long> deleteFileNo = new ArrayList<>();

        if(!(deleteFileList == null  || deleteFileList.isEmpty())) {
            deleteFileNo = deleteFileList.stream().map(AttachFile::getAtacFileNo).collect(Collectors.toList());
            attachFileRepository.updateAtacFileStsCd(deleteFileNo, "2");
        }

        return deleteFileNo;
    }

    @Override
    public List<Long> removeFileHard(List<Long> fileNoList){

        List<AttachFile> deleteFileList = attachFileRepository.findFileList(fileNoList);
        List<Long> deleteFileNo = new ArrayList<>();
        if (!(deleteFileList == null || deleteFileList.isEmpty())) {
            File file;
            for (AttachFile item : deleteFileList) {
                file = new File(item.getAtacFilePathNm());
                if (file.exists()) {
                    if (!file.delete()) {
                        throw new ServiceException("COM.I0003");
                    }
                    deleteFileNo.add(item.getAtacFileNo());
                }
            }
        }
        if(!(deleteFileNo == null || deleteFileNo.isEmpty())) {
            attachFileRepository.updateAtacFileStsCd(deleteFileNo, "X");
        }

        return deleteFileNo;
    }

}
