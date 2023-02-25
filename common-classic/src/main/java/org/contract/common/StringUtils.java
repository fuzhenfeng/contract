package org.contract.common;

public class StringUtils {

    public static final String SPACE = " ";

    public static final String EMPTY = "";

    public static final String SEPARATOR = "\\.";

    public static final String REALM_SEPARATOR = ".";

    public static final String LINUX_SEPARATOR = "/";

    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    public static String defaultIfBlank2(final Object str, final String defaultStr) {
        return defaultIfBlank(String.valueOf(str), defaultStr);
    }

    public static String captureName(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
