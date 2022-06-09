package com.sys.vas.service;

import com.sys.vas.stream.sink.SmsRouterSink;
import com.sys.vas.datamodel.messages.ShortMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(SmsRouterSink.class)
@Slf4j
public class SmsListner {

    private ServiceMapper srvMapper;

    public SmsListner(ServiceMapper srvMapper) {
        this.srvMapper = srvMapper;
    }

    @StreamListener(target = SmsRouterSink.SMSRIN)
    public void processSms(ShortMessage sms) {
        try {
            srvMapper.findMappingService(sms);
            Thread.sleep(15);
        } catch (InterruptedException e) {
            log.error("Ops!, Exception occurred:", e);
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }
    }
}
