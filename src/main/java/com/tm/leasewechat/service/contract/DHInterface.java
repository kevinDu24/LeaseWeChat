package com.tm.leasewechat.service.contract;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/10/18.
 */
//@FeignClient(name = "dHInterface", url = "http://116.228.224.58:8077/TMDH/app")
@FeignClient(name = "dHInterface", url = "http://116.228.224.60:8080/TMDH/app")
public interface DHInterface {

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String payAheadQuery(@RequestParam(value = ".url", required = false) String url,
                   @RequestParam(value = "operator", required = false) String operator,
                   @RequestParam(value = "sign", required = false) String sign,
                   @RequestParam(value = "timestamp", required = false) String timestamp,
                   @RequestParam(value = "bakhxm", required = false) String bakhxm,
                    @RequestParam(value = "bazjhm", required = false) String bazjhm,
                    @RequestParam(value = "bahthm", required = false) String bahthm);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String payAhead(@RequestParam(value = ".url", required = false) String url,
                   @RequestParam(value = "operator", required = false) String operator,
                   @RequestParam(value = "sign", required = false) String sign,
                   @RequestParam(value = "timestamp", required = false) String timestamp,
                   @RequestParam(value = "bakhxm", required = false) String bakhxm,
                   @RequestParam(value = "bazjhm", required = false) String bazjhm,
                   @RequestParam(value = "bahthm", required = false) String bahthm,
                    @RequestParam(value = "basjhm", required = false) String basjhm,
                   @RequestParam(value = "tqhkje", required = false) String tqhkje,
                   @RequestParam(value = "basybj", required = false) String basybj,
                    @RequestParam(value = "badylx", required = false) String badylx,
                   @RequestParam(value = "basxfy", required = false) String basxfy,
                   @RequestParam(value = "bawyjf", required = false) String bawyjf,
                    @RequestParam(value = "babzjf", required = false) String babzjf,
                   @RequestParam(value = "wthbxf", required = false) String wthbxf,
                   @RequestParam(value = "tqhkrq", required = false) String tqhkrq);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getRepayCard(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "operator", required = false) String operator,
                        @RequestParam(value = "sign", required = false) String sign,
                        @RequestParam(value = "timestamp", required = false) String timestamp,
                        @RequestParam(value = "bakhxm", required = false) String bakhxm,
                        @RequestParam(value = "bazjhm", required = false) String bazjhm,
                        @RequestParam(value = "bahthm", required = false) String bahthm);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String changeRepayCard(@RequestParam(value = ".url", required = false) String url,
                           @RequestParam(value = "operator", required = false) String operator,
                           @RequestParam(value = "sign", required = false) String sign,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "bakhyh", required = false) String bakhyh,
                           @RequestParam(value = "bakhkh", required = false) String bakhkh,
                           @RequestParam(value = "khzmfj", required = false) String khzmfj,
                           @RequestParam(value = "khfmfj", required = false) String khfmfj,
                           @RequestParam(value = "bahthm", required = false) String bahthm);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String payAheadCheck(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "operator", required = false) String operator,
                        @RequestParam(value = "sign", required = false) String sign,
                        @RequestParam(value = "timestamp", required = false) String timestamp,
                        @RequestParam(value = "khsfzhm", required = false) String khsfzhm,
                        @RequestParam(value = "khxm", required = false) String khxm);

}
