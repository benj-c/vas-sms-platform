package com.sys.vas.management.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiXmlResponseDto {
    private long id;
    private String name;
    private String description;
    private String version;
    private String commitId;
    private String commitMessage;
    private Boolean isActive;
    private String xml;
}
