package com.skcc.ra.account.config;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ArgosPasswordEncoder.java
 * : Argos 사용 암호 알고리즘   SHA256
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-03-28, 최초 작성
 */
public class ArgosPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {

        return DigestUtils.sha512Hex(String.valueOf(rawPassword)).toUpperCase();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        /*
        byte[] digested = decode(encodedPassword);
		byte[] salt = EncodingUtils.subArray(digested, 0, this.saltGenerator.getKeyLength());
		return MessageDigest.isEqual(digested, digest(rawPassword, salt));
         */
        return encodedPassword.equals(this.encode(rawPassword));
    }
}
