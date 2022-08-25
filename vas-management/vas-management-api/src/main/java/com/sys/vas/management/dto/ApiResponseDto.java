package com.sys.vas.management.dto;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {
    private Long id;
    private String description;
    private String name;
    private String version;
}
