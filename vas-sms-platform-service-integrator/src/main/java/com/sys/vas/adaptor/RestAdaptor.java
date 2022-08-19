package com.sys.vas.adaptor;

import java.io.StringWriter;

import com.sys.vas.util.Constants;
import com.sys.vas.util.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("rest")
public class RestAdaptor implements SCAdaptor {

    private final String RESP_CODE = "resCode";
    private final String RESP_DESC = "resDesc";

    @Autowired
    RestClient rest;

    @SuppressWarnings("unchecked")
    private JSONObject commoncall(JSONObject params, String methode) {
        JSONObject op = new JSONObject();
        try {

            String url = (String) params.get("url");
            String jsonip = (String) params.get("json");
            if (url == null) {
                op.put(RESP_CODE, String.valueOf(org.springframework.http.HttpStatus.BAD_REQUEST));
                op.put(RESP_DESC, "input parameter 'url' not found");
                return op;
            }
            log.debug("Http url [" + methode + "]:" + url);
            String jsonTextParams = "";
            if (jsonip != null) {
                jsonTextParams = jsonip;
            } else {
                StringWriter out = new StringWriter();
                //JSONObject tmp = new JSONObject(params);
                params.remove("url");
                params.writeJSONString(out);
                jsonTextParams = out.toString();
            }
            log.debug("Request body:" + jsonTextParams);
            JSONObject headders = null;
            try {
                if (params.get("httpHeadders") != null)
                    headders = (JSONObject) (new JSONParser().parse((String) params.get("httpHeadders")));

            } catch (Exception e) {
                log.error(Constants.errorMsg, e);
            }
            JSONObject response = null;

            if (methode.equalsIgnoreCase("post")) {
                response = rest.post(url, jsonTextParams, headders);
            } else if (methode.equalsIgnoreCase("put")) {
                response = rest.put(url, jsonTextParams, headders);
            } else if (methode.equalsIgnoreCase("get")) {
                response = rest.get(url, headders);
            } else if (methode.equalsIgnoreCase("delete")) {
                response = rest.delete(url, headders);
            }
            if (response != null) {
                String jsonStr = (String) response.get("entity");
                log.debug("API response:" + jsonStr);
                if (jsonStr.indexOf("ams:fault") >= 0 || jsonStr.indexOf("DOCTYPE HTML") >= 0) {
                    op.put(RESP_CODE, (String) response.get("http_status"));
                    op.put(RESP_DESC, jsonStr);
                } else {
                    try {
                        op = (JSONObject) new JSONParser().parse(jsonStr);
                    } catch (Exception e) {
                        /*e.printStackTrace();*/
                        op.put(RESP_DESC, jsonStr);
                        op.put("apiResponse", jsonStr);
                    }

                    op.put(RESP_CODE, String.valueOf(response.get("http_status")));
                }
            } else {
                op.put(RESP_CODE, String.valueOf(org.springframework.http.HttpStatus.BAD_REQUEST));
            }

        } catch (Exception e) {
            op.put(RESP_CODE, String.valueOf(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR));
            op.put(RESP_DESC, e.getMessage());
            log.error("Exception : " + e.getMessage());
            log.error(Constants.errorMsg, e);
        }
        return op;
    }

    /*Business Functions Begins */

    /* (non-Javadoc)
     * @see org.adl.rts.scp.sc.adaptors.SCAdaptor#invokeFunction(java.lang.String, org.json.simple.JSONObject)
     */
    @Override
    public JSONObject invokeFunction(String functionName, JSONObject params) {
        // TODO Auto-generated method stub
        if (functionName.equals("get")) return commoncall(params, "get");
        else if (functionName.equals("post")) return commoncall(params, "post");
        else if (functionName.equals("put")) return commoncall(params, "put");
        else if (functionName.equals("delete")) return commoncall(params, "delete");
        return null;
    }

    /* End of Business Functions*/
}
