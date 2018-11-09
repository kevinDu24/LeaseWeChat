package com.tm.leasewechat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tm.leasewechat.consts.GlobalConsts;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/**
 * Created by LEO on 2015/9/15.
 */
@Configuration
@EnableScheduling
public class WebConfig extends WebMvcConfigurerAdapter{

    @Bean
    public AccountProperties accountProperties(){
        return new AccountProperties();
    }

    @Bean
    public MessageProperties messageProperties(){return new MessageProperties();}

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public FileUploadProperties fileUploadProperties(){
        return new FileUploadProperties();
    }

    @Bean
    public WzProperties wzProperties(){
        return new WzProperties();
    }

    @Bean
    public TemplateProperties templateProperties(){
        return new TemplateProperties();
    }


    @Bean
    public Gson gson(){
        return new Gson();
    }

    @Bean
    public WxProperties wxProperties(){
        return new WxProperties();
    }

    @Bean
    public WxMpService wxMpService(){
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(GlobalConsts.WeChat.APPID.value());
        config.setSecret(GlobalConsts.WeChat.APPSECRET.value());
        config.setToken(GlobalConsts.WeChat.TOKEN.value());
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer){
        configurer.setUseSuffixPatternMatch(false);
    }

    @Bean
    public WxMpMessageRouter wxMpMessageRouter(){
        return new WxMpMessageRouter(wxMpService());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }
}
