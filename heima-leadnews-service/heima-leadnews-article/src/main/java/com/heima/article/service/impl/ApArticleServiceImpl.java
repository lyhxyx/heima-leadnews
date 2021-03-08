package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.mapper.ApAuthorMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.article.ArticleConstants;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private ApAuthorMapper apAuthorMapper;

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Override
    public ResponseResult saveArticle(ArticleDto articleDto) {
        //1.检查参数
        if(articleDto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.拷贝属性
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(articleDto,apArticle);

        //3.判断文章ID是存在
        //3.1 如果ID不存在，执行保存
        if(apArticle.getId()==null || apArticle.getId()==0){
            //保存ap_article
            //处理作者ID的保存
            ApAuthor apAuthor = apAuthorMapper.selectOne(Wrappers.<ApAuthor>lambdaQuery().eq(ApAuthor::getName, apArticle.getAuthorName()));
            if(apAuthor!=null){
                apArticle.setAuthorId(apAuthor.getId().longValue());
            }
            save(apArticle);

            //保存ap_article_config
            ApArticleConfig apArticleConfig = new ApArticleConfig();
            apArticleConfig.setIsComment(true); //允许评论
            apArticleConfig.setIsForward(true); //允许转发
            apArticleConfig.setIsDelete(false); //不允许删除
            apArticleConfig.setIsDown(false); //不允许删除
            apArticleConfig.setArticleId(apArticle.getId()); //文章ID
            apArticleConfigMapper.insert(apArticleConfig);


            //保存ap_article_content
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setContent(articleDto.getContent()); //文章内容
            apArticleContent.setArticleId(apArticle.getId()); //文章ID
            apArticleContentMapper.insert(apArticleContent);
        } else { //3.2 如果ID存在，更新文章及内容
            //判断数据是否存在
            ApArticle article = getById(apArticle.getId());
            if(article==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }

            //更新ap_article
            updateById(apArticle);

            //更新ap_article_content
            ApArticleContent ApArticleContentUpdate = new ApArticleContent(); //构建要更新的数据
            ApArticleContentUpdate.setContent(articleDto.getContent());
            apArticleContentMapper.update(ApArticleContentUpdate, Wrappers.<ApArticleContent>lambdaUpdate().eq(ApArticleContent::getArticleId, apArticle.getId()));
        }
        //将文章ID设置到响应数据中
        return ResponseResult.okResult(apArticle.getId());
    }

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Value("${file.oss.web-site}")
    private String webSite;

    @Override
    public ResponseResult load(ArticleHomeDto dto, short type) {
        //1.检查参数
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.处理type的值
        if(type!= ArticleConstants.LOADTYPE_LOAD_MORE && type!=ArticleConstants.LOADTYPE_LOAD_NEW){
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        //3.处理size的值
        if(dto.getSize()==null || dto.getSize()<=0){
            dto.setSize(10);
        }
        //限制size最大值不超过50
        dto.setSize(Math.min(dto.getSize(),50));

        //4.处理最大时间和最小时间的值
        if(dto.getMinBehotTime()==null) dto.setMinBehotTime(new Date());
        if(dto.getMaxBehotTime()==null) dto.setMaxBehotTime(new Date());

        //5.处理tag的值
        if(StringUtils.isBlank(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG); //设置默认的频道标识，实际就是推荐频道
        }

        //6.查询文章列表
        List<ApArticle> apArticleList = apArticleMapper.loadArticleList(dto, type);

        //7.响应数据
        ResponseResult responseResult = ResponseResult.okResult(apArticleList);
        responseResult.setHost(webSite);
        return responseResult;
    }
}