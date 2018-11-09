package com.tm.leasewechat.controller.wz;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.wz.BasicInfoDto;
import com.tm.leasewechat.dto.wz.SignSubmitWebDto;
import com.tm.leasewechat.service.wz.WzApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by pengchao on 2018/1/5.
 */
@RestController
@RequestMapping("/apply")
public class WzApplyController {
    @Autowired
    private WzApplyService wzApplyService;


    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getUserInfo(@RequestParam(value = "code", required = true) String code){
        return wzApplyService.getUserInfo(code);
    }

    @RequestMapping(value = "/getUserInfoByOpenId", method = RequestMethod.GET)
    public ResponseEntity<Message> getUserInfoByOpenId(@RequestParam(value = "openId", required = true) String openId){
        return wzApplyService.getUserInfoByOpenId(openId);
    }

    //微众一审提交
    @RequestMapping(value = "/applySubmit", method = RequestMethod.POST)
    public ResponseEntity<Message> applySubmit(@RequestBody BasicInfoDto basicInfoDto){
        return wzApplyService.applySubmit(basicInfoDto);
    }

    /**
     * 根据身份证id获取到已经通过微众预审批记录
     * @param idCard
     * @return
     */
    @RequestMapping(value = "/getwzApplyInfoByIdCard", method = RequestMethod.GET)
    public ResponseEntity<Message> getwzApplyInfoByIdCard(String idCard){
        return wzApplyService.getwzApplyInfoByIdCard(idCard);
    }

    /**
     * 电子签约查询
     * @return
     */
    @RequestMapping(value = "/signSearch", method = RequestMethod.GET)
    public ResponseEntity<Message> signSearch(String openId, String applyNum){
        return wzApplyService.signSearch(openId, applyNum);
    }


    /**
     * 获取APP用户基本信息
     * @return
     */
    @RequestMapping(value = "/searchUserBasicInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> searchAppUserBasicInfo(String uniqueMark){
        return wzApplyService.searchAppUserBasicInfo(uniqueMark);
    }

    /**
     * 电子签约提交
     * @return
     */
    @RequestMapping(value = "/signSubmit", method = RequestMethod.POST)
    public ResponseEntity<Message> signSubmit(@RequestBody SignSubmitWebDto signSubmitWebDto){
        return wzApplyService.signSubmit(signSubmitWebDto);
    }


    /**
     * 一审、二审结果查询
     * @return
     */
    @RequestMapping(value = "/stateSearch", method = RequestMethod.GET)
    public ResponseEntity<Message> stateSearch(String uniqueMark, String applyNum){
        return wzApplyService.stateSearch(uniqueMark, applyNum);
    }

    /**
     * 还款卡变更结果结果查询
     * @return
     */
    @RequestMapping(value = "/repayCardSearch", method = RequestMethod.GET)
    public ResponseEntity<Message> repayCardSearch(String applyNum){
        return wzApplyService.repayCardSearch(applyNum);
    }

    /**
     * 获取银行列表
     * @return
     */
    @RequestMapping(value = "/getBankList", method = RequestMethod.GET)
    public ResponseEntity<Message> getBankList(){
        return wzApplyService.getBankList();
    }


    /**
     * 获取银行
     * @return
     */
    @RequestMapping(value = "/getBank", method = RequestMethod.GET)
    public ResponseEntity<Message> getBank(String bankCardNum){
        return wzApplyService.getBank(bankCardNum);
    }


    /**
     * 获取订单列表
     * @return
     */
    @RequestMapping(value = "/getApplyList", method = RequestMethod.GET)
    public ResponseEntity<Message> getApplyList(){
        return wzApplyService.getApplyList();
    }

    /**
     * 获取在线助力融列表
     * @return
     */
//    @RequestMapping(value = "/getApplyInfoList", method = RequestMethod.POST)
//    public ResponseEntity<Message> getApplyInfoList(@RequestBody List<String> uniqueMarkList){
//        return wzApplyService.getApplyInfoList(uniqueMarkList);
//    }

}
