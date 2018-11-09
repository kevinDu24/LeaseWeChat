package com.tm.leasewechat.service.wechat;

import com.google.common.collect.Lists;
import com.tm.leasewechat.config.WxProperties;
import com.tm.leasewechat.dao.ApplyInfoRepository;
import com.tm.leasewechat.dto.information.InformationDto;
import com.tm.leasewechat.service.information.InformationInterface;
import com.tm.leasewechat.utils.Utils;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by LEO on 16/10/17.
 */
@Service
public class ClickEventHandler implements WxMpMessageHandler{

    @Autowired
    private InformationInterface informationInterface;

    @Autowired
    private WxProperties wxProperties;

    @Autowired
    private ApplyInfoRepository applyInfoRepository;

    @Autowired
    private WeChatService weChatService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        if(wxMessage.getEventKey().equals("company_news")){
            return pushNews(wxMessage, 1);
        }else if(wxMessage.getEventKey().equals("industry_info")){
            return pushNews(wxMessage, 4);
        }else if(wxMessage.getEventKey().equals("finance_introduce")){
            return pushFinanceIntroduce(wxMessage);
        }else if(wxMessage.getEventKey().equals("company_introduce")){
            return pushNews(wxMessage, 6);
        }else if(wxMessage.getEventKey().equals("contact_us")){
            return pushText(wxMessage, Utils.buildText(wxProperties.getContactUs()));
        }else if(wxMessage.getEventKey().equals("material_requested")){
            return pushText(wxMessage, Utils.buildText(wxProperties.getMaterialRequested()));
        }else if(wxMessage.getEventKey().equals("online_help")){
            return pushText(wxMessage, "很高兴为您服务,请简要输入您的问题,我们会耐心为您解答");
        }
        return null;
    }

    /**
     * 推送新闻
     * @param wxMpXmlMessage
     * @return
     */
    private WxMpXmlOutNewsMessage pushNews(WxMpXmlMessage wxMpXmlMessage, Integer type){
        List<InformationDto> informationDtos = getData(type, 5);
        WxMpXmlOutNewsMessage newsMessage = new WxMpXmlOutNewsMessage();
        newsMessage.setCreateTime(System.currentTimeMillis());
        newsMessage.setFromUserName(wxMpXmlMessage.getToUserName());
        newsMessage.setToUserName(wxMpXmlMessage.getFromUserName());
        newsMessage.setMsgType("news");
        List<WxMpXmlOutNewsMessage.Item> items = constructItems(informationDtos);
        items.forEach(item -> newsMessage.addArticle(item));
        return newsMessage;
    }

    private WxMpXmlOutMessage pushText(WxMpXmlMessage wxMpXmlMessage, String text){
        WxMpXmlOutTextMessage textMessage = new WxMpXmlOutTextMessage();
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setFromUserName(wxMpXmlMessage.getToUserName());
        textMessage.setToUserName(wxMpXmlMessage.getFromUserName());
        textMessage.setMsgType("text");
        textMessage.setContent(text);
        return textMessage;
    }


    /**
     * 推送产品介绍
     * @param wxMpXmlMessage
     * @return
     */
    private WxMpXmlOutMessage pushFinanceIntroduce(WxMpXmlMessage wxMpXmlMessage){
        List<InformationDto> products = getData(5, 1);
        List<InformationDto> process = getData(7, 1);
        WxMpXmlOutNewsMessage newsMessage = new WxMpXmlOutNewsMessage();
        newsMessage.setCreateTime(System.currentTimeMillis());
        newsMessage.setFromUserName(wxMpXmlMessage.getToUserName());
        newsMessage.setToUserName(wxMpXmlMessage.getFromUserName());
        newsMessage.setMsgType("news");
        List<WxMpXmlOutNewsMessage.Item> items = constructItems(products);
        items.addAll(constructItems(process));
        items.add(constructProblem());
        items.forEach(item -> newsMessage.addArticle(item));
        return newsMessage;
    }
    /**
     * 获取资讯数据
     * @return
     */
    private List<InformationDto> getData(Integer type, Integer size){
        Map map = (Map) informationInterface.getInfos(type, 1, size).getBody().getData();
        List<Object> temp = (List)map.get("content");
        List<InformationDto> informationDtos = Lists.newArrayList();
        temp.forEach(object -> {
            InformationDto informationDto = new InformationDto((Map<String, Object>) object);
            informationDtos.add(informationDto);
        });
        return informationDtos;
    }

    /**
     * 构造数据
     * @param informationDtos
     * @return
     */
    private List<WxMpXmlOutNewsMessage.Item> constructItems(List<InformationDto> informationDtos){
        List<WxMpXmlOutNewsMessage.Item> items = Lists.newArrayList();
        informationDtos.forEach(informationDto -> {
            WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
            item.setDescription("");
            String url = informationDto.getImgUrls().size() == 0 ? null : informationDto.getImgUrls().get(0);
            item.setPicUrl(url);
            item.setTitle(informationDto.getTitle());
            item.setUrl(wxProperties.getServerUrl() + "/informations/" + informationDto.getId());
            items.add(item);
        });
        return items;
    }

    private WxMpXmlOutNewsMessage.Item constructProblem(){
        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        item.setPicUrl("http://wx.xftm.com:89/information/news/6aad4e23-4d35-4bef-8b79-d7a8851314ac.jpg");
        item.setTitle("常见问题");
        item.setUrl(wxProperties.getServerUrl()+"/#/wx/commonProblem");
        return item;
    }
}
