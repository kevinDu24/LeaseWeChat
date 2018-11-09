package com.tm.leasewechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * 客户发送客服消息表
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSendMsg {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    @LastModifiedDate
    private Date updateTime; //更新时间

    @CreatedDate
    private Date createTime; //提交时间

    private String toUserName;//

    private String fromUserName; //

    private String msgType;//消息类型

    private String msgId;//消息id

    private String content;//内容

    private String picUrl; //图片url

    private String mediaId;

    private Long sendTime;//微信发送时间

    private String ccMessage;//cc返回结果

    public UserSendMsg(WxMpXmlMessage wxMpXmlMessage) {
        this.toUserName = wxMpXmlMessage.getToUserName();
        this.fromUserName = wxMpXmlMessage.getFromUserName();
        this.msgType = wxMpXmlMessage.getMsgType();
        this.msgId = wxMpXmlMessage.getMediaId();
        this.content = wxMpXmlMessage.getContent();
        this.picUrl = wxMpXmlMessage.getPicUrl();
        this.mediaId = wxMpXmlMessage.getMediaId();
        this.sendTime = wxMpXmlMessage.getCreateTime();
    }
}
