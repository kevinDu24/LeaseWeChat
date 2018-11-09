package com.tm.leasewechat.controller.wz;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.wz.AppUserBasicInfoDto;
import com.tm.leasewechat.dto.wz.ContractSignDto;
import com.tm.leasewechat.dto.wz.PushOrderDto;
import com.tm.leasewechat.dto.wz.result.ApplyResultDto;
import com.tm.leasewechat.dto.wz.result.ChangeRepayCardResultDto;
import com.tm.leasewechat.service.wechat.WeChatService;
import com.tm.leasewechat.service.wz.WzApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dys on 16/9/27.
 */
@RestController
@RequestMapping("/pushModel")
public class PushModelController {

    @Autowired
    WeChatService weChatService;

    @Autowired
    WzApplyService wzApplyService;


    /**
     * 接收TMWeChat推送的手机号
     * @return
     */
    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public ResponseEntity<Message> pushOrder(@RequestBody PushOrderDto pushOrderDto){
        return weChatService.sendTemplate(pushOrderDto);
    }

    /**
     * 接收TMWeChat推送的在线助力融用户基本信息
     * @return
     */
//    @RequestMapping(value = "/appUserInfo", method = RequestMethod.POST)
//    public ResponseEntity<Message> pushOrder(@RequestBody AppUserBasicInfoDto appUserBasicInfoDto){
//        return weChatService.sendAppUserInfo(appUserBasicInfoDto);
//    }

    /**
     * 接收主系统推送的微众审批结果
     * @return
     */
    @RequestMapping(value = "/receiveApplyResult", method = RequestMethod.POST)
    public ResponseEntity<Message> receiveApplyResult(@RequestBody ApplyResultDto applyResultDto){
        return wzApplyService.receiveApplyResult(applyResultDto);
    }



    /**
     * 接收主系统推送的微众电子签约
     * @return
     */
    @RequestMapping(value = "/receiveContractSign", method = RequestMethod.POST)
    public ResponseEntity<Message> receiveContractSign(@RequestBody ContractSignDto contractSignDto){
        return wzApplyService.receiveContractSign(contractSignDto);
    }



    /**
     * 接收主系统推送的还款卡变更结果
     * @return
     */
    @RequestMapping(value = "/receiveChangeRepayCardResult", method = RequestMethod.POST)
    public ResponseEntity<Message> receiveChangeRepayCard(@RequestBody ChangeRepayCardResultDto changeRepayCardResultDto){
        return wzApplyService.receiveChangeRepayCard(changeRepayCardResultDto);
    }


}
