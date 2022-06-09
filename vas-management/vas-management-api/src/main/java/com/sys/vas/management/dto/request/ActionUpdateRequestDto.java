package com.sys.vas.management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ActionUpdateRequestDto {
    private Long id;
    private String description;
}
