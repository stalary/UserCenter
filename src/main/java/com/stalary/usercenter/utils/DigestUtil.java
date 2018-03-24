package com.stalary.usercenter.utils;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;

import java.security.Key;
import java.util.Arrays;

/**
 * DigestUtil
 *
 * @author lirongqian
 * @since 2018/02/09
 */
public class DigestUtil {
    private static byte[] bytes;
    private static AesCipherService aesCipherService;

    static {

        String key = "user";
        try {
            bytes = key.getBytes("UTF-8");

            bytes = Arrays.copyOf(bytes, 16);
        } catch (Exception ex) {
            bytes = null;
        }

        aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128); // 设置 key 长度 //生成 key

        Key key1 = aesCipherService.generateNewKey();
        String baseEncrpt =  aesCipherService.encrypt(key.getBytes(), key1.getEncoded()).toHex();
        System.out.println("baseEncrpt is "+baseEncrpt);
    }

    /**
     * 加密
     */
    public static String Encrypt(String value) {
        return aesCipherService.encrypt(value.getBytes(), bytes)
                .toHex();
    }

    /**
     * 解密
     */
    public static String Decrypt(String value) {
        return new String(aesCipherService.decrypt(
                Hex.decode(value), bytes).getBytes());
    }
}
