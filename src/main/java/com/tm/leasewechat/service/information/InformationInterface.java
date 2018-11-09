package com.tm.leasewechat.service.information;

import com.tm.leasewechat.dto.message.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/10/17.
 */
@FeignClient(name = "informationInterface", url = "${request.serverUrl}")
public interface InformationInterface {

    @RequestMapping(value = "/informations", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getInfos(
                                     @RequestParam(value = "type") Integer type,
                                     @RequestParam(value = "page") Integer page,
                                     @RequestParam(value = "size") Integer size);

    @RequestMapping(value = "/informations/{infoId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getInfoDetail(@PathVariable(value = "infoId") Long infoId);

    @RequestMapping(value = "/informations/productList", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getProductList();
}
