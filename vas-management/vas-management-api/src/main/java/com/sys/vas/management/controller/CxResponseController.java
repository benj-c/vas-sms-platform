package com.sys.vas.management.controller;

import com.sys.vas.management.dto.*;
import com.sys.vas.management.dto.request.CreateCxResponseRequestDto;
import com.sys.vas.management.dto.request.CreateKeywordRequestDto;
import com.sys.vas.management.dto.request.UpdateCxResponseRequestDto;
import com.sys.vas.management.service.CxResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class CxResponseController {

    private CxResponseService cxResponseService;

    CxResponseController(CxResponseService cxResponseService) {
        this.cxResponseService = cxResponseService;
    }

    /**
     * @param api_id
     * @return
     */
    @GetMapping(
            path = "/api/{api_id}/cx-responses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getCxResponsesByApiId(
            @PathVariable("api_id") Long api_id
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getCxResponsesByApiId");
        try {
            List<CxResponseDto> list = this.cxResponseService.getResponsesByApiId(api_id);
            Response response = Response.success(list)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getCxResponsesByApiId|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param requestDto
     * @return
     */
    @PutMapping(
            path = "/cx-response",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> updateCxResponse(
            @Valid @RequestBody UpdateCxResponseRequestDto requestDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|updateCxResponse");
        log.info("ReqBody|{}", requestDto.toString());
        try {
            cxResponseService.update(requestDto);
            Response response = Response.success("Response SMS has successfully updated")
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|updateCxResponse|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param requestDto
     * @return
     */
    @PostMapping(
            path = "/cx-response",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> createCxResponse(
            @Valid @RequestBody CreateCxResponseRequestDto requestDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|createCxResponse");
        log.info("ReqBody|{}", requestDto.toString());
        try {
            cxResponseService.create(requestDto);
            Response response = Response.success("Response SMS has successfully created")
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|createCxResponse|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

}
