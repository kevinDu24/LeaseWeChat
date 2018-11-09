package com.tm.leasewechat.controller;

import com.tm.leasewechat.dto.financeApply.FinanceApplyRecordDto;
import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.service.financeApply.FinanceApplyInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/9/27.
 */
@RestController
@RequestMapping("/financeApply")
public class FinanceApplyController {

    @Autowired
    private FinanceApplyInterface financeApplyInterface;

    /**
     * 融资申请
     * @param financeApplyRecord
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> financeApply(@RequestBody FinanceApplyRecordDto financeApplyRecord){
        return financeApplyInterface.financeApply(financeApplyRecord);
    }
}
