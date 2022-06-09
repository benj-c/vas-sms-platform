package com.sys.vas.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

@Data
@AllArgsConstructor
public class ExportXmlDto {
    private ByteArrayResource resource;
    private String fileName;
}
