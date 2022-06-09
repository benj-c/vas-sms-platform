package com.sys.vas;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@Slf4j
@EnableAsync
public class SmsRouterApplication {

    static {
        try {
            System.setProperty("hostAddress", InetAddress.getLocalHost().getHostAddress().replaceAll("\\.", "_"));
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SmsRouterApplication.class, args);
    }
}
