package kr.co.skcc.oss.com.common.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.skcc.oss.com.common.service.EncryptionService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[기타] 암/복호화 API(EncryptionResource)", description = "암/복호화를 위한 API(테스트용..실제로는 CryptoUtil 직접 사용) ")
@RestController
@RequestMapping("/v1/com/common/encryption")
@Slf4j
public class EncryptionResource {
    @Autowired
    private EncryptionService encryptionService;

    @Operation(summary = "문자열 암호화")
    @GetMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestParam String originText) throws Exception {
        return new ResponseEntity<>(encryptionService.encrypt(originText), HttpStatus.OK);
    }

    @Operation(summary = "문자열 복호화")
    @GetMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestParam String cipherText) throws Exception {
        return new ResponseEntity<>(encryptionService.decrypt(cipherText), HttpStatus.OK);
    }
}
