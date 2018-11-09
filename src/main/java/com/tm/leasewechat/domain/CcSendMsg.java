package com.tm.leasewechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.leasewechat.dto.wechat.SendMessageDto;
import lombok.Data;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by pengchao on 2018/6/26.
 * 中通天鸿发送客户消息记录表
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CcSendMsg {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    @LastModifiedDate
    private Date updateTime; //更新时间

    @CreatedDate
    private Date createTime; //提交时间

    private String toUserName;

    private String msgType;

    private String text;

    private String picUrl;

    private String mediaId;

    private String title;

    private String description;

    private String url;

    private String weChatMessage;

    public CcSendMsg(SendMessageDto sendMessageDto) {
        this.toUserName = sendMessageDto.getTouser();
        this.msgType = sendMessageDto.getMsgtype();
        this.text = sendMessageDto.getText() == null ? "" : sendMessageDto.getText().getContent();
        this.mediaId = sendMessageDto.getImage() == null ? "" : sendMessageDto.getImage().getMedia_id();
        this.title = sendMessageDto.getTitle();
        this.picUrl = sendMessageDto.getPicurl();
        this.description = sendMessageDto.getDescription();
        this.url = sendMessageDto.getUrl();
    }

    public CcSendMsg(WxMpXmlMessage wxMpXmlOutMessage) {
        this.toUserName = wxMpXmlOutMessage.getToUserName();
        this.msgType = wxMpXmlOutMessage.getMsgType();
        this.text = wxMpXmlOutMessage.getContent();
        this.mediaId = wxMpXmlOutMessage.getMediaId();
        this.title = wxMpXmlOutMessage.getTitle();
        this.picUrl = wxMpXmlOutMessage.getPicUrl();
        this.description = wxMpXmlOutMessage.getDescription();
        this.url = wxMpXmlOutMessage.getUrl();
    }
}
