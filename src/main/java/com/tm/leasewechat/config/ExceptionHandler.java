package com.tm.leasewechat.config;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.message.MessageType;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by LEO on 16/11/13.
 */
@ControllerAdvice
public class ExceptionHandler {
    @ResponseStatus(value= HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler(value = WxErrorException.class)
    @ResponseBody
    public ResponseEntity<Message> handleWxErrorException(HttpServletRequest req, WxErrorException ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        Message message = new Message(MessageType.MSG_TYPE_ERROR, ex.getError().getErrorMsg());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
}
