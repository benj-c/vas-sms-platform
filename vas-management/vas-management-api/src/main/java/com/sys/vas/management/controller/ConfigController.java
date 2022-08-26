package com.sys.vas.management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sys.vas.management.dto.*;
import com.sys.vas.management.service.ConfigService;
import com.sys.vas.management.service.SysActionLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@Slf4j
public class ConfigController {

    private ConfigService configService;
    private SysActionLogService sysActionLogService;

    public ConfigController(ConfigService configService, SysActionLogService sysActionLogService) {
        this.configService = configService;
        this.sysActionLogService = sysActionLogService;
    }

    /**
     *
     * @return
     */
    @GetMapping(
            path = "/config/health",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getSvrHealth() throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getSvrHealth");
        try {
            List<HealthStatResponseDto> stat = configService.getServiceHealthStats();
            Response response = Response.success(stat)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getSvrHealth|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     *
     * @return
     */
    @GetMapping(
            path = "/config/actions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getUserActions() throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getUserActions");
        try {
            List<UserAction> actions = sysActionLogService.getUserActions();
            Response response = Response.success(actions)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getUserActions|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

}
