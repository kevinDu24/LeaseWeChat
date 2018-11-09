package com.tm.leasewechat.service.icsoc;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pengchao on 2017/4/14.
 */
@FeignClient(name = "wechat", url = "http://file.api.weixin.qq.com/cgi-bin/media/get")
public interface MultimediaDownLoadInterface {
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<byte[]> downLoadMedia(@RequestParam("access_token") String access_token, @RequestParam("media_id") String media_id);
}
