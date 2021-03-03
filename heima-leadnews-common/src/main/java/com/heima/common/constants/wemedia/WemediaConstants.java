package com.heima.common.constants.wemedia;

/**
 * @Description: 素材常量
 * @Version: V1.0
 */
public class WemediaConstants {

    public static final Short COLLECT_MATERIAL = 1;//收藏

    public static final Short CANCEL_COLLECT_MATERIAL = 0;//取消收藏


    public static final String WM_NEWS_TYPE_IMAGE = "image";

    public static final Short WM_NEWS_NONE_IMAGE = 0; //无图布局
    public static final Short WM_NEWS_SINGLE_IMAGE = 1; //单图布局
    public static final Short WM_NEWS_MANY_IMAGE = 3;//多图布局
    public static final Short WM_NEWS_TYPE_AUTO = -1; //自动布局

    public static final Short WM_CONTENT_REFERENCE = 0; //正文（内容）引用
    public static final Short WM_COVER_REFERENCE = 1; //封面（主图）引用

    public static final Short WM_NEWS_ENABLE_UP = 1;//文章上架状态
    public static final Short WM_NEWS_ENABLE_DOWN = 0; //文章下架状态
}