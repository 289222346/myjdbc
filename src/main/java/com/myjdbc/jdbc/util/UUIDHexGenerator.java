package com.myjdbc.jdbc.util;


import com.myjdbc.jdbc.util.more.MacAddressAPI;

/**
 * 该类参考了Hibernate的写法
 * 生成32位UUID字符串
 */
public class UUIDHexGenerator {

    /**
     * 网卡MAC地址
     */
    private static final int MAC = BytesHelper.toInt(MacAddressAPI.getMACAddress().getBytes());
    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);
    private static String sep = "";
    private static short counter = 0;

    public static String getUUID() {
        return new StringBuffer(64).append(format(MAC)).append(sep).append(format(JVM)).append(sep).append(format(getHiTime())).append(sep).append(format(getLoTime())).append(sep).append(format(getCount())).toString();
    }

    private static String format(int intValue) {
        String formatted = Integer.toHexString(intValue);
        StringBuffer buf = new StringBuffer("00000000");

        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    private static String format(short shortValue) {
        String formatted = Integer.toHexString(shortValue);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    private static short getHiTime() {
        return (short) ((int) (System.currentTimeMillis() >>> 32));
    }

    private static int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    private static short getCount() {
        Class var1 = UUIDHexGenerator.class;
        synchronized (UUIDHexGenerator.class) {
            if (counter < 0) {
                counter = 0;
            }
            return counter++;
        }
    }
}
