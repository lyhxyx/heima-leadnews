package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.apis.article.IArticleClient;
import com.heima.apis.wemedia.IWemediaClient;
import com.heima.common.constants.user.UserConstants;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.pojo.ApUser;
import com.heima.model.user.pojo.ApUserRealname;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.mapper.ApUserRealnameMapper;
import com.heima.user.service.ApUserRealnameService;
import com.sun.xml.bind.v2.TODO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 认证信息审核实现类
 */
@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper,ApUserRealname> implements ApUserRealnameService {
    @Override
    public ResponseResult list(AuthDto authDto) {
        //1.检查参数
        if(authDto==null){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.检查分页参数
        authDto.checkParam();
        //3.检查传过来的审核参数
        LambdaQueryWrapper<ApUserRealname> queryWrapper = new LambdaQueryWrapper<>();
        if (authDto.getStatus()!=null){
            queryWrapper.eq(ApUserRealname::getStatus,authDto.getStatus());
        }
        //4.分页
        IPage<ApUserRealname> iPage=new Page<>(authDto.getPage(),authDto.getSize());
        IPage<ApUserRealname> page=page(iPage,queryWrapper);
        ResponseResult responseResult=new PageResponseResult(authDto.getPage(),authDto.getSize(), (int) page.getTotal());
        //5.将分页列表的数据放在data返回给前端
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult auth(AuthDto authDto, short status) {
        //检查参数是否空
        if (authDto==null||status<0||authDto.getId()==0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //判断当前要更新的认证信息是否存在
        ApUserRealname apUserRealname = getById(authDto.getId());
        if (apUserRealname==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"认证信息不存在");
        }
        //更新认证信息的状态及驳回原因
        //构建要更新的认证信息
        ApUserRealname apUserRealnameDB = new ApUserRealname();
        apUserRealnameDB.setId(authDto.getId());
        apUserRealnameDB.setStatus(status);
        if (StringUtils.isNotBlank(authDto.getMsg())){
            apUserRealnameDB.setReason(authDto.getMsg());
        }
        updateById(apUserRealnameDB);
        //创建自媒体用户   创建时首先看是否有要创建的自媒体的用户 没有则创建 要查和要创建都要调用自媒体微服务
        //这里看是否审核通过 可以直接用状态来做判断
        if (status== UserConstants.PASS_AUTH){
            //代码太长 重新写一个方法
            ResponseResult responseResult = createWemediaAndAuthor(authDto);
            if (responseResult!=null){
                return responseResult;
            }
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private IWemediaClient iWemediaClient;
    @Autowired
    private IArticleClient iArticleClient;

    private ResponseResult createWemediaAndAuthor(AuthDto authDto) {
//
        ApUserRealname apUserRealname = getById(authDto.getId());
        //根据app实名认证信息表的userid去app用户表里查app用户信息
        ApUser apUser = apUserMapper.selectById(apUserRealname.getUserId());
        if (apUser==null){
            return  ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"app用户不存在");
        }
        //调用自媒体微服务看是否存在用户  不存在则创建
        WmUser wmUser = iWemediaClient.findByName(apUser.getName());
        if (wmUser!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST,"自媒体用户已存在");
        }
        //创建自媒体用户
        wmUser=new WmUser();
        wmUser.setName(apUser.getName());
        wmUser.setPassword(apUser.getPassword());
        wmUser.setSalt(apUser.getSalt());
        wmUser.setStatus(Integer.valueOf(UserConstants.PASS_AUTH));
        wmUser.setPhone(apUser.getPhone());
        wmUser.setApUserId(apUser.getId());
        wmUser.setImage(apUser.getImage());
        wmUser.setType(0);//0表示个人  1表示 企业  2 表示子账号
        wmUser.setCreatedTime(new Date());
        //调用feign接口保存创建的自媒体用户
        iWemediaClient.save(wmUser);
        //创建文章作者  抽出一个方法
        ResponseResult author = createAuthor(wmUser);
        if (author!=null){
            return author;
        }
        //设置自媒体用户表中的作者id以及自媒体用户的id\

        //TODO  这里还需要写一个根据wemedia更新的fegin接口
        /*ApAuthor apAuthor = iArticleClient.findByUserId(wmUser.getApUserId());
        WmUser wmUser1=new WmUser();
        wmUser1.setApAuthorId(apAuthor.getId());
        iWemediaClient.updatebyId(wmUser1);*/
        //将ap_user的身份标识为自媒体人

        ApUser apUser1=new ApUser();
        apUser1.setFlag((short) 1);
        apUserMapper.updateById(apUser1);
        return null;
    }

    private ResponseResult createAuthor(WmUser wmUser) {
        //创建文章作者  根据app用户的userid （也就是自媒体用户表里的apauthorid）查文章作者是否存在
        ApAuthor apAuthor = iArticleClient.findByUserId(wmUser.getApAuthorId());
        if (apAuthor!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST,"文章作者已经存在");
        }
        apAuthor=new ApAuthor();
        apAuthor.setUserId(wmUser.getApUserId());//app用户id
        apAuthor.setType(2);//类型  0 爬取数据  1签约合作商 2自媒体人

        //调用自媒体微服务feign接口查询刚刚创建好的自媒体用户  将ID赋值给文章作者的自媒体用户
        WmUser wmUser1 = iWemediaClient.findByName(wmUser.getName());
        apAuthor.setWmUserId(wmUser1.getId());
        apAuthor.setName(wmUser.getName());
        apAuthor.setCreatedTime(new Date());
        //调用feign接口保存创建的文章作者
        ResponseResult responseResult = iArticleClient.save(apAuthor);
        return responseResult;
    }
}
