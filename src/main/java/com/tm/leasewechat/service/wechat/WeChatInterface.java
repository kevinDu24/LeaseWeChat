package com.tm.leasewechat.service.wechat;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengchao on 2017/4/25.
 */
@FeignClient(name = "weChatInterface", url = "${request.ccServerUrl}")
public interface WeChatInterface {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    String receiveMsg(@RequestBody String WxMpXmlMessage);

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    String receiveImageMsg(@RequestBody String WxMpXmlMessage);
}
