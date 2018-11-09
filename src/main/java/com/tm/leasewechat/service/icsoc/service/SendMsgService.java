package com.tm.leasewechat.service.icsoc.service;


import com.tm.leasewechat.dao.CcSendMsgRepository;
import com.tm.leasewechat.dao.RedisRepository;
import com.tm.leasewechat.dao.UserSendMsgRepository;
import com.tm.leasewechat.domain.CcSendMsg;
import com.tm.leasewechat.dto.wechat.SendMessageDto;
import com.tm.leasewechat.service.icsoc.SendMsgInterface;
import com.tm.leasewechat.service.icsoc.UserInfoInterface;
import com.tm.leasewechat.service.wechat.WeChatImageService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by pengchao on 2017/4/24.
 */

@Service
public class SendMsgService {
    @Autowired
    private SendMsgInterface sendMsgInterface;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private UserInfoInterface userInfoInterface;

    private static final int MAX_FORCEREFRESH_COUNT = 10;

    @Autowired
    private UserSendMsgRepository userSendMsgRepository;

    @Autowired
    private CcSendMsgRepository ccSendMsgRepository;

    private static final Logger logger = LoggerFactory.getLogger(WeChatImageService.class);


    public Object sendMsg(SendMessageDto sendMessageDto){
        logger.info("cc发送消息给用户接口开始***********");
        CcSendMsg ccSendMsg = new CcSendMsg(sendMessageDto);
        String accessToken = (String) redisRepository.get("wxAccessToken");
        if(accessToken == null){
            try {
                accessToken = wxMpService.getAccessToken();
                redisRepository.save("wxAccessToken", accessToken, 7200);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
        Object result = sendMsgInterface.sendMsg(accessToken, sendMessageDto);
        JSONObject json = JSONObject.fromObject(result);
        if(json.get("errcode") != null){
            int forceRefreshCount = 0;
            if(redisRepository.get("forceRefreshCount") != null){
                forceRefreshCount = (int) redisRepository.get("forceRefreshCount");
            }
            if(("40001".equals(json.get("errcode").toString()) || "42001".equals(json.get("errcode").toString())) && forceRefreshCount < MAX_FORCEREFRESH_COUNT){
                try {
                    accessToken = wxMpService.getAccessToken(true);
                    redisRepository.save("wxAccessToken", accessToken, 7200);
                    forceRefreshCount = forceRefreshCount + 1;
                    redisRepository.save("forceRefreshCount", forceRefreshCount, 24*60*60);
                } catch (WxErrorException e) {
                    e.printStackTrace();
                }
                result = sendMsgInterface.sendMsg(accessToken, sendMessageDto);
            }
        }
        if(result != null){
            ccSendMsg.setWeChatMessage(JSONObject.fromObject(result).toString());
        }
        ccSendMsgRepository.save(ccSendMsg);
        return  result;
    }


    public Object getUserInfo(String openid, String lang){
        String accessToken = (String) redisRepository.get("wxAccessToken");
        if(accessToken == null){
            try {
                accessToken = wxMpService.getAccessToken();
                redisRepository.save("wxAccessToken", accessToken, 7200);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
        System.out.println(accessToken);
        Object userInfo = userInfoInterface.getUserInfo(accessToken, openid, lang);
        JSONObject json = JSONObject.fromObject(userInfo);
        if(json.get("errcode") != null){
            int forceRefreshCount = 0;
            if(redisRepository.get("forceRefreshCount") != null){
                forceRefreshCount = (int) redisRepository.get("forceRefreshCount");
            }
            if(("40001".equals(json.get("errcode").toString()) || "42001".equals(json.get("errcode").toString())) && forceRefreshCount < MAX_FORCEREFRESH_COUNT){
                try {
                    accessToken = wxMpService.getAccessToken(true);
                    redisRepository.save("wxAccessToken", accessToken, 7200);
                    forceRefreshCount = forceRefreshCount + 1;
                    redisRepository.save("forceRefreshCount", forceRefreshCount, 24*60*60);
                } catch (WxErrorException e) {
                    e.printStackTrace();
                }
                userInfo = userInfoInterface.getUserInfo(accessToken, openid, lang);
            }
        }
        return userInfo;
    }
}
