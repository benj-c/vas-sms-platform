package com.sys.vas.datamodel.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@ToString
public class ShortMessage {
	private String msisdn;
	private String message;
	private String destPort;
	private String correlationId;

}
