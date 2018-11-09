package com.tm.leasewechat.service.wechat;

import com.tm.leasewechat.dto.sendall.SendAllByTagDto;
import com.tm.leasewechat.dto.wechat.MsgDto;
import com.tm.leasewechat.dto.wechat.SendTemplateMessageDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by pengchao on 2018/1/6.
 */

@FeignClient(name = "weChatMsgInterface", url = "${request.apiUrl}")
public interface WeChatMsgInterface {


    /**
     * 发送模板消息
     * @param access_token
     * @param sendMessageDto
     * @return
     */
    @RequestMapping(value = "/cgi-bin/message/template/send", method = RequestMethod.POST)
    @ResponseBody
    MsgDto sendTemplateMsg(@RequestParam("access_token") String access_token, @RequestBody SendTemplateMessageDto sendMessageDto);

    /**
     * https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN
     */

    @RequestMapping(value = "/cgi-bin/media/uploadimg", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    String uploadImg(@RequestParam("access_token") String access_token, @RequestPart(value = "file") MultipartFile file);

    /**
     * 根据消息类型进行群发
     * https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN
     *
     */
    @RequestMapping(value = "/cgi-bin/message/mass/sendall", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    String sendallByTag(@RequestParam("access_token") String access_token, @RequestBody SendAllByTagDto sendAllByTagDto);
}
