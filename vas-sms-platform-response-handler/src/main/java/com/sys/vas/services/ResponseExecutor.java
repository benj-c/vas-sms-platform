package com.sys.vas.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.sys.vas.datamodel.entity.SmsHistoryEntity;
import com.sys.vas.datamodel.repository.SmsHistoryRepository;
import com.sys.vas.datamodel.repository.SysConfigRepository;
import com.sys.vas.stream.source.FinalOutSource;
import com.sys.vas.datamodel.entity.ApiEntity;
import com.sys.vas.datamodel.entity.CxResponseEntity;
import com.sys.vas.datamodel.messages.VasTransactionMsg;
import com.sys.vas.datamodel.repository.ApiRepository;
import com.sys.vas.datamodel.repository.CxResponseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@EnableBinding(FinalOutSource.class)
public class ResponseExecutor {

    private CxResponseRepository cxResponseRepository;
    private ApiRepository apiRepository;
    private SmsHistoryRepository smsHistoryRepository;
    private SysConfigRepository sysConfigRepository;

    public ResponseExecutor(
            CxResponseRepository cxResponseRepository,
            ApiRepository apiRepository,
            SmsHistoryRepository smsHistoryRepository,
            SysConfigRepository sysConfigRepository
    ) {
        this.cxResponseRepository = cxResponseRepository;
        this.apiRepository = apiRepository;
        this.smsHistoryRepository = smsHistoryRepository;
        this.sysConfigRepository = sysConfigRepository;
    }

    @Async("responseExecutorPool")
    @Transactional
    public void executeResponse(VasTransactionMsg srm) {
        log.info("Msg Received from the Queue : " + srm.toString());
        try {
            prepareSms(srm.getResCode(), srm.getActionId(), srm.getScApiName(), srm);
            if (srm.getActionId() > 0) // for default message which should be
                // sent for always
                prepareSms(-1, srm.getActionId(), srm.getScApiName(), srm);
        } catch (Exception e) {
            log.error("Ops! Exception:", e);
        }
    }

    private void prepareSms(int resCode, long actionId, String apiName, VasTransactionMsg srm) {
        if (actionId == -1) {
            String plainSms = "Incorrect message";
            log.info(srm.getCorrelationId() + "|Plain Text SMS :" + plainSms);
            sendSms(srm.getMsisdn(), plainSms, srm.getDestPort(), srm.getCorrelationId(), srm);
        } else if (actionId == -2) {// Disable SMS from the transaction message
            String plainSms = srm.getDissableResponseSms();
            sendSms(srm.getMsisdn(), plainSms, srm.getDestPort(), srm.getCorrelationId(), srm);
        } else {
            ApiEntity apiEntity = apiRepository.findByName(apiName).get();
            List<CxResponseEntity> ress = cxResponseRepository.findByResCodeAndApi(resCode, apiEntity);
            if (ress.isEmpty()) {
                log.info(srm.getCorrelationId() + "|No message configured for rescode:" + resCode);
                return;
            }
            ress.forEach(e -> {
                sendSms(srm.getMsisdn(), e.getSms(), srm.getDestPort(), srm.getCorrelationId(), srm);
            });
        }
    }

    @Transactional
    public void sendSms(String destinationNo, String msg, String sourcePort, String correlationId, VasTransactionMsg srm) {
        log.info("{}|CustomerSMS|sms:{}, port:{}, destination:{}", correlationId, msg, sourcePort, destinationNo);
        //TODO: send sms
        save(srm, msg);
        sysConfigRepository.incrementOutSmsCount();
    }

    public void save(VasTransactionMsg srm, String sms) {
        SmsHistoryEntity smsHistoryEntity = new SmsHistoryEntity();
        smsHistoryEntity.setIncomingSms(srm.getOriginalSms());
        smsHistoryEntity.setResponseSms(sms);
        smsHistoryEntity.setMsisdn(srm.getMsisdn());
        smsHistoryEntity.setReceivedTime(LocalDateTime.parse(srm.getSmsReceivedTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        smsHistoryEntity.setSentTime(LocalDateTime.now());

        smsHistoryRepository.save(smsHistoryEntity);
    }
}
