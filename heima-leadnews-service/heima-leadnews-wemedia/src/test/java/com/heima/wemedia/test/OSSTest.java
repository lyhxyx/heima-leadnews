package com.heima.wemedia.test;


import com.heima.file.service.FileStorageService;
import com.heima.wemedia.WemediaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class OSSTest {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 测试文件上传
     */
    @Test
    public void testUpload() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("C:/Users/Administrator/Desktop/2.png");
        String result = fileStorageService.store("itcast", "tiger.png", inputStream);
        System.out.println(result);
    }


    /**
     * 测试文件删除
     */
    @Test
    public void testDelete(){
        fileStorageService.delete( "itcast/2021/2/20210227/tiger.png");
    }
}
