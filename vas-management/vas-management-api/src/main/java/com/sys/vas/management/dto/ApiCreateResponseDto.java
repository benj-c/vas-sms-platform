package com.sys.vas.management.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class ApiCreateResponseDto {
    private long id;
    private String commitId;
}
