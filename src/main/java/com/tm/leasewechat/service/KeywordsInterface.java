package com.tm.leasewechat.service;

import com.tm.leasewechat.dto.message.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/11/11.
 */
@FeignClient(name = "keywordsInterface", url = "${request.serverUrl}")
public interface KeywordsInterface {

    @RequestMapping(value = "/keywordsReply/keywordsReplyList", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getKeywords(@RequestHeader("authorization") String auth);

    @RequestMapping(value = "/keywordsReply/matchKeywords/{keyword}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getMatchKeywords(@PathVariable("keyword") String keyword);
}
