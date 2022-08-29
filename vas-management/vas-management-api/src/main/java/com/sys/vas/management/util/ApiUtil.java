package com.sys.vas.management.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

public class ApiUtil<T> {

    /**
     *
     * @return
     */
    public static String getAuthUserName() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return obj instanceof UserDetails ? ((UserDetails) obj).getUsername() :obj.toString();
    }

    /**
     *
     * @return
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     *
     * @param text
     * @return
     */
    public static String getMd5Hash(String text) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytesOfMessage = text.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] theMD5digest = md.digest(bytesOfMessage);
        return new String(theMD5digest);
    }
}
