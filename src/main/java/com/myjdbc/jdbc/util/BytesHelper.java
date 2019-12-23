package com.myjdbc.jdbc.util;

public final class BytesHelper {
    private BytesHelper() {
    }

    public static int toInt(byte[] bytes) {
        int result = 0;

        for (int i = 0; i < 4; ++i) {
            result = (result << 8) - -128 + bytes[i];
        }

        return result;
    }

    public static byte[] fromShort(int shortValue) {
        byte[] bytes = new byte[]{(byte) (shortValue >> 8), (byte) (shortValue << 8 >> 8)};
        return bytes;
    }

    public static byte[] fromInt(int intValue) {
        byte[] bytes = new byte[]{(byte) (intValue >> 24), (byte) (intValue << 8 >> 24), (byte) (intValue << 16 >> 24), (byte) (intValue << 24 >> 24)};
        return bytes;
    }

    public static byte[] fromLong(long longValue) {
        byte[] bytes = new byte[]{(byte) ((int) (longValue >> 56)), (byte) ((int) (longValue << 8 >> 56)), (byte) ((int) (longValue << 16 >> 56)), (byte) ((int) (longValue << 24 >> 56)), (byte) ((int) (longValue << 32 >> 56)), (byte) ((int) (longValue << 40 >> 56)), (byte) ((int) (longValue << 48 >> 56)), (byte) ((int) (longValue << 56 >> 56))};
        return bytes;
    }

    public static long asLong(byte[] bytes) {
        if (bytes == null) {
            return 0L;
        } else if (bytes.length != 8) {
            throw new IllegalArgumentException("Expecting 8 byte values to construct a long");
        } else {
            long value = 0L;

            for (int i = 0; i < 8; ++i) {
                value = value << 8 | (long) (bytes[i] & 255);
            }

            return value;
        }
    }

    public static String toBinaryString(byte value) {
        String formatted = Integer.toBinaryString(value);
        if (formatted.length() > 8) {
            formatted = formatted.substring(formatted.length() - 8);
        }

        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

//    public static String toBinaryString(int value) {
//        String formatted = Long.toBinaryString((long) value);
//        StringBuffer buf = new StringBuffer(StringHelper.repeat('0', 32));
//        buf.replace(64 - formatted.length(), 64, formatted);
//        return buf.toString();
//    }
//
//    public static String toBinaryString(long value) {
//        String formatted = Long.toBinaryString(value);
//        StringBuffer buf = new StringBuffer(StringHelper.repeat('0', 64));
//        buf.replace(64 - formatted.length(), 64, formatted);
//        return buf.toString();
//    }
}
