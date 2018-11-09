package com.tm.leasewechat.service.car;

import com.google.gson.Gson;
import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.message.MessageType;
import com.tm.leasewechat.dto.result.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by LEO on 16/10/12.
 */
@Service
public class CarService {
    @Autowired
    private CarInterface carInterface;

    @Autowired
    private Gson gson;

    /**
     * 获取车辆品牌
     * @return
     */
    public ResponseEntity<Message> getCarBrands(){
        String result = carInterface.getCarBrands("1");
        ResultDto resultDto = gson.fromJson(result, ResultDto.class);
        if("成功".equals(resultDto.getMessage())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getResult()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取车辆信息失败"), HttpStatus.OK);
    }

    /**
     * 获取车系
     * @param id
     * @return
     */
    public ResponseEntity<Message> getCarSeries(String id){
        String result = carInterface.getCarSeries("3", id);
        ResultDto resultDto = gson.fromJson(result, ResultDto.class);
        if("成功".equals(resultDto.getMessage())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getResult()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取车辆信息失败"), HttpStatus.OK);
    }
}
