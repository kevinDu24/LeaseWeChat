package com.tm.leasewechat.controller;

import com.tm.leasewechat.service.information.InformationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/10/17.
 */
@RestController
@RequestMapping("/informations")
public class InformationController {

    @Autowired
    private InformationInterface informationInterface;

    /**
     * 查询某条新闻
     * @param infoId
     * @return
     */
    @RequestMapping(value = "/{infoId}",method = RequestMethod.GET)
    public String getInfoDetails(@PathVariable Long infoId){
        return informationInterface.getInfoDetail(infoId);
    }

    /**
     * 查询产品列表页面
     * @return
     */
    @RequestMapping(value = "/productList", method = RequestMethod.GET)
    public String getProductList(){
        return informationInterface.getProductList();
    }
}
