package com.heima.admin.listener;

import com.heima.admin.service.WmNewsAutoScanService;
import com.heima.common.constants.admin.NewsAutoScanConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 自媒体文章自动审核Kafka监听器
 */
@Component
public class WmNewsAutoScanListener {

    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;


    @KafkaListener(topics = NewsAutoScanConstants.WM_NEWS_AUTO_SCAN_TOPIC)
    public void autoScan(ConsumerRecord<String,String> consumerRecord){
        Optional<ConsumerRecord<String, String>> optional = Optional.ofNullable(consumerRecord);
        optional.ifPresent(x ->  wmNewsAutoScanService.wmNewAutoScanById(Integer.valueOf(consumerRecord.value())));
    }
}
