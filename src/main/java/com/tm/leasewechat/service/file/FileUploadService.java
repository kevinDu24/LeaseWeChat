package com.tm.leasewechat.service.file;

import com.google.common.collect.Maps;
import com.tm.leasewechat.config.FileUploadProperties;
import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.dto.message.MessageType;
import com.tm.leasewechat.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LEO on 16/10/18.
 */
@Service
public class FileUploadService {

    @Autowired
    private FileUploadProperties fileUploadProperties;
    /**
     * 文件上传
     * @param file
     * @return
     */
    public ResponseEntity<Message> uploadFile(MultipartFile file){
        Message message = null;
        if (!file.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + Utils.getFileSuffix(file.getOriginalFilename());
            try {
                FileUtils.writeByteArrayToFile(new File(fileUploadProperties.getFilePath() + fileName), file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件上传失败"), HttpStatus.OK);
            }
            Map map = Maps.newHashMap();
            map.put("url", fileUploadProperties.getRequestFilePath() + fileName);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, map), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件为空,上传失败"), HttpStatus.OK);
    }

    /**
     * 获取图片
     * @param fileName
     * @return
     */
    public byte[] getImg(String fileName){
        String path = fileUploadProperties.getFilePath() + fileName;
        Resource imgRes = new FileSystemResource(path);
        try {
            return IOUtils.toByteArray(imgRes.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getLog(String filePath){
        Resource imgRes = new FileSystemResource(filePath);
        try {
            return IOUtils.toByteArray(imgRes.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 附件下载
     * @param response
     * @return
     * @throws IOException
     */
    public byte[] downloadFile(HttpServletResponse response, String path) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        File file = new File(path);
        if(!file.exists()){
            String errorMessage = "抱歉. 你访问的文件不存在！";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return null;
        }

        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
        response.setContentLength((int)file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        return null;
    }

}
