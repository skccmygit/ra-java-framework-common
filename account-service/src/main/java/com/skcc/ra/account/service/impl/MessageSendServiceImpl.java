package com.skcc.ra.account.service.impl;

import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.account.adaptor.client.MessagingClient;
import com.skcc.ra.account.api.dto.requestDto.SimpleSendDto;
import com.skcc.ra.account.service.MessageSendService;
import com.skcc.ra.common.api.dto.requestDto.SendReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class MessageSendServiceImpl implements MessageSendService {

    @Autowired
    MessagingClient messagingClient;

    @Override
    public void sendSms(SimpleSendDto simpleSendDto) {

        messagingClient.sendSms(SendReqDto.builder()
                .smsNotitClCd("1")
                .dsprIdentNo("SYSTEM")
                .rcverPhno(simpleSendDto.getRcverPhno())
                .smsMsgFormId(simpleSendDto.getSmsMsgFormId())
                .params(simpleSendDto.getParams())
                .build());
    }

    @Override
    public void sendSmsList(List<SimpleSendDto> simpleSendDtoList) {
        for(SimpleSendDto simpleSendDto : simpleSendDtoList){
            sendSms(simpleSendDto);
        }
    }

}
