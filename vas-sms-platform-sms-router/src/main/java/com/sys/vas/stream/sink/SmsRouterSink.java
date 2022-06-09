package com.sys.vas.stream.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface SmsRouterSink {
	String SMSRIN = "smsRouterIn";

	@Input(SmsRouterSink.SMSRIN)
	SubscribableChannel smsRouterChannel();

}
