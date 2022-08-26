package com.sys.vas.management.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sys.vas.management.dto.HealthStatResponseDto;
import com.sys.vas.management.dto.entity.SysConfigEntity;
import com.sys.vas.management.repository.SysConfigRepository;
import com.sys.vas.management.util.WcResponse;
import com.sys.vas.management.util.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ConfigService {

    @Value("${app.service.endpoints}")
    private String endpoints;

    private SysConfigRepository sysConfigRepository;

    /**
     *
     * @param sysConfigRepository
     */
    public ConfigService(
            SysConfigRepository sysConfigRepository
    ) {
        this.sysConfigRepository = sysConfigRepository;
    }

    /**
     *
     * @return
     * @throws JsonProcessingException
     */
    public List<HealthStatResponseDto> getServiceHealthStats() throws JsonProcessingException {
        ArrayNode jsonNodes = new ObjectMapper().readValue(endpoints, ArrayNode.class);
        List<HealthStatResponseDto> data = new ArrayList<>();
        data.add(HealthStatResponseDto.builder()
                .name("REST API")
                .status(true)
                .build()
        );
        jsonNodes.forEach(ep -> {
            String name = ep.get("name").asText();
            String endpoint = ep.get("endpoint").asText();
            try {
                WcResponse res = WebClient.get(endpoint).create().exchange();
                log.info("SvrHealthCheck|{}", res.toString());
                data.add(HealthStatResponseDto.builder()
                        .name(name)
                        .status(res.getResCode() == 200)
                        .build()
                );
            } catch (Exception e) {
                log.error("SvrHealthCheck|Error|{}", e.getMessage());
                data.add(HealthStatResponseDto.builder()
                        .name(name)
                        .status(false)
                        .build()
                );
            }
        });
        return data;
    }

    /**
     *
     * @return
     */
    public SysConfigEntity getSmsStatsData() {
        return sysConfigRepository.findAll().get(0);
    }
}
