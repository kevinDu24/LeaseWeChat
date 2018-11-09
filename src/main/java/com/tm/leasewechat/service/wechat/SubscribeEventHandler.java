package com.tm.leasewechat.service.wechat;

import com.tm.leasewechat.config.WxProperties;
import com.tm.leasewechat.utils.Utils;
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
 * Created by LEO on 16/10/20.
 */
@Service
public class SubscribeEventHandler implements WxMpMessageHandler {

    @Autowired
    private WxProperties wxProperties;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        return pushText(wxMessage, wxProperties.getSubscribeReply());
    }

    private WxMpXmlOutMessage pushText(WxMpXmlMessage wxMpXmlMessage, String text){
        WxMpXmlOutTextMessage textMessage = new WxMpXmlOutTextMessage();
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setFromUserName(wxMpXmlMessage.getToUserName());
        textMessage.setToUserName(wxMpXmlMessage.getFromUserName());
        textMessage.setMsgType("text");
        textMessage.setContent(Utils.buildText(text));
        return textMessage;
    }
}
