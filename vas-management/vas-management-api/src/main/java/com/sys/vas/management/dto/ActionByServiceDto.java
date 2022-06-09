package com.sys.vas.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ActionByServiceDto {
    private Long id;
    private String description;
    private long apiId;
}
