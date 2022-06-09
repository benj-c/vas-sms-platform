package com.sys.vas.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@ToString
@Builder
public class AuthDataDto {
    private int id;
    private String username;
    private String email;
    private String name;
    private LocalDate createdDate;
    private boolean isAvtive;
    private String scope;
    private String token;
}
