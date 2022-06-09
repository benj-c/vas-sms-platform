package com.sys.vas.datamodel.messages;

import java.util.HashMap;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class VasTransactionMsg {

	// Parameters from originated SMS
	private String msisdn;
	private String destPort;
	private String originalSms;

	// Parameter by sms router service
	private String correlationId;
	private String scApiName;
	private long actionId;
	private String dissableResponseSms;

	// Parameter by service creator service
	private JSONObject responseParams;
	private int resCode;
	private String resDesc;

	// Parameters by response handler service
	private JSONArray customerResponses;
	private HashMap<String, String> tokens;

	public String toString() {
		String desc = "ServiceRequestMsg (msisdn:" + msisdn + "|destPort:" + destPort + "|originalSms:" + originalSms
				+ "|correlation:" + correlationId + "|action:" + actionId + "|api:" + scApiName + "|resCode:"
				+ resCode + "|resDesc:" + resDesc;
		if (tokens != null)
			desc = desc + "|tokens:" + tokens.toString();
		if (responseParams != null)
			desc = desc + "|responseParams:" + responseParams.toJSONString();
		if (customerResponses != null)
			desc = desc + "|customerResponses:" + customerResponses.toJSONString();
		return desc + ")";
	}

}
