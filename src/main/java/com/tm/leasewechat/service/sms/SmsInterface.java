package com.tm.leasewechat.service.sms;

import com.tm.leasewechat.dto.message.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/9/29.
 */
@FeignClient(name = "smsInterface", url = "${request.serverUrl}")
public interface SmsInterface {
    @RequestMapping(value = "/sendCodeUnverified", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> sendCode(@RequestParam(value = "phoneNum") String phoneNum);

    @RequestMapping(value = "/gps/verifyCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> verifyCode(@RequestParam(value = "phoneNum") String phoneNum, @RequestParam(value = "code") String code);
}
