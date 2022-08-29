package com.sys.vas.management.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiXmlResponseDto {
    private Long id;
    private String name;
    private String description;
    private String version;
    private String commitId;
    private String commitMessage;
    private Boolean isActive;
    private String xml;
    private Long totalRequestsCount;
    private Long avgResTime;
    private Long errorCount;
}
