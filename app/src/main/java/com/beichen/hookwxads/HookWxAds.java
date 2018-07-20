package com.beichen.hookwxads;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.beichen.hookwxads.control.WxConst;
import com.beichen.hookwxads.utils.HookHandleUtils;
import com.beichen.hookwxads.utils.JSBridge;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookWxAds implements IXposedHookLoadPackage {
    private static final String TAG = "HookWxAds";
    private static WxConst wx;
    private JSBridge bridge = new JSBridge();


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.tencent.mm")){
            return;
        }
        Log.d(TAG, "开始Hook微信屏蔽广告");
        wx = new WxConst(HookHandleUtils.readWxSettings("wx_ver"));   // 这里在同步操作
        hookLog(loadPackageParam.classLoader);
        x5WebviewAds(loadPackageParam.classLoader);
        snsAds(loadPackageParam.classLoader);
    }

    private void snsAds(ClassLoader loader) {
        try {
            Class<?> AdSnsInfoDatabaseClass = loader.loadClass(wx.getValue(wx.fun_name_adsnsinfo_insert, WxConst.ownClsName));
            Class<?> AdSnsInfoClass = loader.loadClass(wx.getValue(wx.fun_name_adsnsinfo_insert, WxConst.funArg1Name));
            // 这里想将广告信息打印出来,因此获取父类的方法
            final Method getContentValuesMethod = AdSnsInfoClass.getMethod(wx.getValue(wx.fun_name_get_adsnsinfo));
            getContentValuesMethod.setAccessible(true);
            XposedHelpers.findAndHookMethod(AdSnsInfoDatabaseClass, wx.getValue(wx.fun_name_adsnsinfo_insert), long.class, AdSnsInfoClass, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    long snsId = (long) param.args[0];
                    Object obj = param.args[1];
                    Log.w(TAG + ".snsAds", "正在尝试添加朋友圈广告,已被禁止, snsId = " + snsId);
                    ContentValues contentValues = (ContentValues) getContentValuesMethod.invoke(obj);
                    Log.w(TAG + ".snsAds", "想要添加的广告信息为: " + contentValues.toString());
                    return true;
                }
            });
        }catch (Throwable e){
            XposedBridge.log(e);
            Log.e(TAG + ".snsAds", "Hook 朋友圈广告失败", e);
        }
    }

    private void x5WebviewAds(ClassLoader loader){
        try {
            final Class<?> cls_shouldInterceptRequest_arg0 = loader.loadClass(wx.getValue(wx.fun_name_shouldInterceptRequest, WxConst.funArg0Name));
            final Class<?> cls_shouldInterceptRequest_arg1 = loader.loadClass(wx.getValue(wx.fun_name_shouldInterceptRequest, WxConst.funArg1Name));
            final Class<?> cls_WebViewClinet_subclass = loader.loadClass(wx.getValue(wx.fun_name_shouldInterceptRequest, WxConst.ownClsName));
            final Class<?> cls_WebResponseResource_subclass = loader.loadClass(wx.getValue(wx.fun_name_shouldInterceptRequest, WxConst.returnName));

            // 获取url
            final Method getUrlMethod = cls_shouldInterceptRequest_arg1.getDeclaredMethod(wx.getValue(wx.fun_name_getUrl));
            getUrlMethod.setAccessible(true);

            XC_MethodHook callback = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String name = param.method.getName();
                    if (name.equals(wx.getValue(wx.fun_name_shouldInterceptRequest))){   // 这里 onPageFinished 方法名也是a,因此需要判断一下
                        if (param.args.length == 2){                        // onPageFinished
                            // 获取webview实例,并注入对象
                            Log.w(TAG + ".X5Ads", "onPageFinished 方法被调用");
                        }else if (param.args.length == 3){                  // shouldInterceptRequest
                            Object args_1 = param.args[1];
                            Uri uri = (Uri) getUrlMethod.invoke(args_1);
                            if (uri.toString().contains(wx.getValue(wx.filterX5WebViewAdsJsStr))){   // 这里采用拦截ajax是因为一次拦截即可,而不用每次都拦截广告请求
                                Log.w(TAG + ".X5Ads", "准备替换ajax3d3b85.js脚本来过滤广告数据");
                                param.setResult(HookHandleUtils.x5WebViewShouldInterceptRequest(uri.toString(), cls_WebResponseResource_subclass));
                            }
                        }
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String name = param.method.getName();
                    if (name.equals(wx.getValue(wx.fun_name_InjectObject))){
                        Field webviewField = param.thisObject.getClass().getDeclaredField(wx.getValue(wx.field_name_WebView));
                        webviewField.setAccessible(true);
                        Object webView = webviewField.get(param.thisObject);
                        Method aJIMethod = webView.getClass().getMethod("addJavascriptInterface", Object.class, String.class);
                        aJIMethod.setAccessible(true);
                        aJIMethod.invoke(webView, bridge, "MYJSAPI");     // 这里注入时机没选好,应该在ant方法中注入
                        Log.w(TAG + ".X5Ads", "注入JSAPI对象成功");
                    }
                }
            };

            XposedHelpers.findAndHookMethod(cls_WebViewClinet_subclass, wx.getValue(wx.fun_name_shouldInterceptRequest), cls_shouldInterceptRequest_arg0, cls_shouldInterceptRequest_arg1, Bundle.class, callback);
            Class<?> cls_onPageFinished_arg0 = loader.loadClass(wx.getValue(wx.fun_name_onPageFinished, WxConst.funArg0Name));
            XposedHelpers.findAndHookMethod(cls_WebViewClinet_subclass, wx.getValue(wx.fun_name_onPageFinished), cls_onPageFinished_arg0, String.class, callback);
            XposedHelpers.findAndHookMethod(wx.getValue(wx.fun_name_InjectObject, WxConst.ownClsName), loader, wx.getValue(wx.fun_name_InjectObject), callback);
        }catch (Throwable e){
            XposedBridge.log(e);
            Log.e(TAG + ".X5Ads", "Hook X5内核WebView广告失败", e);
        }
    }

    private void hookLog(ClassLoader loader) {
        try {
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

            Class<?> logClass = loader.loadClass(wx.getValue(wx.cls_name_Log));
            XposedHelpers.findAndHookMethod(logClass, "f", String.class, String.class, Object[].class, logCallback);
            XposedHelpers.findAndHookMethod(logClass, "e", String.class, String.class, Object[].class, logCallback);
            XposedHelpers.findAndHookMethod(logClass, "w", String.class, String.class, Object[].class, logCallback);
            XposedHelpers.findAndHookMethod(logClass, "i", String.class, String.class, Object[].class, logCallback);
            XposedHelpers.findAndHookMethod(logClass, "d", String.class, String.class, Object[].class, logCallback);
            XposedHelpers.findAndHookMethod(logClass, "v", String.class, String.class, Object[].class, logCallback);
            XposedHelpers.findAndHookMethod(logClass, "k", String.class, String.class, Object[].class, logCallback);
            XposedHelpers.findAndHookMethod(logClass, "l", String.class, String.class, Object[].class, logCallback);

        }catch (Throwable e){
            XposedBridge.log(e);
            Log.e(TAG + ".log", "Hook 微信日志失败", e);
        }

    }
}
