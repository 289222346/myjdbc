package com.myjdbc.core.util;

import com.myjdbc.core.util.config.properties.PropertiesConfigUtil;
import com.myjdbc.core.util.config.properties.enums.PropertiesFile;
import com.myjdbc.core.util.config.properties.enums.PropertoesSecret;

import java.util.Base64;

/**
 * 密码工具
 *
 * @author 陈文
 * @date 2020/05/27
 */
public class SecretUtil {

    /**
     * 秘钥
     */
    private static final String SECRET_KEY;

    /**
     * 秘钥队列-实际使用
     */
    private static final byte[] SECRET_BYTE;

    static {
        //属性工具
        PropertiesConfigUtil util = new PropertiesConfigUtil(PropertiesFile.SECRET);
        //获取秘钥
        SECRET_KEY = util.readProperty(PropertoesSecret.SECRET_KEY.getCode()) + "";
        //生成秘钥队列
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
        if (value == null) {
            return null;
        }

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
     * 密码加密
     *
     * @param password  原始密码
     * @param publicKey 公钥
     * @return 加密后的密码
     */
    public static String handPassword(String password, String publicKey) {
        password = SecretUtil.encryption(password, publicKey);
        password = Md5Util.md5Hex(password);
        return password;
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

    /**
     * 从密/公钥中按位取出
     * 位超出长度，则从头部继续计算位数（取余）
     *
     * @param index 位
     * @param key   公/密匙
     * @return
     */
    private static byte keyQueue(int index, byte[] key) {
        index = index % key.length;
        return key[index];
    }

}
