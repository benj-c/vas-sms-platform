package com.sys.vas.service;

import com.sys.vas.datamodel.entity.KeywordEntity;
import com.sys.vas.datamodel.messages.ShortMessage;
import com.sys.vas.datamodel.messages.VasTransactionMsg;
import com.sys.vas.datamodel.repository.KeywordRepository;
import com.sys.vas.datamodel.service.VasObjectFactory;
import com.sys.vas.util.AppUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class KeyMatcher {

    private KeywordRepository keywordRepository;
    private VasObjectFactory vasObjectFactory;

    public KeyMatcher(KeywordRepository keywordRepository, VasObjectFactory vasObjectFactory) {
        this.keywordRepository = keywordRepository;
        this.vasObjectFactory = vasObjectFactory;
    }

    public VasTransactionMsg getMatchingAction(ShortMessage shortMessage) {
        HashMap<String, String> tokenMap = AppUtil.msgTockenizer(shortMessage.getMessage());
        Set<KeywordEntity> kwords = keywordRepository.findByFirstKey(tokenMap.get("token1").toUpperCase());
        KeywordEntity matchedKW = null;
        VasTransactionMsg transMsg = vasObjectFactory.ceateScpTrMsgFromShortMsg(shortMessage);
        for (KeywordEntity keywordEntity : kwords) {
            if (keywordEntity.getAction().getService().getDisable()) {
                transMsg.setActionId(-2);
                transMsg.setDissableResponseSms(keywordEntity.getAction().getService().getDisableSms());
            } else {
                if (shortMessage.getMessage().matches(keywordEntity.getRegEx())) {
                    matchedKW = keywordEntity;
                    break;
                }
            }
        }
        if (matchedKW != null) {
            if (matchedKW.getAction().getService().getDisable()) {
                transMsg.setActionId(-2);
                transMsg.setDissableResponseSms(matchedKW.getAction().getService().getDisableSms());
            } else {
                transMsg.setActionId(matchedKW.getAction().getId());
                transMsg.setScApiName(matchedKW.getAction().getApi().getName());
                transMsg.setTokens(tokenMap);
            }
        } else if (transMsg.getActionId() == 0) {
            transMsg.setActionId(-1);
        }
        return transMsg;
    }
}
