package com.sys.vas.management.controller;

import com.sys.vas.management.dto.Response;
import com.sys.vas.management.dto.ResponseType;
import com.sys.vas.management.dto.UserRoles;
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
public class SearchController {


//    @GetMapping(
//            path = "/search",
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @RolesAllowed({UserRoles.USER})
//    public ResponseEntity<Response> search(
//            @RequestParam("q") String query
//    ) {
//        long startTime = System.currentTimeMillis();
//        log.info("Initiating|search");
//        log.info("ReqParam|q:{}", query);
//        try {
//            List<Object> results = this.searchService.search(query);
//            Response response = Response.success(results)
//                    .build(ResponseType.OPERATION_SUCCESS);
//            log.info("Res|{}", response.toString());
//            return ResponseEntity.ok(response);
//        } finally {
//            log.info("Completed|search|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
//        }
//    }
}
