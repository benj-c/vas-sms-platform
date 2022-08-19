package com.sys.vas.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class AppUtil {

    /**
     *
     * @param base
     * @return
     */
    public static String generateTransactionId(String base) {
        if (base != null) {
            SecureRandom random = new SecureRandom();
            return "" + new Date().getTime() + formattedMsisdn(base) + new BigInteger(16, random).toString(256);
        } else {
            return UUID.randomUUID().toString();
        }
    }

    /**
     *
     * @param rowMsisdn
     * @return
     */
    public static String formattedMsisdn(String rowMsisdn) {
        return rowMsisdn.replaceFirst("\\+94", "");
    }

    /**
     *
     * @param msg
     * @return
     */
    public static HashMap<String, String> msgTockenizer(String msg) {
        String[] tokenArray = msg.split("\\s+");
        HashMap<String, String> tokenMap = new HashMap<>();
        for (int i = 0; i < tokenArray.length; i++) {
            if (i < 5) {
                tokenMap.put("token" + (i + 1), tokenArray[i]);
            } else {
                break;
            }
        }
        return tokenMap;
    }

}
