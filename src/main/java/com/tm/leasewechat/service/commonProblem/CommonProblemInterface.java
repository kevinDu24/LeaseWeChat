package com.tm.leasewechat.service.commonProblem;

import com.tm.leasewechat.dto.message.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangbiao on 2016/11/16.
 */
@FeignClient(name = "commonProblemInterface", url = "${request.serverUrl}")
public interface CommonProblemInterface {
    @RequestMapping(value = "/commonProblems/queryProblems", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getCommonProblemList(@RequestParam(value = "page") Integer page,
                                                 @RequestParam(value = "size") Integer size);
}
