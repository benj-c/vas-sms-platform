package com.sys.vas.stream.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface FinalOutSource {
	String SMSROUT = "finalOut";
	@Output(FinalOutSource.SMSROUT)
	MessageChannel finalOutputChannel();

}
