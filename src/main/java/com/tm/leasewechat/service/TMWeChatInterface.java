package com.tm.leasewechat.service;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.wz.SendMessageDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengchao on 2018/6/28.
 */
@FeignClient(name = "tmWeChatInterface", url = "${request.tmWeChatUrl}")
public interface TMWeChatInterface {
    @RequestMapping(value = "/sysUsers/addressInputSendCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> addressInputSendCode(@RequestBody SendMessageDto sendMessageDto);

    /**
     * 根据身份证id获取到微众预审批记录
     * @param idCard
     * @return
     */
    @RequestMapping(value = "/apply/getwzApplyInfoByIdCard",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getwzApplyInfoByIdCard(@RequestParam(value = "idCard") String idCard);
}
