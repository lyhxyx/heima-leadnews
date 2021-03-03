package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.wemedia.WemediaConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.threadlocal.WmThreadLocalUtils;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Value("${file.oss.web-site}")
    private String webSite;

    //select * from wm_user where user_id = 当前登陆的用户ID;
    @Override
    public ResponseResult list(WmNewsPageReqDto dto) {
        //1.检查参数
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.判断用户是否登陆
        WmUser user = WmThreadLocalUtils.getUser();
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        LambdaQueryWrapper<WmNews> wrapper = new LambdaQueryWrapper<>();
        //3.判断dto内属性值是否为空，如果不为空，就拼接查询条件
        if(dto.getStatus()!=null){ //判断请求参数中的状态值
            wrapper.eq(WmNews::getStatus, dto.getStatus());
        }

        if(StringUtils.isNotBlank(dto.getKeyword())){ //判断请求参数中的搜索关键词
            wrapper.like(WmNews::getTitle,dto.getKeyword());
        }

        if(dto.getChannelId()!=null && dto.getChannelId()>0){//判断请求参数中的频道ID
            wrapper.eq(WmNews::getChannelId, dto.getChannelId());
        }

        if(dto.getBeginPubDate()!=null && dto.getEndPubDate()!=null){//判断请求参数中的发布时间的起止时间
            wrapper.between(WmNews::getPublishTime,dto.getBeginPubDate(), dto.getEndPubDate());
        }

        //4.查询条件添加当前登陆用户ID
        wrapper.eq(WmNews::getUserId,user.getId());

        //5.查询条件指定排序方式，按照创建时间倒排
        wrapper.orderByDesc(WmNews::getCreatedTime);

        //6.执行分页查询，得到结果
        IPage<WmNews> ipage = new Page<>(dto.getPage(), dto.getSize());
        IPage<WmNews> page = page(ipage, wrapper);

        //7.封装响应结果，设置素材的域名前缀
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        responseResult.setHost(webSite);
        responseResult.setData(page.getRecords());
        return responseResult;
    }


    @Override
    public ResponseResult submit(WmNewsDto dto, Short isSumit) {
        //1.第一部分：准入校验
        //1.1 校验参数
        if(dto==null || isSumit==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //1.2 判断用户是否登录
        WmUser user = WmThreadLocalUtils.getUser();
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //2.第二部分：处理文章的保存或者更新（根据ID是否有值）

        //2.1拷贝属性
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto,wmNews);

        //2.2 处理布局字段的值
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){ //如果当前页面选择的布局方式是自动，那么保存时设置为null
            wmNews.setType(null);
        }

        //2.3 处理封面图片的值
        /**
         * 由于页面的封面图片请求进来时值的形式是JSON数组，格式如["http://xxx.aliyn.com/a/b.jpg","http://xxx.aliyn.com/a/b.jpg","http://xxx.aliyn.com/a/b.jpg"]
         * 这种值如果要存储到封面对应的images字段中时，需要转化为逗号分割的方式，分割之前注意把域名前缀去掉，格式如/a/b.jpg,/a/b.jpg,/a/b.jpg
         */
        List<String> images = dto.getImages();

        if(images!=null && images.size()>0){
            //去除图片的域名前缀
            List<String> imageList = images.stream().map(x -> x.replace(webSite, "").replace(" ", "")).collect(Collectors.toList());
            //将列表转换为逗号分割的方式
            String imagesStr = StringUtils.join(imageList, ",");
            wmNews.setImages(imagesStr);
        }


        //2.4 操作保存或者更新文章
        saveOrUpdateWmNews(wmNews,isSumit);


        //抽取正文中的图片
        List<String> contentImageList = extractContentImage(dto.getContent());
        //3.第三部分：如果是提交文章，那么保存正文中图片素材与文章的关系
        if(isSumit.equals(WmNews.Status.SUBMIT.getCode()) && contentImageList.size()>0){
            ResponseResult responseResult = saveRelativeForContent(contentImageList, wmNews.getId());
            if(responseResult!=null){
                return responseResult;
            }
        }

        //4.第四部分：如果是提交文章，那么保存封面中图片素材与文章的关系
        if(isSumit.equals(WmNews.Status.SUBMIT.getCode())){
            ResponseResult responseResult = saveRelativeForCover(contentImageList, wmNews, dto);
            if(responseResult!=null){
                return responseResult;
            }
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    //保存封面中图片素材与文章的关系
    private ResponseResult saveRelativeForCover(List<String> contentImageList, WmNews wmNews, WmNewsDto dto) {
        List<String> images = dto.getImages(); //封面图片
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){ //如果是自动布局，那么封面的图片必须从文章正文中取，取图片时要符合匹配规则
            int size = contentImageList.size();
            if(size>=3){ //多图布局时，取3张图片
                images = contentImageList.stream().limit(3).collect(Collectors.toList());
            } else if(size>=1 &&size<3){ //单图布局时，取1张图片
                images = contentImageList.stream().limit(1).collect(Collectors.toList());
            } else { //无图布局
                images = new ArrayList<>();
            }

            if(images.size()>0){ //如果封面图片内有值
                images = images.stream().map(x -> x.replace(webSite, "").replace(" ", "")).collect(Collectors.toList());
                wmNews.setImages(StringUtils.join(images, ","));
                updateById(wmNews);//更新文章
            }
        }

        if(images!=null && images.size()>0){
            images = images.stream().map(x -> x.replace(webSite, "").replace(" ", "")).collect(Collectors.toList());
            //在关系表中需要指明此时保存的关系是封面图片的关系
            ResponseResult responseResult = saveRelativeInfo(images, wmNews.getId(), WemediaConstants.WM_COVER_REFERENCE);
            if(responseResult!=null){
                return responseResult;
            }
        }
        return null;

    }

    //保存正文中图片素材与文章的关系
    private ResponseResult saveRelativeForContent(List<String> contentImageList, Integer newsId) {
        //在关系表中需要指明此时保存的关系是正文图片的关系
        ResponseResult responseResult = saveRelativeInfo(contentImageList, newsId, WemediaConstants.WM_CONTENT_REFERENCE);
        if(responseResult!=null){
            return responseResult;
        }
        return null;
    }
    
    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    //公共方法：保存素材与文章的关系 ["/a/b/c.jpg", "/b/c/d.jpg"]
    private ResponseResult saveRelativeInfo(List<String> imageList, Integer newsId, Short type) {
        //1.根据素材路径查询到素材ID值，将图片路径当做查询条件，实际就是in查询  select * from wm_material where url in ("/a/b/c.jpg", "/b/c/d.jpg")
        List<WmMaterial> wmMaterialList = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery().in(WmMaterial::getUrl, imageList));
        if(wmMaterialList==null || wmMaterialList.size()==0){
            return ResponseResult.errorResult(AppHttpCodeEnum.MATERIAL_REFRENCE_FAIL);
        }

        //2.处理查询条件中，哪些条件没有查到值，如果没查找直接响应错误

        //实现逻辑：将查询的结果转为map，map的key是素材url，map的值是素材id，然后遍历imageList，将imagList的遍历的元素当做map的key从map查数据
        Map<String, Integer> wmMateriaMap = wmMaterialList.stream().collect(Collectors.toMap(WmMaterial::getUrl, WmMaterial::getId));
        List<Integer> materialIdList = new ArrayList<>();//素材ID集合
        for (String imageUrl : imageList) {
            Integer materialId = wmMateriaMap.get(imageUrl);
            if(materialId==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.MATERIAL_REFRENCE_FAIL);
            }
            materialIdList.add(materialId);
        }

        // 保存素材与文章的关系到关联表中，插入的SQL语句类似于 insert into
        wmNewsMaterialMapper.saveRelationsByContent(materialIdList, newsId, type);
        return null;
    }

    //抽取文章正文中的图片
    private List<String> extractContentImage(String content) {
        List<String> contentImageList = new ArrayList<>();
        if(StringUtils.isNotBlank(content)){
            List<Map> mapList = JSON.parseArray(content, Map.class);
            for (Map<String,String> map : mapList) {
                if(map.get("type").equals("image")){//只取图片
                    String imageUrl = map.get("value");
                    //去除前缀之后的图片路径
                    imageUrl = imageUrl.replace(webSite,"").replace(" ","");
                    contentImageList.add(imageUrl);
                }
            }
        }
        return  contentImageList;
    }

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    //保存或者更新文章
    private void saveOrUpdateWmNews(WmNews wmNews, Short isSumit) {
        wmNews.setUserId(WmThreadLocalUtils.getUser().getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setStatus(isSumit);//保存草稿或提交审核
        wmNews.setEnable((short)1); //设置为上架状态

        if (wmNews.getId()!=null && wmNews.getId()>0){ //如果ID存储应该修改文章
            //删除之前，先删除文章和素材的关系
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,wmNews.getId() ));

            //根据ID更新文章
            updateById(wmNews);

        } else { //如果ID无值应该保存文章
            save(wmNews);
        }

    }
}
