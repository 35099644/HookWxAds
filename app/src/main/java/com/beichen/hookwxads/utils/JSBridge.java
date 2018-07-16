package com.beichen.hookwxads.utils;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * JS掉java类,在脚本中发现一些 "undefined" != typeof JSAPI && JSAPI.log && JSAPI.log(e), 因此为了复用则直接注入JSAPI对象
 */
public class JSBridge {
    private final static String TAG = "JSBridge";

    /** 取log名是为了方便在已存在的js脚本中不用改就能调用
     * @param str
     */
    @JavascriptInterface
    public void log(String str){
        Log.w(TAG, str);
    }
}
