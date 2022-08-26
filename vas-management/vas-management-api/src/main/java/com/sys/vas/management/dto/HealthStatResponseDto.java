package com.sys.vas.management.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class HealthStatResponseDto {
    private String name;
    private boolean status;
}
