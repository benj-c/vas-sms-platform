package com.sys.vas.stream.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CxResponseSource {
	String CXRSOUT = "cxResponseOut";
	@Output(CxResponseSource.CXRSOUT)
	MessageChannel cxResponseChannel();

}
