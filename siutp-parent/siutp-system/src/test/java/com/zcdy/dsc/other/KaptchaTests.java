package com.zcdy.dsc.other;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.zcdy.dsc.common.util.KaptchaUtils;

/**
 * @author Roberto
 * @date 2020/06/30
 */
public class KaptchaTests {

    @Test
    public void test() throws IOException {
        File dir = new File("C:\\Users\\admin\\Desktop\\kaptcha");
        if(!dir.exists()) {
            dir.mkdirs();
        }
        int w = 200, h = 80;
        for (int i = 0; i < 50; i++) {
            String verifyCode = KaptchaUtils.generateVerifyCode(4);
            File file = new File(dir, verifyCode + ".jpg");
            KaptchaUtils.outputImage(w, h, file, verifyCode);
        }
    }
    
    @Test
    public void testBase64Img() throws IOException {
        int w = 200, h = 80;
        String verifyCode = KaptchaUtils.generateVerifyCode(4);
        String base64Str = KaptchaUtils.outputVerifyBase64(w, h, verifyCode);
        System.out.println(base64Str);
    }
    
}
