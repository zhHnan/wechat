package com.hnz.utils;

import com.hnz.result.R;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



@Slf4j
public class OkHttpUtil {

    public static R get(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().get().url(url).build();
            Response response = client.newCall(request).execute();
            String res = null;
            if (response.body() != null) {
                res = response.body().string();
            }
            return JsonUtils.jsonToPojo(res, R.class);
        } catch (Exception e) {
            log.error("OkHttp get failed:", e);
        }
        return null;
    }

}
