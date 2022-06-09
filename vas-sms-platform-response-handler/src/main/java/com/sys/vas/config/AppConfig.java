package com.sys.vas.config;

import com.sys.vas.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Executor;

@Configuration
@Slf4j
public class AppConfig {

    @Value("${vas.resp-exec.thread-pool.core-size}")
    private int corePoolSize;

    @Value("${vas.resp-exec.thread-pool.max-size}")
    private int maxPoolSize;

    @Value("${vas.resp-exec.thread-pool.queue-capacity}")
    private int queueCapacity;

    @Bean("responseExecutorPool")
    public Executor getNotifyExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("resp-srv-mapper-");
        executor.initialize();
        return executor;
    }

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
                        crid = AppUtil.generateTransactionId(request.getParameter("msisdn"));
                    }
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
