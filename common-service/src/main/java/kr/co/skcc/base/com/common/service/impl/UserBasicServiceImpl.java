package kr.co.skcc.base.com.common.service.impl;

import org.springframework.transaction.annotation.Transactional;
import kr.co.skcc.base.com.common.api.dto.domainDto.UserBasicDto;
import kr.co.skcc.base.com.common.api.dto.responseDto.InnerCallInfoDto;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.InnerCallInfoIDto;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.UserBasicScrenIDto;
import kr.co.skcc.base.com.common.domain.userBasic.UserBasic;
import kr.co.skcc.base.com.common.exception.ServiceException;
import kr.co.skcc.base.com.common.repository.UserBasicRepository;
import kr.co.skcc.base.com.common.service.UserBasicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class UserBasicServiceImpl implements UserBasicService {

    @Autowired
    UserBasicRepository userBasicRepository;


    @Override
    public Page<UserBasicDto> searchUserBasicPage(String bssmacd, String deptcd, String reofoCd, String clofpNm, String vctnNm, String empno, String deptNm, List<String> deptcdList, Pageable pageable) {

        if(bssmacd == null) bssmacd = "";
        if(deptcd == null) deptcd = "";
        if(reofoCd == null) reofoCd = "";
        if(clofpNm == null) clofpNm = "";
        if(vctnNm == null) vctnNm = "";
        if(empno == null) empno = "";
        if(deptNm == null) deptNm = "";

        String deptcdListYn = "Y";
        if(deptcdList == null || deptcdList.isEmpty())    deptcdListYn = "N";

        Page<UserBasicScrenIDto> userBasicScrenIDtoList = userBasicRepository.searchUserBasicForScren(bssmacd, deptcd, reofoCd, clofpNm, vctnNm, empno, deptNm, deptcdListYn, deptcdList, pageable);
        List<UserBasicDto> userBasicDtoList = userBasicScrenIDtoList.stream().map(m -> new UserBasicDto(m, "N", "Y")).collect(Collectors.toList());

        return new PageImpl<>(userBasicDtoList, userBasicScrenIDtoList.getPageable(), userBasicScrenIDtoList.getTotalElements());
    }

    @Override
    public String searchUserPhone(String empno) {

        Optional<UserBasic> userBasic = userBasicRepository.findByEmpno(empno);
        if(userBasic.isEmpty()) throw new ServiceException("COM.I0019");

        return userBasic.get().getMphno();
    }

    @Override
    public UserBasicDto searchTeamLeader(String deptcd, String empno) {

        if(deptcd == null && empno == null) throw new ServiceException("COM.I0003");
        if(deptcd == null && userBasicRepository.findByEmpno(empno).isPresent()) {
            Optional<UserBasic> userBasic = userBasicRepository.findByEmpno(empno);
            if(userBasic.isPresent()) {
                deptcd = userBasic.get().getDeptcd();
            }
        }

        UserBasic userBasic = userBasicRepository.searchTeamLeader(deptcd);
        if(userBasic == null) {
            userBasic = userBasicRepository.searchSuperTeamLeader(deptcd);
        }
        return userBasic.toApi();
    }

    @Override
    public List<UserBasicDto> searchUserList(List<String> empnoList, List<String> empnmList, List<String> deptcdList, String empNm, String deptNm) {
        String empnoListYn = "Y";
        String empnmListYn = "Y";
        String deptcdListYn = "Y";

        if(empnoList == null || empnoList.isEmpty())    empnoListYn = "N";
        if(empnmList == null || empnmList.isEmpty())    empnmListYn = "N";
        if(deptcdList == null || deptcdList.isEmpty())    deptcdListYn = "N";
        if(empNm == null) empNm = "";
        if(deptNm == null) deptNm = "";

        List<UserBasicScrenIDto> userBasicScrenIDtoList = userBasicRepository.searchUserList(empnoListYn, empnoList, empnmListYn, empnmList, deptcdListYn, deptcdList, empNm, deptNm);

        return userBasicScrenIDtoList.stream().map(m -> new UserBasicDto(m, "Y", "N")).collect(Collectors.toList());
    }

    @Override
    public List<UserBasicDto> searchUserListNoMasking(List<String> empnoList, List<String> empnmList, List<String> deptcdList, String empNm, String deptNm) {
        String empnoListYn = "Y";
        String empnmListYn = "Y";
        String deptcdListYn = "Y";

        if(empnoList == null || empnoList.isEmpty())    empnoListYn = "N";
        if(empnmList == null || empnmList.isEmpty())    empnmListYn = "N";
        if(deptcdList == null || deptcdList.isEmpty())    deptcdListYn = "N";
        if(empNm == null) empNm = "";
        if(deptNm == null) deptNm = "";

        List<UserBasicScrenIDto> userBasicScrenIDtoList = userBasicRepository.searchUserList(empnoListYn, empnoList, empnmListYn, empnmList, deptcdListYn, deptcdList, empNm, deptNm);

        return userBasicScrenIDtoList.stream().map(m -> new UserBasicDto(m, "N", "N")).collect(Collectors.toList());
    }

    @Override
    public List<InnerCallInfoDto> searchUserByTelno(String telno) {
        if("".equals(telno)) return null;

        List<InnerCallInfoIDto> innerCallInfoIDto = userBasicRepository.searchUserByTelno(telno);
        if(innerCallInfoIDto == null || innerCallInfoIDto.isEmpty()){
            return null;
        }

        return innerCallInfoIDto.stream().map(InnerCallInfoDto::new).collect(Collectors.toList());
    }

    @Override
    public List<UserBasicDto> searchUserBasic(String userNm, List<String> deptcdList, String empno){
        String deptcdListYn = "Y";
        if (empno == null) empno = "";
        if (userNm == null) userNm = "";
        if (deptcdList == null || deptcdList.isEmpty()) deptcdListYn = "N";

        return userBasicRepository.searchUserBasic(userNm, deptcdList, deptcdListYn, empno);
    }

}
