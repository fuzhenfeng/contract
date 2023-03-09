package org.contract.common;

import java.io.UnsupportedEncodingException;

public class BytesUtils {

    public static byte[] get(String data){
        try {
            return data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // ignore
            return null;
        }
    }
}
