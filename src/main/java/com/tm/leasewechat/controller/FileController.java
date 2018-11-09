package com.tm.leasewechat.controller;

import com.tm.leasewechat.dto.message.Message;
import com.tm.leasewechat.service.file.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by LEO on 16/10/18.
 */
@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> uploadFile(MultipartFile file){
        return fileUploadService.uploadFile(file);
    }

    /**
     * 获取图片
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/{fileName}", method = RequestMethod.GET)
    public byte[] getDeviceImg(@PathVariable String fileName){
        return fileUploadService.getImg(fileName);
    }

    /**
     * 获取文件
     * @param filePath
     * @return
     */
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public byte[] getLog(@RequestParam String filePath){
        return fileUploadService.getLog(filePath);
    }

    @RequestMapping(value="/download", method = RequestMethod.GET)
    public byte[] downloadFile(HttpServletResponse response, @RequestParam String path) throws IOException {
        return fileUploadService.downloadFile(response, path);
    }
}
