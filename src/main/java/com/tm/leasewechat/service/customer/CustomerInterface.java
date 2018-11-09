package com.tm.leasewechat.service.customer;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/9/27.
 */
@FeignClient(name = "customer", url = "http://happyleasing.cn/TMZL/app")
//@FeignClient(name = "customer", url = "http://116.228.224.58:8077/TMZLTEST/app")
public interface CustomerInterface {

    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getPhoneNum(@RequestParam(value = ".url", required = false) String url,
                       @RequestParam(value = "operator", required = false) String operator,
                       @RequestParam(value = "sign", required = false) String sign,
                       @RequestParam(value = "timestamp", required = false) String timestamp,
                       @RequestParam(value = "BASQXM", required = false) String basqxm,
                       @RequestParam(value = "BAZJHM", required = false) String bazjhm);
}
