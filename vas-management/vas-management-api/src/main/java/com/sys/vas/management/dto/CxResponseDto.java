package com.sys.vas.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class CxResponseDto {
    private Long id;
    private Integer resCode;
    private String resDesc;
    private Integer sendToOriginatedNo;
    private String sms;
    private long apiId;
}
