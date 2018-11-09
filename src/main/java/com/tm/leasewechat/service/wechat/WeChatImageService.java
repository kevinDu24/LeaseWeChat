package com.tm.leasewechat.service.wechat;

import com.tm.leasewechat.dao.CcSendMsgRepository;
import com.tm.leasewechat.dao.UserSendMsgRepository;
import com.tm.leasewechat.domain.CcSendMsg;
import com.tm.leasewechat.domain.UserSendMsg;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by pengchao on 2017/4/26.
 */
@Service
public class WeChatImageService {
    @Autowired
    private WeChatInterface weChatInterface;

    @Autowired
    private UserSendMsgRepository userSendMsgRepository;

    @Autowired
    private CcSendMsgRepository ccSendMsgRepository;

    private static final Logger logger = LoggerFactory.getLogger(WeChatImageService.class);

    public WxMpXmlMessage receiveMsg(WxMpXmlMessage wxMpXmlMessage){
        WxMpXmlMessage wxMpXmlOutMessage = new WxMpXmlMessage();
        UserSendMsg userSendMsg = new UserSendMsg(wxMpXmlMessage);
        String xml = "<xml>\n" +
                " <ToUserName><![CDATA["+ wxMpXmlMessage.getToUserName()+"]]></ToUserName>\n" +
                " <FromUserName><![CDATA["+ wxMpXmlMessage.getFromUserName() +"]]></FromUserName>\n" +
                " <CreateTime>"+ wxMpXmlMessage.getCreateTime() +"</CreateTime>\n" +
                " <MsgType><![CDATA["+ wxMpXmlMessage.getMsgType()+"]]></MsgType>\n" +
                " <PicUrl><![CDATA["+ wxMpXmlMessage.getPicUrl() +"]]></PicUrl>\n" +
                " <MediaId><![CDATA["+ wxMpXmlMessage.getMediaId() +"]]></MediaId>\n" +
                " <MsgId>"+ wxMpXmlMessage.getMsgId() +"</MsgId>\n" +
                " </xml>";
        String message = "";
        logger.info("调用cc接收消息接口开始***********");
        try {
            message = weChatInterface.receiveImageMsg(xml);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        userSendMsg.setCcMessage(message);
        userSendMsgRepository.save(userSendMsg);
        if("success".equals(message)){
            return null;
        }
        wxMpXmlOutMessage =  WxMpXmlMessage.fromXml(message);
        CcSendMsg ccSendMsg = new CcSendMsg(wxMpXmlOutMessage);
        ccSendMsg.setWeChatMessage("cc自动回复");
        ccSendMsgRepository.save(ccSendMsg);
        return wxMpXmlOutMessage;
    }
}
