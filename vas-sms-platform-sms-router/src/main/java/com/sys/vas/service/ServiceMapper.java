package com.sys.vas.service;

import com.sys.vas.datamodel.messages.VasTransactionMsg;
import com.sys.vas.datamodel.repository.SysConfigRepository;
import com.sys.vas.stream.source.CxResponseSource;
import com.sys.vas.stream.source.ServiceCreatorSource;
import com.sys.vas.datamodel.messages.ShortMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@EnableBinding(value = {ServiceCreatorSource.class, CxResponseSource.class})
@Slf4j
public class ServiceMapper {

    private KeyMatcher keyMatcher;
    private ServiceCreatorSource serviceCreatorSource;
    private CxResponseSource cxResponseSource;
    private SysConfigRepository sysConfigRepository;

    public ServiceMapper(
            KeyMatcher kMatcher,
            ServiceCreatorSource sce,
            CxResponseSource cxrsps,
            SysConfigRepository sysConfigRepository
    ) {
        this.keyMatcher = kMatcher;
        this.serviceCreatorSource = sce;
        this.cxResponseSource = cxrsps;
        this.sysConfigRepository = sysConfigRepository;
    }

    @Async("serviceMapperPool")
    @Transactional
    public void findMappingService(ShortMessage sm) {
        log.info("SMS Received: " + sm.toString());
        VasTransactionMsg srMsg = null;
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            sysConfigRepository.incrementInSmsCount();
            srMsg = keyMatcher.getMatchingAction(sm);
            srMsg.setSmsReceivedTime(date);
            if (srMsg.getActionId() == -1) {
                log.info("Incorrect Message");
                srMsg.setResCode(404);
                cxResponseSource.cxResponseChannel().send(MessageBuilder.withPayload(srMsg).build());
            } else if (srMsg.getActionId() == -2) {
                log.info("Service is disabled");
                srMsg.setResCode(403);
                cxResponseSource.cxResponseChannel().send(MessageBuilder.withPayload(srMsg).build());
            } else {
                serviceCreatorSource.srvCreatorChannel().send(MessageBuilder.withPayload(srMsg).build());
                log.info("SMS published to SC queue: " + srMsg.toString());
            }
        } catch (Exception e) {
            if (srMsg != null) {
                srMsg.setResCode(500);
                srMsg.setActionId(-1);
                cxResponseSource.cxResponseChannel().send(MessageBuilder.withPayload(srMsg).build());
            }
            log.error("Exception", e);
        }
    }

}
