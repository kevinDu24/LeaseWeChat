package com.tm.leasewechat.config;

import com.tm.leasewechat.dao.RedisRepository;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by pengchao on 2017/4/17.
 */
@Component
public class ControlCommandLineRunner implements CommandLineRunner {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public void run(String... strings) throws Exception {
        //项目启动时保存AccessToken两小时
        redisRepository.save("wxAccessToken", wxMpService.getAccessToken(), 7200);
    }
}
