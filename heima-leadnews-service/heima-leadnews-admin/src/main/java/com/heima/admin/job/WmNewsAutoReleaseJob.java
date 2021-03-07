package com.heima.admin.job;

import com.heima.admin.service.WmNewsAutoScanService;
import com.heima.apis.wemedia.IWemediaClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自媒体文章自动发布定时任务
 */
@Component
public class WmNewsAutoReleaseJob {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IWemediaClient wemediaClient;

    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;


    @XxlJob("wemediaScanJob")
    public ReturnT<String> demoJobHandler(String param) throws Exception {


        List<Integer> idList = wemediaClient.findRelease();
        if(idList!=null && idList.size()>0){
            logger.info("开始执行自媒体文章自动发布，此时时间戳：{}" , System.currentTimeMillis()/1000);
            for (Integer wmNewsId : idList) {
                logger.info("自动发布文章：{}", wmNewsId);
                wmNewsAutoScanService.wmNewAutoScanById(wmNewsId);
            }
            logger.info("完成执行自媒体文章自动发布，此时时间戳：{}" ,  System.currentTimeMillis()/1000);
        } else {
            logger.info("暂无可自动发布的文章,时间：{} ",  System.currentTimeMillis()/1000);
        }


        return ReturnT.SUCCESS;
    }
}
