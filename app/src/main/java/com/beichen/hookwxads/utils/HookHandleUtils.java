package com.beichen.hookwxads.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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

                // 同样在ajax脚本中也可以拦截其它请求数据, 这里 MYJSAPI对象直接注入,因此不判断是否存在了
                // if(url.search('/mp/getappmsgext\\?f=json&mock=') == 0){ if("undefined" != typeof resp.advertisement_num){ resp.advertisement_num = 0, resp.advertisement_info = []; MYJSAPI.log('留言广告修改后: ' + JSON.stringify(resp));}}
                // 这里拦截是视频广告由于没有什么返回类型为json因此需要执行eval函数,执行完替换后还应该还原为字符串
                // if(url.search('/mp/ad_video\\?uin') == 0){ responseText = eval("(" + resp + ")"); if("undefined" != typeof responseText.video_ad_item_list){responseText.video_ad_item_list = [];resp = JSON.stringify(responseText); MYJSAPI.log('视频广告修改后: ' + resp);}}
                // obj.success&&obj.success(resp);
                ret = ret.replace("obj.success&&obj.success(resp);",
                        "try{" +
                                "if(url.search('/mp/getappmsgext\\\\?f=json&mock=') == 0){ if(\"undefined\" != typeof resp.advertisement_num){ resp.advertisement_num = 0, resp.advertisement_info = []; MYJSAPI.log('留言广告修改后: ' + JSON.stringify(resp));}}" +
                                "if(url.search('/mp/ad_video\\\\?uin') == 0){ responseText = eval(\"(\" + resp + \")\"); if(\"undefined\" != typeof responseText.video_ad_item_list){responseText.video_ad_item_list = [];resp = JSON.stringify(responseText); MYJSAPI.log('视频广告修改后: ' + resp);}}" +
                                "}catch(err){ " + "MYJSAPI.log && MYJSAPI.log('修改错误: ' + err.message);}" +
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


    /** 获取微信版本号,用于多版本匹配
     * @param context
     * @return
     */
    public static String getWXVerName(Context context){
        try {
            return context.getPackageManager().getPackageInfo("com.tencent.mm", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "获取版本失败", e);
        }
        return null;
    }

    /** 写入配置文件,这里写入时覆盖的,因此json必须是完整的
     * @param object
     */
    public static void saveWxSettings(JSONObject object){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e(TAG, "没有正确挂载SD卡,无法读写数据");
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory(), "beichen_settings");
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(object.toString());
            fw.close();
        } catch (IOException e) {
            Log.e(TAG, "写入配置文件 beichen_settings 失败", e);
        }
    }

    public static String readWxSettings(String key){
        JSONObject jsonObject = readWxSettings();
        if (jsonObject == null){    // 为空可能没有配置文件或读取出错,都可以尝试写入
            jsonObject = new JSONObject();
        }
        return jsonObject.optString(key);
    }


    public static JSONObject readWxSettings(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e(TAG, "没有正确挂载SD卡,无法读写数据");
            return null;
        }
        File file = new File(Environment.getExternalStorageDirectory(), "beichen_settings");
        if (!file.exists()){
            return new JSONObject();
        }
        if (file.exists() && !file.isFile()){
            file.delete();
            return new JSONObject();
        }
        BufferedReader br = null;
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try {
            String line = "";
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            fis.close();
            br.close();
        }catch (Exception e) {
            try {
                if (fis != null) fis.close();
                if (br != null) br.close();
            } catch (Exception e2) {
                Log.e(TAG, "读取配置文件 beichen_settings 失败", e2);
            }
        }
        if (sb.toString().isEmpty()){
            return null;
        }else {
            try {
                return new JSONObject(sb.toString());
            } catch (JSONException e) {
                Log.e(TAG, "读取配置文件 beichen_settings 失败", e);
            }
        }
        return null;
    }


}
