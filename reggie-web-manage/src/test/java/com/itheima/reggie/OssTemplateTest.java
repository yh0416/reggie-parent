package com.itheima.reggie.test;

import com.itheima.reggie.common.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
@SpringBootTest
@Slf4j
//高版本的spingboottest不需要写@RunWith
public class OssTemplateTest {
    @Autowired
    private OssTemplate ossTemplate;

    @Test
    public void testFileUpload() throws FileNotFoundException {
        //上传之前的文件名,上传之前的文件路径
        String filePath = ossTemplate.upload("janey.jpg", new FileInputStream("C:\\Users\\晓彤\\Desktop\\janey.jpg"));
        log.info("文件上传完毕之后的路径{}", filePath);
    }
}