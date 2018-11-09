package com.tm.leasewechat.service.wz;

import com.tm.leasewechat.dto.wz.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengchao on 2018/1/8.
 */
//@FeignClient(name = "contract", url = "http://116.236.234.246:8077/XFTM_ZL")
@FeignClient(name = "contract", url = "http://happyleasing.cn/TMZL")
public interface WzApplyInterface {

    @RequestMapping(value = "/WZHttpReturnRep", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String applySubmit(@RequestBody FirstApplyDto firstApplyDto);

    @RequestMapping(value = "/WZHttpReturnRep", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String signSubmit(@RequestBody SignSubmitDto signSubmitDto);

    @RequestMapping(value = "/WZHttpReturnRep", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String changeRepayCardSubmit(@RequestBody ChangeRepayCard changeRepayCard);

    @RequestMapping(value = "/WZHttpReturnRep", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String querySign(@RequestBody SignQueryDto signQueryDto);

    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryAddressInfo(@RequestParam(value = ".url", required = false) String url,
                            @RequestBody SignQueryDto signQueryDto);

    //发送短信接口
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String SendShortMessage(@RequestParam(value = ".url", required = false) String url,
                            @RequestBody SendShortMessageDto sendShortMessageDto);

}
