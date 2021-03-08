package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.apis.article.IArticleClient;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.UserRelationDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserFan;
import com.heima.model.user.pojos.ApUserFollow;
import com.heima.user.mapper.ApUserFanMapper;
import com.heima.user.mapper.ApUserFollowMapper;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserRelationService;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ApUserRelationServiceImpl implements ApUserRelationService {

    @Autowired
    private IArticleClient articleClient;

    @Autowired
    private ApUserMapper apUserMapper;

    @Override
    public ResponseResult follow(UserRelationDto dto) {
        //1.检查参数
        if(dto==null || dto.getAuthorId()==null || dto.getAuthorId()==1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.判断当前用户是否已登录
        ApUser apUser = AppThreadLocalUtils.getUser();
        if(apUser==null || apUser.getId()==null || apUser.getId()<=0){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //3.根据作者ID查询作作者判断是否存在
        ApAuthor apAuthor = articleClient.findOne(dto.getAuthorId());
        if(apAuthor==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "作者信息不存在");
        }

        //4.判断该作者对应的ap_user是否存在
        ApUser apUserForAuthor = apUserMapper.selectById(apAuthor.getUserId());
        if(apUserForAuthor==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "作者对应的用户不存在");
        }

        //5.用户不能关注自己
        if(apUser.getId()==apUserForAuthor.getId()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "不能关注自己");
        }


        //6.执行关注或取消关注
        if(dto.getOperation()==0){  //关注
            ResponseResult responseResult = followRelation(apUser, apUserForAuthor);
            if(responseResult!=null){
                return responseResult;
            }
        } else { //取消关注
            ResponseResult responseResult =  followCancelRelation(apUser, apUserForAuthor);
            if(responseResult!=null){
                return responseResult;
            }
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Autowired
    private ApUserFollowMapper apUserFollowMapper;

    @Autowired
    private ApUserFanMapper apUserFanMapper;

    //关注
    private ResponseResult followRelation(ApUser apUser, ApUser apUserForAuthor) {
        Integer userId = apUser.getId();//当前登录用户的ID
        Integer authorUserId = apUserForAuthor.getId();//作者的用户ID

        //根据用户ID和作者用户ID查询关注记录
        ApUserFollow apUserFollow = apUserFollowMapper.selectOne(Wrappers.<ApUserFollow>lambdaQuery().
                eq(ApUserFollow::getUserId, userId).eq(ApUserFollow::getFollowId, authorUserId));
        if(apUserFollow!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"已关注，无需再次关注");
        }

        //保存用户关注表信息
        apUserFollow = new ApUserFollow();
        apUserFollow.setCreatedTime(new Date());
        apUserFollow.setLevel((short)0);
        apUserFollow.setIsNotice(true);
        apUserFollow.setFollowName(apUserForAuthor.getName());
        apUserFollow.setFollowId(authorUserId);
        apUserFollow.setUserId(userId);
        apUserFollowMapper.insert(apUserFollow);


        //根据用户ID和作者用户ID查询粉丝记录
        ApUserFan apUserFan = apUserFanMapper.selectOne(Wrappers.<ApUserFan>lambdaQuery().
                eq(ApUserFan::getUserId, authorUserId).eq(ApUserFan::getFansId, userId));
        if(apUserFan==null){
            apUserFan = new ApUserFan();
            apUserFan.setUserId(authorUserId);
            apUserFan.setFansId(userId.longValue());
            apUserFan.setLevel((short)0);
            apUserFan.setCreatedTime(new Date());
            apUserFan.setIsShieldLetter(false);
            apUserFan.setIsShieldComment(false);
            apUserFan.setIsDisplay(true);

            ApUser appUser = apUserMapper.selectById(userId);
            apUserFan.setFansName(appUser.getName());
            apUserFanMapper.insert(apUserFan);
        }
        return null;
    }

    //取消关注
    private ResponseResult followCancelRelation(ApUser apUser, ApUser apUserForAuthor) {

        //1.判断关注的关系是否存在
        Integer userId = apUser.getId();//当前登录用户的ID
        Integer authorUserId = apUserForAuthor.getId();//作者的用户ID

        ApUserFollow apUserFollow = apUserFollowMapper.selectOne(Wrappers.<ApUserFollow>lambdaQuery().
                eq(ApUserFollow::getUserId, userId).eq(ApUserFollow::getFollowId, authorUserId));
        if(apUserFollow==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"关注关系不存在");
        }

        //2.判断粉丝关系是否存在
        ApUserFan apUserFan = apUserFanMapper.selectOne(Wrappers.<ApUserFan>lambdaQuery().
                eq(ApUserFan::getUserId, authorUserId).eq(ApUserFan::getFansId, userId));
        if(apUserFan==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"粉丝关系不存在");
        }

        //3.删除关注记录
        apUserFollowMapper.delete(Wrappers.<ApUserFollow>lambdaQuery().
                eq(ApUserFollow::getUserId, userId).eq(ApUserFollow::getFollowId, authorUserId));

        //4.删除粉丝记录
        apUserFanMapper.delete(Wrappers.<ApUserFan>lambdaQuery().
                eq(ApUserFan::getUserId, authorUserId).eq(ApUserFan::getFansId, userId));
        return null;
    }
}
