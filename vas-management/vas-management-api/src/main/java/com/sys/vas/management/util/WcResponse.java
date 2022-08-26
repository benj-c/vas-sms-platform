package com.sys.vas.management.util;

import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.Objects;

public class WcResponse {

    private HttpMethod httpMethod;
    private URI requestUri;
    private long processTime;
    private int resCode;
    private String requestBody;
    private String content;

    @Override
    public String toString() {
	return String.format(
		"WebClientDetails[httpMethod:%s][requestUri:%s][requestBody:%s][resCode:%d][processTime:%d] response:%s",
		httpMethod.toString(), requestUri.toString(), requestBody, resCode, processTime, content);
    }

    @Override
    public int hashCode() {
	return Objects.hashCode(this);
    }

    /**
     * @param httpMethod
     * @param requestUri
     * @param processTime
     * @param resCode
     * @param content
     */
    public WcResponse(
	HttpMethod httpMethod, URI requestUri, String requestBody, long processTime, int resCode, String content
    ) {
	super();
	this.httpMethod = httpMethod;
	this.requestBody = requestBody;
	this.requestUri = requestUri;
	this.processTime = processTime;
	this.resCode = resCode;
	this.content = content;
    }

    /**
     * 
     * @return
     */
    public static Builder builder() {
	return new Builder();
    }

	/**
	 *
	 */
    public static class Builder {

	private HttpMethod httpMethod;
	private URI requestUri;
	private long processTime;
	private int resCode;
	private String requestBody;
	private String content;

	public Builder method(HttpMethod httpMethod) {
	    this.httpMethod = httpMethod;
	    return this;
	}

	public Builder uri(URI requestUri) {
	    this.requestUri = requestUri;
	    return this;
	}

	public Builder processTime(long processTime) {
	    this.processTime = processTime;
	    return this;
	}

	public Builder requestBody(String reqBody) {
	    this.requestBody = reqBody;
	    return this;
	}

	public Builder content(String content) {
	    this.content = content;
	    return this;
	}

	public Builder code(int resCode) {
	    this.resCode = resCode;
	    return this;
	}

	public WcResponse build() {
	    return new WcResponse(this.httpMethod, this.requestUri, this.requestBody, this.processTime, this.resCode,
		    this.content);
	}
    }

    public HttpMethod getHttpMethod() {
	return httpMethod;
    }

    public URI getRequestUri() {
	return requestUri;
    }

    public long getProcessTime() {
	return processTime;
    }

    public int getResCode() {
	return resCode;
    }

    public String getContent() {
	return content;
    }

    public String getRequestBody() {
	return requestBody;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
	this.httpMethod = httpMethod;
    }

    public void setRequestUri(URI requestUri) {
	this.requestUri = requestUri;
    }

    public void setProcessTime(long processTime) {
	this.processTime = processTime;
    }

    public void setResCode(int resCode) {
	this.resCode = resCode;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public void setRequestBody(String requestBody) {
	this.requestBody = requestBody;
    }

}
