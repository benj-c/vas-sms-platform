package com.sys.vas.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiHistoryVersionDto {
    private Long id;
    private Long apiId;
    private String commitId;
    private String commitMessage;
    private String version;
    private Boolean isActive;
}
