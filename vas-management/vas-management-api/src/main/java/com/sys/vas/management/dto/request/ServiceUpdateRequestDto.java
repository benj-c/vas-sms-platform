package com.sys.vas.management.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceUpdateRequestDto {
    private Long id;
    private String description;
    private String name;
    private Boolean active;
    private String disableSms;
}
