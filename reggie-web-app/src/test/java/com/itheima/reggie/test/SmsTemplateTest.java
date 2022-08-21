package com.itheima.reggie.test;

import com.itheima.reggie.common.SmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SmsTemplateTest {
    @Autowired
    private SmsTemplate smsTemplate;
    @Test
    public void testFileUpload(){
        smsTemplate.sendSms("19995304136","6666");
    }

}
