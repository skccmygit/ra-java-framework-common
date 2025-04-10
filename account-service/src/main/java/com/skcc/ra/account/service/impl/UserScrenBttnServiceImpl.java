package com.skcc.ra.account.service.impl;

import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.account.adaptor.client.CommonClient;
import com.skcc.ra.account.domain.addAuth.UserAuthReq;
import com.skcc.ra.account.repository.RoleScrenBttnRepository;
import com.skcc.ra.account.repository.UserAuthReqRepository;
import com.skcc.ra.account.repository.UserRoleRepository;
import com.skcc.ra.account.repository.UserScrenBttnRepository;
import com.skcc.ra.account.service.UserScrenBttnService;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.domainDto.ScrenDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ScrenIDto;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserScrenBttnServiceImpl implements UserScrenBttnService {

    @Autowired
    RoleScrenBttnRepository roleScrenBttnRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    UserScrenBttnRepository userScrenBttnRepository;

    @Autowired
    UserAuthReqRepository userAuthReqRepository;

    @Autowired
    CommonClient commonClient;

    @Override
    public List<ScrenDto> findUserRoleScren(String userid) {

        if ("".equals(userid) || userid == null) throw new ServiceException("COM.I0003");

        List<ScrenDto> screnDtoList = new ArrayList<>();
        List<String> roleList = userRoleRepository.findRoles(userid);

        // 역할기준
        if (!roleList.isEmpty()) {
            List<ScrenIDto> screnIDtoList = roleScrenBttnRepository.findUserScren(roleList);
            screnDtoList = screnIDtoList.stream().map(ScrenDto::new).collect(Collectors.toList());
        }

        // 추가권한 기준
        List<ScrenIDto> addAuth = userScrenBttnRepository.searchScrenByUserid(userid);
        if (addAuth != null && !addAuth.isEmpty()) {
            screnDtoList.addAll(addAuth.stream().map(ScrenDto::new).collect(Collectors.toList()));
        }

        return screnDtoList.stream().filter(ObjectUtil.distinctByKeys(ScrenDto::getScrenId)).collect(Collectors.toList());
    }

    @Override
    public List<BttnDto> findBttnByUserScren(String userid, String screnId) {

        List<String> roleList = userRoleRepository.findRoles(userid);
        if (roleList.isEmpty()) throw new ServiceException("COM.I1016");

        return roleScrenBttnRepository.findByScrenId(roleList, userid, screnId);
    }

    @Override
    public List<BttnDto> findUserRoleScrenBttn(String userid, String screnId) {

        if ("".equals(userid) || userid == null) throw new ServiceException("COM.I0003");
        if ("".equals(screnId) || screnId == null) throw new ServiceException("COM.I0003");

        List<String> roleList = userRoleRepository.findRoles(userid);

        return roleList.isEmpty() ? new ArrayList<>() : roleScrenBttnRepository.findUserScrenBttns(roleList, screnId);

    }

    @Override
    public List<ScrenDto> searchScrenAuthByUserid(String chrgTaskGroupCd, String screnClCd, String screnNm) {

        if (chrgTaskGroupCd == null) chrgTaskGroupCd = "";
        if (screnClCd == null) screnClCd = "";
        if (screnNm == null) screnNm = "";

        return commonClient.findUseScren(chrgTaskGroupCd, screnClCd, screnNm, "Y").toList();
    }

    @Override
    public List<BttnDto> searchBttnAuthByUserid(Integer athrtyReqstSeq, String screnId, String userid) {

        String reqUserid = userid;
        if (StringUtils.isEmpty(reqUserid) && athrtyReqstSeq > 0) {
            Optional<UserAuthReq> oUserAuthReq = userAuthReqRepository.findByAthrtyReqstSeq(athrtyReqstSeq);
            if (oUserAuthReq.isEmpty()) throw new ServiceException("COM.I1022");

            reqUserid = oUserAuthReq.get().getUserid();
        }

        return userScrenBttnRepository.searchBttnAuthByUserid(reqUserid, screnId);
    }

    @Override
    public List<ScrenDto> searchReqScrenAuthByAthrtyReqstSeq(Integer athrtyReqstSeq, String chrgTaskGroupCd, String screnNm) {
        if (chrgTaskGroupCd == null) chrgTaskGroupCd = "";
        if (screnNm == null) screnNm = "";

        return userScrenBttnRepository.searchReqScrenAuthByAthrtyReqstSeq(athrtyReqstSeq, chrgTaskGroupCd, screnNm);
    }

    @Override
    public List<BttnDto> searchReqBttnAuthByAthrtyReqstSeq(Integer athrtyReqstSeq, String screnId) {
        return userScrenBttnRepository.searchReqBttnAuthByAthrtyReqstSeq(athrtyReqstSeq, screnId);
    }
}

