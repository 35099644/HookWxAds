package com.beichen.hookwxads.control;

public class WxVer6_6_7 extends WxVerBase {

    public WxVer6_6_7(){
        initX5WebViewAds();
        initLog();
        initFilterKeywords();
    }

    @Override
    protected void initX5WebViewAds() {
        cls_name_WebViewClient = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI$i";
        field_name_WebViewUI = "pZJ";
        field_name_WebView = "mhH";
        fun_name_shouldInterceptRequest = "a";
        cls_name_shouldInterceptRequest_arg0 = "com.tencent.xweb.WebView";
        cls_name_shouldInterceptRequest_arg1 = "com.tencent.xweb.l";
        fun_name_SIR_arg1_getUrl = "getUrl";
        cls_name_WebResourceResponse = "com.tencent.xweb.m";
        field_name_RR_mEncoding = "mEncoding";
        field_name_RR_mMimeType = "mMimeType";
        field_name_RR_mInputStream = "mInputStream";
        fun_name_onLoadResource = "f";
        cls_name_onLoadResource_arg0 = cls_name_shouldInterceptRequest_arg0;
        fun_name_onPageFinished = "a";
        cls_name_onPageFinished_arg0 = cls_name_shouldInterceptRequest_arg0;

    }

    @Override
    protected void initFilterKeywords() {

    }

    @Override
    protected void initLog() {
        cls_name_Log = "com.tencent.mm.sdk.platformtools.x";
    }
}
