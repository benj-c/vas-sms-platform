package com.sys.vas.stream.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface SmsRouterSource {
	String SMSROUT = "smsRouterOut";
	@Output(SmsRouterSource.SMSROUT)
	MessageChannel smsRouterChannel();

}
