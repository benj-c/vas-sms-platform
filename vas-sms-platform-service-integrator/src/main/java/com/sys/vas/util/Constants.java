package com.sys.vas.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

@Component("constant")
public class Constants {

    public static final Logger logger = LogManager.getLogger(Constants.class);
    public static final AtomicInteger kernalTreads = new AtomicInteger(0);
    private static final Map<String, String> sysClients = new HashMap<String, String>();
    private static final Map<String, Document> apis = new HashMap<String, Document>();
    public static final String errorMsg = "Ops! Execption occured:";
    @Value("${msce.adaptors.adaptorclass}")
    private String adaptors;

    public int incrementAndGetThreadId() {
        return kernalTreads.incrementAndGet();
    }

    public void decrementThreadid() {
        kernalTreads.decrementAndGet();
    }

    public void addToSysClients(String cli, String cls) {
        sysClients.put(cli, cls);
    }

    public String getSysClientsByName(String cli) {
        return sysClients.get(cli);
    }

    @PostConstruct
    public void readConfig() {
        JSONParser parser = new JSONParser();
        try {
            sysClients.clear();
            JSONArray mifeApps = (JSONArray) parser.parse(adaptors);
            for (int i = 0; i < mifeApps.size(); i++) {
                JSONObject app = (JSONObject) mifeApps.get(i);
                String adaptorClass = (String) app.get("class");
                String adaptorName = (String) app.get("name");
                sysClients.put(adaptorName, adaptorClass);
            }
        } catch (Exception e) {
            logger.error(errorMsg, e);
        }
    }

    public Document getApis(String apiName) {
        return apis.get(apiName);
    }

    public void setApis(String apiName, Document doc) {
        apis.put(apiName, doc);
    }

    public void clearApiInMemory() {
        apis.clear();
    }
}
