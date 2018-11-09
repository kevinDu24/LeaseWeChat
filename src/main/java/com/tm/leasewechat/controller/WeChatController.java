package com.tm.leasewechat.controller;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.wechat.UserBindDto;
import com.tm.leasewechat.dto.wechat.VerificationDto;
import com.tm.leasewechat.service.wechat.WeChatService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * Created by LEO on 16/9/26.
 */
@RestController
@RequestMapping("/weChats")
public class WeChatController {

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private WxMpService wxMpService;

    /**
     * 微信验证
     * @param verificationDto
     * @return
     */
    @RequestMapping(value = "/push", method = RequestMethod.GET)
    public String authentication(VerificationDto verificationDto){
        return verificationDto.getEchostr();
    }

    /**
     * 微信事件推送接口
     * @param verificationDto
     * @param request
     * @return
     */
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public String eventListener(VerificationDto verificationDto, HttpServletRequest request, HttpServletResponse response){
        return weChatService.eventListener(request, response);
    }

    /**
     * 微信绑定账号
     * @param userBindDto
     * @return
     */
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public ResponseEntity weChatBind(@RequestBody UserBindDto userBindDto){
        return weChatService.weChatBind(userBindDto);
    }


    /**
     * 微众用户微信绑定账号
     * @param userBindDto
     * @return
     */
    @RequestMapping(value = "/wzBind", method = RequestMethod.POST)
    public ResponseEntity wzWeChatBind(@RequestBody UserBindDto userBindDto){
        return weChatService.wzWeChatBind(userBindDto);
    }

    /**
     * 微众用户微信绑定账号查询是否已经绑定
     * @param wxCode
     * @return
     */
    @RequestMapping(value = "/getWzBind", method = RequestMethod.GET)
    public ResponseEntity wzWeChatBind(String wxCode){
        return weChatService.getWzWeChatBind(wxCode);
    }


    /**
     * 获取客服聊天记录
     * @return
     */
    @RequestMapping(value = "/getChatRecord", method = RequestMethod.GET)
    public ResponseEntity<Message> getChatRecord(@RequestParam String startTime, @RequestParam String endTime, @RequestParam Long msgId, @RequestParam Integer number) throws ParseException{
        return weChatService.getChatRecord(startTime, endTime, msgId, number);
    }


    /**
     * 获取微信url签名
     * @param url
     * @return
     */
    @RequestMapping(value = "/urlSignature", method = RequestMethod.GET)
    public ResponseEntity<Message> getUrlSignature(String url){
        return weChatService.getUrlSignature(url);
    }


}
