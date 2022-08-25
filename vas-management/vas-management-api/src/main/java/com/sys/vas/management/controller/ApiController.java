package com.sys.vas.management.controller;

import com.sys.vas.management.dto.*;
import com.sys.vas.management.dto.entity.ApiEntity;
import com.sys.vas.management.dto.entity.ApiHistoryEntity;
import com.sys.vas.management.dto.request.AddApiCommitRequest;
import com.sys.vas.management.dto.request.CreateApiRequestDto;
import com.sys.vas.management.dto.request.UpdateApiDto;
import com.sys.vas.management.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class ApiController {

    private ApiService apiService;

    ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * @return
     */
    @GetMapping(
            path = "/api",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getApis(
//            @PathVariable("aid") Long aid
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getApis");
        try {
            List<ApiResponseDto> allApis = this.apiService.getAllApis();
            Response response = Response.success(allApis)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getApis|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param requestDto
     * @return
     */
    @PostMapping(
            path = "/api",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> createApi(
            @Valid @RequestBody CreateApiRequestDto requestDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|createApi");
        log.info("ReqBody|{}", requestDto.toString());
        try {
            long id = this.apiService.create(requestDto);
            Response response = Response.success(id)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|createApi|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @return
     */
    @GetMapping(
            path = "/api/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getApiById(
            @PathVariable("id") Long id,
            @RequestParam("commit") String commit
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getApiById");
        try {
            ApiXmlResponseDto api = this.apiService.getApiByIdAndCommit(id, commit);
            Response response = Response.success(api)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getApiById|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param updateApiDto
     * @return
     */
    @PutMapping(
            path = "/api",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> updateApi(
            @Valid @RequestBody UpdateApiDto updateApiDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|updateApi");
        log.info("ReqBody|{}", updateApiDto.toString());
        try {
            long id = this.apiService.update(updateApiDto);
            Response response = Response.success(id)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|updateApi|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     *
     * @param addApiCommitRequest
     * @return
     */
    @PostMapping(
            path = "/api/commit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> commitApi(
            @Valid @RequestBody AddApiCommitRequest addApiCommitRequest
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|commitApi");
        log.info("ReqBody|{}", addApiCommitRequest.toString());
        try {
            String commitId = this.apiService.commit(addApiCommitRequest);
            Response response = Response.success(commitId)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|commitApi|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(
            path = "/api/{id}/versions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getApiVersions(
            @PathVariable("id") Long id
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getApiVersions");
        try {
            List<ApiHistoryVersionDto> apis = this.apiService.getApiVersionsById(id);
            Response response = Response.success(apis)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getApiVersions|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @PostMapping(
            path = "/api/{id}/deploy/{commitId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> deployApi(
            @PathVariable("id") Long id,
            @PathVariable("commitId") String commitId
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|deployApi");
        log.info("PathVar|id:{}, commitId:{}", id, commitId);
        try {
            this.apiService.deploy(id, commitId);
            Response response = Response.success("success")
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|deployApi|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }
}
