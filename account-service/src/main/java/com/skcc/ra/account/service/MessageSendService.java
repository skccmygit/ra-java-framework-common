package com.skcc.ra.account.service;

import com.skcc.ra.account.api.dto.requestDto.SimpleSendDto;

import java.util.List;

public interface MessageSendService {

    void sendSms(SimpleSendDto simpleSendDto);

    void sendSmsList(List<SimpleSendDto> simpleSendDto);
}
