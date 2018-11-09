package com.tm.leasewechat.service.bank;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.message.MessageType;
import com.tm.leasewechat.service.contract.ContractInterface;
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
public class BankService {
    @Autowired
    private ContractInterface contractInterface;
    @Autowired
    private ContractService contractService;

    /**
     * 获取银行列表
     * @return
     */
    public ResponseEntity<Message> getBanks(){
        String result = contractInterface.wxQuery("queryRepaymentBankList", "admin", GlobalConsts.SIGN.value(),
                GlobalConsts.TIMESTAMP.value(), null, null, null);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, contractService.stringToObject(result).getRepaymentBankList()), HttpStatus.OK);
    }
}
