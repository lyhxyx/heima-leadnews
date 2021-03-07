package com.heima.article.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.common.constants.message.WmNewsMessageConstants;
import com.heima.model.article.pojos.ApArticleConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * APP文章上下架的KAFKA监听器
 */
@Component
public class ApArticleDownOrUpListener {

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;


    @KafkaListener(topics = WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC)
    public void downOrUp(ConsumerRecord<String,String> consumerRecord){
        Optional<ConsumerRecord<String, String>> optional = Optional.ofNullable(consumerRecord);
        if(optional.isPresent()){
            Map map = JSON.parseObject(consumerRecord.value(), Map.class);
            Short enable = Short.valueOf(map.get("enable")+"");
            boolean isDown = true;
            if(enable==1){ //如果是上架
                isDown = false; //未下架
            } else { //如果是下架
                isDown = true;
            }

            Long articleId = Long.valueOf(map.get("articleId")+"");

            ApArticleConfig apArticleConfig  = new ApArticleConfig();
            apArticleConfig.setIsDown(isDown);
            apArticleConfigMapper.update(apArticleConfig, Wrappers.<ApArticleConfig>lambdaUpdate().eq(ApArticleConfig::getArticleId, articleId));
        }
    }
}
