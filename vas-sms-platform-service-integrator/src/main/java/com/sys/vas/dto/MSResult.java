package com.sys.vas.dto;

import javax.xml.bind.annotation.XmlElement;

import org.json.simple.JSONObject;

public class MSResult extends Result {
	@XmlElement(name = "content") private  JSONObject content ;

	public JSONObject getContent() {
		return content;
	}

	public void setContent(JSONObject content) {
		this.content = content;
	}

}
