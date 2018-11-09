package com.tm.leasewechat.service;

import com.tm.leasewechat.dto.message.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/11/15.
 */
@FeignClient(name = "calculatorInterface", url = "http://wx.xftm.com")
public interface CalculatorInterface {

    @RequestMapping(value = "/financeProducts", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getProducts();
}
