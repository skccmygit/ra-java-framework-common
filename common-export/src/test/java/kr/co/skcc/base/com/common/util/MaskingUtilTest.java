package kr.co.skcc.base.com.common.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MaskingUtil.class)
public class MaskingUtilTest {

    @Test
    public void getMaskedIdTest() {

        // Name Masking Test
        String nameTestResult = MaskingUtil.getMaskedId("이기정", "name");
        assertEquals("이*정", nameTestResult);

        String phoneTestResult = MaskingUtil.getMaskedId("010-5257-6025", "phone");
        assertEquals("010-****-6025", phoneTestResult);

    }

}
