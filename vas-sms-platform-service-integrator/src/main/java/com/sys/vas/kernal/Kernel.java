package com.sys.vas.kernal;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.script.ScriptEngine;
import javax.script.SimpleBindings;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.sys.vas.adaptor.SCAdaptor;
import com.sys.vas.datamodel.entity.ApiEntity;
import com.sys.vas.datamodel.entity.ApiHistoryEntity;
import com.sys.vas.datamodel.repository.ApiHistoryRepository;
import com.sys.vas.datamodel.repository.ApiRepository;
import com.sys.vas.datamodel.repository.SysConfigRepository;
import com.sys.vas.exception.ApiNotFoundException;
import com.sys.vas.exception.InParamNotFoundException;
import com.sys.vas.exception.MissingParamException;
import com.sys.vas.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Slf4j
@Component("Kernel")
public class Kernel {
    private final String RESP_CODE = "resCode";
    private final String RESP_DESC = "resDesc";
    private int thrdId = 0;

    @Autowired
    ScriptEngine scriptEngine;

    @Autowired
    private Constants constant;

    @Autowired
    private Map<String, SCAdaptor> scAdaptors;

    @Autowired
    private ApiHistoryRepository acApi;

    // @Value("${msce.home.dir}")
    // private String homeDirectoty;
    /*
     * public SCKernelNew() { scriptEngine = new
     * ScriptEngineManager().getEngineByName("javascript"); }
     */
    @PostConstruct
    public void start() {
        thrdId = constant.incrementAndGetThreadId();
        log.info("Kernal thread Initiated. [" + thrdId + "]");
    }

    @PreDestroy
    public void end() {
        constant.decrementThreadid();
        log.info("Kernal thread terminated. [" + thrdId + "]");
    }

    /**
     * @param currentBlock
     * @param bTag
     * @param globalBaseTag
     * @param sessionVariables
     * @param apiRequest
     * @param lastResponse
     * @return
     */
    @SuppressWarnings("unchecked")
    private JSONObject executeFunctionBlock(
            Element currentBlock,
            String bTag,
            String globalBaseTag,
            JSONObject sessionVariables,
            JSONObject apiRequest,
            JSONObject lastResponse
    ) {
        JSONObject funcResponse = new JSONObject();
        JSONParser parser = new JSONParser();
        String className = currentBlock.getElementsByTagName("class").item(0).getTextContent();
        String functionName = currentBlock.getElementsByTagName("method").item(0).getTextContent();
        JSONObject ipParams = new JSONObject();
        NodeList nListparam = currentBlock.getElementsByTagName("param");
        String prastr = "";
        try {
            for (int j = 0; j < nListparam.getLength(); j++) {
                Node pNode = nListparam.item(j);
                if (pNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element ele = (Element) pNode;
                    String var = getVariableValue(ele.getTextContent(), globalBaseTag, sessionVariables, apiRequest,
                            lastResponse);
                    if (var != null)
                        var = var.replaceAll("\\\\", "");

                    log.debug(bTag + "parameterName:variable:value |" + ele.getAttribute("name") + ":"
                            + ele.getTextContent() + ":[" + var + "]");
                    if (ele.getAttribute("name").equalsIgnoreCase("jsono")) {
                        ipParams = (JSONObject) parser.parse(var);
                        prastr = var + ",";
                        break;
                    } else {
                        ipParams.put(ele.getAttribute("name"), var);
                        prastr = prastr + ele.getAttribute("name") + ":" + var + ",";
                    }
                }
            }
            log.debug(bTag + "System Cient Name :" + className);
            /**
             * String classinfo = constant.getSysClientsByName(className);
             */
            SCAdaptor obj = scAdaptors.get(className);

            if (obj == null) {
                funcResponse.put(RESP_CODE, HttpStatus.INTERNAL_SERVER_ERROR + "");
                funcResponse.put(RESP_DESC, "System Client Not found");
                log.error("System Client Not found");
                return funcResponse;
            }
            funcResponse = obj.invokeFunction(functionName, ipParams);
            if (prastr.equalsIgnoreCase(""))
                log.info(bTag + "Call " + className + "->" + functionName + "() | response :"
                        + funcResponse.toJSONString());
            else
                log.info(bTag + "Call " + className + "->" + functionName + "("
                        + prastr.substring(0, prastr.length() - 1) + ") | response :" + funcResponse.toJSONString());

        } catch (Exception ee) {
            log.error(bTag + "|" + Constants.errorMsg, ee);
            funcResponse.put(RESP_CODE, HttpStatus.INTERNAL_SERVER_ERROR + "");
            funcResponse.put(RESP_DESC, ee.getMessage());
        } finally {
            ipParams = null;
            parser = null;
        }
        return funcResponse;
    }

    /**
     * @param currentBlock
     * @param bTag
     * @param globalBaseTag
     * @param sessionVariables
     * @param apiRequest
     * @param lastResponse
     * @throws Exception
     */
    private void executeAssignBlock(
            Element currentBlock,
            String bTag,
            String globalBaseTag,
            JSONObject sessionVariables,
            JSONObject apiRequest,
            JSONObject lastResponse
    ) throws Exception {
        NodeList nListvar = currentBlock.getElementsByTagName("variable");
        setPropertyValues(nListvar, bTag, globalBaseTag, sessionVariables, apiRequest, lastResponse);
    }

    /**
     * @param nListvar
     * @param bTag
     * @param globalBaseTag
     * @param sessionVariables
     * @param apiRequest
     * @param lastResponse
     * @throws Exception
     */
    private void setPropertyValues(
            NodeList nListvar,
            String bTag,
            String globalBaseTag,
            JSONObject sessionVariables,
            JSONObject apiRequest,
            JSONObject lastResponse
    ) throws Exception {
        if (nListvar != null) {
            SimpleBindings sb = new SimpleBindings();
            String tempLogInfo = "ListOf[property:value]|";
            for (int k = 0; k < nListvar.getLength(); k++) {
                Node pNodeVar = nListvar.item(k);
                if (pNodeVar.getNodeType() == Node.ELEMENT_NODE) {
                    Element elevar = (Element) pNodeVar;
                    String vName = elevar.getAttribute("name");
                    vName = getVariableValue(vName, globalBaseTag, sessionVariables, apiRequest, lastResponse);
                    String vVal = getVariableValue(elevar.getTextContent(), globalBaseTag, sessionVariables, apiRequest,
                            lastResponse);
                    if (vVal != null)
                        vVal = (scriptEngine.eval(vVal, sb) + "").replaceAll("\\\\", "");
                    setVariable(sessionVariables, vName, vVal);
                    tempLogInfo = tempLogInfo + vName + ":" + vVal + ",";
                    log.debug(bTag + "Property [name]:[variable]:[value]|[" + vName + "]:[" + elevar.getTextContent()
                            + "]:[" + vVal + "]");
                }
            }
            log.info(bTag + tempLogInfo.substring(0, tempLogInfo.length() - 1));
        }
    }

    /**
     * @param currentBlock
     * @param bTag
     * @param globalBaseTag
     * @param sessionVariables
     * @param apiRequest
     * @param lastResponse
     * @return
     * @throws Exception
     */
    private String executeBranchBlock(
            Element currentBlock,
            String bTag,
            String globalBaseTag,
            JSONObject sessionVariables,
            JSONObject apiRequest,
            JSONObject lastResponse
    ) throws Exception {
        String nextBlock = null;
        NodeList nListparam = currentBlock.getElementsByTagName("case");
        boolean matchCase = false;

        for (int j = 0; j < nListparam.getLength(); j++) {
            Node pNode = nListparam.item(j);
            if (pNode.getNodeType() == Node.ELEMENT_NODE) {
                Element ele = (Element) pNode;
                log.debug(bTag + "Case Id:|" + ele.getAttribute("id"));
                nextBlock = ele.getElementsByTagName("next-node").item(0).getTextContent();
                String expression = getVariableValue(ele.getElementsByTagName("expression").item(0).getTextContent(),
                        globalBaseTag, sessionVariables, apiRequest, lastResponse);
                expression = expression.replaceAll("\\\\", "");
                matchCase = (Boolean) scriptEngine.eval(expression, new SimpleBindings());
                log.debug(bTag + "(Expression):result| (" + expression + ") : " + matchCase);
                if (matchCase) {
                    log.info(bTag + "(Expression):result| (" + expression + ") : " + matchCase);
                    NodeList nListvar = ele.getElementsByTagName("variable");
                    setPropertyValues(nListvar, bTag, globalBaseTag, sessionVariables, apiRequest, lastResponse);
                    break;
                }
            }
        }
        if (!matchCase) {
            log.info(bTag + "No Match found go to default the node...");
            Element ele = (Element) currentBlock.getElementsByTagName("default").item(0);
            NodeList nListvar = ele.getElementsByTagName("variable");
            setPropertyValues(nListvar, bTag, globalBaseTag, sessionVariables, apiRequest, lastResponse);
            nextBlock = ele.getElementsByTagName("next-node").item(0).getTextContent();
        }
        return nextBlock;
    }

    /**
     * @param ele1
     * @param bTag
     * @param globalBaseTag
     * @param sessionVariables
     * @param apiRequest
     * @param lastResponse
     * @param apiResponse
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void setApiResponse(
            Element ele1,
            String bTag,
            String globalBaseTag,
            JSONObject sessionVariables,
            JSONObject apiRequest,
            JSONObject lastResponse,
            JSONObject apiResponse
    ) throws Exception {
        NodeList nListparam = ele1.getElementsByTagName("outputparams");
        for (int j = 0; j < nListparam.getLength(); j++) {
            Node pNode = nListparam.item(j);
            if (pNode.getNodeType() == Node.ELEMENT_NODE) {
                Element ele = (Element) pNode;
                log.info(bTag + "Return-attribute [name]:[variable]:[value] | [" + ele.getAttribute("name") + "]:["
                        + ele.getTextContent() + "]:[" + getReturnValues(ele.getTextContent(), sessionVariables,
                        lastResponse, apiRequest, globalBaseTag)
                        + "]");
                String vName = ele.getAttribute("name");
                String vType = ele.getAttribute("type");
                String vVal = getReturnValues(ele.getTextContent(), sessionVariables, lastResponse, apiRequest,
                        globalBaseTag);
                boolean isJObj = false;
                boolean isJArray = false;
                JSONObject jobj = new JSONObject();
                JSONArray jarr = new JSONArray();
                if (vType != null && vVal != null) {
                    if (vType.equalsIgnoreCase("jsono")) {
                        Object obj = JSONValue.parse(vVal);
                        jobj = (JSONObject) obj;
                        isJObj = true;
                    } else if (vType.equalsIgnoreCase("jsona")) {
                        Object obj = JSONValue.parse(vVal);
                        jarr = (JSONArray) obj;
                        isJArray = true;
                    }
                }
                if (vName != null) {

                    if (vName.equals("last")) {
                        apiResponse.putAll(lastResponse);
                        log.debug(bTag + "Put all LAST values.........");
                        // break;
                    } else if (vName.equals("ses")) {
                        apiResponse.putAll(sessionVariables);
                        log.debug(bTag + "Put all SES values.........");
                        // break;
                    } else {
                        if (isJObj)
                            apiResponse.put(vName, jobj);
                        else if (isJArray)
                            apiResponse.put(vName, jarr);
                        else {
                            // this.apiResponse.put(vName,vVal);
                            setVariable(apiResponse, vName, vVal);
                        }
                        log.debug(bTag + "Set return attribute name => value|" + vName + " => " + vVal);
                    }
                }

            }
        }
    }

    /**
     * @param inParams
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public JSONObject executeAppLogic(JSONObject inParams) {
        // Initializing attributes..
        JSONObject lastResponse = new JSONObject();
        JSONObject sessionVariables = new JSONObject();
        JSONObject apiResponse = new JSONObject();
        JSONObject apiRequest = new JSONObject();
        // SimpleBindings bindings = new SimpleBindings();
        String globalBaseTag = "";
        long startTimeStamp = 0;

        JSONParser parser = new JSONParser();
        String inputparamsjsonString = inParams.toJSONString();
        log.info("Input parameters|" + inputparamsjsonString);
        try {
            startTimeStamp = (new Date()).getTime();
            apiRequest = (JSONObject) parser.parse(inputparamsjsonString);
        } catch (ParseException e1) {
            log.error(Constants.errorMsg, e1);
        }
        String api = (String) apiRequest.get("api");
        String bTag = api + "|" + apiRequest.get("crId") + "|";
        globalBaseTag = bTag;
        Document doc = null;
        boolean hasError = false;
        try {
            log.info(bTag + "Start executing request....");
            sessionVariables.clear();
            doc = constant.getApis(api);
            if (doc == null) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                ApiHistoryEntity scapi = acApi.findActiveApiByName(api).orElseThrow(() -> new ApiNotFoundException("Service creater Api [" + api + "] is not found."));
                if (scapi.getXml() == null) {
                    throw new ApiNotFoundException("Service creater Api [" + api + "] XML is not available.");
                }
                ;
                log.info("|Found API:{}, Commit:{}, Version:{}", api, scapi.getCommitId(), scapi.getVersion());
                doc = dBuilder.parse(new ByteArrayInputStream(scapi.getXml().getBytes()));
                log.info(bTag + "|Loading api XML to memory from DB");
                constant.setApis(api, doc);
            }
            Element currentBlock = null;
            String currentBlockType = null;
            /**
             * optional, but recommended read this
             * http://stackoverflow.com/questions/13786607/normalization-in-dom-
             * parsing-with-java-how-does-it-work
             */
            doc.getDocumentElement().normalize();
            currentBlock = getBlockbyId(doc, "0", globalBaseTag);
            boolean endofapp = false;
            while (currentBlock != null && !endofapp) {
                bTag = api + "|" + apiRequest.get("crId") + "|";
                if (lastResponse.containsKey(RESP_CODE)) {
                    if (lastResponse.get(RESP_CODE).equals(HttpStatus.INTERNAL_SERVER_ERROR + "")) {
                        apiResponse.put(RESP_CODE, HttpStatus.INTERNAL_SERVER_ERROR + "");
                        sessionVariables.put(RESP_CODE, HttpStatus.INTERNAL_SERVER_ERROR + "");
                        if (lastResponse.containsKey(RESP_DESC)) {
                            apiResponse.put(RESP_DESC, (lastResponse.get(RESP_DESC)));
                            sessionVariables.put(RESP_DESC, (lastResponse.get(RESP_DESC)));
                        }
                        break;
                    }
                }
                currentBlockType = currentBlock.getAttribute("type");
                bTag = bTag + "BLK-" + currentBlock.getAttribute("id") + "|" + currentBlockType + "|";

                if (currentBlockType.equals("func")) { // Execute Function BLOCK
                    log.debug(bTag + "STARTING FUNC BLOCK....");
                    lastResponse = executeFunctionBlock(currentBlock, bTag, globalBaseTag, sessionVariables, apiRequest,
                            lastResponse);
                    log.debug(bTag + "END OF FUNC BLOCK");
                    String nextBlock = currentBlock.getElementsByTagName("next-node").item(0).getTextContent();
                    if (nextBlock.equalsIgnoreCase("END")) {
                        endofapp = true;
                    } else {
                        endofapp = false;
                        currentBlock = getBlockbyId(doc, nextBlock, globalBaseTag);
                    }
                } else if (currentBlockType.equalsIgnoreCase("assign")) { // Execute
                    // Assign
                    // BLOCK
                    String nextBlock = currentBlock.getElementsByTagName("next-node").item(0).getTextContent();
                    executeAssignBlock(currentBlock, bTag, globalBaseTag, sessionVariables, apiRequest, lastResponse);
                    if (nextBlock.equalsIgnoreCase("END")) {
                        endofapp = true;
                    } else {
                        endofapp = false;
                        currentBlock = getBlockbyId(doc, nextBlock, globalBaseTag);
                    }
                } else if (currentBlockType.equalsIgnoreCase("pass")) {// Execute
                    // Bypass
                    // BLOCK
                    String nextBlock = currentBlock.getElementsByTagName("next-node").item(0).getTextContent();
                    if (nextBlock.equalsIgnoreCase("END")) {
                        endofapp = true;
                    } else {
                        endofapp = false;
                        currentBlock = getBlockbyId(doc, nextBlock, globalBaseTag);
                    }
                } else if (currentBlockType.equalsIgnoreCase("branch")
                        || currentBlockType.equalsIgnoreCase("response")) {// Execute
                    // Branch
                    // BLOCK

                    String nextBlock = executeBranchBlock(currentBlock, bTag, globalBaseTag, sessionVariables,
                            apiRequest, lastResponse);
                    if (nextBlock.equalsIgnoreCase("END")) {
                        endofapp = true;
                    } else {
                        endofapp = false;
                        currentBlock = getBlockbyId(doc, nextBlock, globalBaseTag);
                    }
                } else
                    endofapp = true;

            }
            boolean setReturnParam = true;
            if (apiResponse.containsKey(RESP_CODE)) {
                if (!apiResponse.get(RESP_CODE).equals(HttpStatus.INTERNAL_SERVER_ERROR + "")) {
                    setReturnParam = false;
                }
            }
            if (setReturnParam) {
                currentBlock = getBlockbyId(doc, "return", globalBaseTag);
                bTag = api + "|" + apiRequest.get("crId") + "|RETURN|";
                if (currentBlock != null) {
                    NodeList nListparam0 = currentBlock.getElementsByTagName("case");
                    if (nListparam0.getLength() > 0) {
                        boolean matchCase = false;
                        for (int k = 0; k < nListparam0.getLength(); k++) {
                            Node pNode0 = nListparam0.item(k);
                            if (pNode0.getNodeType() == Node.ELEMENT_NODE) {
                                Element ele0 = (Element) pNode0;
                                log.debug(bTag + "Case Id:|" + ele0.getAttribute("id"));
                                String expression = getVariableValue(
                                        ele0.getElementsByTagName("expression").item(0).getTextContent(), globalBaseTag,
                                        sessionVariables, apiRequest, lastResponse);
                                expression = expression.replaceAll("\\\\", "");
                                matchCase = (Boolean) scriptEngine.eval(expression, new SimpleBindings());
                                log.debug(bTag + "(Expression):result| (" + expression + ") : " + matchCase);
                                if (matchCase) {
                                    log.info(bTag + "(Expression):result| (" + expression + ") : " + matchCase);
                                    setApiResponse(ele0, bTag, globalBaseTag, sessionVariables, apiRequest,
                                            lastResponse, apiResponse);
                                    break;
                                }
                            }
                        }
                        if (!matchCase) {
                            log.info(bTag + "No Match found go to default the node...");
                            Element ele1 = (Element) currentBlock.getElementsByTagName("default").item(0);
                            setApiResponse(ele1, bTag, globalBaseTag, sessionVariables, apiRequest, lastResponse,
                                    apiResponse);
                        }

                    } else {
                        setApiResponse(currentBlock, bTag, globalBaseTag, sessionVariables, apiRequest, lastResponse,
                                apiResponse);

                    }
                }
            }

        } catch (ApiNotFoundException e) {
            apiResponse.clear();
            apiResponse.put(RESP_CODE, HttpStatus.NOT_FOUND + "");
            apiResponse.put(RESP_DESC, e.getMessage());
            log.error(bTag + "|" + Constants.errorMsg, e);
            hasError = true;
        } catch (FileNotFoundException e) {
            apiResponse.clear();
            apiResponse.put(RESP_CODE, HttpStatus.NOT_FOUND + "");
            apiResponse.put(RESP_DESC, e.getMessage());
            log.error(bTag + "|" + Constants.errorMsg, e);
            hasError = true;
        } catch (InParamNotFoundException e) {
            apiResponse.clear();
            apiResponse.put(RESP_CODE, HttpStatus.BAD_REQUEST + "");
            apiResponse.put(RESP_DESC, e.getMessage());
            log.error(bTag + "|" + Constants.errorMsg, e);
            hasError = true;
        } catch (MissingParamException e) {
            apiResponse.clear();
            apiResponse.put(RESP_CODE, HttpStatus.INTERNAL_SERVER_ERROR + "");
            apiResponse.put(RESP_DESC, e.getMessage());
            log.error(bTag + "|" + Constants.errorMsg, e);
            hasError = true;
        } catch (Exception e) {
            apiResponse.clear();
            apiResponse.put(RESP_CODE, HttpStatus.INTERNAL_SERVER_ERROR + "");
            apiResponse.put(RESP_DESC, e.getMessage());
            log.error(bTag + "|" + Constants.errorMsg, e);
            hasError = true;
        } finally {
            lastResponse = null;
            sessionVariables = null;
            apiRequest = null;
            parser = null;
            doc = null;
        }
        /////////////////////////////////////////////
        // Print the content of the hashMap
        Set<Entry<String, String>> hashSet = apiResponse.entrySet();
        String retMap = "{";
        for (@SuppressWarnings("rawtypes")
                Entry entry : hashSet) {
            retMap = retMap + "'" + entry.getKey() + "':'" + entry.getValue() + "',";
        }
        retMap = retMap.substring(0, retMap.length() - 1) + "}";
        long totalProcessingTime = new Date().getTime() - startTimeStamp;
        log.info(bTag + "Return : " + retMap);
        log.info(bTag + "End of executing request|Total Process Time (ms) :" + totalProcessingTime);


        //update db with stats
        acApi.updateStats(api, totalProcessingTime, hasError ? 1l : 0);

        ////////////////////////////////////////////
        // ***************
        lastResponse = null;
        sessionVariables = null;
        apiRequest = null;
        parser = null;
        // *******************
        return apiResponse;
    }

    private String getVariableValue(
            String ip,
            String globalBaseTag,
            JSONObject sessionVariables,
            JSONObject apiRequest,
            JSONObject lastResponse
    ) throws MissingParamException, InParamNotFoundException {
        // ,globalBaseTag,sessionVariables,apiRequest,lastResponse)
        String out = null;
        String paramName = "";
        if (ip.indexOf("%ses.") == 0 && ip.indexOf(" ") == -1) {
            log.debug(globalBaseTag + "serch in session var:" + ip);
            // out=(String) sessionVariables.get(ip.substring(5,
            // ip.length()-1));
            out = readParamValue(sessionVariables, ip.substring(5, ip.length() - 1), globalBaseTag);
            paramName = ip.substring(5, ip.length() - 1);
        } else if (ip.indexOf("%ipo.") == 0 && ip.indexOf(" ") == -1) {
            // out=(String) apiRequest.get(ip.substring(4, ip.length()-1));
            out = readParamValue(apiRequest, ip.substring(5, ip.length() - 1), globalBaseTag);
            paramName = ip.substring(5, ip.length() - 1);
        } else if (ip.indexOf("%ip.") == 0 && ip.indexOf(" ") == -1) {
            // out=(String) apiRequest.get(ip.substring(4, ip.length()-1));
            out = readParamValue(apiRequest, ip.substring(4, ip.length() - 1), globalBaseTag);
            paramName = ip.substring(4, ip.length() - 1);
            if (out == null) {
                throw new InParamNotFoundException("input param not found [" + paramName + "]");
            }
        } else if (ip.indexOf("%") == 0 && ip.indexOf(" ") == -1) {
            log.debug(globalBaseTag + "serch in Last response:" + ip);
            // out=(String) this.lastResponse.get(ip.substring(1,
            // ip.length()-1));
            out = readParamValue(lastResponse, ip.substring(1, ip.length() - 1), globalBaseTag);
            paramName = ip.substring(1, ip.length() - 1);
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
            log.debug(globalBaseTag + "% Indexes :" + idx.substring(1));
            String[] idxx = null;
            int preIdx = 0;
            if (idx.contains(",")) {
                idxx = idx.substring(1).split(",");
                for (int i = 0; i < idxx.length; i++) {
                    if (i % 2 == 0) {
                        // logger.debug("Fix text part|"+ip.substring(preIdx,
                        // Integer.parseInt(idxx[i])));
                        out = out + ip.substring(preIdx, Integer.parseInt(idxx[i]));
                        preIdx = Integer.parseInt(idxx[i]) + 1;
                    } else {
                        // logger.debug("Varibale Name |"+ip.substring(preIdx,
                        // Integer.parseInt(idxx[i])));
                        out = out + getVariableValue("%" + ip.substring(preIdx, Integer.parseInt(idxx[i])) + "%",
                                globalBaseTag, sessionVariables, apiRequest, lastResponse);
                        preIdx = Integer.parseInt(idxx[i]) + 1;
                    }
                }
            }
            out = out + ip.substring(preIdx);
            // out = out.replaceAll("\\\\", "");
            // logger.debug("Last part |"+ip.substring(preIdx));
            log.debug(globalBaseTag + "IN text |" + ip);
            log.debug(globalBaseTag + "OUT Text |" + out);
        } else
            out = ip;
        // if(out==null){throw new MissingParamException("missing parameter
        // ["+paramName+"]");}

        return out;
    }

    private String getValueFromArray(JSONObject jobj, String ip) {
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
        // System.out.println("IDX1:"+idx1.substring(1));
        // System.out.println("IDX2:"+idx2.substring(1));
        String[] idxa1 = idx1.substring(1).split(",");
        String[] idxa2 = idx2.substring(1).split(",");
        int k = 0;
        String joName = ip.substring(k, Integer.parseInt(idxa1[0]));
        jarr = (JSONArray) jobj.get(joName);
        // System.out.println("OBJ:"+joName);
        for (int i = 0; i < idxa1.length; i++) {
            String jaidx = ip.substring(Integer.parseInt(idxa1[i]) + 1, Integer.parseInt(idxa2[i]));
            // System.out.println("IDX:"+jaidx);
            if (i < idxa1.length - 1) {
                jarr = (JSONArray) jarr.get(Integer.parseInt(jaidx));
                // System.out.println("xx:"+jarr.toJSONString());
            } else {

                res = jarr.get(Integer.parseInt(jaidx)).toString();
                // System.out.println("Return value :"+res);
            }
            k = Integer.parseInt(idxa2[i]) + 1;
        }
        return res;
    }

    private String getReturnValues(String ip, JSONObject sessionVariables, JSONObject lastResponse,
                                   JSONObject inputParams, String globalBaseTag) throws MissingParamException, InParamNotFoundException {
        String out = null;
        if (ip.indexOf("%ses.") == 0) {
            out = readParamValue(sessionVariables, ip.substring(5, ip.length() - 1), globalBaseTag);
        } else if (ip.indexOf("%ip.") == 0) {
            out = readParamValue(inputParams, ip.substring(4, ip.length() - 1), globalBaseTag);
        } else if (ip.indexOf("%") == 0) {
            out = readParamValue(lastResponse, ip.substring(1, ip.length() - 1), globalBaseTag);
        } else
            out = ip;
        // logger.info("Return attributes name:value|"+ip+":"+out);
        return out;
    }

    private String readParamValue(JSONObject jobj, String p, String globalBaseTag) {
        String val = null;
        try {

            if (p.equals("."))
                return jobj.toJSONString();// added on 2019-05-28 to get full
            // object

            String[] l0 = p.split("\\.");

            for (int l = 0; l < l0.length; l++) {
                log.debug(globalBaseTag + l + "|" + l0[l]);
                if (l < l0.length - 1) {
                    // getValueFromArray(JSONObject jobj,String ip )
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
            log.error(Constants.errorMsg, e);
        }
        return val;

    }

    private Element getBlockbyId(Document doc, String blockid, String globalBaseTag) {
        Element resp = null;
        NodeList nList = doc.getElementsByTagName("block");
        for (int l = 0; l < nList.getLength(); l++) {
            Node nNode = nList.item(l);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (eElement.getAttribute("id").equals(blockid)) {
                    log.debug(globalBaseTag + "Current Element id:" + ((Element) nNode).getAttribute("id"));
                    resp = eElement;
                    break;
                }
            }
        }
        return resp;
    }

    private String breakLine(String ip, int len) {
        char[] str = ip.toCharArray();
        String out = "";
        int k = 0;
        for (int i = 0; i < str.length; i++) {
            out = out + str[i];
            k++;
            if (k == len) {
                out = out + "\n";
                k = 0;
            }
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    private void setVariable(JSONObject jobj, String ip, String value) {
        int index1 = ip.indexOf(".");
        if (index1 > 0) {
            String var = ip.substring(0, index1);
            String val = ip.substring(index1 + 1);
            JSONObject njobj1 = (JSONObject) jobj.get(var);
            if (njobj1 == null)
                njobj1 = new JSONObject();
            setVariable(njobj1, val, value);
            jobj.put(var, njobj1);

        } else {
            if (jobj.containsKey(ip))
                jobj.remove(ip);
            jobj.put(ip, value);
        }
    }
}
