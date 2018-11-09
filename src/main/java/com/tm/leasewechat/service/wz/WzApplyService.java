package com.tm.leasewechat.service.wz;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.leasewechat.config.WzProperties;
import com.tm.leasewechat.dao.*;
import com.tm.leasewechat.domain.*;
import com.tm.leasewechat.dto.customer.SysUserDto;
import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.message.MessageType;
import com.tm.leasewechat.dto.result.CommonResultDto;
import com.tm.leasewechat.dto.wz.*;
import com.tm.leasewechat.dto.wz.result.*;
import com.tm.leasewechat.service.TMWeChatInterface;
import com.tm.leasewechat.service.wechat.WeChatService;
import com.tm.leasewechat.utils.CommonUtils;
import com.tm.leasewechat.utils.DateUtils;
import com.tm.leasewechat.utils.IPAddressUtils;
import com.tm.leasewechat.consts.ApplyType;
import com.tm.leasewechat.consts.ContractType;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by pengchao on 2018/1/5.
 */
@Service
public class WzApplyService {

    @Autowired
    private WzSysUserRepository wzSysUserRepository;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private ApplyInfoRepository applyInfoRepository;

    @Autowired
    private WzProperties wzProperties;

    @Autowired
    private WzApplyInterface wzApplyInterface;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private ContractInfoRepository contractInfoRepository;

    @Autowired
    private WzRepayCardChangeRecordRepository wzRepayCardChangeRecordRepository;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private AppUserBasicInfoRepository appUserBasicInfoRepository;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private TMWeChatInterface tmWeChatInterface;


    private static final Logger logger = LoggerFactory.getLogger(WzApplyService.class);


    //根据code获取用户openId，并判断该openId是否过绑定手机号
    public ResponseEntity<Message> getUserInfo(String code){
        String openId = weChatService.getOpenId(code);
        if(openId == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请关闭微信页面重新进入,如无法解决,请联系客服"), HttpStatus.OK);
        }
        WzSysUser wzSysUser = wzSysUserRepository.findByOpenId(openId);
        if (wzSysUser == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "1"), HttpStatus.OK);
        }
        SysUserDto sysUserDto = new SysUserDto();
        BeanUtils.copyProperties(wzSysUser, sysUserDto);
        sysUserDto.setCode("wz20d38ca487094ed0b22c97bd855d507d1");
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, sysUserDto), HttpStatus.OK);
    }


    public ResponseEntity<Message> getUserInfoByOpenId(String openId){
        WzSysUser wzSysUser = wzSysUserRepository.findByOpenId(openId);
        if (wzSysUser == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "1"), HttpStatus.OK);
        }
        SysUserDto sysUserDto = new SysUserDto();
        BeanUtils.copyProperties(wzSysUser, sysUserDto);
        sysUserDto.setCode("wz20d38ca487094ed0b22c97bd855d507d1");
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, sysUserDto), HttpStatus.OK);
    }

    //微众一审提交
    public ResponseEntity<Message> applySubmit(BasicInfoDto basicInfoDto){
        logger.info("BasicInfoDto={}", JSONObject.fromObject(basicInfoDto).toString(2));
        String locationData = basicInfoDto.getLocationData();
        //经纬度位置互换
        if(!CommonUtils.isNull(locationData)){
            String[] strings = locationData.split(",");
            if(strings.length == 2){
                String lat = strings[0];
                String lon = strings[1];
                basicInfoDto.setLocationData(lon + "," + lat);
            }
        }
        if (CommonUtils.isNull(basicInfoDto.getIp())){
            String ip = IPAddressUtils.getIpAddress(httpServletRequest);
            if(ip == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取ip地址失败"), HttpStatus.OK);
            }
            basicInfoDto.setIp(ip);
        }
        String bankCardNum = basicInfoDto.getBankCardNum(); //获取银行卡号
        Bank bank = checkCardNum(bankCardNum);
        if(bank == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "银行卡号输入有误，请仔细核验"), HttpStatus.OK);
        } else if(!bank.getName().equals(basicInfoDto.getBank())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "您输入的卡号和银行名称不一致，请仔细核验"), HttpStatus.OK);
        }
        //APP推送过来的单子有唯一标识
        String uniqueMark = basicInfoDto.getUniqueMark();
        if(CommonUtils.isNull(basicInfoDto.getUniqueMark())){
            uniqueMark = UUID.randomUUID().toString().replace("-", "");
        }
        String message = applyCheck(basicInfoDto);
        if(!"".equals(message)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        WzSysUser wzSysUser = wzSysUserRepository.findByOpenId(basicInfoDto.getOpenId());

        //提交到主系统
        FirstApplyDto firstApplyDto = buildFirstApplyDto(basicInfoDto);//提交参数构建
        firstApplyDto.setWX_UNION_ID(wzSysUser.getOpenId());//测试号无unionId，暂由openid
//        firstApplyDto.setWX_UNION_ID(wzSysUser.getUnionId());//union无构造方法
        firstApplyDto.setWXID(uniqueMark);//wxId无构造方法
        WzResultCommonDto wzResultCommonDto = new WzResultCommonDto();
//        System.out.println(JSONObject.fromObject(firstApplyDto).toString(2));
        logger.info("firstApplyDto={}", JSONObject.fromObject(firstApplyDto).toString(2));
        try {
            //提交一审信息到主系统
            String result = wzApplyInterface.applySubmit(firstApplyDto);
            logger.info("applyResult={}", result);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            wzResultCommonDto = objectMapper.readValue(result, WzResultCommonDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交申请失败"), HttpStatus.OK);
        }

        //更新申请状态
        if("0000".equals(wzResultCommonDto.getCode())){
            // 申请信息保存到本地DB中
            ApplyInfo applyInfo = new ApplyInfo();
            BeanUtils.copyProperties(basicInfoDto, applyInfo);
            applyInfo.setIdCard(basicInfoDto.getIdCard().toUpperCase());
            applyInfo.setIdType(CommonUtils.idType);
            applyInfo.setUniqueMark(uniqueMark);
            applyInfo.setSignStatus(ContractType.NO_SIGN.code());
            applyInfo.setStatus(ApplyType.NEW.code());
            applyInfoRepository.save(applyInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }

        //
        if(wzResultCommonDto.getMessage().contains("【一审】已经通过")){
            String FpName = "0000";

            // 在微信中查找通过一审的记录，如果没有找到则在太盟宝去查找 -- 2018/9/13 18:30 By ChengQiChuan
            // 一审通过在微信端是 3，太盟宝APP 是 2
            ApplyInfo wzApplyInfo = applyInfoRepository.findTop1ByIdCardAndStatusAndCreateTimeAfterOrderByCreateTimeDesc(basicInfoDto.getIdCard(),
                    3+"", DateUtils.getBeforeDateByDay(new Date(),-30));
            if (wzApplyInfo != null){
                wzResultCommonDto.setMessage("！您当前【一审】已经在 (微信:"+wzApplyInfo.getFpName()+") 处通过，不能重复提交。感谢您的申请");
            }else{
                //调用接口查找太盟宝APP中通过微众预审批的记录
                ResponseEntity<Message> responseEntity = tmWeChatInterface.getwzApplyInfoByIdCard(basicInfoDto.getIdCard());
                //强制转换类型
                HashMap hashMap = (HashMap) responseEntity.getBody().getData();
                if(hashMap != null && !hashMap.isEmpty()){
                    wzResultCommonDto.setMessage("！您当前【一审】已经在 (太盟宝APP:"+hashMap.get("fpName").toString()+") 处通过，不能重复提交。感谢您的申请");
                }
            }

        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, wzResultCommonDto.getMessage()), HttpStatus.OK);
    }

    //一审提交验证
    public String applyCheck(BasicInfoDto basicInfoDto){
        String message = "";
        String phoneNum = basicInfoDto.getPhoneNum();
        String openId = basicInfoDto.getOpenId();
        WzSysUser sysUser = wzSysUserRepository.findByOpenId(openId);
        if(sysUser == null){
            message = "您的微信号暂未绑定【HPL太盟融资租赁】公众号，请先进入公众号完成绑定再录单！";
            return message;
        }
        if(phoneNum == null || phoneNum.isEmpty()){
            message = "手机号不可为空";
        }
//        //手机号必须是绑定的手机号
//        if (!phoneNum.equals(sysUser.getPhoneNum())){
//            message = "该手机号与您绑定的手机号不一致，无法申请";
//        }
        return message;
    }

    //换款卡变更提交验证
    public String changeRepayCardCheck(ChangeRepayCardWebDto changeRepayCardWebDto){
        String message = "";
        String phoneNum = changeRepayCardWebDto.getPhoneNum();
        if(phoneNum == null || phoneNum.isEmpty()){
            message = "银行预留手机号手机号不可为空";
        }
        return message;
    }



    //微众审批结果推送
    public ResponseEntity<Message> receiveApplyResult(ApplyResultDto applyResultDto){
        logger.info("applyResultDto={}", applyResultDto);
        ApplyInfo applyInfo = applyInfoRepository.findByUniqueMark(applyResultDto.getUniqueMark());
        //通过主系统申请编号查询
        if(applyInfo==null && applyResultDto.getApplyNum() != null && !"".equals(applyResultDto.getApplyNum().trim())){
            applyInfo = applyInfoRepository.findByApplyNum(applyResultDto.getApplyNum());
        }
        if(applyInfo != null){
            WzSysUser wzSysUser = wzSysUserRepository.findByOpenId(applyInfo.getOpenId());
            if (wzSysUser == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "推送失败，未查询到该用户信息"), HttpStatus.OK);
            }
            applyInfo.setApplyNum(applyResultDto.getApplyNum());
            applyInfo.setStatus(applyResultDto.getType());
            applyInfo.setReason(applyResultDto.getResultReason());
            applyInfo.setUpdateTime(new Date());
            applyInfoRepository.save(applyInfo);
            return weChatService.sendApplyResult(applyInfo, wzSysUser.getOpenId());
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "审批结果推送失败,未查询到订单信息"), HttpStatus.OK);
    }



    //微众电子签约推送
    @Transactional
    public ResponseEntity<Message> receiveContractSign(ContractSignDto contractSignDto){
        logger.info("contractSignDto={}", contractSignDto);
        ContractInfo contractInfo = new ContractInfo();
        ApplyInfo applyInfo = null;
        if(contractSignDto.getApplyNum() != null && !"".equals(contractSignDto.getApplyNum().trim())){
             applyInfo = applyInfoRepository.findByApplyNum(contractSignDto.getApplyNum());
        }
        if(applyInfo != null){
            WzSysUser wzSysUser = wzSysUserRepository.findByOpenId(applyInfo.getOpenId());
            if (wzSysUser == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "推送失败，未查询到该用户信息"), HttpStatus.OK);
            }
            applyInfo.setUpdateTime(new Date());
            applyInfo.setSignStatus(ContractType.WAIT_SIGN.code());
            applyInfoRepository.save(applyInfo);
            BeanUtils.copyProperties(contractSignDto, contractInfo);
            contractInfo.setCreateTime(new Date());
            contractInfoRepository.save(contractInfo);
            return weChatService.sendContractSign(contractInfo, wzSysUser.getOpenId());
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询订单信息，请至太盟宝进行签约"), HttpStatus.OK);
    }

    //微众还款卡变更推送
    public ResponseEntity<Message> receiveChangeRepayCard(ChangeRepayCardResultDto changeRepayCardResultDto){
        logger.info("changeRepayCardResultDto={}", changeRepayCardResultDto);
        ApplyInfo applyInfo = null;
        if(changeRepayCardResultDto.getApplyNum() != null && !"".equals(changeRepayCardResultDto.getApplyNum().trim())){
            applyInfo = applyInfoRepository.findByApplyNum(changeRepayCardResultDto.getApplyNum());
        }
        //通过主系统申请编号查询
        if(applyInfo != null){
            WzSysUser wzSysUser = wzSysUserRepository.findByOpenId(applyInfo.getOpenId());
            if (wzSysUser == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "推送失败，未查询到该用户信息"), HttpStatus.OK);
            }
            return weChatService.sendChangeRepayCardResult(changeRepayCardResultDto, wzSysUser.getOpenId());
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "改绑卡推送失败,未查询到订单信息"), HttpStatus.OK);
    }

    /**
     * 电子签约查询
     * @return
     */
    public ResponseEntity<Message> signSearch(String openId, String applyNum){
        ApplyInfo applyInfo = null;
//        if(applyNum == null || applyNum.isEmpty()){
//            applyInfo = applyInfoRepository.findTop1ByOpenIdAndSignStatusOrderByCreateTimeDesc(openId, ContractType.WAIT_SIGN.code());
//        }else {
            applyInfo = applyInfoRepository.findByApplyNumAndSignStatus(applyNum, ContractType.WAIT_SIGN.code());
//        }
        if(applyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查到需要签约的单子"), HttpStatus.OK);
        }
        ContractInfo contractInfo = contractInfoRepository.findTop1ByApplyNumOrderByCreateTimeDesc(applyInfo.getApplyNum());
        if(contractInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查到需要签约的单子"), HttpStatus.OK);
        }
        ContractSignDto contractSignDto = new ContractSignDto();
        BeanUtils.copyProperties(contractInfo, contractSignDto);
        contractSignDto.setPhoneNum(applyInfo.getPhoneNum());
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, contractSignDto), HttpStatus.OK);
    }


    /**
     * app四要素信息查询
     * @return
     */
    public ResponseEntity<Message> searchAppUserBasicInfo(String uniqueMark){
        AppUserBasicInfo appUserBasicInfo = appUserBasicInfoRepository.findByUniqueMark(uniqueMark);
        if(appUserBasicInfo == null){
            appUserBasicInfo = new AppUserBasicInfo();
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, appUserBasicInfo), HttpStatus.OK);
    }

    /**
     * 电子签约提交
     * @return
     */
    public ResponseEntity<Message> signSubmit(SignSubmitWebDto signSubmitWebDto){
        logger.info("SignSubmitWebDto={}", JSONObject.fromObject(signSubmitWebDto).toString(2));
        //若前端获取不到ip，后台获取
        if (CommonUtils.isNull(signSubmitWebDto.getIp())){
            String ip = IPAddressUtils.getIpAddress(httpServletRequest);
            if(ip == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取ip地址失败"), HttpStatus.OK);
            }
            signSubmitWebDto.setIp(ip);
        }
        String locationData = signSubmitWebDto.getLocationData();
        //经纬度位置互换
        if(!CommonUtils.isNull(locationData)){
            String[] strings = locationData.split(",");
            if(strings.length == 2){
                String lat = strings[0];
                String lon = strings[1];
                signSubmitWebDto.setLocationData(lon + "," + lat);
            }
        }
        ApplyInfo applyInfo = applyInfoRepository.findByApplyNum(signSubmitWebDto.getApplyNum());
        if(applyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "申请编号不存在"), HttpStatus.OK);
        }
        SignSubmitDto dto = buildDto(signSubmitWebDto); //提交参数构建
        WzSysUser wzSysUser = wzSysUserRepository.findByOpenId(applyInfo.getOpenId());
        if(wzSysUser == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该用户不存在"), HttpStatus.OK);
        }
        dto.setWX_OPENID(wzSysUser.getOpenId()); //微信号openId
//        dto.setWX_UNION_ID(wzSysUser.getUnionId());//微信号unionId
        dto.setWX_UNION_ID(wzSysUser.getOpenId()); //微信号unionId
        WzResultCommonDto wzResultCommonDto = new WzResultCommonDto();
        try {
            //电子签约提交
            logger.info("SignSubmitDto={}",JSONObject.fromObject(dto).toString(2));
            String result = wzApplyInterface.signSubmit(dto);
            logger.info("SignSubmitResult={}", result);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            wzResultCommonDto = objectMapper.readValue(result, WzResultCommonDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签约失败"), HttpStatus.OK);
        }
        //更新申请状态
        if("0000".equals(wzResultCommonDto.getCode())){
            applyInfo.setSignStatus(ContractType.SIGNED.code());
            applyInfo.setSignDate(wzResultCommonDto.getData().toString());
            applyInfoRepository.save(applyInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, wzResultCommonDto.getMessage()), HttpStatus.OK);
    }

    private SignSubmitDto buildDto(SignSubmitWebDto signSubmitWebDto) {
        SignSubmitDto result = new SignSubmitDto(signSubmitWebDto, wzProperties);
        return result;
    }

    private FirstApplyDto buildFirstApplyDto(BasicInfoDto basicInfoDto) {
        FirstApplyDto result = new FirstApplyDto(basicInfoDto, wzProperties);
        return result;
    }

    private ChangeRepayCard buildChangeRepayCard(ChangeRepayCardWebDto changeRepayCardWebDto) {
        ChangeRepayCard result = new ChangeRepayCard(changeRepayCardWebDto, wzProperties);
        return result;
    }

    //还款卡变更提交
    public ResponseEntity<Message> changeRepayCardSubmit(ChangeRepayCardWebDto changeRepayCardWebDto, String username){
        logger.info("ChangeRepayCardWebDto={}", JSONObject.fromObject(changeRepayCardWebDto).toString(2));
        String locationData = changeRepayCardWebDto.getLocationData();
        //经纬度位置互换
        if(!CommonUtils.isNull(locationData)){
            String[] strings = locationData.split(",");
            if(strings.length == 2){
                String lat = strings[0];
                String lon = strings[1];
                changeRepayCardWebDto.setLocationData(lon + "," + lat);
            }
        }
        //若前端获取不到ip，后台获取
        if (CommonUtils.isNull(changeRepayCardWebDto.getIp())){
            String ip = IPAddressUtils.getIpAddress(httpServletRequest);
            if(ip == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取ip地址失败"), HttpStatus.OK);
            }
            changeRepayCardWebDto.setIp(ip);
        }
        String bankCardNum = changeRepayCardWebDto.getBankCardNum(); //获取银行卡号
        Bank bank = checkCardNum(bankCardNum);
        if(bank == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "银行卡号输入有误，请仔细核验"), HttpStatus.OK);
        }
        String message = changeRepayCardCheck(changeRepayCardWebDto);
        if(!"".equals(message)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        SysUser sysUser = sysUserRepository.findByCardId(username);

        //提交到主系统
        ChangeRepayCard changeRepayCard = buildChangeRepayCard(changeRepayCardWebDto);//提交参数构建
        changeRepayCard.setWX_UNION_ID(sysUser.getUnionId()!= null ? sysUser.getUnionId() : sysUser.getOpenId());//union无构造方法
        changeRepayCard.setWX_OPENID(sysUser.getOpenId());
        changeRepayCard.setID_NO(username);
        changeRepayCard.setNAME(sysUser.getName());
        WzResultCommonDto wzResultCommonDto = new WzResultCommonDto();
        logger.info("changeRepayCard={}", JSONObject.fromObject(changeRepayCard).toString(2));
        try {
            String result = wzApplyInterface.changeRepayCardSubmit(changeRepayCard);
            logger.info("changeRepayCardResult={}", result);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            wzResultCommonDto = objectMapper.readValue(result, WzResultCommonDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交申请失败"), HttpStatus.OK);
        }
        //保存数据
        if("0000".equals(wzResultCommonDto.getCode())){
            WzRepayCardChangeRecord wzRepayCardChangeRecord = new WzRepayCardChangeRecord(changeRepayCard);
            wzRepayCardChangeRecord.setMessage(wzResultCommonDto.getCode());
            wzRepayCardChangeRecordRepository.save(wzRepayCardChangeRecord);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        WzRepayCardChangeRecord wzRepayCardChangeRecord = new WzRepayCardChangeRecord(changeRepayCard);
        wzRepayCardChangeRecord.setMessage(wzResultCommonDto.getMessage());
        wzRepayCardChangeRecordRepository.save(wzRepayCardChangeRecord);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, wzResultCommonDto.getMessage()), HttpStatus.OK);
    }

    /**
     * 一审、二审结果查询
     * @return
     */
    public ResponseEntity<Message> stateSearch(String uniqueMark, String applyNum){
        ApplyInfo applyInfo;
        if(uniqueMark != null && !uniqueMark.isEmpty()){
            applyInfo = applyInfoRepository.findByUniqueMark(uniqueMark);
        } else if (applyNum != null && !applyNum.isEmpty()){
            applyInfo = applyInfoRepository.findByApplyNum(applyNum);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查到您的融资申请，请联系客服"), HttpStatus.OK);
        }
        if(applyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查到您的融资申请，请联系客服"), HttpStatus.OK);
        }
        if(ApplyType.CANCEL.code().equals(applyInfo.getStatus())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "您的申请已处于终止状态，请联系经销商重新申请"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, applyInfo), HttpStatus.OK);
    }




    /**
     * 还款卡变更结果查询
     * @return
     */
    public ResponseEntity<Message> repayCardSearch(String applyNum){
        WzRepayCardChangeRecord wzRepayCardChangeRecord;
        if(applyNum != null && !applyNum.isEmpty()){
            wzRepayCardChangeRecord = wzRepayCardChangeRecordRepository.findTop1ByApplyNumOrderByUpdateTimeDesc(applyNum);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查到您的还款卡变更消息"), HttpStatus.OK);
        }
        if(wzRepayCardChangeRecord == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查到您的还款卡变更消息"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, wzRepayCardChangeRecord), HttpStatus.OK);
    }

    /**
     * 获取银行列表
     * @return
     */
    public ResponseEntity<Message> getBankList(){
        List<Object> result = bankRepository.getBankList();
        if(result == null || result.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "银行列表获取失败"), HttpStatus.OK);
        }
        List<BankListDto> resultList = new ArrayList();
        Object[] objs;
        BankListDto recoveryListDto;
        for (Object object : result) {
            objs = (Object[]) object;
            recoveryListDto = new BankListDto(objs);
            resultList.add(recoveryListDto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }

    //根据银行卡号获取银行名称
    public ResponseEntity<Message> getBank(String bankCardNum){
//        String ip = IPAddressUtils.getIpAddress(httpServletRequest);
//        System.out.println(ip);
        Bank bank = checkCardNum(bankCardNum);
        if(bank == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未找到相关银行，请检查银行卡号是否填写正确"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, bank), HttpStatus.OK);
    }


    public Bank checkCardNum(String cardNum) {
        Bank bank = null;
        if(cardNum != null && !cardNum.isEmpty()){
            for(int i=10;i>=3;i--){
                String param = cardNum.substring(0,i);
                bank = bankRepository.findByBin(param);
                if(bank != null){
                    break;
                }
            }
        }
        return bank;
    }

    /**
     * 获取申请列表
     * @return
     */
    public ResponseEntity<Message> getApplyList(){
        List<ApplyInfo> result = applyInfoRepository.findAll();
        if(result == null || result.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查到数据"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, result), HttpStatus.OK);
    }

    /**
     * 获取预审批拒绝状态列表
     * @return
     */
//    public ResponseEntity<Message> getApplyInfoList(List<String> uniqueMarkList){
//        List<ApplyInfo> result = applyInfoRepository.getApplyInfo(uniqueMarkList);
//        if(result == null || result.isEmpty()){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查到数据"), HttpStatus.OK);
//        }
//        List<FinancePreApplyResultDto> financePreApplyResultDtoList = new ArrayList<>();
//        for(ApplyInfo applyInfo : result){
//            FinancePreApplyResultDto financePreApplyResultDto = new FinancePreApplyResultDto(applyInfo);
//            financePreApplyResultDtoList.add(financePreApplyResultDto);
//        }
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, financePreApplyResultDtoList), HttpStatus.OK);
//    }


    /**
     * 发送短信接口
     *
     * @return
     */
    public ResponseEntity<Message> sendShortMessage(SendShortMessageDto sendShortMessageDto) {
        if (CommonUtils.isNull(sendShortMessageDto.getPhoneNum()) || CommonUtils.isNull(sendShortMessageDto.getText())) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        //提交到主系统
        CommonResultDto codeResult = new CommonResultDto();
        try {
            logger.info("sendShortMessageDto={}", JSONObject.fromObject(sendShortMessageDto).toString(2));
            String coreResult = wzApplyInterface.SendShortMessage("SendShortMessage", sendShortMessageDto);
            logger.info("SendShortMessage={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CommonResultDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if (codeResult.getResult().getIsSuccess()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }

    /**
     * 根据身份证id获取到已经通过微众预审批记录
     * @param idCard
     * @return
     */
    public ResponseEntity<Message> getwzApplyInfoByIdCard(String idCard) {
        // 在太盟宝中查找通过一审的记录，如果没有找到则在微信端去查找 -- 2018/9/13 18:30 By ChengQiChuan
        // 一审通过在微信端是 3，太盟宝APP 是 2
        ApplyInfo wzApplyInfo = applyInfoRepository.findTop1ByIdCardAndStatusAndCreateTimeAfterOrderByCreateTimeDesc(idCard,
                3+"", DateUtils.getBeforeDateByDay(new Date(),-30));
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, wzApplyInfo), HttpStatus.OK);
    }
}
