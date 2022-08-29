package com.sys.vas.controller;

import com.sys.vas.dto.MSResult;
import com.sys.vas.kernal.Kernel;
import com.sys.vas.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ScController {
    @Autowired
    Kernel msKernal;

    @Autowired
    Constants constant;

    /**
     *
     * @param path
     * @return
     */
    private HashMap<String, String> parsePath(String path) {
        HashMap<String, String> pathMap = new HashMap<String, String>();
        if (path != null) {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (!path.equals("")) {
                String[] pathParts = path.split("/");
                for (int i = 0; i < pathParts.length; i++) {
                    if (i > 2) {
                        String key = "pp" + (i - 2);
                        String value = pathParts[i];
                        pathMap.put(key, value);
                    }
                }
            }
        }
        return pathMap;
    }

    /**
     *
     * @param params
     * @return
     */
    private HashMap<String, String> parseQueryParam(String params) {
        HashMap<String, String> qMap = new HashMap<String, String>();
        String[] paramA = params.split(",");
        for (int k = 0; k < paramA.length; k++) {
            String[] kv = paramA[k].split(":");
            qMap.put(kv[0], kv[1]);
        }
        return qMap;
    }

    /**
     *
     * @param api
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(
            value = "/{api}/**",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> msGetJson(@PathVariable(value = "api") String api, HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        log.info("PATH:" + path);
        JSONObject req = new JSONObject();
        HashMap<String, String> pathMap = parsePath(path);
        req.putAll(pathMap);
        req.put("api", api);
        req.put("httpcall", "get");
        Map<String, String[]> queryParams = request.getParameterMap();
        queryParams.forEach((k, v) -> {
            if (k.equals("params")) {
                req.putAll(parseQueryParam(v[0]));
            } else {
                req.put(k, v[0]);
            }
        });
        Enumeration<String> hNames = request.getHeaderNames();
        JSONObject h = new JSONObject();
        while (hNames.hasMoreElements()) {
            String param = hNames.nextElement();
            h.put(param, request.getHeader(param));
        }
        String logmsg = "MSENV_STAT|" + api + "|" + req.get("httpcall") + "|";
        req.put("httpHeadders", h);
        ResponseEntity<?> rsp = msCallJson(req);
        logmsg = logmsg + rsp.getStatusCode();
        log.info(logmsg);
        return rsp;

    }

    /**
     *
     * @param api
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(
            value = "/{api}/**",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> msPostJson(
            @PathVariable(value = "api") String api,
            HttpServletRequest request
    ) {
        JSONObject req = null;
        JSONParser parser = new JSONParser();
        String body = "";
        try {
            body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (body != null) {
                req = (JSONObject) parser.parse(body);
                if (req.get("params") != null) req = (JSONObject) req.get("params");
            } else {
                req = new JSONObject();
            }
        } catch (Exception e) {
            log.error("Ops! Execption occurred :", e);
            req = new JSONObject();
        }
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        log.info("PATH:" + path);
        HashMap<String, String> pathMap = parsePath(path);
        req.putAll(pathMap);
        req.put("api", api);
        req.put("httpcall", "post");
        Enumeration<String> hNames = request.getHeaderNames();
        JSONObject h = new JSONObject();
        while (hNames.hasMoreElements()) {
            String param = hNames.nextElement();
            h.put(param, request.getHeader(param));
        }
        String logmsg = "MSENV_STAT|" + api + "|" + req.get("httpcall") + "|";
        req.put("httpHeadders", h);
        ResponseEntity<?> rsp = msCallJson(req);
        logmsg = logmsg + rsp.getStatusCode();
        log.info(logmsg);
        return rsp;

    }

    /**
     *
     * @param req
     * @return
     */
    private ResponseEntity<?> msCallJson(JSONObject req) {
        JSONObject resp = null;
        //Kernel msKernal = (Kernel)context.getBean("kernel");
        int status = HttpStatus.CONFLICT.value();
        MSResult result = new MSResult();
        try {
            resp = (JSONObject) msKernal.executeAppLogic(req);
            if (resp.get("resCode") != null) {
                if (HttpStatus.valueOf(Integer.parseInt((String) resp.get("resCode"))) != null) {
                    status = Integer.parseInt((String) resp.get("resCode"));
                } else {
                    status = HttpStatus.CONFLICT.value();
                }
            }

        } catch (Exception e) {
            log.error("Ops! Execption occurred :", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();

        } finally {
            if (resp != null) {
                if (resp.get("resCode") != null) {
                    result.setResCode((String) resp.get("resCode"));
                    if (status == 200 || status == 201 || status == 204) {
                        if (req.get("httpcall").toString().equalsIgnoreCase("post") || req.get("httpcall").toString().equalsIgnoreCase("put")) {
                            result.setResCode("201");
                            status = 201;
                        } else if (req.get("httpcall").toString().equalsIgnoreCase("get")) {
                            result.setResCode("200");
                            status = 200;
                        } else if (req.get("httpcall").toString().equalsIgnoreCase("delete")) {
                            result.setResCode("204");
                            status = 204;
                        }
                    }

                }
                if (resp.get("resDesc") != null) {
                    result.setResDesc((String) resp.get("resDesc"));
                } else {
                    log.debug("ResDesc is null...");
                    result.setResDesc("");
                }
                resp.remove("resCode");
                resp.remove("resDesc");
                result.setContent(resp);
            }

            //msKernal.end();
        }
        log.info("Status/Content of the response to rest client :" + status + "/" + result.getContent().toJSONString());

        return ResponseEntity.status(status).body(result);
    }

    /**
     *
     * @return
     */
    @RequestMapping(
            value = "/clearmem",
            method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> msClearMemory() {
        constant.clearApiInMemory();
        log.info("API chache cleared");
        return ResponseEntity.status(204).body("Success");
    }
}
