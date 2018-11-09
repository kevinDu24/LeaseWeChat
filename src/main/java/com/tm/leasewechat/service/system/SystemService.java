package com.tm.leasewechat.service.system;


import com.tm.leasewechat.config.MessageProperties;
import com.tm.leasewechat.dao.MessageLogsRepository;
import com.tm.leasewechat.dao.RedisRepository;
import com.tm.leasewechat.domain.MessageLogs;
import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.message.MessageType;
import com.tm.leasewechat.dto.wz.SendShortMessageDto;
import com.tm.leasewechat.service.wz.WzApplyService;
import com.tm.leasewechat.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by pengchao on 2017/12/7.
 */
@Service
public class SystemService {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    MessageLogsRepository messageLogsRepository;

    @Autowired
    MessageProperties messageProperties;

    @Autowired
    WzApplyService wzApplyService;

    /**
     * 发送短信验证码
     * @param phoneNum
     * origin 0 梦网科技发送   其他的由主系统发送
     * @return
     */
    public ResponseEntity<Message> sendRandomCode(String phoneNum){
        String code = "" + (int)(Math.random()*900000+100000);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        redisRepository.save(timeStamp, code, 300);
        String origin = "1";
        if(redisRepository.get("wechat_shortMessageOrigin") != null){
            origin = (String) redisRepository.get("wechat_shortMessageOrigin");
        }
        SendShortMessageDto sendShortMessageDto = new SendShortMessageDto();
        sendShortMessageDto.setPhoneNum(phoneNum);
        sendShortMessageDto.setText(messageProperties.getPszMsg().replace("xxxxxx",code));
        //调用外部的发送短信接口
        String send = "";
        try {
            //梦网科技发送
            if("0".equals(origin)){
                send = messageUtil.senRadomCode(phoneNum, code);
                //解析后的返回值不为空且长度不大于10，则提交失败，交给主系统发送
                if(send == null || "".equals(send) || send.length()< 10){
                    ResponseEntity<Message> responseEntity = wzApplyService.sendShortMessage(sendShortMessageDto);
                    send = responseEntity.getBody().getStatus();
                }
            } else {
                //主系统发送
                ResponseEntity<Message> responseEntity = wzApplyService.sendShortMessage(sendShortMessageDto);
                send = responseEntity.getBody().getStatus();
                //主系统发送失败，再由梦网科技发送
                if("ERROR".equals(send)){
                    send = messageUtil.senRadomCode(phoneNum, code);
                }
            }
            saveMessageLog(phoneNum, messageProperties.getPszMsg().replace("xxxxxx",code), send);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "系统异常"), HttpStatus.OK);
        }
        if(send != null && !"".equals(send) && ("SUCCESS".equals(send) || send.length()>10)){
            //解析后的返回值不为空且长度大于10，则是提交成功
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, (Object)timeStamp), HttpStatus.OK);
        }else{//解析后的返回值不为空且长度不大于10，则提交失败，返回错误码
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "发送短信异常"), HttpStatus.OK);
        }
    }


    public ResponseEntity<Message> redisSave(String key, String value, int time){
        redisRepository.save(key, value, time);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,  redisRepository.get(key)), HttpStatus.OK);
    }

    public ResponseEntity<Message> getRedisValue(String key){
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,  redisRepository.get(key)), HttpStatus.OK);
    }

    /**
     * 保存发送短信log
     * @param phoneNum
     * @param content
     */
    public void saveMessageLog(String phoneNum, String content, String sendStatus){
        Date nowDate = new Date();
        MessageLogs messageLog = new MessageLogs();
        messageLog.setPhone(phoneNum);
        messageLog.setContent(content);
        messageLog.setSendTime(nowDate);
        messageLog.setUpdateTime(nowDate);
        messageLog.setStatus(sendStatus);
        messageLogsRepository.save(messageLog);
    }


    /**
     * 登录接口（web端）
     * @param timeStamp
     * @param code
     * @return
     */
    public ResponseEntity<Message> loginWeb( String timeStamp, String code) {
        if (redisRepository.get(timeStamp) == null) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "验证码已过期,请重新获取验证码"), HttpStatus.OK);
        }
        if (!redisRepository.get(timeStamp).equals(code)) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "验证码错误"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

}
