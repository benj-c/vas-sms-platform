package com.sys.vas.management.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
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
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        return HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * Creates ResponseErrorHandler to be used in RestTemplate.
     *
     * @return ResponseErrorHandler
     */
    public ResponseErrorHandler getResponseErrorHandler() {
        return new DefaultResponseErrorHandler() {
            // filter out only 5xx errors
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
                    throw new HttpServerErrorException(response.getStatusCode(), "SERVER_ERROR");
                }
            }
        };
    }

    @Bean("RestTemplate")
    public RestTemplate restTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(getResponseErrorHandler());
        return restTemplate;
    }
}
