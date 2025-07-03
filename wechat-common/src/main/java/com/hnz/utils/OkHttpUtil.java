package com.hnz.utils;

import com.hnz.grace.result.GraceJSONResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



@Slf4j
public class OkHttpUtil {

    public static GraceJSONResult get(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .get()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            return JsonUtils.jsonToPojo(res, GraceJSONResult.class);
        } catch (Exception e) {
            log.error("OkHttp get failed:", e);
        }
        return null;
    }

}
