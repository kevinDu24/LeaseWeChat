package com.tm.leasewechat.service.wechat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tm.leasewechat.config.AccountProperties;
import com.tm.leasewechat.dao.RedisRepository;
import com.tm.leasewechat.dto.result.CommonResultDto;
import com.tm.leasewechat.dto.wz.SignQueryDto;
import com.tm.leasewechat.dto.wz.result.WzResultCommonDto;
import com.tm.leasewechat.service.KeywordsInterface;
import com.tm.leasewechat.service.TMWeChatInterface;
import com.tm.leasewechat.service.wz.WzApplyInterface;
import com.tm.leasewechat.utils.CommonUtils;
import com.tm.leasewechat.consts.WzSubmitType;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * Created by LEO on 16/11/11.
 */
@Service
@Slf4j
public class TextEventHandler implements WxMpMessageHandler {

    @Autowired
    private KeywordsInterface keywordsInterface;

    @Autowired
    private AccountProperties accountProperties;

    @Autowired
    private WeChatKfInterface weChatKfInterface;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private WeChatTextService weChatTextService;

    @Autowired
    private WzApplyInterface wzApplyInterface;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TMWeChatInterface tmWeChatInterface;

    @Autowired
    private Gson gson;

    private static final Logger logger = LoggerFactory.getLogger(TextEventHandler.class);

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        WxMpXmlMessage wxMpXmlMessage = null;
        try {
            String message = wxMessage.getContent().replaceAll(" ","");
            //拦截销售人员电子签约指令
            if(message != null && message.length() > 6 && ("#588mq").equals(message.substring(0,6))){
                logger.info("signQuery={}", wxMessage.getContent() + ":" + wxMessage.getFromUserName());
                SignQueryDto signQueryDto = new SignQueryDto();
                signQueryDto.setBASQBH(wxMessage.getContent());
                signQueryDto.setTXN_ID(WzSubmitType.SIGNQUERY.code());
                //调用主系统签约指令
                String result = wzApplyInterface.querySign(signQueryDto);
                logger.info("signQueryResult={}",result);
                WzResultCommonDto wzResultCommonDto = new WzResultCommonDto();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                wzResultCommonDto = objectMapper.readValue(result, WzResultCommonDto.class);
                //将主系统返回结果推送给用户
                wxMpXmlMessage = new WxMpXmlMessage();
                // -- 2018/9/12 11:48 By ChengQC
                // 此处返回 9999 0000 注意不同情况取不同内容
                //{"CODE":"9999","DATA":"电子签约推送失败,未查询订单信息","MESSAGE":"系统异常"}
                //{"CODE":"0000","DATA":null,"MESSAGE":"推送成功"}
                //"CODE":"9999","DATA":null,"MESSAGE":"当前状态不能电子签约！"}
                if("9999".equals(wzResultCommonDto.getCode()) && wzResultCommonDto.getData() != null && !CommonUtils.isNull(wzResultCommonDto.getData().toString())){
                    wxMpXmlMessage.setContent(wzResultCommonDto.getData().toString());
                }else{
                    wxMpXmlMessage.setContent(wzResultCommonDto.getMessage());
                }
                wxMpXmlMessage.setCreateTime(System.currentTimeMillis());
                wxMpXmlMessage.setFromUserName(wxMessage.getToUserName());
                wxMpXmlMessage.setToUserName(wxMessage.getFromUserName());
                wxMpXmlMessage.setMsgType(wxMessage.getMsgType());
            } else if(message != null && message.length() > 6 && ("#588YJ").equals(message.substring(0,6))){
                logger.info("addressInputQuery={}", wxMessage.getContent() + ":" + wxMessage.getFromUserName());
                SignQueryDto signQueryDto = new SignQueryDto();
                signQueryDto.setBASQBH(wxMessage.getContent());
                signQueryDto.setTXN_ID(WzSubmitType.ADDRESS_INPUT_QUERY.code());
                //调用主系统签约指令
                String result = wzApplyInterface.queryAddressInfo("IsItPossibleToSendChit",signQueryDto);
                logger.info("IsItPossibleToSendChit={}",result);
                CommonResultDto wzResultCommonDto = gson.fromJson(result, CommonResultDto.class);
                //短信改成由主系统调用接口发送发送
//                wzResultCommonDto.setCode("0000");
//                wzResultCommonDto.setData("{\n" +
//                        "        \"applyNum\": \"36143945\",\n" +
//                        "\t\t\"phoneNum\":\"18055313782\"\n" +
//                        "    }\n" +
//                        "   ");
//                if("0000".equals(wzResultCommonDto.getCode())){
//                    //发送短信给客户 todo
//                    SendMessageDto sendMessageDto = new SendMessageDto();
//                    sendMessageDto = objectMapper.readValue(wzResultCommonDto.getData().toString(), SendMessageDto.class);
//                    ResponseEntity<Message> responseEntity = tmWeChatInterface.addressInputSendCode(sendMessageDto);
//                    if("ERROR".equals(responseEntity.getBody().getStatus())){
//                        wzResultCommonDto.setMessage(responseEntity.getBody().getError());
//                    }
//                }
                //将主系统返回结果发送给用户
                wxMpXmlMessage = new WxMpXmlMessage();
                wxMpXmlMessage.setContent(wzResultCommonDto.getResult().getResultMsg());
                wxMpXmlMessage.setCreateTime(System.currentTimeMillis());
                wxMpXmlMessage.setFromUserName(wxMessage.getToUserName());
                wxMpXmlMessage.setToUserName(wxMessage.getFromUserName());
                wxMpXmlMessage.setMsgType(wxMessage.getMsgType());
            } else {
                wxMpXmlMessage = weChatTextService.receiveMsg(wxMessage);
                if(wxMpXmlMessage == null){
                    return null;
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return buildResponse(wxMpXmlMessage);
    }

    private WxMpXmlOutTextMessage buildResponse(WxMpXmlMessage wxMpXmlMessage){
        WxMpXmlOutTextMessage textMessage = new WxMpXmlOutTextMessage();
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setFromUserName(wxMpXmlMessage.getFromUserName());
        textMessage.setToUserName(wxMpXmlMessage.getToUserName());
        textMessage.setMsgType(wxMpXmlMessage.getMsgType());
        textMessage.setContent(wxMpXmlMessage.getContent());
        return textMessage;
    }
//         ResponseEntity<Message> responseEntity = null;
//        if(Utils.isNumber(wxMessage.getContent())){ //88
//            List redisResult =(List) redisRepository.get(wxMessage.getFromUserName());
//            if(0 == redisResult.size()){
//                return gotoKf(wxMessage, wxMpService);
//            }
//            Integer number = Integer.parseInt(wxMessage.getContent());
//            List result = (List) redisRepository.get(wxMessage.getFromUserName());
//            try {
//                Object reply = result.get(number - 1);
//                return buildResponse(wxMessage, "text", (String) reply);
//            } catch (Exception e){
//                return buildResponse(wxMessage, "text", "未查询到对应结果");
//            }
//        } else {
//            try {
//                log.info("用户输入:========" + wxMessage.getContent());
//                responseEntity = keywordsInterface.getMatchKeywords(wxMessage.getContent());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if(null == responseEntity){
//                log.info("关键字匹配结果:======" + responseEntity);
//                return gotoKf(wxMessage, wxMpService);
//            }
//            List<KeywordDto> keywords = (List<KeywordDto>) responseEntity.getBody().getData();
//            if(0 == keywords.size()){
//                return gotoKf(wxMessage, wxMpService);
//            }
//            long i = 0;
//            List reply = new ArrayList();
//            List keywordsReply = new LinkedList();
//            redisRepository.delete(wxMessage.getFromUserName());
//            for (Object temp : keywords) {
//                i += 1;
//                Map map = (Map)temp;
//                reply.add("\n" + i + "." + map.get("keywords").toString());
//                keywordsReply.add(map.get("replyContent").toString());
//            }
//            reply.add("\n" + "回复序列号查看详情" + "\n");
//            int saveTime = 1800;
//            redisRepository.save(wxMessage.getFromUserName(), keywordsReply, saveTime);
//            return buildResponse(wxMessage, "text", reply.toString());
//        }
//    }
//
//    //转入客服
//    private WxMpXmlOutTextMessage gotoKf(WxMpXmlMessage wxMessage, WxMpService wxMpService) throws WxErrorException {
//        Map kfServiceTime = getKfServiceTime();
//        log.info("=========" + kfServiceTime);
//        if((Boolean) kfServiceTime.get("isNotInServiceTime")){
//            log.info("不在客服时间");
//            return buildResponse(wxMessage, "text", (String)kfServiceTime.get("reply"));
//        }else {
//            List<WxMpKfInfo> kfInfoList = wxMpService.getKefuService().kfOnlineList().getKfOnlineList();
//            log.info("************" + kfInfoList);
//            for(WxMpKfInfo kfInfo : kfInfoList){
//                if(0 == kfInfo.getAcceptedCase()){
//                    log.info("转入客服系统");
//                    return buildResponse(wxMessage, "transfer_customer_service", "欢迎进入人工客服，请稍等...");
//                }
//            }
//            log.info("客服繁忙");
//            return buildResponse(wxMessage, "text", "客服繁忙，请稍后再试...");
//        }
//    }
//
//    /**
//     * 构造响应
//     * @param wxMpXmlMessage
//     * @param msgType
//     * @param reply
//     * @return
//     */
//    private WxMpXmlOutTextMessage buildResponse(WxMpXmlMessage wxMpXmlMessage, String msgType, String reply){
//        WxMpXmlOutTextMessage textMessage = new WxMpXmlOutTextMessage();
//        textMessage.setCreateTime(System.currentTimeMillis());
//        textMessage.setFromUserName(wxMpXmlMessage.getToUserName());
//        textMessage.setToUserName(wxMpXmlMessage.getFromUserName());
//        textMessage.setMsgType(msgType);
//        textMessage.setContent(reply);
//        return textMessage;
//    }
//
//    private Boolean hasIdleCustomService(WxMpService wxMpService){
//        int count = 0;
//        try {
//            count = wxMpService.getKefuService().kfSessionGetWaitCase().getKfSessionWaitCaseList().size();
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
//        logger.info("客服是否繁忙:"+ (count == 0));
//        return count == 0;
//    }
//
//    //客服是否在服务时间
//    private Map getKfServiceTime(){
//        Map map = (Map) weChatKfInterface.getKfServiceTime().getBody().getData();
//        Map result = new HashMap();
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");//设置日期格式
//            String now = dateFormat.format(new Date());
//            Date nowTime = dateFormat.parse(now);
//            Date startTime1 = dateFormat.parse((String)map.get("startTime1"));
//            Date endTime1 = dateFormat.parse((String)map.get("endTime1"));
//            Boolean isNotInServiceTime = nowTime.before(startTime1) || nowTime.after(endTime1);
//            result.put("isNotInServiceTime", isNotInServiceTime);
//            result.put("reply", map.get("reply"));
//        }catch (ParseException e){
//            e.printStackTrace();
//        }
//        return result;
//    }


}
