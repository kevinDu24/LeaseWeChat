package com.tm.leasewechat.service.wechat;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by pengchao on 2017/4/26.
 */
@Service
public class ImageEventHandler implements WxMpMessageHandler {
    @Autowired
    private WeChatImageService weChatImageService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        WxMpXmlMessage wxMpXmlMessage = weChatImageService.receiveMsg(wxMessage);
        if(wxMpXmlMessage ==null){
            return null;
        }
        return buildResponse(wxMpXmlMessage);
    }
    private WxMpXmlOutTextMessage buildResponse(WxMpXmlMessage wxMpXmlMessage) {
        WxMpXmlOutTextMessage textMessage = new WxMpXmlOutTextMessage();
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setFromUserName(wxMpXmlMessage.getFromUserName());
        textMessage.setToUserName(wxMpXmlMessage.getToUserName());
        textMessage.setMsgType(wxMpXmlMessage.getMsgType());
        textMessage.setContent(wxMpXmlMessage.getContent());
        return textMessage;
    }

}
