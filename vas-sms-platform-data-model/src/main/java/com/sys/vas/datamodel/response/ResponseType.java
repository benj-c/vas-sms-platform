package com.sys.vas.datamodel.response;

public interface ResponseType {
    int getCode();
    String getMessage();
    int getHttpStatus();
}
