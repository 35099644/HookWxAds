package com.beichen.hookwxads.control;

import com.beichen.hookwxads.utils.Details6_6_7;
import com.beichen.hookwxads.utils.HookHandleUtils;
import com.beichen.hookwxads.utils.MetadataType;

/**
 * 微信版本控制基类
 */
public class WxVerBase {
    /**
     * 微信X5浏览器内核对应WebViewClient的一个接口类
     */
    @Details6_6_7(type = MetadataType.CLASS, value = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI$i")
    public String cls_name_WebViewClient;


    /**
     * 微信承载WebView的界面类
     */
    @Details6_6_7(type = MetadataType.CLASS, value = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI")
    public String cls_name_WebViewUI;

    /**
     * 在外部类中的字段名
     */
    @Details6_6_7(type = MetadataType.FIELD, value = "mhH", ownClsName = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI", clsName = "com.tencent.mm.ui.widget.MMWebView")
    public String field_name_WebView;

    /**
     * 合适的时机注入JS对象方法名
     */
    @Details6_6_7(type = MetadataType.METHOD, value = "ant", ownClsName = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI", description = "此方法在onPageStarted方法之前调用,因此时机最合适")
    public String fun_name_InjectObject;

    /**
     * X5内核WebViewClient类中对应 shouldInterceptRequest 的方法名
     */
    @Details6_6_7(type = MetadataType.METHOD, value = "a", ownClsName = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI$i")
    public String fun_name_shouldInterceptRequest;

    /**
     * shouldInterceptRequest 方法参数0的类名
     */
    @Details6_6_7(type = MetadataType.CLASS, value = "com.tencent.xweb.WebView")
    public String cls_name_shouldInterceptRequest_arg0;

    /**
     * shouldInterceptRequest 方法参数1的类名
     */
    @Details6_6_7(type = MetadataType.CLASS, value = "com.tencent.xweb.l")
    public String cls_name_shouldInterceptRequest_arg1;

    /**
     * 微信封装的 shouldInterceptRequest 参数1类中获取url的方法名
     */
    @Details6_6_7(type = MetadataType.METHOD, value = "getUrl", ownClsName = "com.tencent.xweb.l")
    public String fun_name_SIR_arg1_getUrl;

    /**
     * 微信封装的 shouldInterceptRequest 返回类的类名
     */
    @Details6_6_7(type = MetadataType.CLASS, value = "com.tencent.xweb.m")
    public String cls_name_WebResourceResponse;

    /**
     * 封装 WebResourceResponse 类中获取编码的字段名
     */
    @Details6_6_7(type = MetadataType.FIELD, value = "mEncoding", ownClsName = "com.tencent.xweb.m")
    public String field_name_RR_mEncoding;

    /**
     * 封装 WebResourceResponse 类中获取数据流的字段名
     */
    @Details6_6_7(type = MetadataType.FIELD, value = "mInputStream", ownClsName = "com.tencent.xweb.m")
    public String field_name_RR_mInputStream;

    /**
     * 封装 WebResourceResponse 类中获取数类型的字段名
     */
    @Details6_6_7(type = MetadataType.FIELD, value = "mMimeType", ownClsName = "com.tencent.xweb.m")
    public String field_name_RR_mMimeType;

    /**
     * 微信 WebViewClient 封装类对应 onLoadResource 方法名
     */
    @Details6_6_7(type = MetadataType.METHOD, value = "f", ownClsName = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI$i")
    public String fun_name_onLoadResource;

    /**
     * 微信 WebViewClient 封装类 onLoadResource 参数0的类名
     */
    @Details6_6_7(type = MetadataType.CLASS, value = "com.tencent.xweb.WebView")
    public String cls_name_onLoadResource_arg0;

    /**
     * 微信 WebViewClient 封装类 onPageFinished 方法名
     */
    @Details6_6_7(type = MetadataType.METHOD, value = "a", ownClsName = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI$i")
    public String fun_name_onPageFinished;

    /**
     * 微信 WebViewClient 封装类 onPageFinished 参数0类名
     */
    @Details6_6_7(type = MetadataType.CLASS, value = "com.tencent.xweb.WebView")
    public String cls_name_onPageFinished_arg0;


    /**
     * 微信X5内核WebView下载ajax脚本地址的关键字,更新应该不是很频繁
     */
    @Details6_6_7(type = MetadataType.OTHER, value = "/mmbizwap/zh_CN/htmledition/js/biz_wap/utils/ajax3d3b85.js", description = "要替换的js脚本地址关键字,在shouldInterceptRequest中进行拦截")
    public String filterX5WebViewAdsJsStr = "/mmbizwap/zh_CN/htmledition/js/biz_wap/utils/ajax3d3b85.js";


    @Details6_6_7(type = MetadataType.OTHER, value = "/mp/getappmsgext?f=json&mock=", description = "公众号留言广告获取地址的关键字")
    public String filterX5WebViewAdsKeywords0 = "/mp/getappmsgext?f=json&mock=";

    /**
     * 微信日志类名
     */
    @Details6_6_7(type = MetadataType.CLASS, value = "com.tencent.mm.sdk.platformtools.x")
    public String cls_name_Log;


    public WxVerBase(String ver){
        switch (ver){
            case "6.6.7":
                HookHandleUtils.initSetting(this, Details6_6_7.class);
                break;
            default:
                HookHandleUtils.initSetting(this, Details6_6_7.class);
                break;
        }
    }
}
