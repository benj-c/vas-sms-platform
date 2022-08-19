package com.sys.vas.management.controller;

import com.sys.vas.management.dto.Response;
import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.AuthDataDto;
import com.sys.vas.management.dto.UserRoles;
import com.sys.vas.management.dto.entity.UserEntity;
import com.sys.vas.management.dto.request.UserCredentialsRequestDto;
import com.sys.vas.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.lang.JoseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * authenticate user credentials and create jwt access token
     * @param userCredentialsRequestDto
     * @return
     * @throws JoseException
     */
    @PostMapping(
            path = "/authenticate",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response> doAuthenticate(
            @Valid @RequestBody UserCredentialsRequestDto userCredentialsRequestDto
    ) throws JoseException {
        System.out.println(BCrypt.hashpw(userCredentialsRequestDto.getPassword(), BCrypt.gensalt()));
        long startTime = System.currentTimeMillis();
        log.info("Initiating|doAuthenticate");
        log.info("ReqBody|{}", userCredentialsRequestDto.toString());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userCredentialsRequestDto.getUsername(),
                            userCredentialsRequestDto.getPassword()
                    )
            );

            AuthDataDto data = userService.createAccessToken(userCredentialsRequestDto.getUsername());
            Response response = Response.success(data).build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|doAuthenticate|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * activation of deactivation of user
     * @return
     */
    @PatchMapping(
            path = "/user/act-deact/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed({UserRoles.ADMIN})
    public ResponseEntity<Response> actDeactUser(
            @PathVariable int userId
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|actDeactUser");
        log.info("PathVars|userId={}", userId);
        try {
            boolean b = userService.actDeatUser(userId);
            Response response = Response.success("User has successfully " + (b ? "activated" : "de-activated"))
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response);
            return ResponseEntity.ok().body(response);
        } finally {
            log.info("Completed|actDeactUser|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * create new user
     * @param userCredentialsRequestDto
     * @return
     */
    @PostMapping(
            path = "/user/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
//    @RolesAllowed({UserRoles.ADMIN})
    public ResponseEntity<Response> createUser(
            @Valid @RequestBody UserCredentialsRequestDto userCredentialsRequestDto
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|createUser");
        log.info("ReqBody|{}", userCredentialsRequestDto.toString());
        try {
            userService.createUser(userCredentialsRequestDto);
            Response response = Response.success("User has successfully created")
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|createUser|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     *
     * @return
     */
    @GetMapping(
            path = "/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RolesAllowed(UserRoles.USER)
    public ResponseEntity<Response> getUsers() {
        long startTime = System.currentTimeMillis();
        log.info("Initiating|getUsers");
        try {
            List<UserEntity> allUsers = this.userService.getAllUsers();
            Response response = Response.success(allUsers)
                    .build(ResponseCodes.OPERATION_SUCCESS);
            log.info("Res|{}", response.toString());
            return ResponseEntity.ok(response);
        } finally {
            log.info("Completed|getUsers|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
        }
    }

}
