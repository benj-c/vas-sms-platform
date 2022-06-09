package com.sys.vas.stream.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ServiceResponseSink {
	String RSHIN = "responseHandlerIn";
	@Input(ServiceResponseSink.RSHIN)
	SubscribableChannel responseHandlerChannel();

}
