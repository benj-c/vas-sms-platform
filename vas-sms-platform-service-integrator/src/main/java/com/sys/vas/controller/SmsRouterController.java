package com.sys.vas.controller;

import com.sys.vas.datamodel.messages.ShortMessage;
import com.sys.vas.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@RestController
@Slf4j
//@EnableBinding(SmsRouterSource.class)
public class SmsRouterController {

//    private SmsRouterSource smsRouterSource;
//
//    public SmsRouterController(SmsRouterSource smsRouterSource) {
//        this.smsRouterSource = smsRouterSource;
//    }
//
//    /**
//     * @return
//     */
//    @GetMapping(
//            path = "/vas/sms/publish",
//            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE
//    )
//    public ResponseEntity publishSms(
//            @QueryParam(value = "msisdn") String msisdn,
//            @QueryParam(value = "msg") String msg
//    ) {
//        long startTime = System.currentTimeMillis();
//        log.info("Initiating|publishSms");
//        log.info("ReqParams|msisdn:{},msg:{}", msisdn, msg);
//        try {
//            ShortMessage sms = new ShortMessage(
//                    AppUtil.formattedMsisdn(msisdn),
//                    msg,
//                    "678",
//                    MDC.get("RequestId")
//            );
////            smsRouterSource.smsRouterChannel().send(MessageBuilder.withPayload(sms).build());
//            log.info("SMS received and added to the queue|{}", sms.toString());
//            return ResponseEntity.ok().build();
//        } finally {
//            log.info("Completed|publishSms|ProcessingTime:{}ms", System.currentTimeMillis() - startTime);
//        }
//    }

}
