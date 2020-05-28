package com.myjdbc.core.util;

import com.myjdbc.core.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Random;

/**
 * 密码配置工具
 *
 * @author 陈文
 * @date 2020/05/27
 */
public class SecretUtil {

    /**
     * 秘钥
     */
    private static final String SECRET_KEY = "Z8SjxH4Lc/Inebb9Zl5G9RziP6eY";

    /**
     * 秘钥-实际使用
     */
    private static final byte[] SECRET_BYTE;

    static {
        SECRET_BYTE = keyAdaptation(SECRET_KEY);
    }

    /**
     * 秘钥适配
     *
     * @param key 秘钥
     * @return
     */
    private static byte[] keyAdaptation(String key) {
        char[] secretKey = key.toCharArray();
        byte[] bytes = new byte[secretKey.length];
        int index = 0;
        for (char c : secretKey) {
            bytes[index++] = (byte) c;
        }
        return bytes;
    }

    /**
     * 加密
     *
     * @param value          明文
     * @param publicKeyValue 公钥
     * @return 加密后的Base64字符串
     */
    public static String encryption(String value, String publicKeyValue) {
        //公钥
        byte[] publicKey = keyAdaptation(publicKeyValue);
        //转为字节码
        byte[] bytes = value.getBytes();
        //加密
        for (int i = 0; i < bytes.length; i++) {
            byte a = bytes[i];
            byte b = keyQueue(i, publicKey);
            byte c = keyQueue(i, SECRET_BYTE);
            byte d = (byte) (a + b + c);
            bytes[i] = d;
        }
        value = Base64.getEncoder().encodeToString(bytes);
        return value;
    }

    /**
     * 解密
     *
     * @param value          密文
     * @param publicKeyValue 公钥
     * @return 明文
     */
    public static String decrypt(String value, String publicKeyValue) {
        //公钥
        byte[] publicKey = keyAdaptation(publicKeyValue);
        //转为字节码
        byte[] bytes = Base64.getDecoder().decode(value);
        //解密
        for (int i = 0; i < bytes.length; i++) {
            byte a = bytes[i];
            byte b = keyQueue(i, publicKey);
            byte c = keyQueue(i, SECRET_BYTE);
            byte d = (byte) (a - c - b);
            bytes[i] = d;
        }
        value = new String(bytes);
        return value;
    }

    private static byte keyQueue(int index, byte[] key) {
        index = index % key.length;
        return key[index];
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        test("中国A");
        for (int i = 0; i < 100; i++) {
            String publicKey = (new Random().nextInt()) + "";
            System.out.println(publicKey);
            String value = encryption("wip", publicKey);
            System.out.println("加密:" + value);
            String value2 = decrypt(value, publicKey);
            System.out.println("解密:" + value2);
            String value3 = encryption("wipinf0123", publicKey);
            System.out.println("加密:" + value3);
            String value4 = decrypt(value3, publicKey);
            System.out.println("解密:" + value4);
        }
    }

}
