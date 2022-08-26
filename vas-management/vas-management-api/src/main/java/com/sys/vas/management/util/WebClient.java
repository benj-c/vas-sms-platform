package com.sys.vas.management.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WebClient {

    private static final String USER_AGENT = "web-client-1.0/websheet";
	private static ApplicationContext applicationContext;

	private RestTemplate restTemplate;

	/**
	 *
	 * @param restTemplate
	 */
	public WebClient(RestTemplate restTemplate, ApplicationContext ctx) {
		this.restTemplate = restTemplate;
		applicationContext = ctx;
	}

	/**
	 *
	 * @return
	 */
	public static RestTemplate getRestTemplate() {
		return (RestTemplate) applicationContext.getBean("RestTemplate");
	}

	/**
     *
     * @param api - url of request
     * @return new ApiCreator of post type
     */
    public static ApiCreator post(String api) {
	return new ApiCreator(api, HttpMethod.POST);
    }

    /**
     *
     * @param api - url of request
     * @return new ApiCreator of get type
     */
    public static ApiCreator get(String api) {
	return new ApiCreator(api, HttpMethod.GET);
    }

    /**
     *
     * @param api - url of request
     * @return new ApiCreator of put type
     */
    public static ApiCreator put(String api) {
	return new ApiCreator(api, HttpMethod.PUT);
    }

    /**
     *
     * @param api - url of request
     * @return new ApiCreator of patch type
     */
    public static ApiCreator patch(String api) {
	return new ApiCreator(api, HttpMethod.PATCH);
    }

    /**
     *
     * @param api - url of request
     * @return new ApiCreator of delete type
     */
    public static ApiCreator delete(String api) {
	return new ApiCreator(api, HttpMethod.DELETE);
    }

    /**
     *
     */
    public static class ApiCreator {

	private String url;
	private HttpMethod method;
	private Object body = null;
	private final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
	private final Map<String, Object> uriVariables = new HashMap<>();

	/**
	 * 
	 * @param url
	 */
	public ApiCreator(String url, HttpMethod method) {
	    this.method = method;
	    this.url = url;
	}

	/**
	 * This appends URI query parameter and its value to request URL.
	 * 
	 * @param name  http request query parameter name
	 * @param value value of this parameter
	 * @return ApiCreator
	 */
	public ApiCreator queryParam(String name, Object value) {
	    this.queryParams.add(name, String.valueOf(value));
	    return this;
	}

	/**
	 * This appends URI path variable and its value to request URL.
	 * 
	 * @param name  http request URI variable name
	 * @param value value of this URI variable
	 * @return ApiCreator
	 */
	public ApiCreator uriVariable(String name, Object value) {
	    this.uriVariables.put(name, value);
	    return this;
	}

	/**
	 * This adds request body to this request.
	 * 
	 * @param body request body object
	 * @return ApiCreator
	 */
	public ApiCreator body(Object body) {
	    this.body = body;
	    return this;
	}

	/**
	 * Create new HTTP URI object using given URI data
	 * 
	 * @return Builder with created URI object
	 */
	public Builder create() {
	    URI uri = UriComponentsBuilder.fromHttpUrl(this.url)
		    .uriVariables(this.uriVariables)
		    .queryParams(this.queryParams)
		    .build()
		    .toUri();
	    return new Builder(uri, this.method, this.body);
	}
    }

	/**
	 *
	 */
    public static class Builder {

		private URI uri;
		private Object body;
		private HttpMethod method;

		/**
		 * @param uri
		 * @param method
		 * @param body
		 */
		public Builder(URI uri, HttpMethod method, Object body) {
			this.uri = uri;
			this.body = body;
			this.method = method;
		}

		/**
		 * Sends http request and returns response details as Response.
		 *
		 * @return Response
		 * @throws IOException on any error occurred during request sending. (Ex:timeouts)
		 */
		public WcResponse exchange() throws IOException {
			final StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			ResponseEntity<String> response = getRestTemplate().exchange(getRequestEntity(), String.class);
			stopWatch.stop();

			return WcResponse.builder()
					.requestBody(this.body != null ? new ObjectMapper().writeValueAsString(this.body) : null)
					.content(response.getBody())
					.method(this.method)
					.uri(this.uri)
					.code(response.getStatusCodeValue())
					.processTime(stopWatch.getTotalTimeMillis())
					.build();
		}

		/**
		 * @return
		 */
		private RequestEntity<?> getRequestEntity() {
			return RequestEntity.method(this.method, this.uri)
					.header(HttpHeaders.USER_AGENT, USER_AGENT)
					.accept(MediaType.APPLICATION_JSON)
					.body(this.body);
		}
	}

}
