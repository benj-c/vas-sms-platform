package com.sys.vas.service;

import com.sys.vas.datamodel.messages.VasTransactionMsg;
import com.sys.vas.stream.sink.ServiceCreatorSink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service("request-msg-listner")
@EnableBinding(ServiceCreatorSink.class)
public class RequestMsgListner {

	@Autowired
	ServiceExecutor srvExecutor;

	@StreamListener(target = ServiceCreatorSink.SRVCIN)
	public void processRequest(VasTransactionMsg srm) throws InterruptedException{
		// Inside this should be handled by Async
		srvExecutor.executeService(srm);
		Thread.sleep(20);
		
	}
}
