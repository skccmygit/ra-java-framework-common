package com.skcc.ra.account.service.impl;

import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.account.adaptor.client.CommonClient;
import com.skcc.ra.account.api.dto.domainDto.UserActvyLogDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.UserActvyLogIDto;
import com.skcc.ra.account.domain.hist.UserActvyLog;
import com.skcc.ra.account.domain.loginCert.RefreshToken;
import com.skcc.ra.account.repository.RefreshTokenRepository;
import com.skcc.ra.account.repository.UserActvyLogRepository;
import com.skcc.ra.account.service.UserActvyLogService;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.util.ExcelStream;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserActvyLogServiceImpl implements UserActvyLogService {

    @Autowired
    UserActvyLogRepository userActvyLogRepository;

    @Autowired
    CommonClient commonClient;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public Page<UserActvyLogIDto> findUserActvyLog(String systmCtgryCd, String userActvyTypeCd, List<String> deptcdList,
                                                   String userNm, String userActvyDtmFrom, String userActvyDtmTo, Pageable pageable) {

        if (userActvyDtmFrom == null || userActvyDtmTo == null) throw new ServiceException("COM.I1030");

        if (systmCtgryCd == null) systmCtgryCd = "";
        if (userActvyTypeCd == null) userActvyTypeCd = "";
        if (userNm == null) userNm = "";
        String deptcdListYn = (deptcdList == null || deptcdList.isEmpty()) ? "N" : "Y";
        LocalDate toDate = LocalDate.parse(userActvyDtmTo, DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays(1L);
        userActvyDtmTo = toDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));


        return userActvyLogRepository.findUserActvyLogList(systmCtgryCd, userActvyTypeCd, deptcdList, deptcdListYn,
                                                                                                userNm, userActvyDtmFrom, userActvyDtmTo, pageable);
    }
    @Override
    public void regUserActvyLog(UserActvyLogDto userActvyLogDto) {

        try {
            UserActvyLog userActvyLog = userActvyLogDto.toEntity();
            // 현재시간
            userActvyLog.setUserActvyDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

            if (userActvyLog.getUserid() == null || "".equals(userActvyLog.getUserid())) {
                userActvyLog.setUserid(RequestUtil.getLoginUserid());
            }

            if (userActvyLog.getLastChngrId() == null || "".equals(userActvyLog.getLastChngrId())) {
                userActvyLog.setLastChngrId(userActvyLog.getUserid());
            }

            if (userActvyLog.getSystmCtgryCd() == null || "".equals(userActvyLog.getSystmCtgryCd())) {
                userActvyLog.setSystmCtgryCd("ARG");
            }

            if (userActvyLog.getConnIpaddr() == null || "".equals(userActvyLog.getConnIpaddr())) {
                userActvyLog.setConnIpaddr(RequestUtil.getClientIP().length() > 16 ? "127.0.0.1" : RequestUtil.getClientIP());
            }
            if ("ARG".equals(userActvyLogDto.getSystmCtgryCd())) {
                Optional<RefreshToken> oRt = refreshTokenRepository.findById(userActvyLog.getUserid());
                RefreshToken rt;
                if (oRt.isPresent()) {
                    rt = oRt.get();
                    userActvyLog.setAccssTokenVal(rt.getAccessToken());
                    userActvyLog.setRefshTokenVal(rt.getRefreshToken());
                }
            }

            userActvyLogRepository.save(userActvyLog);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void makeExcel(String systmCtgryCd, String userActvyTypeCd, List<String> deptcdList,
                                String userNm, String userActvyDtmFrom, String userActvyDtmTo) throws IOException {
        if(systmCtgryCd == null) systmCtgryCd = "";
        if(userActvyTypeCd == null) userActvyTypeCd = "";
        if(userNm == null) userNm = "";
        String deptcdListYn = (deptcdList == null || deptcdList.isEmpty()) ? "N" : "Y";
        LocalDate toDate = LocalDate.parse(userActvyDtmTo, DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays(1L);
        userActvyDtmTo = toDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String fileName = "사용자활동로그(" + userActvyDtmFrom + "-" + userActvyDtmTo + ")";

        String[] arrayHeaderNm = {"시스템명", "부서명","직급","직능"
                                  ,"사용자ID","사용자명","활동유형","로그일시","접속IP","업무","메뉴명","화면명","다운로드사유"};

        String[] arrayBodyColNm = {"systmCtgryNm","deptNm", "clofpNm", "vctnNm"
                                    ,"userid","userNm","userActvyTypeNm","userActvyDtm","connIpaddr","chrgTaskGroupNm","menuNm","screnNm","dwnldResonCntnt"};

        ExcelStream e = new ExcelStream(arrayHeaderNm, arrayBodyColNm);

        int allCount = userActvyLogRepository.excelDataCount(systmCtgryCd,
                userActvyTypeCd,
                deptcdList,
                deptcdListYn,
                userNm,
                userActvyDtmFrom,
                userActvyDtmTo);

        int size = 5000;
        int maxExcelSize = "prd".equals(env) ? 200000 : 10000;
        int pageSize;

        if (allCount != 0) {
            if (allCount > maxExcelSize) throw new ServiceException("COM.I0032");
            pageSize = allCount % size == 0 ? allCount/size : allCount/size + 1;
        } else {
            throw new ServiceException("COM.I0033");
        }

        List<UserActvyLogIDto> userActvyLogIDtoList;
        for (int i = 0; i < pageSize; i++) {
            int offset = (i * size);
            log.info("allCount : {}, pageSize : {}, size : {}, i : {}, offset = {} ", allCount, pageSize, size, i, offset);
            userActvyLogIDtoList = userActvyLogRepository.findUserActvyLogListForExcel(systmCtgryCd,
                    userActvyTypeCd,
                    deptcdList,
                    deptcdListYn,
                    userNm,
                    userActvyDtmFrom,
                    userActvyDtmTo,
                    offset,
                    size);
            e.start(userActvyLogIDtoList);
        }
        e.download(fileName);
    }
}
