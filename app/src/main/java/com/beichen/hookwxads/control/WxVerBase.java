package com.beichen.hookwxads.control;

import android.os.Bundle;

/**
 * 微信版本控制基类
 */
public abstract class WxVerBase {
    /**
     * 微信X5浏览器内核对应WebViewClient的一个接口类
     */
    public String cls_name_WebViewClient;
    /**
     * X5内核WebViewClient类中对应 shouldInterceptRequest 的方法名
     */
    public String fun_name_shouldInterceptRequest;
    /**
     * shouldInterceptRequest 方法参数0的类名
     */
    public String cls_name_shouldInterceptRequest_arg0;
    /**
     * shouldInterceptRequest 方法参数1的类名
     */
    public String cls_name_shouldInterceptRequest_arg1;
    /**
     * shouldInterceptRequest 方法参数2对应的类,5.0需要,固定为Bundle
     */
    public Class<?> cls_shouldInterceptRequest_arg2 = Bundle.class;
    /**
     * 微信封装的 shouldInterceptRequest 参数1类中获取url的方法名
     */
    public String fun_name_SIR_arg1_getUrl;

    /**
     * 微信封装的 shouldInterceptRequest 返回类的类名
     */
    public String cls_name_WebResourceResponse;
    /**
     * 封装 WebResourceResponse 类中获取编码的字段名
     */
    public String field_name_RR_mEncoding;
    /**
     * 封装 WebResourceResponse 类中获取数据流的字段名
     */
    public String field_name_RR_mInputStream;
    /**
     * 封装 WebResourceResponse 类中获取数类型的字段名
     */
    public String field_name_RR_mMimeType;

    /**
     * 微信 WebViewClient 封装类对应 onLoadResource 方法名
     */
    public String fun_name_onLoadResource;
    /**
     * 微信 WebViewClient 封装类 onLoadResource 参数0的类名
     */
    public String cls_name_onLoadResource_arg0;
    /**
     * 微信 WebViewClient 封装类 onLoadResource 参数1的类,固定为String
     */
    public Class<?> cls_onLoadResource_arg1 = String.class;

    /**
     * 微信X5内核WebView下载ajax脚本地址的关键字,更新应该不是很频繁
     */
    public String filterX5WebViewAdsJsStr = "/mmbizwap/zh_CN/htmledition/js/biz_wap/utils/ajax3d3b85.js";

    public String filterX5WebViewAdsKeywords0 = "/mp/getappmsgext?f=json&mock=";


    public String cls_name_Log;

    /**
     * 微信X5内核WebView加载广告有关类、方法、字段名初始化
     */
    protected abstract void initX5WebViewAds();

    /**
     * X5内核WebView广告过滤有关脚本及关键词初始化
     */
    protected abstract void initFilterKeywords();


    /**
     * 初始化微信Log有关类
     */
    protected abstract void initLog();
}
