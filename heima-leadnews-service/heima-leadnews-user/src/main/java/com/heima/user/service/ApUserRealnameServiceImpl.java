package com.heima.user.service;

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
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.mapper.ApUserRealnameMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {


    @Override
    public ResponseResult list(AuthDto dto) {
        //1.校验参数
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.检查分页参数
        dto.checkParam();

        LambdaQueryWrapper<ApUserRealname> wrapper = new LambdaQueryWrapper<>();
        //3.判断status参数是否为空，如果不为空，按照状态字段查询
        if(dto.getStatus()!=null){
            wrapper.eq(ApUserRealname::getStatus,dto.getStatus());
        }

        //4.分页查询
        IPage<ApUserRealname> ipage = new Page<>(dto.getPage(), dto.getSize());
        IPage<ApUserRealname> page = page(ipage, wrapper);

        //5.封装结果并响应
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(), (int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }


    @GlobalTransactional  //seata的分布式事务注解
    @Override
    public ResponseResult auth(AuthDto dto, Short status) {
        //第一部分：判断参数

        //1.1检查参数是否为空
        if(dto==null || status==null || status==0 || dto.getId()==null || dto.getId()==0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //1.2判断当前更新的认证信息是否存在
        ApUserRealname apUserRealname = getById(dto.getId());
        if(apUserRealname==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "认证信息不存在");
        }

        //第二部分：更新认证信息的状态及驳回原因

        //2.1 构建要更新的认证数据
        ApUserRealname apUserRealnameDB = new ApUserRealname();
        apUserRealnameDB.setId(dto.getId());
        apUserRealnameDB.setStatus(status);
        if(StringUtils.isNotBlank(dto.getMsg())){
            apUserRealnameDB.setReason(dto.getMsg());//设置驳回原因
        }

        //2.2 根据ID执行更新认证信息
        updateById(apUserRealnameDB);

        if(status == UserConstants.PASS_AUTH){
            //第三部分：创建自媒体用户 +  第四部分：创建文章作者 +  第五部分：更新ap_user的身份标识为自媒体人
            ResponseResult responseResult = createWmUserAndApAuthor(dto);
            if(responseResult!=null){
                return responseResult;
            }
        }


        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Autowired
    private ApUserMapper apUserMapper;

    @Autowired
    private IWemediaClient wemediaClient;

    @Autowired
    private IArticleClient articleClient;


    //创建自媒体用户 + 创建文章作者 + 更新ap_user的身份标识为自媒体人
    private ResponseResult createWmUserAndApAuthor(AuthDto dto) {
        //第三部分：创建自媒体用户

        //3.1 根据认证ID查询认证信息
        ApUserRealname apUserRealname = getById(dto.getId());
        Integer userId = apUserRealname.getUserId(); //获取APP用户ID

        //3.2根据userId查询ApUser信息
        ApUser apUser = apUserMapper.selectById(userId);
        if(apUser==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "APP用户不存在");
        }

        //3.3通过feign远程接口查询自媒体用户是否存在
        WmUser wmUser = wemediaClient.findByName(apUser.getName());
        if(wmUser!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "自媒体用户已经存在不能重复创建");
        }

        //3.4 通过feign远程接口创建自媒体用户
        wmUser = new WmUser();
        wmUser.setName(apUser.getName()); //登录名
        wmUser.setPassword(apUser.getPassword()); //密码
        wmUser.setType(0); //'账号类型：  0 个人 ， 1 企业  ， 2 子账号
        wmUser.setSalt(apUser.getSalt()); //盐
        wmUser.setImage(apUser.getImage());//用户图片
        wmUser.setStatus(9);//审核通过
        wmUser.setPhone(apUser.getPhone());//电话
        wmUser.setApUserId(apUser.getId());//app用户ID
        wmUser.setCreatedTime(new Date()); //创建时间
        wemediaClient.save(wmUser);

        //第四部分：创建文章作者
        ResponseResult responseResult = saveApAuthor(wmUser);
        if(responseResult!=null){
            return responseResult;
        }

        //TODO 设置自媒体用户表中的作者ID
//        //查询刚刚创建成功的文章作者
//        ApAuthor apAuthor = articleClient.findByUserId(wmUser.getApUserId());
//
//        //查询刚刚创建成功的自媒体用户
//        WmUser wmUserDB = wemediaClient.findByName(wmUser.getName());
//
//        WmUser wmuserDB = new WmUser();
//        wmuserDB.setApAuthorId(apAuthor.getId());
//        wmuserDB.setId(wmUserDB.getId());


        //第五部分：更新ap_user的身份标识为自媒体人
        ApUser apUserDB = new ApUser();
        apUserDB.setFlag((short)1);
        apUserDB.setId(apUser.getId());
        apUserMapper.updateById(apUserDB);

        return null;
    }

    private ResponseResult saveApAuthor(WmUser wmUser) {
        //第四部分：创建文章作者
        //4.1 通过远程feign接口根据app用户ID查询作者，并判断是否已经创建
        ApAuthor apAuthor = articleClient.findByUserId(wmUser.getApUserId());
        if(apAuthor!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST,"文章作者已经存在不能重复创建");
        }

        //4.2 通过远程feign接口创建文章作者
        apAuthor = new ApAuthor();
        apAuthor.setUserId(wmUser.getApUserId());//APP用户ID
        apAuthor.setType(2);//类型：  0 爬取数据 ，1 签约合作商  ，  2 平台自媒体人
        apAuthor.setName(wmUser.getName());//登陆用户名
        apAuthor.setCreatedTime(new Date());//创建时间

        //查询刚刚创建成功的自媒体用户
        WmUser wmUserDB = wemediaClient.findByName(wmUser.getName());
        apAuthor.setWmUserId(wmUserDB.getId());
        articleClient.save(apAuthor);

        return null;

    }
}
