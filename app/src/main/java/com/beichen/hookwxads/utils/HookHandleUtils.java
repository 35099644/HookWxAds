package com.beichen.hookwxads.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;

import de.robv.android.xposed.XposedBridge;

public class HookHandleUtils {
    private static final String TAG = "HookWxAds.Utils";
    public static Object x5WebViewShouldInterceptRequest(String addr, Class<?> webResourceResponse){
        addr = addr.replace("https", "http");   // http一样能获取到数据
        try {
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = conn.getInputStream();
                String mime = conn.getContentType();
                String encoding = conn.getContentEncoding();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String ret = "";
                String line = "";
                while ((line = br.readLine()) != null){
                    ret += line + '\n';
                }
                br.close();
                in.close();

                // 同样在ajax脚本中也可以拦截其它请求数据
                // if(url.search("/mp/getappmsgext?f=json&mock=")){if("undefined"!=typeof resp.advertisement_num){resp.advertisement_num=0,resp.advertisement_info=[];}}
                // 这里拦截是视频广告由于没有什么返回类型为json因此需要执行eval函数,执行完替换后还应该还原为字符串
                // if(url.search("/mp/ad_video?")){resp = eval("(" + resp + ")");if(resp.video_ad_item_list){resp.video_ad_item_list=[];resp = JSON.stringify(resp);}}
                // obj.success&&obj.success(resp);
                ret = ret.replace("obj.success&&obj.success(resp);",
                        "if(url.search(\"/mp/getappmsgext?f=json&mock=\")&&resp.advertisement_num){resp.advertisement_num=0,resp.advertisement_info=[]; \"undefined\" != typeof JSAPI && JSAPI.log && JSAPI.log(JSON.stringify(resp));}" +
//                 "if(url.search(\"/mp/ad_video?\")){resp = eval(\"(\" + resp + \")\");if(resp.video_ad_item_list){resp.video_ad_item_list=[];resp = JSON.stringify(resp);}}" +
                            "obj.success&&obj.success(resp);");
                InputStream is = new ByteArrayInputStream(ret.getBytes());
                if (is == null){return null;}
                if (TextUtils.isEmpty(encoding)){encoding = "utf-8";}
                Constructor constructor = webResourceResponse.getConstructor(String.class, String.class, InputStream.class);
                Log.w(TAG, "替换脚本成功");
                return constructor.newInstance(mime, encoding, is);
            }
            Log.e(TAG, "访问网络请求失败, addr: " + addr);
        }catch (Throwable e){
            XposedBridge.log(e);
            Log.e(TAG, "拦截失败", e);
        }
        return null;
    }
}
