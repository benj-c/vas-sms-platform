package com.sys.vas.management.util;

import com.sys.vas.management.dto.JwtDataDto;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@Slf4j
public class JwtUtil {

    private static final String JWT_SUBJECT = "";
    private static final String JWT_ISSUER = "http://localhost:8400/api";
    private static final String JWT_AUDIENCE = "http://localhost:8401/svr";

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    private Key getKey() {
        return new HmacKey(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(int userId, String userName, String role) throws JoseException {
        log.info("creating token with|userId:{}, username:{}, role:{}", userId, userName, role);
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setExpirationTimeMinutesInTheFuture(12 * 60);
        jwtClaims.setIssuer(JWT_ISSUER);
        jwtClaims.setSubject(JWT_SUBJECT);
        jwtClaims.setAudience(JWT_AUDIENCE);

        jwtClaims.setClaim("userId", userId);
        jwtClaims.setClaim("scope", role);
        jwtClaims.setClaim("userName", userName);

        JsonWebSignature webSignature = new JsonWebSignature();
        webSignature.setPayload(jwtClaims.toJson());
        webSignature.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA512);
        webSignature.setKey(getKey());

        return webSignature.getCompactSerialization();
    }

    public JwtDataDto validate(String jwt) throws InvalidJwtException, MalformedClaimException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setExpectedSubject(JWT_SUBJECT)
                .setExpectedIssuer(JWT_ISSUER)
                .setExpectedAudience(JWT_AUDIENCE)
                .setVerificationKey(getKey())
                .setRelaxVerificationKeyValidation()
                .build();
        JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
        return JwtDataDto.builder()
                .role(jwtClaims.getClaimValue("scope", String.class))
                .userId(jwtClaims.getClaimValue("userId", Long.class))
                .userName(jwtClaims.getClaimValue("userName", String.class))
                .build();
    }
}
