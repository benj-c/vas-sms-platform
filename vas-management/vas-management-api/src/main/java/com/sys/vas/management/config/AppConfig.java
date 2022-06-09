package com.sys.vas.management.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> servletFilter() {
        final FilterRegistrationBean<OncePerRequestFilter> filters = new FilterRegistrationBean<>();
        filters.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                log.info("IncomingReq|Origin:{}|[{}]{}", request.getRemoteUser(), request.getMethod(), request.getServletPath());
                try {
                    String crid = request.getParameter("crid");
                    if (!StringUtils.hasText(crid)) {
                        crid = UUID.randomUUID().toString().replace("-", "");
                    }
                    log.info("RequestID:{}", crid);
                    MDC.put("RequestId", crid);
                    filterChain.doFilter(request, response);
                } finally {
                    MDC.clear();
                }
            }
        });
        filters.setOrder(1);
        return filters;
    }

}
