package com.sys.vas.stream.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ServiceCreatorSource {
	String SRVCOUT = "serviceCreatorOut";
	@Output(ServiceCreatorSource.SRVCOUT)
	MessageChannel srvCreatorChannel();

}
