package com.beichen.hookwxads;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.beichen.hookwxads.control.WxVerBase;
import com.beichen.hookwxads.utils.HookHandleUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String ver = HookHandleUtils.getWXVerName(this);
        String hint = TextUtils.isEmpty(ver) ? "没有获取到微信版本号" : "当前微信版本号: " + ver;
        Toast.makeText(this, hint, Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(ver)){
            try {
                HookHandleUtils.saveWxSettings(new JSONObject().put("wx_ver", ver));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
