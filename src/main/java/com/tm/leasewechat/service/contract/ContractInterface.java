package com.tm.leasewechat.service.contract;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/9/27.
 */
@FeignClient(name = "contract", url = "http://happyleasing.cn/TMZL/app")
//@FeignClient(name = "contract", url = "http://116.228.224.58:8077/TMZLTEST/app")
public interface ContractInterface {

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String wxQuery(@RequestParam(value = ".url", required = false) String url,
                   @RequestParam(value = "operator", required = false) String operator,
                   @RequestParam(value = "sign", required = false) String sign,
                   @RequestParam(value = "timestamp", required = false) String timestamp,
                   @RequestParam(value = "bakhxm", required = false) String bakhxm,
                   @RequestParam(value = "bazjhm", required = false) String bazjhm,
                   @RequestParam(value = "bahthm", required = false) String bahthm);


    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String query(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "operator", required = false) String operator,
                        @RequestParam(value = "timestamp", required = false) String timestamp,
                        @RequestParam(value = "sign", required = false) String sign,
                        @RequestParam(value = "basqbh", required = false) String applyNum,
                 //审批日志
                       @RequestParam(value = "BASQXM", required = false) String basqxm,
                       @RequestParam(value = "BAZJHM", required = false) String bazjhm);

}
