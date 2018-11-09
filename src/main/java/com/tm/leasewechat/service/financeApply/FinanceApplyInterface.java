package com.tm.leasewechat.service.financeApply;

import com.tm.leasewechat.dto.financeApply.FinanceApplyRecordDto;
import com.tm.leasewechat.dto.message.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/10/24.
 */
//@FeignClient(name = "informationInterface", url = "http://localhost:8089")
@FeignClient(name = "informationInterface", url = "${request.serverUrl}")
public interface FinanceApplyInterface {
    @RequestMapping(value = "/financeApply", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> financeApply(@RequestBody FinanceApplyRecordDto financeApplyRecordDto);
}
