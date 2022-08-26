package com.sys.vas.management.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAction {
    private String type;
    private String target;
    private String comment;
    private String user;
    private String timestamp;
}
