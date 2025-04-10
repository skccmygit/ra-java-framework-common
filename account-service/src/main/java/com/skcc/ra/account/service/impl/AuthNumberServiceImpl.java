package com.skcc.ra.account.service.impl;

import com.skcc.ra.account.adaptor.client.CommonClient;
import com.skcc.ra.account.adaptor.client.MessagingClient;
import com.skcc.ra.account.api.dto.requestDto.SimpleSendDto;
import com.skcc.ra.account.api.type.MsgFormType;
import com.skcc.ra.account.domain.loginCert.UserAuth;
import com.skcc.ra.account.repository.UserAuthRepository;
import com.skcc.ra.account.service.AuthNumberService;
import com.skcc.ra.account.service.MessageSendService;
import com.skcc.ra.common.api.dto.domainDto.UserBasicDto;
import com.skcc.ra.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@Slf4j
public class AuthNumberServiceImpl implements AuthNumberService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    CommonClient commonClient;

    @Autowired
    MessagingClient messagingClient;

    @Autowired
    MessageSendService messageSendService;

    @Override
    public String getAuthNum(String userid, String method) {

        //사번 대문자
        userid = userid.toUpperCase();

        //인증번호 생성 후 입력
        String authRand = createAuthNumber();
        saveAuthNumber(userid, authRand);

        //사원정보 조회
        List<UserBasicDto> userBasicDtoList = commonClient.searchUserBasic(null,null, userid);
        if(userBasicDtoList.isEmpty()) { throw new ServiceException("COM.I1006"); }

        //계정신청자에게 인증번호 발송
        if("phno".equals(method)){
            List<String> params = new ArrayList<>();
            params.add(userid);
            params.add(authRand);
            messageSendService.sendSms(new SimpleSendDto(userBasicDtoList.get(0).getMphno(), MsgFormType.SEND_AUTH_NUM.getCode(), params));
        }

        return authRand;
    }

    @Override
    public Boolean authByAuthNum(String userid, String authNumber){

        userid = userid.toUpperCase();

        //인증번호 빈 값으로 들어올 때
        if(authNumber.isEmpty()) throw new ServiceException("COM.I1026");

        Optional<UserAuth> result = userAuthRepository.findById(userid);
        if(result.isPresent()) {
            UserAuth userAuth = result.get();

            //이미 인증 완료된 경우
            if ("Y".equals(userAuth.getUseYn())) throw new ServiceException("COM.I1025");
            //인증 완료 시, UserAuth 테이블에서 인증번호 데이터 삭제 후 성공(return true)
            if (userAuth.getAuthNumber().equals(authNumber) && "N".equals(userAuth.getUseYn())) {
                userAuth.setUseYn("Y");
                userAuthRepository.save(userAuth);
                return true;
            }
            //인증번호 오기입 시, 실패(return false)
            else throw new ServiceException("COM.I1017");
        }else{
            throw new ServiceException("COM.I1017");
        }
    }

    @Override
    public String createAuthNumber(){

        Random rand;
        try {
            rand = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        rand.setSeed(System.currentTimeMillis());
        StringBuilder authRand = new StringBuilder();

        for(int i=0; i<6; i++){
            authRand.append(rand.nextInt(10));
        }
        return authRand.toString();
    }

    @Override
    public void saveAuthNumber(String userid, String authRand){
        UserAuth userAuth = new UserAuth();

        userAuth.setUserid(userid);
        userAuth.setAuthNumber(authRand);
        userAuth.setUseYn("N");

        userAuthRepository.save(userAuth);
    }

}