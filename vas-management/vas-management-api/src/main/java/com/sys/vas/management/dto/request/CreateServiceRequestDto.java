package com.sys.vas.management.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateServiceRequestDto {
    private String name;
    private String description;
    private String disabledSms;
}
