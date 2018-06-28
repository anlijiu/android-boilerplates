package com.anlijiu.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringsUtils {

    public static final String DOT = ".";
    public static final String COLON = ":";

    public enum CHAR_TYPE {
        NUMBER,
        ENGLISH,
        CHINESE,
        UNKNOWN
    }

    private StringsUtils() {
        // No instances.
    }

    public static boolean isBlank(CharSequence string) {
        return (string == null || string.toString().trim().length() == 0);
    }

    public static String valueOrDefault(String string, String defaultString) {
        return isBlank(string) ? defaultString : string;
    }

    public static String truncateAt(String string, int length) {
        return string.length() > length ? string.substring(0, length) : string;
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static CHAR_TYPE charType(String txt) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(txt);
        if(m.matches() ){
            return CHAR_TYPE.NUMBER;
        }
        p=Pattern.compile("[a-zA-Z]");
        m=p.matcher(txt);
        if(m.matches()){
            return CHAR_TYPE.ENGLISH;
        }
        p=Pattern.compile("[\u4e00-\u9fa5]");
        m=p.matcher(txt);
        if(m.matches()){
            return CHAR_TYPE.CHINESE;
        }
        return CHAR_TYPE.UNKNOWN;
    }

    public static int realLength(String value) { //中文算2个， 英文算一个
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    public static String normalizeUrl(String url) {
        return url.replaceAll("(?<!(http:|https:))[//]+", "/");
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
