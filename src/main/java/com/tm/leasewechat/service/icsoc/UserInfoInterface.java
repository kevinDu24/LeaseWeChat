package com.tm.leasewechat.service.icsoc;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengchao on 2017/4/24.
 */
@FeignClient(name = "wechat", url = "https://api.weixin.qq.com/cgi-bin/user/info")
public interface UserInfoInterface {
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
   Object getUserInfo(@RequestParam(value = "access_token") String access_token, @RequestParam(value = "openid") String openid, @RequestParam(value = "lang", required = false) String lang );
}
