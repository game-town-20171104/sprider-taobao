package com.ylfin.spider;

import okhttp3.*;
import sun.misc.BASE64Encoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoteClient {


    public static void main(String[] args) throws IOException {
        String codeUrl = "http://vote.paynews.net/vote/static/images/kaptcha.jpg";
        String savePath = "D://kaptcha.jpg";
        OkHttpClient client = VoteClient.getClient();
        downLoand(client,savePath,codeUrl);

    }

    public static String  downLoand(OkHttpClient client,String savePath,String codeUrl){
        final String[] base64 = {""};
        final Request request = new Request.Builder()
                .url(codeUrl).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                System.out.println("下载失败");
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(  response.toString());
                InputStream is = null;
                byte[] data ;
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录

                try {
                    is = response.body().byteStream();
                    data = new byte[is.available()];

                    is.read(data);
                    fos.flush();
                    // 下载完成
                    BASE64Encoder encoder = new BASE64Encoder();

                    base64[0] =encoder.encode(data);
                    System.out.println("下载完成");
                } catch (Exception e) {
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
        return  base64[0];
    }


    public String decodeKaptcha(){

        return "";
    }


    public static OkHttpClient getClient() throws IOException {
        OkHttpClient client ;

        CookieJar cookieJar = new CookieJar() {
            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
                cookieStore.put(HttpUrl.parse("http://vote.paynews.net"), cookies);
                for (Cookie cookie : cookies) {
                    System.out.println("cookie Name:" + cookie.name());
                    System.out.println("cookie Path:" + cookie.path());
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = cookieStore.get(HttpUrl.parse("http://vote.paynews.net"));
                if (cookies == null) {
                    System.out.println("没加载到cookie");
                }
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };

        client = new OkHttpClient();
        client.newBuilder().cookieJar(cookieJar).build();

        return client;
    }
}
