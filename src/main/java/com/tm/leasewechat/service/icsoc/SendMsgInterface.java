package com.tm.leasewechat.service.icsoc;


import com.tm.leasewechat.dto.wechat.SendMessageDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengchao on 2017/4/24.
 */
@FeignClient(name = "wechat", url = "https://api.weixin.qq.com/cgi-bin/message/custom/send")
public interface SendMsgInterface {
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    Object sendMsg(@RequestParam("access_token") String access_token, @RequestBody SendMessageDto sendMessageDto);
}
