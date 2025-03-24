package kr.co.skcc.base.com.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 데이터 암/복호화 해싱 처리
 */
@Component
@Slf4j
public class CryptoUtil {
    private static String PASSWORD_HASH_SALT;

    @Value("${app.hashSalt:#{null}}")
    public void setPASSWORD_HASH_SALT(String value){
        CryptoUtil.PASSWORD_HASH_SALT = value;
    }

    // 암호화 변환 규칙(암호화알고리즘/운용모드/패딩)
    private static String transformationRule = "AES/CBC/PKCS5Padding";
    // Initialization Vector
    // ( 블록 암호의 운용 모드[Block engine modes of operation]가 CBC/OFB/CFB를 사용할 경우에는
    //   Initialization Vector[IV]를 설정해줘야한다. )
    private static String IV = "ABCDEFGHIJKLMNOP";

    /**
     * AES 복호화 메소드
     *
     * @param plainText
     * @param encryptionKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText, String encryptionKey) throws Exception {
        return encrypt( plainText, encryptionKey, IV);
    }

    /**
     * AES 암호화 메소드
     *
     * @param plainText
     * @param encryptionKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText, String encryptionKey, String IVOption) throws Exception {

        // 암호화 객체 생성
        Cipher cipher = Cipher.getInstance(transformationRule);
        // AES 알고리즘에 사용할 비밀키(SecretKey) 를 생성
        SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        // Initialization Vector 객체 생성, 암호화 객체 '암호화 모드'로 초기화
        IvParameterSpec ivParameterSpec;
        if(!(IVOption == null || "".equals(IVOption))) {
            ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        }else{
            ivParameterSpec = new IvParameterSpec(new byte[16]);
        }
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        // 암호화 수행
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        String encodedText = Base64.getEncoder().encodeToString(encryptedBytes);

        return encodedText;
    }

    /**
     * AES 복호화 메소드
     *
     * @param cipherText
     * @param encryptionKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String cipherText, String encryptionKey) throws Exception {
        return decrypt(cipherText, encryptionKey, IV);
    }

    public static String decrypt(String cipherText, String encryptionKey, String IVOption) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        return decrypt(decodedBytes, encryptionKey, IVOption);
    }
    /**
     * AES 복호화 메소드
     *
     * @param decodedBytes
     * @param encryptionKey
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] decodedBytes, String encryptionKey, String IVOption) throws Exception {
        // 암호화 객체 생성
        Cipher cipher = Cipher.getInstance(transformationRule);
        // AES 알고리즘에 사용할 비밀키(SecretKey) 를 생성
        SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec;
        if(!(IVOption == null || "".equals(IVOption))) {
            // Initialization Vector 객체 생성 , 암호화 객체 '복호화 모드'로 초기화
            ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        }else{
            ivParameterSpec = new IvParameterSpec(new byte[16]);
        }
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        // 복호화 수행
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);

        return decryptedText;
    }

    public static String encryptSha256(String plainTxt, String saltKey) {

        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((plainTxt + saltKey).getBytes());

            byte[] bytes = md.digest();

            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }

            result = builder.toString();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return result.toUpperCase();
    }
}
