package com.heima.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.admin.mapper.AdChannelMapper;
import com.heima.admin.mapper.AdSensitiveMapper;
import com.heima.admin.service.WmNewsAutoScanService;
import com.heima.apis.article.IArticleClient;
import com.heima.apis.wemedia.IWemediaClient;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.model.admin.pojos.AdChannel;
import com.heima.model.admin.pojos.AdSensitive;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.common.SensitiveWordUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Autowired
    private IWemediaClient wemediaClient;

    @Autowired
    private IArticleClient articleClient;
    
    @Value("${file.oss.web-site}")
    private String webSite;
    
    
    @Override
    public void wmNewAutoScanById(Integer id) {
        //第一部分：准入校验
        //1.1 检查参数
        if(id==null || id==0){
            return;
        }

        //1.2 判断文章是否存在
        WmNews wmNews = wemediaClient.findNewsById(id);
        if(wmNews==null){
            return;
        }

        //第二部分：处理曾经审核通过待发布的文章
        //2.1 状态为4（人工审核通过的）并且发布时间小于当前时间的，可以进行发布保存文章
        if(wmNews.getStatus().equals(WmNews.Status.ADMIN_SUCCESS.getCode())
                && wmNews.getPublishTime().getTime()< System.currentTimeMillis()){
            saveArticle(wmNews);
            return;
        }

        //2.2 状态为8（自动审核通过的）并且发布时间小于当前时间的，可以进行发布保存文章
        if(wmNews.getStatus().equals(WmNews.Status.SUCCESS.getCode())
                && wmNews.getPublishTime().getTime()< System.currentTimeMillis()){
            saveArticle(wmNews);
            return;
        }

        //第三部分：对文章内容进行检测审核
        if(wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
            //3.1从文章正文抽取纯文本信息和图片列表信息
            Map result = extractContent(wmNews);

            //3.2自管理敏感词审核（文本）
            boolean sensitiveScanResult = scanSensitives(wmNews, (String) result.get("text"));
            if(!sensitiveScanResult) {
                return;
            }

            //3.3审核文本（调用阿里云接口）
            boolean scanTextResult = scanText(wmNews, (String) result.get("text"));
            if(!scanTextResult){
                return;
            }

            //3.4审核图片（正文图片+封面图片，调用阿里云接口）
            boolean scanImageResult = scanImage(wmNews, (List<String>) result.get("imageList"));
            if(!scanImageResult){
                return;
            }

            //第四部分：将文章数据保存，并修改审核状态为已发布
            //4.1 判断发布时间是否允许发布，如果不允许，直接修改文章状态为8（审核通过）
            if(wmNews.getPublishTime().getTime()>System.currentTimeMillis()){
                updateWmNews(wmNews,WmNews.Status.SUCCESS.getCode(),"审核通过待发布");
                return;
            }

            //4.2 保存APP文章并发布
            saveArticle(wmNews);
        }
    }


    //审核图片（正文图片+封面图片，调用阿里云接口）
    private boolean scanImage(WmNews wmNews, List<String> imageList) {
        boolean flag = true;
        if(imageList!=null && imageList.size()>0){
            imageList = imageList.stream().distinct().collect(Collectors.toList()); //去重URL
            try {
                Map<String,String> map = greenImageScan.imageScan(imageList);
                String suggestion = map.get("suggestion");
                if(suggestion.equals("block")){ //命中违规词
                    flag = false;
                    updateWmNews(wmNews, WmNews.Status.FAIL.getCode(),"图片内容违规");
                } else if(suggestion.equals("review")){ //需要人工审核
                    flag = false;
                    updateWmNews(wmNews, WmNews.Status.ADMIN_AUTH.getCode(),"图片内容又不确定因素需要人工审核");
                }

            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }

    @Autowired
    private GreenTextScan greenTextScan;

    @Autowired
    private GreenImageScan greenImageScan;

    //审核文本（调用阿里云接口）
    private boolean scanText(WmNews wmNews, String text) {
        boolean flag = true;
        if (StringUtils.isNotBlank(text)) {
            try {
                Map<String,String> map = greenTextScan.greeTextScan(text);
                String suggestion = map.get("suggestion");
                if(suggestion.equals("block")){ //命中违规词
                    flag = false;
                    updateWmNews(wmNews, WmNews.Status.FAIL.getCode(),"文本内容违规");
                } else if(suggestion.equals("review")){ //需要人工审核
                    flag = false;
                    updateWmNews(wmNews, WmNews.Status.ADMIN_AUTH.getCode(),"文本内容又不确定因素需要人工审核");
                }

            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
            }
        }

        return flag;
    }

    @Autowired
    private AdSensitiveMapper adSensitiveMapper;

    //自管理敏感词审核（文本）
    private boolean scanSensitives(WmNews wmNews, String text) {
        boolean flag = true;
        if(StringUtils.isNotBlank(text)){
            //查询敏感词列表
            List<AdSensitive> adSensitiveList = adSensitiveMapper.selectList(Wrappers.<AdSensitive>lambdaQuery().select(AdSensitive::getSensitives));
            List<String> sensitiveList = adSensitiveList.stream().map(x -> x.getSensitives()).collect(Collectors.toList());

            //DFA算法工具类进行检测
            SensitiveWordUtil.initMap(sensitiveList);

            Map<String, Integer> map = SensitiveWordUtil.matchWords(text);
            if(map.size()>0){ //如果命中敏感词，那么就是违规，应该审核失败
                flag = false;
                updateWmNews(wmNews,WmNews.Status.FAIL.getCode(),"自管理敏感词审核失败，命中：" + map);
            }
        }

        return flag;
    }

    //抽取纯文本信息和图片列表信息
    private Map extractContent(WmNews wmNews) {
        
        Map result = new HashMap();

        StringBuilder text = new StringBuilder();
        List<String> imageList = new ArrayList<>();
        
        //从正文中抽取纯文本和图片
        if(StringUtils.isNotBlank(wmNews.getContent())){
            List<Map> mapList = JSON.parseArray(wmNews.getContent(), Map.class);
            for (Map<String,String> map : mapList) {
                if(map.get("type").equals("text")){
                    text.append(map.get("value"));
                }
                if(map.get("type").equals("image")){
                    String imageUrl = map.get("value");
                    if(!imageUrl.startsWith(webSite)){
                        imageUrl = webSite + imageUrl; //拼接域名前缀
                    }
                    imageList.add(imageUrl);
                }
            }
        }

        //从封面中抽取图片
        if(StringUtils.isNotBlank(wmNews.getImages())){
            String images = wmNews.getImages(); //逗号分割的字符串
            String[] imagesArr = images.split(",");
            for (String imageUrl : imagesArr) {
                if(!imageUrl.startsWith(webSite)){
                    imageUrl = webSite + imageUrl; //拼接域名前缀
                }
                imageList.add(imageUrl);
            }
        }

        result.put("text", text.toString());
        result.put("imageList", imageList);
        return result;
    }

    @Autowired
    private AdChannelMapper adChannelMapper;

    //保存APP文章、设置文章状态为已发布
    private void saveArticle(WmNews wmNews) {
        //1.构建dto
        ArticleDto dto = new ArticleDto();
        dto.setTitle(wmNews.getTitle()); //标题
        dto.setContent(wmNews.getContent()); //内容
        dto.setLayout(wmNews.getType()); //布局方式
        dto.setImages(wmNews.getImages()); //封面图片
        dto.setCreatedTime(wmNews.getCreatedTime()); //创建时间
        dto.setPublishTime(wmNews.getPublishTime()); //发布时间
        WmUser wmUser = wemediaClient.findWmUserById(wmNews.getUserId().longValue());
        dto.setAuthorName(wmUser.getName()); //作者名，实际上就是自媒体用户名
        AdChannel adChannel = adChannelMapper.selectById(wmNews.getChannelId());
        if(adChannel!=null){
            dto.setChannelId(wmNews.getChannelId()); //频道ID
            dto.setChannelName(adChannel.getName()); //频道名称
        }

        //2.feign远程调用保存app文章
        ResponseResult responseResult = articleClient.saveArticle(dto);
        if(responseResult.getCode()!=200){
            throw new RuntimeException("保存APP文章失败");
        }

        //3.将APP文章ID设置到wmNews上
        Long appArticleId = (Long)responseResult.getData();
        wmNews.setArticleId(appArticleId);

        //4.更新wmNew的状态为发布状态9
        updateWmNews(wmNews, WmNews.Status.PUBLISHED.getCode(), "审核文章通过并已发布");
    }

    //更新自媒体文章
    private void updateWmNews(WmNews wmNews, short status, String reason) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        wemediaClient.updateWmNews(wmNews);
    }
}
