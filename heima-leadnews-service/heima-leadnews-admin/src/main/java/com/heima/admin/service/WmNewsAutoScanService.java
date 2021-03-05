package com.heima.admin.service;

public interface WmNewsAutoScanService {

    /**
     * 通过文章ID完成自动审核
     * @param id
     */
    void wmNewAutoScanById(Integer id);
}
