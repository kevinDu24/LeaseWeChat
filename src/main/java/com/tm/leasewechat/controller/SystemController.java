package com.tm.leasewechat.controller;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.service.sms.SmsInterface;
import com.tm.leasewechat.service.system.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/9/29.
 */
@RestController
@RequestMapping("/systems")
public class SystemController {
    @Autowired
    private SmsInterface smsInterface;

    @Autowired
    private SystemService systemService;

    /**
     * 发送短信验证码
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public ResponseEntity<Message> sendCode(String phoneNum){
        return smsInterface.sendCode(phoneNum);
    }

    /**
     * 校验短信验证码
     * @param phoneNum
     * @param code
     * @return
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
    public ResponseEntity<Message> verifyCode(String phoneNum, String code){
        return smsInterface.verifyCode(phoneNum, code);
    }


    /**
     * 发送短信验证码（先锋太盟）
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/sendRandomCode", method = RequestMethod.POST)
    public ResponseEntity<Message> sendRandomCode(String phoneNum){
        return systemService.sendRandomCode(phoneNum);
    }

    /**
     * 验证登录
     * @param timestamp 发送短信时间戳
     * @param code 验证码
     * @return
     */
    @RequestMapping(value = "/loginWeb", method = RequestMethod.POST)
    public ResponseEntity<Message> loginWeb(String timestamp, String code){
        return systemService.loginWeb(timestamp, code);
    }


    @RequestMapping(value = "/redisSave", method = RequestMethod.POST)
    public ResponseEntity<Message> redisSave(String key, String value, int time){
        return systemService.redisSave(key,value,time);
    }

    @RequestMapping(value = "/getRedisValue", method = RequestMethod.POST)
    public ResponseEntity<Message> getRedisValue(String key){
        return systemService.getRedisValue(key);
    }


}
