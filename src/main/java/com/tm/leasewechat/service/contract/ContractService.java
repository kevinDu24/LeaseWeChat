package com.tm.leasewechat.service.contract;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tm.leasewechat.dao.RepaycardchangerecordRepository;
import com.tm.leasewechat.dao.SysUserRepository;
import com.tm.leasewechat.domain.RepaycardChangeRecord;
import com.tm.leasewechat.domain.SysUser;
import com.tm.leasewechat.dto.contract.*;
import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.message.MessageType;
import com.tm.leasewechat.dto.result.CommonResultDto;
import com.tm.leasewechat.consts.GlobalConsts;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by LEO on 16/9/27.
 */
@Service
public class ContractService {

    @Autowired
    private ContractInterface contractInterface;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Gson gson;

    @Autowired
    private DHInterface dhInterface;

    @Autowired
    private RepaycardchangerecordRepository repaycardchangerecordRepository;

    /**
     * 个人合同列表查询
     * @param user
     * @return
     */
    public ResponseEntity<Message> getContracts(Principal user){
        SysUser sysUser = sysUserRepository.findByCardId(user.getName());
        String result = contractInterface.wxQuery("queryRepaymentInfo", "admin", GlobalConsts.SIGN.value(),
                GlobalConsts.TIMESTAMP.value(), sysUser.getName(), sysUser.getCardId(), null);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, stringToObject(result).getRepaymentInfoList()), HttpStatus.OK);
    }

    /**
     * 提前还款资格查询
     * @param user
     * @return
     */
    public ResponseEntity<Message> payAheadCheck(Principal user){
        SysUser sysUser = sysUserRepository.findByCardId(user.getName());
        String result = dhInterface.payAheadCheck("earlyRepaymentCheck", "admin", GlobalConsts.SIGN.value(),
                GlobalConsts.TIMESTAMP.value(), sysUser.getCardId(), sysUser.getName());
        PayAheadCheckResultDto payAheadCheckResultDto = null;
        try {
            payAheadCheckResultDto = objectMapper.readValue(result, PayAheadCheckResultDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<PayAheadCheckDto> earlyRepayReport = payAheadCheckResultDto.getEarlyRepayReport();
        String resultFlag = "0";
        if (earlyRepayReport != null) {
            for (PayAheadCheckDto item : earlyRepayReport) {
                if ("1".equals(item.getSczt()) || "1".equals(item.getSszt()) || "1".equals(item.getYqzt())) {
                    resultFlag = "1";
                    break;
                }
            }
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultFlag), HttpStatus.OK);
    }

    /**
     * 提前还款查询
     * @param contractId
     * @param user
     * @return
     */
    public ResponseEntity<Message> payAheadQuery(String contractId, Principal user){
        SysUser sysUser = sysUserRepository.findByCardId(user.getName());
        String result = dhInterface.payAheadQuery("earlyRepaymentQuery", "admin", GlobalConsts.SIGN.value(),
                GlobalConsts.TIMESTAMP.value(), sysUser.getName(), sysUser.getCardId(), contractId);
        CommonResultDto commonResultDto = gson.fromJson(result, CommonResultDto.class);
        if(commonResultDto.getResult().getIsSuccess()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, commonResultDto.getEarlyRepaymentInfo()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, commonResultDto.getResult().getResultMsg()), HttpStatus.OK);
    }

    /**
     * 提前还款
     * @param payAheadRecDto
     * @param user
     * @return
     */
    public ResponseEntity<Message> payAhead(PayAheadRecDto payAheadRecDto, Principal user){
        SysUser sysUser = sysUserRepository.findByCardId(user.getName());
        String result = dhInterface.payAhead("earlyRepaymentApplication", "admin", GlobalConsts.SIGN.value(), GlobalConsts.TIMESTAMP.value(),
                sysUser.getName(), sysUser.getCardId(), payAheadRecDto.getContractId(), sysUser.getPhoneNum(),
                payAheadRecDto.getRepayMoney(), payAheadRecDto.getRemainMoney(), payAheadRecDto.getMonthInterest(),
                payAheadRecDto.getCommission(), payAheadRecDto.getBreach(), payAheadRecDto.getDeposit().equals("") ? " " : payAheadRecDto.getDeposit(), payAheadRecDto.getInsurance(),
                payAheadRecDto.getDate());
        CommonResultDto commonResultDto = gson.fromJson(result, CommonResultDto.class);
        if(commonResultDto.getResult().getIsSuccess()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, commonResultDto.getResult().getResultMsg()), HttpStatus.OK);
    }

    /**
     * 查询合同审批日志
     * @param user
     * @return
     */
    public ResponseEntity<Message> getContractApproveLog(String applyNum, Principal user){
        CommonObject commonObject = getContractMsg(applyNum, user, "queryContractState");
        Map map = (Map) commonObject.getContractinfo();
        if(null == map){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, Lists.newArrayList()), HttpStatus.OK);
        }
        List<Map> list = (List<Map>) map.get("contractstatelist");
        Collections.sort(list, (arg0, arg1) -> Long.parseLong(arg0.get("BASHRQ").toString()) > Long.parseLong(arg1.get("BASHRQ").toString()) ? 1 : -1);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, commonObject.getContractinfo()), HttpStatus.OK);
    }

    /**
     * 个人合同信息查询
     * @param user
     * @param url
     * @return
     */
    public CommonObject getContractMsg(String applyNum, Principal user, String url){
        SysUser sysUser = sysUserRepository.findByCardId(user.getName());
        String result = contractInterface.query(url, "admin", GlobalConsts.TIMESTAMP.value(),
                GlobalConsts.SIGN.value(), applyNum, sysUser.getName(), sysUser.getCardId());
        return stringToObject(result);
    }



    /**
     * 微众还款计划表
     * @return
     */
    public CommonObject getWzContractMsg(String applyNum){
        String result = contractInterface.query("queryContractRepayplan", "admin", GlobalConsts.TIMESTAMP.value(),
                GlobalConsts.SIGN.value(), applyNum, null, null);
        return stringToObject(result);
    }

    /**
     * 个人还款卡查询
     * @param contractId
     * @param user
     * @return
     */
    public ResponseEntity<Message> getRepayCard(String contractId, Principal user){
        SysUser sysUser = sysUserRepository.findByCardId(user.getName());
        String result = dhInterface.getRepayCard("repaymentCardChangeQuery", "admin", GlobalConsts.SIGN.value(),
                GlobalConsts.TIMESTAMP.value(), sysUser.getName(), sysUser.getCardId(), contractId);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, stringToObject(result).getRepaymentCardInfo()), HttpStatus.OK);
    }

    /**
     * 还款卡变更
     * @param changeRepayCardRecDto
     * @return
     */
    public ResponseEntity<Message> changeRepayCard(ChangeRepayCardRecDto changeRepayCardRecDto){
        RepaycardChangeRecord repaycardChangeRecord = new RepaycardChangeRecord(changeRepayCardRecDto.getContractId(),
                changeRepayCardRecDto.getBankMsg(), changeRepayCardRecDto.getBankCard(),
                changeRepayCardRecDto.getFrontMsg(), changeRepayCardRecDto.getBehindMsg());
        repaycardchangerecordRepository.save(repaycardChangeRecord);
        String result = dhInterface.changeRepayCard("repaymentCardChange", "admin", GlobalConsts.SIGN.value(),
                GlobalConsts.TIMESTAMP.value(), changeRepayCardRecDto.getBankMsg(), changeRepayCardRecDto.getBankCard(),
                changeRepayCardRecDto.getFrontMsg(), changeRepayCardRecDto.getBehindMsg(), changeRepayCardRecDto.getContractId());
        CommonResultDto commonResultDto = gson.fromJson(result, CommonResultDto.class);
        if(commonResultDto.getResult().getIsSuccess()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, commonResultDto.getResult().getResultMsg()), HttpStatus.OK);
    }

    /**
     * 还款卡变更消息通知
     * @param contractNum
     * @param cardId
     * @param result
     * @return
     */
    public WxMpXmlOutTextMessage repayCardNotice(String contractNum, String cardId, String result){
        RepaycardChangeRecord repaycardChangeRecord = repaycardchangerecordRepository.findByContractNum(contractNum);
        SysUser sysUser = sysUserRepository.findByCardId(cardId);
        repaycardChangeRecord.setCardId(cardId);
        repaycardChangeRecord.setName(sysUser.getName());
        repaycardchangerecordRepository.save(repaycardChangeRecord);
        String openId = sysUserRepository.findByCardId(cardId).getOpenId();
        WxMpXmlOutTextMessage textMessage = new WxMpXmlOutTextMessage();
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setToUserName(openId);
        textMessage.setFromUserName("xftm4000218888");
        textMessage.setMsgType("text");
        textMessage.setContent(result + " 合同编号：" + contractNum);
        return textMessage;
    }

    /**
     * 字符串转对象
     * @param result
     * @return
     */
    public CommonObject stringToObject(String result){
        CommonObject commonObject = null;
        try {
            commonObject = objectMapper.readValue(result, CommonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commonObject;
    }
}
