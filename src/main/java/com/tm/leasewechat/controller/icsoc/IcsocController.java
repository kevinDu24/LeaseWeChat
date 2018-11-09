package com.tm.leasewechat.controller.icsoc;
import com.tm.leasewechat.dto.wechat.SendMessageDto;
import com.tm.leasewechat.service.icsoc.service.MultimediaDownLoadService;
import com.tm.leasewechat.service.icsoc.service.SendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * cc公司服务对接接口
 * Created by pengchao on 2017/4/14.
 */
@RestController
@RequestMapping("/icsoc")
public class IcsocController {
    @Autowired
    private MultimediaDownLoadService multimediaDownLoadService;

    @Autowired
    private SendMsgService sendMsgService;


    /**
     * 下载多媒体接口
     * @param media_id(媒体id)
     * @return
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadMedia(@RequestParam String media_id){
      return multimediaDownLoadService.downloadMedia(media_id);
    }

    /**
     * 上传多媒体接口
     * @param type 多媒体类型
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadMedia(@RequestParam String type, MultipartFile file) throws Exception {
        return multimediaDownLoadService.uploadMedia(type, file);
    }

    /**
     * 发送消息接口
     * @param sendMessageDto
     * @return
     */
    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
    public Object sendMsg (@RequestBody SendMessageDto sendMessageDto){
        return  sendMsgService.sendMsg(sendMessageDto);
    }

    /**
     * 获取用户信息
     * @param openid 用户当前公众号openid
     * @param lang 语言
     * @return
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public Object getUserInfo(@RequestParam String openid,@RequestParam String lang){
        return sendMsgService.getUserInfo(openid, lang);

    }
}
