package com.tm.leasewechat.service.wechat;

import com.tm.leasewechat.config.TemplateProperties;
import com.tm.leasewechat.dao.*;
import com.tm.leasewechat.domain.*;
import com.tm.leasewechat.dto.customer.SysUserDto;
import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.message.MessageType;
import com.tm.leasewechat.dto.wechat.*;
import com.tm.leasewechat.dto.wz.PushOrderDto;
import com.tm.leasewechat.dto.wz.result.ChangeRepayCardResultDto;
import com.tm.leasewechat.consts.ApplyType;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.kefu.result.WxMpKfMsgList;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by LEO on 16/9/26.
 */
@Service
public class WeChatService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpMessageRouter router;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private WzSysUserRepository wzSysUserRepository;

    @Autowired
    private ClickEventHandler clickEventHandler;

    @Autowired
    private SubscribeEventHandler subscribeEventHandler;

    @Autowired
    private TextEventHandler textEventHandler;

    @Autowired
    private CustomServiceEventHandler customServiceEventHandler;

    @Autowired
    private ChatEventHandler chatEventHandler;

    @Autowired
    private ImageEventHandler imageEventHandler;

    @Autowired
    private WeChatMsgInterface weChatMsgInterface;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private TemplateProperties templateProperties;

    @Autowired
    private WzRepayCardChangeRecordRepository wzRepayCardChangeRecordRepository;

    @Autowired
    private AppUserBasicInfoRepository appUserBasicInfoRepository;


    private static final Logger logger = LoggerFactory.getLogger(WeChatService.class);
    private static final int MAX_FORCEREFRESH_COUNT = 20;

    /**
     * 微信事件监听
     * @param request
     * @return
     */
    public String eventListener(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("UTF-8");

        router
            .rule()
            .msgType(WxConsts.XML_MSG_EVENT)
            .event(WxConsts.EVT_CLICK)
            .async(false)
            .handler(clickEventHandler)
            .end()

            .rule()
            .msgType(WxConsts.XML_MSG_EVENT)
            .event(WxConsts.EVT_SUBSCRIBE)
            .async(false)
            .handler(subscribeEventHandler)
            .end()

            .rule()
            .msgType(WxConsts.XML_MSG_TEXT)
            .async(false)
            .handler(textEventHandler)
            .end()

            .rule()
            .msgType(WxConsts.XML_MSG_EVENT)
            .event(WxConsts.EVT_KF_CREATE_SESSION)
            .async(false)
            .handler(customServiceEventHandler)
            .end()

            .rule()
            .msgType(WxConsts.XML_MSG_EVENT)
            .event(WxConsts.EVT_KF_CLOSE_SESSION)
            .async(false)
            .handler(customServiceEventHandler)
            .end()

            .rule()
            .msgType(WxConsts.XML_TRANSFER_CUSTOMER_SERVICE)
            .event(WxConsts.CUSTOM_MSG_TEXT)
            .async(false)
            .handler(chatEventHandler)
            .end()

            .rule()
            .msgType(WxConsts.XML_MSG_IMAGE)
            .async(false)
            .handler(imageEventHandler)
            .end();

        WxMpXmlOutMessage responseMsg = router.route(messageDecoder(request));
        if (responseMsg != null) {
          // 说明是同步回复的消息
          // 将xml写入HttpServletResponse
            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.print(responseMsg.toXml());
            out.flush();
            out.close();
        }
        return "";
    }

    /**
     * 消息解析
     * @param request
     * @return
     */
    public WxMpXmlMessage messageDecoder(HttpServletRequest request){
        String data = null;
        try {
            data = IOUtils.toString(request.getInputStream(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WxMpXmlMessage.fromXml(data);
    }

    /**
     * 微信账号绑定
     * @param userBindDto
     * @return
     */
    public ResponseEntity weChatBind(UserBindDto userBindDto){
        userBindDto.setCardId(userBindDto.getCardId().toUpperCase());
        SysUser sysUser = null;
        try {
            WxMpOAuth2AccessToken token = wxMpService.oauth2getAccessToken(userBindDto.getWxCode());
            String code = passwordEncoder.encode("20d38ca487094ed0b22c97bd855d507d1");
            SysUser sysUserByOpenId = sysUserRepository.findByOpenId(token.getOpenId());
            SysUser sysUserByCard = sysUserRepository.findByCardId(userBindDto.getCardId().toUpperCase());
            if(sysUserByCard == null && sysUserByOpenId == null){
                // 身份证号和openId都未被绑定
                sysUser = new SysUser(token.getOpenId(), userBindDto, code);
                sysUser.setUnionId(token.getUnionId());
                sysUserRepository.save(sysUser);
            }else if(sysUserByCard != null && sysUserByOpenId == null){
                // 身份证已被绑定,openId未绑定
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该身份证号已被绑定,请更换身份证号"), HttpStatus.OK);
            }else if(sysUserByCard == null && sysUserByOpenId != null){
                // 身份证未被绑定,openId已绑定
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "您已绑定过别的身份证号"), HttpStatus.OK);
            }else{
                // 身份证和openId都被绑定
                if(sysUserByCard.getOpenId().equals(token.getOpenId())){
                    sysUser = sysUserByCard;
                    if(!passwordEncoder.matches("20d38ca487094ed0b22c97bd855d507d1", sysUser.getCode())){
                        sysUser.setCode(code);
                        sysUser.setUnionId(token.getUnionId());
                        sysUserRepository.save(sysUser);
                    }
                }else{
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "您已绑定过别的身份证号"), HttpStatus.OK);
                }
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, e.getError().getErrorCode() + "请关闭微信绑定页面重新进入,如无法解决,请联系客服"), HttpStatus.OK);
        }
        SysUserDto sysUserDto = new SysUserDto();
        BeanUtils.copyProperties(sysUser, sysUserDto);
        sysUserDto.setCode("20d38ca487094ed0b22c97bd855d507d1");
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, sysUserDto), HttpStatus.OK);
    }

    //通过code换取用户openId
    public String getOpenId(String code){
        try {
            WxMpOAuth2AccessToken token = wxMpService.oauth2getAccessToken(code);
            return  token.getOpenId();
        } catch (WxErrorException e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取客服聊天记录
    public ResponseEntity<Message> getChatRecord(String startTime, String endTime, Long msgId, Integer number) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTimeD = sdf.parse(startTime);
        Date endTimeD = sdf.parse(endTime);
        List<WxMpKfMsgList> wxMpKfMsgLists = new ArrayList<WxMpKfMsgList>();
        wxMpKfMsgLists = getWxMpKfMsgLists(startTimeD, endTimeD, msgId, number, wxMpKfMsgLists);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, wxMpKfMsgLists), HttpStatus.OK);
    }

    //获取客服聊天记录实现
    private List<WxMpKfMsgList> getWxMpKfMsgLists(Date startTimeD, Date endTimeD, Long msgId, Integer number, List wxMpKfMsgLists){
        try{
            WxMpKfMsgList wxMpKfMsgList =  wxMpService.getKefuService().kfMsgList(startTimeD, endTimeD, msgId, number);
            wxMpKfMsgLists.add(wxMpKfMsgList);
            if(number.equals(wxMpKfMsgList.getNumber())){
                getWxMpKfMsgLists(startTimeD, endTimeD, wxMpKfMsgList.getMsgId(), number, wxMpKfMsgLists);
            }
        }catch (WxErrorException e){
            e.printStackTrace();
        }
        return wxMpKfMsgLists;
    }

    //check用户是否已经绑定微众
    public ResponseEntity getWzWeChatBind(String wxCode){
        try {
            WxMpOAuth2AccessToken token = wxMpService.oauth2getAccessToken(wxCode);
            WzSysUser sysUserByOpenId = wzSysUserRepository.findByOpenId(token.getOpenId());
            //openId已绑定
            if(sysUserByOpenId != null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "您已绑定过微信号,请勿重复绑定"), HttpStatus.OK);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,null, token.getOpenId()), HttpStatus.OK);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, e.getError().getErrorCode() + "请关闭微信绑定页面重新进入,如无法解决,请联系客服"), HttpStatus.OK);
        }
    }

    //微众项目用户绑定
    public ResponseEntity wzWeChatBind(UserBindDto userBindDto){
        WzSysUser wzSysUser = null;
        String openId = userBindDto.getOpenId();
        if(openId == null || openId.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请关闭微信绑定页面重新进入,如无法解决,请联系客服"), HttpStatus.OK);
        }
        //绑定页面check用户是否已经绑定微众，已经获取过openid，此处不能再使用token获取
       // WxMpOAuth2AccessToken token = wxMpService.oauth2getAccessToken(userBindDto.getWxCode());
        String code = passwordEncoder.encode("wz20d38ca487094ed0b22c97bd855d507d1");
        WzSysUser sysUserByOpenId = wzSysUserRepository.findByOpenId(openId);
        WzSysUser sysUserByPhoneNum = wzSysUserRepository.findByPhoneNum(userBindDto.getPhoneNum());
        if(sysUserByPhoneNum == null && sysUserByOpenId == null){
            // 手机号和openId都未被绑定
            wzSysUser = new WzSysUser(openId, userBindDto, code);
            wzSysUser.setUnionId(openId);
            wzSysUser.setUpdateTime(new Date());
            wzSysUserRepository.save(wzSysUser);
        }else if(sysUserByOpenId != null){
            // 手机号未被绑定,openId已绑定
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "您已绑定过微信号,请勿重复绑定"), HttpStatus.OK);
        }else if(sysUserByPhoneNum != null && sysUserByOpenId == null){
            // 手机号已被绑定,openId未绑定
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "手机号已被绑定,请更换手机号"), HttpStatus.OK);
        }
        SysUserDto sysUserDto = new SysUserDto();
        BeanUtils.copyProperties(wzSysUser, sysUserDto);
        sysUserDto.setCode("wz20d38ca487094ed0b22c97bd855d507d1");
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, sysUserDto), HttpStatus.OK);
    }



    /**
     * 获取微信url签名
     * @param url
     * @return
     */
    public ResponseEntity<Message> getUrlSignature(String url){
        //授权页面url不带state，原因未知，现手动加上，后期待调查
        if(url.indexOf("code")!=-1){
            url = url + "&state=123";
        }
        WxJsapiSignature wxJsapiSignature = (WxJsapiSignature) redisRepository.get(url);
//        WxJsapiSignature wxJsapiSignature = null;
        if(wxJsapiSignature == null){
            try {
                wxJsapiSignature = wxMpService.createJsapiSignature(url);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
            redisRepository.save(url, wxJsapiSignature, 7200);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, wxJsapiSignature), HttpStatus.OK);
    }




    //通过手机号推送
    @Transactional
    public ResponseEntity<Message> sendTemplate(PushOrderDto pushOrderDto){
        logger.info("pushOrderDto={}", pushOrderDto);
        WzSysUser wzSysUser = wzSysUserRepository.findByPhoneNum(pushOrderDto.getPhoneNum());
        if (wzSysUser == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "推送失败，未查询到手机号"), HttpStatus.OK);
        }
//        String url = "http://18k53892a0.51mypc.cn/#/wx/wzApply?applyCode=" + pushOrderDto.getUserName();
        SendTemplateMessageDto sendMessageDto = new SendTemplateMessageDto();
        sendMessageDto.setUrl(templateProperties.getPushOrder().getUrl().replace("applyCodeValue",pushOrderDto.getUserName()).replace("timeStampValue",String.valueOf(System.currentTimeMillis())));
        sendMessageDto.setTemplate_id(templateProperties.getPushOrder().getId());
        sendMessageDto.setTouser(wzSysUser.getOpenId());
        /*
        模版ID: ssVt6Lnbwlx6XhXSXygDAJ-va0IznEYyUyP2PMB2M00
        标题: 业务提醒
        详细内容
        {{first.DATA}}
        姓名：{{keyword1.DATA}}
        业务类型：{{keyword2.DATA}}
        {{remark.DATA}}
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TemplateMessageDto templateMessageDto = new TemplateMessageDto();
        //配置模板信息参数

        KeyNoteDto first = new KeyNoteDto("尊敬的" + wzSysUser.getName() + ", 您有新的通知");
        KeyNoteDto name = new KeyNoteDto(wzSysUser.getName());
        KeyNoteDto serviceType = new KeyNoteDto("车贷申请");
        KeyNoteDto remark = new KeyNoteDto("点击即可进入申请页面进行申请");
        templateMessageDto.setFirst(first);
        templateMessageDto.setKeyword1(name);
        templateMessageDto.setKeyword2(serviceType);
        templateMessageDto.setRemark(remark);
        sendMessageDto.setData(JSONObject.fromObject(templateMessageDto));
        return sendTemplateMsg(sendMessageDto);
    }

//    //推送在线助力融用户基本信息
//    public ResponseEntity<Message> sendAppUserInfo(AppUserBasicInfoDto appUserBasicInfoDto){
//        logger.info("appUserBasicInfoDto={}", appUserBasicInfoDto);
//       //分别通过用户银行预留手机号，手机号，副手机号，住宅区查找微信用户
//        WzSysUser wzSysUser = wzSysUserRepository.findByPhoneNum(appUserBasicInfoDto.getPhoneNum());
//        if (wzSysUser == null){
//            //通过用户其他信息手机号查找用户
//            wzSysUser = wzSysUserRepository.findByPhoneNum(appUserBasicInfoDto.getOtherPhoneNum());
//        }
//        if(wzSysUser == null){
//            //通过用户副手机号查找用户
//            wzSysUser = wzSysUserRepository.findByPhoneNum(appUserBasicInfoDto.getVicePhoneNumber());
//        }
//        if(wzSysUser == null){
//            //通过用户住宅电话查找用户
//            wzSysUser = wzSysUserRepository.findByPhoneNum(appUserBasicInfoDto.getHomeNumber());
//        }
//        if (wzSysUser == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "当前客户没有进行微信绑定，请绑定成功后，再来点击提交。谢谢"), HttpStatus.OK);
//        }
//        //保存app推送过来的用户信息，一审页面查询出来
//        AppUserBasicInfo appUserBasicInfo = appUserBasicInfoRepository.findByUniqueMark(appUserBasicInfoDto.getUniqueMark());
//        AppUserBasicInfo newAppUserBasicInfo = new AppUserBasicInfo(appUserBasicInfoDto);
//
//        if(appUserBasicInfo != null){
//            appUserBasicInfoRepository.delete(appUserBasicInfo);
//        }
//        appUserBasicInfoRepository.save(newAppUserBasicInfo);
//
//        SendTemplateMessageDto sendMessageDto = new SendTemplateMessageDto();
//        sendMessageDto.setUrl(templateProperties.getPushOrder().getUrl().replace("applyCodeValue",appUserBasicInfoDto.getFpName()).replace("uniqueMarkValue", appUserBasicInfoDto.getUniqueMark()));
//        sendMessageDto.setTemplate_id(templateProperties.getPushOrder().getId());
//        sendMessageDto.setTouser(wzSysUser.getOpenId());
//        /*
//        模版ID: ssVt6Lnbwlx6XhXSXygDAJ-va0IznEYyUyP2PMB2M00
//        标题: 业务提醒
//        详细内容
//        {{first.DATA}}
//        姓名：{{keyword1.DATA}}
//        业务类型：{{keyword2.DATA}}
//        {{remark.DATA}}
//         */
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        TemplateMessageDto templateMessageDto = new TemplateMessageDto();
//        //配置模板信息参数
//
//        KeyNoteDto first = new KeyNoteDto("尊敬的" + appUserBasicInfoDto.getName() +", 您有新的通知");
//        KeyNoteDto name = new KeyNoteDto(wzSysUser.getName());
//        KeyNoteDto serviceType = new KeyNoteDto("车贷申请信息确认");
//        KeyNoteDto remark = new KeyNoteDto("点击即可进入申请页面进行申请");
//        templateMessageDto.setFirst(first);
//        templateMessageDto.setKeyword1(name);
//        templateMessageDto.setKeyword2(serviceType);
//        templateMessageDto.setRemark(remark);
//        sendMessageDto.setData(JSONObject.fromObject(templateMessageDto));
//        return sendTemplateMsg(sendMessageDto);
//    }


    //还款卡变更推送
    public ResponseEntity<Message> sendChangeRepayCardResult(ChangeRepayCardResultDto changeRepayCardResultDto, String openId){
        //还款卡变更推送URL
        String url = templateProperties.getRepayCardChange().getUrl();
        if(changeRepayCardResultDto.getApplyNum() != null){
            url = url.replace("applyNumValue", changeRepayCardResultDto.getApplyNum());
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "系统异常"), HttpStatus.OK);
        }
        //保存还款卡变更结果
        WzRepayCardChangeRecord wzRepayCardChangeRecord = wzRepayCardChangeRecordRepository.findTop1ByApplyNumOrderByUpdateTimeDesc(changeRepayCardResultDto.getApplyNum());
        if(wzRepayCardChangeRecord != null){
            wzRepayCardChangeRecord.setBankCard(changeRepayCardResultDto.getBankCardNum());
            wzRepayCardChangeRecord.setMessage(changeRepayCardResultDto.getMessage());
            wzRepayCardChangeRecord.setName(changeRepayCardResultDto.getUserName());
            wzRepayCardChangeRecordRepository.save(wzRepayCardChangeRecord);
        }
        SendTemplateMessageDto sendMessageDto = new SendTemplateMessageDto();
        sendMessageDto.setUrl(url);
        //手机号推送模板 业务提醒
        sendMessageDto.setTemplate_id(templateProperties.getRepayCardChange().getId());
        sendMessageDto.setTouser(openId);
        TemplateMessageDto templateMessageDto = new TemplateMessageDto();
        //配置模板信息参数
        KeyNoteDto first = new KeyNoteDto("尊敬的"+ changeRepayCardResultDto.getUserName() + ", 您有新的还款卡变更通知");
        KeyNoteDto name = new KeyNoteDto(changeRepayCardResultDto.getUserName());
        KeyNoteDto serviceType = new KeyNoteDto("还款卡变更");
        KeyNoteDto remark = new KeyNoteDto("点击查看详情");
        templateMessageDto.setFirst(first);
        templateMessageDto.setKeyword1(name);
        templateMessageDto.setKeyword2(serviceType);
        templateMessageDto.setRemark(remark);
        sendMessageDto.setData(JSONObject.fromObject(templateMessageDto));
        return sendTemplateMsg(sendMessageDto);
    }


    //推送审批结果
    public ResponseEntity<Message> sendApplyResult(ApplyInfo applyInfo,String openId){
//        String url = "http://18k53892a0.51mypc.cn/#/wx/wzResult?applyNum=" + applyInfo.getApplyNum() + "&uniqueMark=" + applyInfo.getUniqueMark();
        String url = templateProperties.getApplyResult().getUrl();
        if(applyInfo.getUniqueMark() == null && applyInfo.getApplyNum() != null){
            url = url.replace("applyNumValue", applyInfo.getApplyNum()).replace("uniqueMarkValue","");
        } else if(applyInfo.getUniqueMark() != null && applyInfo.getApplyNum() == null){
            url = url.replace("applyNumValue", "").replace("uniqueMarkValue",applyInfo.getUniqueMark());
        } else {
            url = url.replace("applyNumValue", applyInfo.getApplyNum()).replace("uniqueMarkValue",applyInfo.getUniqueMark());
        }

        SendTemplateMessageDto sendMessageDto = new SendTemplateMessageDto();
        sendMessageDto.setUrl(url);
        sendMessageDto.setTemplate_id(templateProperties.getApplyResult().getId());
        sendMessageDto.setTouser(openId);
         /*
        模版ID: L6uR8LVuDNqRVJ8ErboMTD3WujaWZoePCi6C71O7BgM
        标题: 审核结果通知
        {{first.DATA}}
        审核结果：{{keyword1.DATA}}
        审核日期：{{keyword2.DATA}}
        {{remark.DATA}}
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TemplateMessageDto templateMessageDto = new TemplateMessageDto();
        //配置模板信息参数
        KeyNoteDto first = new KeyNoteDto("尊敬的"+ applyInfo.getName() + ", 您有新的申请状态通知");
        String result = ApplyType.getCode(applyInfo.getStatus());
        KeyNoteDto applyResult = new KeyNoteDto(result);
        KeyNoteDto date = new KeyNoteDto(sdf.format(new Date()));
        KeyNoteDto remark = new KeyNoteDto("点击查看详情");
        templateMessageDto.setFirst(first);
        templateMessageDto.setKeyword1(applyResult);
        templateMessageDto.setKeyword2(date);
        templateMessageDto.setRemark(remark);
        sendMessageDto.setData(JSONObject.fromObject(templateMessageDto));
        return sendTemplateMsg(sendMessageDto);
    }

    //推送电子签约
    public ResponseEntity<Message> sendContractSign(ContractInfo contractInfo, String openId){
        String url = templateProperties.getContractSign().getUrl();
        if(contractInfo.getApplyNum() != null){
            url = url.replace("applyNumValue", contractInfo.getApplyNum());
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "系统异常"), HttpStatus.OK);
        }

        SendTemplateMessageDto sendMessageDto = new SendTemplateMessageDto();
        sendMessageDto.setUrl(url);
        sendMessageDto.setTemplate_id(templateProperties.getContractSign().getId());
        sendMessageDto.setTouser(openId);
        /*
        	模版ID: oE3hSuS2SPD7BAf1jCd5k0ebJNApANiSNSzrNO9aPdE
            标题:服务签约提醒
            详细内容
            {{first.DATA}}
            服务名：{{keyword1.DATA}}
            有效期：{{keyword2.DATA}}
            {{remark.DATA}}
        * */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TemplateMessageDto templateMessageDto = new TemplateMessageDto();
        //配置模板信息参数
        KeyNoteDto first = new KeyNoteDto("尊敬的"+ contractInfo.getName() + ", 您有新的通知");
        KeyNoteDto serviceName = new KeyNoteDto("合同签约");
        KeyNoteDto effective = new KeyNoteDto("永久");
        KeyNoteDto remark = new KeyNoteDto("点击开始签约");
        templateMessageDto.setFirst(first);
        templateMessageDto.setKeyword1(serviceName);
        templateMessageDto.setKeyword2(effective);
        templateMessageDto.setRemark(remark);
        sendMessageDto.setData(JSONObject.fromObject(templateMessageDto));
        return sendTemplateMsg(sendMessageDto);
    }

    //模板消息发送
    public ResponseEntity<Message> sendTemplateMsg(SendTemplateMessageDto sendTemplateMessageDto){
        MsgDto msgDto = new MsgDto();
        String accessToken = (String) redisRepository.get("wxAccessToken");
        try {
            if(accessToken == null){
                try {
                    accessToken = wxMpService.getAccessToken();
                    redisRepository.save("wxAccessToken", accessToken, 7200);
                } catch (WxErrorException e) {
                    e.printStackTrace();
                }
            }
            msgDto = weChatMsgInterface.sendTemplateMsg(accessToken,sendTemplateMessageDto);
            logger.info("msgDto={}", msgDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "系统异常"), HttpStatus.OK);
        }
        JSONObject json = JSONObject.fromObject(msgDto);
        if(json.get("errcode") != null){
            int forceRefreshCount = 0;
            if(redisRepository.get("messageForceRefreshCount") != null){
                forceRefreshCount = (int) redisRepository.get("messageForceRefreshCount");
            }
            if(("40001".equals(json.get("errcode").toString()) || "42001".equals(json.get("errcode").toString())) && forceRefreshCount < MAX_FORCEREFRESH_COUNT){
                try {
                    accessToken = wxMpService.getAccessToken(true);
                    redisRepository.save("wxAccessToken", accessToken, 7200);
                    forceRefreshCount = forceRefreshCount + 1;
                    redisRepository.save("messageForceRefreshCount", forceRefreshCount, 24*60*60);
                } catch (WxErrorException e) {
                    e.printStackTrace();
                }
                msgDto = weChatMsgInterface.sendTemplateMsg(accessToken,sendTemplateMessageDto);
            }
        }
        if(!"0".equals(msgDto.getErrcode())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "推送失败，错误码：" + msgDto.getErrcode()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, msgDto), HttpStatus.OK);
    }
}
