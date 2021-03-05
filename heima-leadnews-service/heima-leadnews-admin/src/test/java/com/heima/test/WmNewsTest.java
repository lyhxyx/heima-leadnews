package com.heima.test;

import com.heima.admin.AdminApplication;
import com.heima.admin.service.WmNewsAutoScanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = AdminApplication.class)
@RunWith(SpringRunner.class)
public class WmNewsTest {

    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    @Test
    public void testWmNewsAutoScan(){
        wmNewsAutoScanService.wmNewAutoScanById(6223);
    }
}
