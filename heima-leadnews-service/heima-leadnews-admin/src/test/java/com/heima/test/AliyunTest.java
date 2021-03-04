package com.heima.test;

import com.heima.admin.AdminApplication;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Map;

@SpringBootTest(classes = AdminApplication.class)
@RunWith(SpringRunner.class)
public class AliyunTest {

    @Autowired
    private GreenTextScan greenTextScan;

    @Autowired
    private GreenImageScan greenImageScan;

    /**
     * 测试文本检测
     */
    @Test
    public void testTextScan() throws Exception {
        Map<String,String> map = greenTextScan.greeTextScan("买卖电脑是非法的");
        if(map.get("suggestion").equals("block")){
            System.out.println("文本中出现违规内容......");
        } else if(map.get("suggestion").equals("review")){
            System.out.println("文本中出现不确定因素，需要人工审核");
        } else {
            System.out.println("文本内容正常");
        }
    }

    /**
     * 测试图片检测
     */
    @Test
    public void testImageScan() throws Exception {
        //图片地址是能访问的URL地址
        Map<String,String> map = greenImageScan.imageScan(Collections.singletonList("https://hmleadnews371.oss-cn-beijing.aliyuncs.com/material/2021/3/20210303/a533deeec5b647789856873591a9df5b.png"));
        if(map.get("suggestion").equals("block")){
            System.out.println("图片中出现违规内容......");
        } else if(map.get("suggestion").equals("review")){
            System.out.println("图片中出现不确定因素，需要人工审核");
        } else {
            System.out.println("图片内容正常");
        }
    }

}

