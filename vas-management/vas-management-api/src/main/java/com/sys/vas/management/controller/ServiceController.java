package com.sys.vas.management.controller;

import com.sys.vas.management.dto.*;
import com.sys.vas.management.dto.request.CreateServiceRequestDto;
import com.sys.vas.management.dto.request.ServiceUpdateRequestDto;
import com.sys.vas.management.service.VasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class ServiceController {

    private VasService vasService;

    public ServiceController(VasService vasService) {
        this.vasService = vasService;
    }

    /**
     * @return
     */
    @GetMapping(
            path = "/service",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getServices() {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getServices");
        try {
            List<ServiceDto> allServices = this.vasService.getAllServices();
            Response response = Response.success(allServices)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getServices|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param sid
     * @return
     */
    @GetMapping(
            path = "/service/{sid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getServiceDetailById(
            @PathVariable("sid") Long sid
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getServiceDetailById");
        try {
            ServiceDto serviceDto = this.vasService.getServiceDetailById(sid);
            Response response = Response.success(serviceDto)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getServiceDetailById|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param requestDto
     * @return
     */
    @PutMapping(
            path = "/service",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> updateService(
            @Valid @RequestBody ServiceUpdateRequestDto requestDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|updateService");
        log.info("ReqBody|{}", requestDto.toString());
        try {
            this.vasService.update(requestDto);
            Response response = Response.success("Service has successfully updated")
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|updateService|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param requestDto
     * @return
     */
    @PostMapping(
            path = "/service",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> createService(
            @Valid @RequestBody CreateServiceRequestDto requestDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|createService");
        log.info("ReqBody|{}", requestDto.toString());
        try {
            long id = this.vasService.create(requestDto);
            Response response = Response.success(id)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|createService|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }


    /**
     * @param sId
     * @return
     */
    @GetMapping(
            path = "/service/{sId}/download",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @RolesAllowed({UserRoles.USER})
    public ResponseEntity<Resource> generateAndDownloadServiceXml(
            @PathVariable long sId
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|generateAndDownloadServiceXml");
        log.info("PathVars|{}", sId);
        try {
            ExportXmlDto data = this.vasService.createXml(sId);
            log.info("Res|file");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + data.getFileName() + "\"")
                    .body(data.getResource());
        } finally {
            log.info("Completed|generateAndDownloadServiceXml|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }
}
