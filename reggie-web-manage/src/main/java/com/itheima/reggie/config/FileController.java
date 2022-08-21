package com.itheima.reggie.config;

import com.itheima.reggie.common.OssTemplate;
import com.itheima.reggie.common.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
public class FileController {
    //文件上传要和文件解析器搭配在一起用,但是springboot已经配置好了,如果有文件大小的特殊要求,需要自己加设置
    @Autowired
    private OssTemplate ossTemplate;
    @PostMapping("/common/upload")
    public ResultInfo uploadFile(MultipartFile file) throws IOException {
        if (file.getSize()>0){
        String upload = ossTemplate.upload(file.getOriginalFilename(), file.getInputStream());
        return ResultInfo.success(upload);
        }
        return ResultInfo.error("上传的文件为空");
    }
}
