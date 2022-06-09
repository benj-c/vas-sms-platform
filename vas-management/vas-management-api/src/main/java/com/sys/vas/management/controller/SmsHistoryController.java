package com.sys.vas.management.controller;

import com.sys.vas.management.dto.Response;
import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.UserRoles;
import com.sys.vas.management.dto.entity.SmsHistoryEntity;
import com.sys.vas.management.service.SmsHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@Slf4j
public class SmsHistoryController {

    private SmsHistoryService smsHistoryService;

    SmsHistoryController(SmsHistoryService smsHistoryService) {
        this.smsHistoryService = smsHistoryService;
    }

    /**
     * @return
     */
    @GetMapping(
            path = "/history",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getHistoryByMsisdn(
            @RequestParam("msisdn") String msisdn
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getHistoryByMsisdn");
        try {
            List<SmsHistoryEntity> history = this.smsHistoryService.getHistoryByMsisdn(msisdn);
            Response response = Response.success(history)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getHistoryByMsisdn|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }
}
