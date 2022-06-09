package com.sys.vas.management.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateKeywordRequestDto {
    private String firstKey;
    private String regex;
    private long actionId;
}
