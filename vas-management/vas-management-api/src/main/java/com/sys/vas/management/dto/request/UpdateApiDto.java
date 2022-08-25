package com.sys.vas.management.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UpdateApiDto {
    private long id;
    private String name;
    private String description;
}
