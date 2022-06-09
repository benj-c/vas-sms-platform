package com.sys.vas.management.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateApiRequestDto {
    private String name;
    private String description;
    private String version;
}
