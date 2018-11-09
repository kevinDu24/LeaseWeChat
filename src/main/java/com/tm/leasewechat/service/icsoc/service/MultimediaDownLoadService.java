package com.tm.leasewechat.service.icsoc.service;

import com.alibaba.fastjson.JSONObject;
import com.tm.leasewechat.config.FileUploadProperties;
import com.tm.leasewechat.dao.RedisRepository;
import com.tm.leasewechat.service.icsoc.MultimediaDownLoadInterface;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;


/**
 * Created by pengchao on 2017/4/14.
 */
@Service
public class MultimediaDownLoadService {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private MultimediaDownLoadInterface multimediaDownLoadService;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private FileUploadProperties fileUploadProperties;
    /**

    /**
     * 下载多媒体文件（文件上传3天内可下载）
     * @param media_id
     * @return
     */
    public ResponseEntity<byte[]> downloadMedia(String media_id){
        String accessToken = (String) redisRepository.get("wxAccessToken");
        if(accessToken == null){
            try {
                accessToken = wxMpService.getAccessToken();
                redisRepository.save("wxAccessToken", accessToken, 7200);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
        return  multimediaDownLoadService.downLoadMedia(accessToken, media_id);
    }


    /**
     * 上传多媒体文件至微信服务器
     * @param
     * @param
     * @return
     * @throws Exception
     */
//    @Bean
//    public Servlet baiduProxyServlet(){
//        return new ProxyServlet();
//    }
//
//    @Bean
//    public ServletRegistrationBean proxyServletRegistration(){
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(baiduProxyServlet(), "/");
//        String accessToken = (String) redisRepository.get("wxAccessToken");
//        if(accessToken == null){
//            try {
//                accessToken = wxMpService.getAccessToken();
//                redisRepository.save("wxAccessToken", accessToken, 7200);
//            } catch (WxErrorException e) {
//                e.printStackTrace();
//            }
//        }
//        Map<String, String> params = ImmutableMap.of(
//                "targetUri", "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token="+ accessToken + "&",
//                "log", "true");
//        registrationBean.setInitParameters(params);
//        registrationBean.getInitParameters();
//        return registrationBean;
//    }
    public  Object uploadMedia(String fileType, MultipartFile file) throws Exception {
        JSONObject jsonObj = null;
        String result = null;
        if (null == file || file.isEmpty()) {
            throw new IOException("文件不存在");
        }

        String accessToken = (String) redisRepository.get("wxAccessToken");
        if (accessToken == null) {
            try {
                accessToken = wxMpService.getAccessToken();
                redisRepository.save("wxAccessToken", accessToken, 7200);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
        URL url = new URL("http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token="+ accessToken + "&type="+fileType);
//        Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress("192.168.1.199", 80));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);
        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getOriginalFilename() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(file.getInputStream());
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                buffer.append(line);
            }

            if(result == null){
                result = buffer.toString();
            }
            jsonObj =JSONObject.parseObject(result);
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            throw new IOException("数据读取异常");
        } finally {
            if(reader!=null){
                reader.close();
            }
        }
        return jsonObj;
    }

}
