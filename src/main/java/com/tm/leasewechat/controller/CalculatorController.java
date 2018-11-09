package com.tm.leasewechat.controller;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.service.CalculatorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/11/15.
 */
@RestController
@RequestMapping("/calculators")
public class CalculatorController {

    @Autowired
    private CalculatorInterface calculatorInterface;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<Message> getProducts(){
        return calculatorInterface.getProducts();
    }
}
