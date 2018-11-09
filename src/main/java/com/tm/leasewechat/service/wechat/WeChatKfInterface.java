package com.tm.leasewechat.service.wechat;

import com.tm.leasewechat.dto.message.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangbiao on 2016/11/25 0025.
 */
@FeignClient(name = "weChatKfInterface", url = "${request.serverUrl}")
public interface WeChatKfInterface {

    @RequestMapping(value = "customService/getKfServiceTime", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getKfServiceTime();

}
