package com.tm.leasewechat.service.car;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/9/27.
 */
@FeignClient(name = "carInterface", url = "https://www.autohome.com.cn/ashx/AjaxIndexCarFind.ashx")
public interface CarInterface {
    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCarBrands(@RequestParam(value = "type", required = true) String type);

    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCarSeries(@RequestParam(value = "type", required = true) String type, @RequestParam(value = "value", required = true) String value);
}
