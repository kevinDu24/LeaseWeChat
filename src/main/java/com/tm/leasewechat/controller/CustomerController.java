package com.tm.leasewechat.controller;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/10/10.
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 获取客户手机号码
     * @param name
     * @param cardId
     * @return
     */
    @RequestMapping(value = "/getPhoneNum", method = RequestMethod.GET)
    public ResponseEntity<Message> sendCode(String name, String cardId){
        return customerService.getPhoneNum(name, cardId);
    }
}
