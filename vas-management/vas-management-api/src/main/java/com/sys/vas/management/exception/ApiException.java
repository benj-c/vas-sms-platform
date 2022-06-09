package com.sys.vas.management.exception;

import com.sys.vas.management.dto.ResponseCodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiException extends RuntimeException {
    private ResponseCodes responseType;
    private String msg;
}
