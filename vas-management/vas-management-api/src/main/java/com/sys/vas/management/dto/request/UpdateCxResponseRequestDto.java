package com.sys.vas.management.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UpdateCxResponseRequestDto {
    private long id;
    private int resCode;
    private String resDesc;
    private String sms;
}
