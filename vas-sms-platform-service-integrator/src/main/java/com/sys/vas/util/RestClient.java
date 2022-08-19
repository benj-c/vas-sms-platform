package com.sys.vas.util;

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component("restClient")
public class RestClient {
    private final String HTTP_STATUS = "http_status";
    @Autowired
    RestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    private JSONObject getJSONResp(String txt, int status) {
        JSONObject resp = new JSONObject();
        resp.put("entity", txt);
        resp.put(HTTP_STATUS, status);
        return resp;
    }

    @SuppressWarnings({"unchecked"})
    public JSONObject post(String url, String content, JSONObject headersip) {
        JSONObject resp = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (headersip != null) {
                Iterator<String> it = headersip.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String val = "";
                    Object obj = headersip.get(key);
                    if (obj instanceof String) val = (String) obj;
                    else val = (String) ((JSONArray) obj).get(0);
                    headers.set(key, val);
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
            HttpEntity<?> entity = new HttpEntity<>(content, headers);

            // RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            // String hed = (String) response.getBody().get("ResultHeader");
            if (response.getBody() != null) {
                resp = getJSONResp(StringUtils.chomp(response.getBody().toString()), response.getStatusCode().value());
            } else {
                resp = getJSONResp("{}", response.getStatusCode().value());
            }

        } catch (HttpClientErrorException cex) // HTTP response code 4xx
        {
            resp = getJSONResp(cex.getResponseBodyAsString(), cex.getStatusCode().value());
            log.debug("Inside HttpClientErrorException:" + cex.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(Constants.errorMsg, e);
            resp = new JSONObject();
            resp.put(HTTP_STATUS, 500);
        }
        return resp;
    }

    @SuppressWarnings("unchecked")
    public JSONObject put(String url, String content, JSONObject headersip) {
        JSONObject resp = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (headersip != null) {
                Iterator<String> it = headersip.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String val = "";
                    Object obj = headersip.get(key);
                    if (obj instanceof String) val = (String) obj;
                    else val = (String) ((JSONArray) obj).get(0);
                    headers.set(key, val);
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
            HttpEntity<?> entity = new HttpEntity<>(content, headers);

            // RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            // String hed = (String) response.getBody().get("ResultHeader");
            if (response.getBody() != null) {
                resp = getJSONResp(StringUtils.chomp(response.getBody().toString()), response.getStatusCode().value());
            } else {
                resp = getJSONResp("{}", response.getStatusCode().value());
            }

        } catch (HttpClientErrorException cex)// HTTP status 4xx
        {
            resp = getJSONResp(cex.getResponseBodyAsString(), cex.getStatusCode().value());
            log.debug("Inside HttpClientErrorException:" + cex.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(Constants.errorMsg, e);
            resp = new JSONObject();
            resp.put(HTTP_STATUS, 500);
        }
        return resp;
    }

    @SuppressWarnings({"unchecked"})
    public JSONObject get(String url, JSONObject headersip) {
        JSONObject resp = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (headersip != null) {
                Iterator<String> it = headersip.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String val = "";
                    Object obj = headersip.get(key);
                    if (obj instanceof String) val = (String) obj;
                    else val = (String) ((JSONArray) obj).get(0);
                    headers.set(key, val);
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
            HttpEntity<?> entity = new HttpEntity<>("", headers);

            // RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            // String hed = (String) response.getBody().get("ResultHeader");
            if (response.getBody() != null) {
                resp = getJSONResp(StringUtils.chomp(response.getBody().toString()), response.getStatusCode().value());
            } else {
                resp = getJSONResp("{}", response.getStatusCode().value());
            }

        } catch (HttpClientErrorException cex)// for HTTP status 4xx
        {
            resp = getJSONResp(cex.getResponseBodyAsString(), cex.getStatusCode().value());
            log.debug("Inside HttpClientErrorException:" + cex.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(Constants.errorMsg, e);
            resp = new JSONObject();
            resp.put(HTTP_STATUS, 501);
        }
        return resp;
    }

    @SuppressWarnings({"unchecked"})
    public JSONObject delete(String url, JSONObject headersip) {
        JSONObject resp = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (headersip != null) {
                Iterator<String> it = headersip.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String val = "";
                    Object obj = headersip.get(key);
                    if (obj instanceof String) val = (String) obj;
                    else val = (String) ((JSONArray) obj).get(0);
                    headers.set(key, val);
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
            HttpEntity<?> entity = new HttpEntity<>("", headers);

            // RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
            // String hed = (String) response.getBody().get("ResultHeader");
            if (response.getBody() != null) {
                resp = getJSONResp(response.getBody().toString(), response.getStatusCode().value());
                log.debug("No exp:" + response.getBody().toString());
            } else {
                resp = getJSONResp("{}", response.getStatusCode().value());
            }

        } catch (HttpClientErrorException cex) {
            log.debug("Inside HttpClientErrorException:" + cex.getResponseBodyAsString());
            resp = getJSONResp(cex.getResponseBodyAsString(), cex.getStatusCode().value());

        } catch (Exception e) {
            log.error(Constants.errorMsg, e);
            resp = new JSONObject();
            resp.put(HTTP_STATUS, 501);
        }
        log.debug("Delete resp:" + resp.toJSONString());
        return resp;
    }

}
