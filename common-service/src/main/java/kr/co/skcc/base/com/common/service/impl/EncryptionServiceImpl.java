package kr.co.skcc.oss.com.common.service.impl;

import org.springframework.transaction.annotation.Transactional;
import kr.co.skcc.oss.com.common.service.EncryptionService;
import kr.co.skcc.oss.com.common.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * EncryptionServiceImpl.java
 * : 암복호화 라이브러리 테스트를 위한 ServiceImple Class
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2021-10-14, 최초 작성
 */
@Service
@Transactional
@Slf4j
public class EncryptionServiceImpl implements EncryptionService {

    @Value("${app.secret-key}")
    private String secretKey;

    @Override
    public String encrypt(String text) throws Exception {
        return CryptoUtil.encrypt(text, secretKey);
    }

    @Override
    public String decrypt(String text) throws Exception {
        return CryptoUtil.decrypt(text, secretKey);
    }

}
