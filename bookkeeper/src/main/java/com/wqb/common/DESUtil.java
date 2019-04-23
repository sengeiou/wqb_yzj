package com.wqb.common;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class DESUtil {
    private static Key key;
    private static String KEY_STR = "mykey";

    static {
        try {
            // 密钥生成器
            KeyGenerator gen = KeyGenerator.getInstance("DES");
            // 防止linux下 随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(KEY_STR.getBytes());

            gen.init(56, secureRandom);
            // key = gen.generateKey();
            // gen.init(new SecureRandom(KEY_STR.getBytes()));
            key = gen.generateKey();
            gen = null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // 加密
    @SuppressWarnings("static-access")
    public static String encrypt(String source) {
        BASE64Encoder base64 = new BASE64Encoder();
        try {
            byte[] b = source.getBytes("UTF-8");
            // 密码器 完成加密或解密
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(cipher.ENCRYPT_MODE, key);
            byte[] final_byte = cipher.doFinal(b);
            return base64.encode(final_byte);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // 解密
    @SuppressWarnings("static-access")
    public static String decrypt(String source) {
        BASE64Decoder base64 = new BASE64Decoder();
        try {
            byte[] b = base64.decodeBuffer(source);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(cipher.DECRYPT_MODE, key);
            byte[] decrypt_byte = cipher.doFinal(b);
            return new String(decrypt_byte, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        String a = "1";
        System.out.println(encrypt(a));

        System.out.println(decrypt("Nk2LIX0RrFs="));
    }

}
