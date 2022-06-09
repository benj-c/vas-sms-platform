package com.sys.vas.management.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class JwtDataDto {
    private Long userId;
    private String role;
    private String userName;
}
