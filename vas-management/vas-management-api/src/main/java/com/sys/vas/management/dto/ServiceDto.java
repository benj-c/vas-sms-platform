package com.sys.vas.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {
    private long id;
    private String name;
    private String description;
    private boolean active;
    private String disabledSms;
    private LocalDate createdDate;
}
