package com.sys.vas.management.controller;

import com.sys.vas.management.dto.KeywordDto;
import com.sys.vas.management.dto.Response;
import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.UserRoles;
import com.sys.vas.management.dto.request.ActionUpdateRequestDto;
import com.sys.vas.management.dto.request.CreateKeywordRequestDto;
import com.sys.vas.management.service.KeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class KeywordController {

    private KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    /**
     * @param aid
     * @return
     */
    @GetMapping(
            path = "/action/{aid}/keyword",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getKeywordByActionsId(
            @PathVariable("aid") Long aid
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getKeywordByActionsId");
        try {
            List<KeywordDto> keywords = this.keywordService.getKeywordsByActionId(aid);
            Response response = Response.success(keywords)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getKeywordByActionsId|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param requestDto
     * @return
     */
    @PutMapping(
            path = "/keyword",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> updateKeyword(
            @Valid @RequestBody KeywordDto requestDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|updateKeyword");
        log.info("ReqBody|{}", requestDto.toString());
        try {
            keywordService.update(requestDto);
            Response response = Response.success("Keyword has successfully updated")
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|updateKeyword|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param requestDto
     * @return
     */
    @PostMapping(
            path = "/keyword",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> createKeyword(
            @Valid @RequestBody CreateKeywordRequestDto requestDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|createKeyword");
        log.info("ReqBody|{}", requestDto.toString());
        try {
            keywordService.create(requestDto);
            Response response = Response.success("Keyword has successfully created")
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|createKeyword|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

}
