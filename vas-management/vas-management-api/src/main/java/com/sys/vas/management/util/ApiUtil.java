package com.sys.vas.management.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

public class ApiUtil<T> {

    public static String getAuthUserName() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return obj instanceof UserDetails ? ((UserDetails) obj).getUsername() :obj.toString();
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
