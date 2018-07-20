package com.beichen.hookwxads.control;

import android.util.Log;

import com.beichen.hookwxads.utils.Details6_6_7;
import com.beichen.hookwxads.utils.MetadataType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 微信版本控制基类
 */
public class WxConst {

    private static final String TAG = "HookWxAds.Const";

    @Details6_6_7
    public static String ownClsName;        // 方法所属的类,用于反射获取所对应的注解值,与注解类中的方法名必须相同

    @Details6_6_7
    public static String funArg0Name;       // 方法对应的参数0的类名,用于反射获取所对应的注解值,与注解类中的方法名必须相同

    @Details6_6_7
    public static String funArg1Name;       // 方法对应的参数1的类名,用于反射获取所对应的注解值,与注解类中的方法名必须相同

    @Details6_6_7
    public static String funArg2Name;       // 方法对应的参数2的类名,用于反射获取所对应的注解值,与注解类中的方法名必须相同

    @Details6_6_7
    public static String funArg3Name;       // 方法对应的参数3的类名,用于反射获取所对应的注解值,与注解类中的方法名必须相同

    @Details6_6_7
    public static String returnName;         // 方法对应的返回值的类名,用于反射获取所对应的注解值,与注解类中的方法名必须相同


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
    @Details6_6_7(type = MetadataType.METHOD, value = "a", ownClsName = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI$i", funArg0Name = "com.tencent.xweb.WebView", funArg1Name = "com.tencent.xweb.l",
    returnName = "com.tencent.xweb.m")
    public String fun_name_shouldInterceptRequest;

    /**
     * 微信封装的 shouldInterceptRequest 参数1类中获取url的方法名
     */
    @Details6_6_7(type = MetadataType.METHOD, value = "getUrl", ownClsName = "com.tencent.xweb.l")
    public String fun_name_getUrl;

    /**
     * 微信 WebViewClient 封装类 onPageFinished 方法名
     */
    @Details6_6_7(type = MetadataType.METHOD, value = "a", ownClsName = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI$i", funArg0Name = "com.tencent.xweb.WebView")
    public String fun_name_onPageFinished;


    /**
     * 微信X5内核WebView下载ajax脚本地址的关键字,更新应该不是很频繁
     */
    @Details6_6_7(type = MetadataType.OTHER, value = "/mmbizwap/zh_CN/htmledition/js/biz_wap/utils/ajax3d3b85.js", description = "要替换的js脚本地址关键字,在shouldInterceptRequest中进行拦截")
    public String filterX5WebViewAdsJsStr = "/mmbizwap/zh_CN/htmledition/js/biz_wap/utils/ajax3d3b85.js";

    /**
     * 微信日志类名
     */
    @Details6_6_7(type = MetadataType.CLASS, value = "com.tencent.mm.sdk.platformtools.x")
    public String cls_name_Log;

    /**
     * 微信朋友圈插入广告数据操作的方法名
     */
    @Details6_6_7(type = MetadataType.METHOD, value = "a", ownClsName = "com.tencent.mm.plugin.sns.storage.f", funArg1Name = "com.tencent.mm.plugin.sns.storage.e")
    public String fun_name_adsnsinfo_insert;

    /**
     * 获取插入广告对应的ContentValues值
     */
    @Details6_6_7(type = MetadataType.METHOD, value = "wH", ownClsName = "com.tencent.mm.plugin.sns.storage.e", description = "该方法是继承到父类com.tencent.mm.g.c.f的方法")
    public String fun_name_get_adsnsinfo;


    Class<? extends Annotation> curCls;

    public WxConst(String ver){
        switch (ver){
            case "6.6.7":
                curCls = Details6_6_7.class;
                initSetting();
                break;
            default:
                curCls = Details6_6_7.class;
                initSetting();
                break;
        }
    }

    /**
     * 初始化这些字段,将其值设置为变量名,方便之后通过变量反射获取该变量的注解值
     */
    private void initSetting(){
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field field : fields){
                Annotation annotation = field.getAnnotation(curCls);
                if (annotation != null){
                    Object obj = Modifier.isStatic(field.getModifiers()) ? null : this;
                    Log.e(TAG, "初始化字段: " + field.getName() + " 设置value: " + field.getName());
                    field.set(obj, field.getName());
                }
            }
        }catch (Throwable e){
            Log.e(TAG, "修改值失败", e);
        }
    }

    /**
     * @param fieldName   字段名字 == 变量值
     * @return          返回字段默认注解 value 的值
     */
    public String getValue(String fieldName){
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            return getValue(field, "value");
        }catch (NoSuchFieldException e){
            Log.e(TAG, "获取申明字段失败, 字段名: " + fieldName);
        }
        return null;
    }

    /**
     * @param fieldName   字段名字 == 变量名
     * @param annoName  要获取的注解名
     * @return          字段对应的注解值
     */
    public String getValue(String fieldName, String annoName){
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            return getValue(field, annoName);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "获取申明字段失败, 字段名: " + fieldName);
        }
        return null;
    }

    /**
     * @param field     当前类的字段
     * @param annoName      当前字段的注解名
     * @return          返回注解值
     */
    private String getValue(Field field, String annoName){
        Annotation annotation = field.getAnnotation(curCls);
        if (annotation != null){
            try {
                String value = (String) annotation.annotationType().getDeclaredMethod(annoName).invoke(annotation);
                return value;
            } catch (Throwable e) {
                Log.e(TAG, "获取字段注解失败,字段名: " + field.getName() + " 注解名: " + annoName);
            }
        }
        return null;
    }
}
