package com.sys.vas.management.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AddApiCommitRequest {
    private long apiId;
    private String commitMessage;
    private String version;
    private String xml;
}
