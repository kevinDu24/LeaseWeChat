package com.tm.leasewechat.controller;

import com.tm.leasewechat.dto.contract.ChangeRepayCardRecDto;
import com.tm.leasewechat.dto.contract.PayAheadRecDto;
import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.message.MessageType;
import com.tm.leasewechat.dto.wz.ChangeRepayCardWebDto;
import com.tm.leasewechat.service.contract.ContractService;
import com.tm.leasewechat.service.wz.WzApplyService;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by LEO on 16/9/27.
 */
@RestController
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private WzApplyService wzApplyService;

    /**
     * 个人合同列表查询
     * url:localhost:8082/contracts
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Message> getContracts(Principal user){
        return contractService.getContracts(user);
    }

    /**
     * 提前还款资格查询
     * @param user
     * @return
     */
    @RequestMapping(value = "/payAheadCheck", method = RequestMethod.GET)
    public ResponseEntity<Message> payAheadCheck(Principal user){
        return contractService.payAheadCheck(user);
    }

    /**
     * 个人合同申请进度查询
     * url:localhost:8082/contracts/3521826/log
     * @param user
     * @return
     */
    @RequestMapping(value = "/{applyNum}/log", method = RequestMethod.GET)
    public ResponseEntity<Message> getContractLog(@PathVariable String applyNum, Principal user){
        return contractService.getContractApproveLog(applyNum, user);
    }

    /**
     * 个人还款计划查询
     * url:localhost:8082/contracts/3521826/repayDetail
     * @param user
     * @return
     */
    @RequestMapping(value = "/{applyNum}/repayDetail", method = RequestMethod.GET)
    public ResponseEntity<Message> getRepayDetail(@PathVariable String applyNum, Principal user){
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, contractService.getContractMsg(applyNum, user, "queryContractRepayplan").getContractrepayplaninfo()), HttpStatus.OK);
    }


    /**
     * 微众个人还款计划查询（电子签约页跳转到还款计划表页面，只通过申请编号查询）
     * url:localhost:8082/contracts/3521826/repayDetail
     * @return
     */
    @RequestMapping(value = "/{applyNum}/wzRepayDetail", method = RequestMethod.GET)
    public ResponseEntity<Message> getRepayDetail(@PathVariable String applyNum){
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, contractService.getWzContractMsg(applyNum).getContractrepayplaninfo()), HttpStatus.OK);
    }

    /**
     * 提前还款查询
     * url:localhost:8082/contracts/65105501/payAhead
     * @param contractId
     * @param user
     * @return
     */
    @RequestMapping(value = "/{contractId}/payAhead", method = RequestMethod.GET)
    public ResponseEntity<Message> payAheadQuery(@PathVariable String contractId, Principal user){
        return contractService.payAheadQuery(contractId, user);
    }

    /**
     * 提前还款
     * @param contractId
     * @param payAheadRecDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/{contractId}/payAhead", method = RequestMethod.PUT)
    public ResponseEntity<Message> payAhead(@PathVariable String contractId, @RequestBody PayAheadRecDto payAheadRecDto, Principal user){
        payAheadRecDto.setContractId(contractId);
        return contractService.payAhead(payAheadRecDto, user);
    }

    /**
     * 个人还款卡查询
     * url:localhost:8082/contracts/65105501/repayCard
     * @param contractId
     * @param user
     * @return
     */
    @RequestMapping(value = "/{contractId}/repayCard", method = RequestMethod.GET)
    public ResponseEntity<Message> getRepayCard(@PathVariable String contractId, Principal user){
        return contractService.getRepayCard(contractId, user);
    }

    /**
     * 还款卡变更
     * @param contractId
     * @param changeRepayCardRecDto
     * @return
     */
    @RequestMapping(value = "/{contractId}/repayCard", method = RequestMethod.PUT)
    public ResponseEntity<Message> changeRepayCard(@PathVariable String contractId, @RequestBody ChangeRepayCardRecDto changeRepayCardRecDto){
        changeRepayCardRecDto.setContractId(contractId);
        return contractService.changeRepayCard(changeRepayCardRecDto);
    }

    /**
     *微众 还款卡变更提交
     * @return
     */
    @RequestMapping(value = "/changeRepayCardSubmit", method = RequestMethod.POST)
    public ResponseEntity<Message> changeRepayCardSubmit(@RequestBody ChangeRepayCardWebDto changeRepayCardWebDto, Principal user){
        return wzApplyService.changeRepayCardSubmit(changeRepayCardWebDto, user.getName());
    }

    /**
     * 还款卡变更消息通知
     * @param contractNum
     * @param cardId
     * @param result
     * @return
     */
    @RequestMapping(value = "/repayCardNotice", method = RequestMethod.POST)
    public WxMpXmlOutTextMessage repayCardNotice(@RequestParam String contractNum, @RequestParam String cardId, @RequestParam String result){
        return contractService.repayCardNotice(contractNum, cardId, result);
    }

}
