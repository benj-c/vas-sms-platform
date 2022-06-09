package com.sys.vas.management.controller;

import com.sys.vas.management.dto.ActionByServiceDto;
import com.sys.vas.management.dto.Response;
import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.UserRoles;
import com.sys.vas.management.dto.request.ActionUpdateRequestDto;
import com.sys.vas.management.dto.request.CreateActionRequestDto;
import com.sys.vas.management.service.ActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class ActionController {

    private ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    /**
     * @param requestDto
     * @return
     */
    @PutMapping(
            path = "/action",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> updateAction(
            @Valid @RequestBody ActionUpdateRequestDto requestDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|updateAction");
        log.info("ReqBody|{}", requestDto.toString());
        try {
            this.actionService.update(requestDto);
            Response response = Response.success("Action has successfully updated")
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|updateAction|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param sid
     * @return
     */
    @GetMapping(
            path = "/service/{sid}/action",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getActionsByServiceId(
            @PathVariable("sid") Long sid
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getActionsByServiceId");
        try {
            List<ActionByServiceDto> actionsByServiceId = this.actionService.getActionsByServiceId(sid);
            Response response = Response.success(actionsByServiceId)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getActionsByServiceId|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param requestDto
     * @return
     */
    @PostMapping(
            path = "/action",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> createAction(
            @Valid @RequestBody CreateActionRequestDto requestDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|createAction");
        log.info("ReqBody|{}", requestDto.toString());
        try {
            this.actionService.create(requestDto);
            Response response = Response.success("Action has successfully created")
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|createAction|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

}
