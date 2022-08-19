package com.sys.vas.config;

import com.sys.vas.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Executor;

@Configuration
@Slf4j
public class AppConfig {

    @Value("${vas.srv-int.thread-pool.core-size}")
    private int corePoolSize;
    @Value("${vas.srv-int.thread-pool.max-size}")
    private int maxPoolSize;
    @Value("${vas.srv-int.thread-pool.queue-capacity}")
    private int queueCapacity;

    @Bean("serviceExecutorPool")
    public Executor getNotifyExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("sc-srv-exec-");
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

    @Bean
    public ScriptEngine getScriptEngine() {
        return new ScriptEngineManager().getEngineByName("javascript");
    }

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
        result.setMaxTotal(500);
        result.setDefaultMaxPerRoute(150);
        return result;
    }

    @Bean
    public RequestConfig requestConfig() {
        return  RequestConfig.custom()
                .setConnectionRequestTimeout(2000)
                .setConnectTimeout(3000)
                .setSocketTimeout(3000)
                .build();
        //SocketTimeout : Defines the socket timeout (SO_TIMEOUT) in milliseconds, which is the timeout for waiting for data or, put differently, a maximum period inactivity between two consecutive data packets).
        //ConnectionRequestTimeout : timeout in milliseconds used when requesting a connection from the connection manager.
        //ConnectTimeout : Determines the timeout in milliseconds until a connection is established
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        return HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Bean
    public RestTemplate restTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }

}
