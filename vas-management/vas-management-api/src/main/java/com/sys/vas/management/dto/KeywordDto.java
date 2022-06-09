package com.sys.vas.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class KeywordDto {
    private long id;
    private String firstKey;
    private String regex;
}
