package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news/")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;


    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto){
        return wmNewsService.list(dto);
    }

    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        if(dto.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
            return wmNewsService.submit(dto, WmNews.Status.SUBMIT.getCode()); //提交文章
        } else {
            return wmNewsService.submit(dto,WmNews.Status.NORMAL.getCode());// 保存草稿
        }
    }


    @GetMapping("/one/{id}")
    public ResponseResult findOne(@PathVariable("id") Integer id){
        return wmNewsService.findOne(id);
    }


    @GetMapping("/del_news/{id}")
    public ResponseResult delOne(@PathVariable("id") Integer id){
        return wmNewsService.delOne(id);
    }

    @PostMapping("/down_or_up")
    public ResponseResult downOrUp(@RequestBody WmNewsDto dto) {
        return wmNewsService.downOrUp(dto);
    }

    /**
     * 查询文章列表
     * @param dto
     * @return
     */
    @PostMapping("/list_vo")
    public ResponseResult findList(@RequestBody NewsAuthDto dto) {
        return wmNewsService.findList(dto);
    }
}
