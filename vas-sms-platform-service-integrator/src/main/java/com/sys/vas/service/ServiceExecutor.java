package com.sys.vas.service;

import com.sys.vas.datamodel.messages.VasTransactionMsg;
import com.sys.vas.datamodel.repository.SysConfigRepository;
import com.sys.vas.kernal.Kernel;
import com.sys.vas.stream.source.CxResponseSource;
import com.sys.vas.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableBinding(CxResponseSource.class)
public class ServiceExecutor {
    @Autowired
    Kernel kernel;

    @Autowired
    CxResponseSource cxrs;

    @Async("serviceExecutorPool")
    public void executeService(VasTransactionMsg srm) {
        log.info("Msg Received from the Queue : " + srm.toString());
        try {
            JSONObject req = new JSONObject();
            req.put("api", srm.getScApiName());
            req.put("msisdn", srm.getMsisdn());
            req.put("crId", srm.getCorrelationId());
            req.put("port", srm.getDestPort());
            req.put("tokens", srm.getTokens());
            JSONObject resp = kernel.executeAppLogic(req);

            log.debug("SC response:" + resp.toJSONString());
            srm.setResCode(Integer.parseInt((String) resp.get("resCode")));
            srm.setResDesc((String) resp.get("resDesc"));
            srm.setResponseParams(resp);
            cxrs.cxResponseChannel().send(MessageBuilder.withPayload(srm).build());
            req = null;
            resp = null;
        } catch (Exception e) {
            log.error(Constants.errorMsg, e);
        }
    }

}
