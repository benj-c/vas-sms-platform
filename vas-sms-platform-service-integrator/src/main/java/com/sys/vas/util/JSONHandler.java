package com.sys.vas.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

@Service
public class JSONHandler {
    public static final Logger logger = LogManager.getLogger(JSONHandler.class);

    public String readParamValue(JSONObject jobj, String p) {
        String val = null;
        try {
            String[] l0 = p.split("\\.");

            for (int l = 0; l < l0.length; l++) {

                if (l < l0.length - 1) {
                    if (l0[l].indexOf("[") >= 0) {
                        jobj = (JSONObject) JSONValue.parse(getValueFromArray(jobj, l0[l]));
                        if (jobj == null)
                            break;
                    } else {
                        if (jobj.get(l0[l]) != null)
                            jobj = (JSONObject) jobj.get(l0[l]);
                        else
                            break;
                    }

                } else {
                    if (l0[l].indexOf("[") >= 0) {
                        val = getValueFromArray(jobj, l0[l]);
                    } else {
                        if (jobj.get(l0[l]) != null)
                            val = jobj.get(l0[l]).toString();
                    }

                }
            }
        } catch (Exception e) {
            logger.error("Ops!:", e);
        }
        return val;

    }

    public String getValueFromArray(JSONObject jobj, String ip) {
        String res = null;
        JSONArray jarr = null;
        int index1 = ip.indexOf("[");
        int index2 = ip.indexOf("]");
        String idx1 = "";
        String idx2 = "";
        while (index1 >= 0) {
            idx1 = idx1 + "," + index1;
            index1 = ip.indexOf("[", index1 + 1);
            idx2 = idx2 + "," + index2;
            index2 = ip.indexOf("]", index2 + 1);
        }
        String[] idxa1 = idx1.substring(1).split(",");
        String[] idxa2 = idx2.substring(1).split(",");
        int k = 0;
        String joName = ip.substring(k, Integer.parseInt(idxa1[0]));
        jarr = (JSONArray) jobj.get(joName);
        for (int i = 0; i < idxa1.length; i++) {
            String jaidx = ip.substring(Integer.parseInt(idxa1[i]) + 1, Integer.parseInt(idxa2[i]));
            if (i < idxa1.length - 1) {
                jarr = (JSONArray) jarr.get(Integer.parseInt(jaidx));
            } else {

                res = jarr.get(Integer.parseInt(jaidx)).toString();
            }
            k = Integer.parseInt(idxa2[i]) + 1;
        }
        return res;
    }

    public String getVariableValue(String ip, JSONObject object) {
        String out = null;
        if (ip.indexOf("%") == 0 && ip.indexOf(" ") == -1) {
            out = readParamValue(object, ip.substring(1, ip.length() - 1));
        } else if (ip.indexOf("%") >= 0) {
            out = "";
            char[] ipc = ip.toCharArray();
            int index = ip.indexOf("%");
            String idx = "";
            while (index >= 0) {
                if (index == 0)
                    idx = idx + "," + index;
                else {
                    if (ipc[index - 1] != '\\')
                        idx = idx + "," + index;
                }
                index = ip.indexOf("%", index + 1);
            }
            String[] idxx = null;
            int preIdx = 0;
            if (idx.contains(",")) {
                idxx = idx.substring(1).split(",");
                for (int i = 0; i < idxx.length; i++) {
                    if (i % 2 == 0) {
                        /*
                         * logger.debug("Fix text part|"+ip.substring(preIdx,Integer.parseInt(idxx[i])));
                         */
                        out = out + ip.substring(preIdx, Integer.parseInt(idxx[i]));
                        preIdx = Integer.parseInt(idxx[i]) + 1;
                    } else {
                        /*
                         *  logger.debug("Varibale Name |"+ip.substring(preIdx,Integer.parseInt(idxx[i])));
                         */
                        out = out
                                + getVariableValue("%" + ip.substring(preIdx, Integer.parseInt(idxx[i])) + "%", object);
                        preIdx = Integer.parseInt(idxx[i]) + 1;
                    }
                }
            }
            out = out + ip.substring(preIdx);
            /*
             * out = out.replaceAll("\\\\", "");
             * logger.debug("Last part |"+ip.substring(preIdx));
             * logger.debug("IN text |"+ip);
             * logger.debug("OUT Text |"+out);
             */

        } else
            out = ip;
        /*
         *  if(out==null){throw new MissingParamException("missing parameter["+paramName+"]");}
         */
        return out;
    }
}
