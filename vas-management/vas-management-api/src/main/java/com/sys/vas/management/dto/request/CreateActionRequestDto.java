package com.sys.vas.management.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateActionRequestDto {
    private long serviceId;
    private long apiId;
    private String description;
}
