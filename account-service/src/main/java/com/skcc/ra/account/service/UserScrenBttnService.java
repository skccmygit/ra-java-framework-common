package com.skcc.ra.account.service;

import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.domainDto.ScrenDto;

import java.util.List;

public interface UserScrenBttnService {

    List<ScrenDto> findUserRoleScren(String userid);
    List<BttnDto> findUserRoleScrenBttn(String userid, String screnId);
    List<BttnDto> findBttnByUserScren(String userid, String screnId);

    List<ScrenDto> searchScrenAuthByUserid(String chrgTaskGroupCd, String screnClCd, String screnNm);
    List<BttnDto> searchBttnAuthByUserid(Integer athrtyReqstSeq, String screnId, String userid);

    List<ScrenDto> searchReqScrenAuthByAthrtyReqstSeq(Integer athrtyReqstSeq, String chrgTaskGroupCd, String screnNm);
    List<BttnDto> searchReqBttnAuthByAthrtyReqstSeq(Integer athrtyReqstSeq, String screnId);
}
