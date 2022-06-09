package com.sys.vas.services;

import com.sys.vas.stream.sink.ServiceResponseSink;
import com.sys.vas.datamodel.messages.VasTransactionMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service("service-resp-listner")
@EnableBinding(ServiceResponseSink.class)
public class ServiceResponseMsgListner {

    @Autowired
    ResponseExecutor srvExecutor;

    @StreamListener(target = ServiceResponseSink.RSHIN)
    public void processRequest(VasTransactionMsg srm) {

        try {
            srvExecutor.executeResponse(srm);
            Thread.sleep(15);
        } catch (Exception e) {
            log.error("Ops! Exception:", e);
            Thread.currentThread().interrupt();
        }
    }
}
