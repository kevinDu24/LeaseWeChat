package com.tm.leasewechat.controller;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.service.bank.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/9/27.
 */
@RestController
@RequestMapping("/banks")
public class BankController {
    @Autowired
    private BankService bankService;

    /**
     * 获取银行列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Message> getBanks(){
        return bankService.getBanks();
    }
}
