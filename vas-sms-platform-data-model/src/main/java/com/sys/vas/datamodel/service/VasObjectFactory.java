package com.sys.vas.datamodel.service;

import com.sys.vas.datamodel.messages.VasTransactionMsg;
import com.sys.vas.datamodel.messages.ShortMessage;
import org.springframework.stereotype.Service;

@Service("scp-object-factory")
public class VasObjectFactory {

    public VasTransactionMsg ceateScpTrMsgFromShortMsg(ShortMessage sm) {
        if (sm == null) {
            return null;
        }
        VasTransactionMsg scpTransMsg = new VasTransactionMsg();
        scpTransMsg.setMsisdn(sm.getMsisdn());
        scpTransMsg.setCorrelationId(sm.getCorrelationId());
        scpTransMsg.setDestPort(sm.getDestPort());
        scpTransMsg.setOriginalSms(sm.getMessage());
        return scpTransMsg;
    }
}
