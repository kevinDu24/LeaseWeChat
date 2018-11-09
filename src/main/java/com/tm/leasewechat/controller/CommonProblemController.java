package com.tm.leasewechat.controller;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.service.commonProblem.CommonProblemInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangbiao on 2016/11/16.
 */
@RestController
@RequestMapping("/commonProblems")
public class CommonProblemController {
    @Autowired
    private CommonProblemInterface commonProblemInterface;

    @RequestMapping(value = "/commonProblemList", method = RequestMethod.GET)
    public ResponseEntity<Message> queryCommonProblems(Integer page, Integer size){
        return  commonProblemInterface.getCommonProblemList(page, size);
    }


}
