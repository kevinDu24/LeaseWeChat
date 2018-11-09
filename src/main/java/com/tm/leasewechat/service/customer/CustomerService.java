package com.tm.leasewechat.service.customer;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.message.MessageType;
import com.tm.leasewechat.service.contract.ContractService;
import com.tm.leasewechat.consts.GlobalConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by LEO on 16/9/27.
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerInterface customerInterface;

    @Autowired
    private ContractService contractService;

    /**
     * 获取客户手机号码
     * @param cardId
     * @return
     */
    public ResponseEntity<Message> getPhoneNum(String name, String cardId){
        cardId = cardId.toUpperCase();
        String result = customerInterface.getPhoneNum("getPhone", "admin", GlobalConsts.SIGN.value(),
                GlobalConsts.TIMESTAMP.value(), name, cardId);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, contractService.stringToObject(result).getPhone()), HttpStatus.OK);
    }
}
