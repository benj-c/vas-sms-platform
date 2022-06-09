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

}
