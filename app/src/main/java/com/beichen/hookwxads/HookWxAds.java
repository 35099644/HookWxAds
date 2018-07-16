package com.beichen.hookwxads;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.beichen.hookwxads.control.WxVer6_6_7;
import com.beichen.hookwxads.control.WxVerBase;
import com.beichen.hookwxads.utils.Details;
import com.beichen.hookwxads.utils.HookHandleUtils;
import com.beichen.hookwxads.utils.JSBridge;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookWxAds implements IXposedHookLoadPackage {
    private static final String TAG = "HookWxAds";
    private static WxVerBase wx;

    @Details (clsName = "com.tencent.mm.ui.widget.MMWebView", fieldName = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI.mhH")
    private Object webView;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.tencent.mm")){
            return;
        }
        Log.d(TAG, "开始Hook微信屏蔽广告");
        wx = new WxVer6_6_7();
        hookLog(loadPackageParam.classLoader);
        x5WebviewAds(loadPackageParam.classLoader);

    }

    private void x5WebviewAds(ClassLoader loader){
        try {
            Class<?> cls_sIR_arg0 = loader.loadClass(wx.cls_name_shouldInterceptRequest_arg0);
            Class<?> cls_sIR_arg1 = loader.loadClass(wx.cls_name_shouldInterceptRequest_arg1);
            final Class<?> cls_WVC_subclass = loader.loadClass(wx.cls_name_WebViewClient);
            Class<?> cls_oLR_arg0 = loader.loadClass(wx.cls_name_onLoadResource_arg0);
            final Class<?> cls_WRR_subclass = loader.loadClass(wx.cls_name_WebResourceResponse);
            Class<?> cls_oPF_arg0 = loader.loadClass(wx.cls_name_onPageFinished_arg0);
            // 获取url
            final Method getUrlMethod = cls_sIR_arg1.getDeclaredMethod(wx.fun_name_SIR_arg1_getUrl);
            getUrlMethod.setAccessible(true);
            XC_MethodHook callback = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String name = param.method.getName();
                    if (name.equals(wx.fun_name_shouldInterceptRequest)){   // 这里 onPageFinished 方法名也是a,因此需要判断一下
                        if (param.args.length == 2){                        // onPageFinished
                            // 获取webview实例,并注入对象
                            Log.w(TAG, "onPageFinished 方法被调用");
                            Field field = cls_WVC_subclass.getDeclaredField(wx.field_name_WebViewUI);
                            field.setAccessible(true);
                            Object obj = field.get(param.thisObject);
                            Field webviewField = obj.getClass().getDeclaredField(wx.field_name_WebView);
                            webviewField.setAccessible(true);
                            webView = webviewField.get(obj);
                            Method aJIMethod = webView.getClass().getMethod("addJavascriptInterface", Object.class, String.class);
                            aJIMethod.setAccessible(true);
                            aJIMethod.invoke(webView, new JSBridge(), "JSAPI");     // 这里注入时机没选好,应该在ant方法中注入
                            Log.w(TAG, "注入JSAPI对象成功");
                        }else if (param.args.length == 3){                  // shouldInterceptRequest
                            Object args_1 = param.args[1];
                            Uri uri = (Uri) getUrlMethod.invoke(args_1);
                            if (uri.toString().contains(wx.filterX5WebViewAdsJsStr)){   // 这里采用拦截ajax是因为一次拦截即可,而不用每次都拦截广告请求
                                Log.w(TAG, "准备替换ajax3d3b85.js脚本来过滤广告数据");
                                param.setResult(HookHandleUtils.x5WebViewShouldInterceptRequest(uri.toString(), cls_WRR_subclass));
                            }
                        }
                    }else if (name.equals(wx.fun_name_onLoadResource)){
                        String addr = (String) param.args[1];
                        if (addr.contains(wx.filterX5WebViewAdsKeywords0)){
                            Log.w(TAG, "正在加载WebView广告资源,已过滤");
                        }
                    }
                }
            };
            XposedHelpers.findAndHookMethod(cls_WVC_subclass, wx.fun_name_shouldInterceptRequest, cls_sIR_arg0, cls_sIR_arg1, wx.cls_shouldInterceptRequest_arg2, callback);
            XposedHelpers.findAndHookMethod(cls_WVC_subclass, wx.fun_name_onLoadResource, cls_oLR_arg0, wx.cls_onLoadResource_arg1, callback);
            XposedHelpers.findAndHookMethod(cls_WVC_subclass, wx.fun_name_onPageFinished, cls_oPF_arg0, String.class, callback);
        }catch (Throwable e){
            XposedBridge.log(e);
            Log.e(TAG, "Hook X5内核WebView广告失败", e);
        }
    }

    private void hookLog(ClassLoader loader) throws Throwable{
        XC_MethodHook logCallback = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int level = 0;
                String name = param.method.getName();
                String arg0 = (String) param.args[0];
                String arg1 = (String) param.args[1];
                Object[] arg2 = (Object[]) param.args[2];
                String format = arg2 == null ? arg1 : String.format(arg1, arg2);
                if (TextUtils.isEmpty(format)){
                    format = "null";
                }
                if (name.equals("f") || name.equals("i")){
                    level = 0;
                }else if (name.equals("d") || name.equals("v") || name.equals("k") || name.equals("l")){
                    level = 1;
                }else if (name.equals("e") || name.equals("w")){
                    level = 2;
                }
                switch (level){
                    case 0:
                        Log.i(TAG + " " + name + " " + arg0, format);
                        break;
                    case 1:
                        Log.d(TAG + " " + name + " " + arg0, format);
                        break;
                    case 2:
                        Log.e(TAG + " " + name + " " + arg0, format);
                        break;
                }
            }
        };

        Class<?> logClass = loader.loadClass(wx.cls_name_Log);
        XposedHelpers.findAndHookMethod(logClass, "f", String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, "e", String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, "w", String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, "i", String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, "d", String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, "v", String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, "k", String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, "l", String.class, String.class, Object[].class, logCallback);

    }
}
