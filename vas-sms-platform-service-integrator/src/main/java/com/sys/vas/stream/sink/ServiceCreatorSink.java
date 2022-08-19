package com.sys.vas.stream.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ServiceCreatorSink {
	String SRVCIN = "serviceCreatorIn";
	@Input(ServiceCreatorSink.SRVCIN)
	SubscribableChannel srvCreatorChannel();

}
