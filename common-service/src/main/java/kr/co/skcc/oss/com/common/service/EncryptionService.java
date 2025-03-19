package kr.co.skcc.oss.com.common.service;

import java.io.UnsupportedEncodingException;

public interface EncryptionService {
    /**
     * Encrypt string.
     *
     * @param text the text
     * @return the string
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    String encrypt(String text) throws Exception;

    /**
     * Decrypt string.
     *
     * @param text the text
     * @return the string
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    String decrypt(String text) throws Exception;

}
