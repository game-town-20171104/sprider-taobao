package com.ylfin.spider.utils;

import okhttp3.FormBody.Builder;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class OkHttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpUtil.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("text/xml; charset=utf-8");
    private static final int CONNECT_TIMEOUT = 60;
    private static final int READ_TIMEOUT = 600;
    private static final int WRITE_TIMEOUT = 60;
    private static final OkHttpClient client;
    private static final OkHttpClient sslClient;

    public OkHttpUtil() {
    }

    public static String postJson(String url, String json, Boolean ssl) throws IOException {
        return post(url, json, ssl, JSON);
    }

    public static String postAndGetJson(String url, String json, Boolean ssl) throws IOException {
        return postAndGetJson(url, json, ssl, JSON);
    }

    public static byte[] postJsonAndGetBytes(String url, String json, Boolean ssl) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        LOGGER.info("对外请求url:{}\tcontentType:{}\tbody:{}", new Object[]{url, body.contentType(), json});
        return post(url, body, ssl, false).bytes();
    }

    public static byte[] postFormAndGetBytes(String url, Map<String, String> map, Boolean ssl) throws IOException {
        Builder builder = new Builder();
        map.forEach((key, value) -> {
            builder.add(key, value);
        });
        RequestBody body = builder.build();
        LOGGER.info("对外流请求url:{}\tcontentType:{}\tbody:{}", new Object[]{url, body.contentType(), com.alibaba.fastjson.JSON.toJSONString(map)});
        return post(url, (RequestBody)body, ssl, (Boolean)false).bytes();
    }

    public static InputStream postJsonAndGetIs(String url, String json, Boolean ssl) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        LOGGER.info("对外流请求url:{}\tcontentType:{}\tbody:{}", new Object[]{url, body.contentType(), json});
        return post(url, body, ssl, false).byteStream();
    }

    public static InputStream postFormAndGetIs(String url, Map<String, String> map, Boolean ssl) throws IOException {
        Builder builder = new Builder();
        map.forEach((key, value) -> {
            builder.add(key, value);
        });
        RequestBody body = builder.build();
        LOGGER.info("对外流请求url:{}\tcontentType:{}\tbody:{}", new Object[]{url, body.contentType(), com.alibaba.fastjson.JSON.toJSONString(map)});
        return post(url, (RequestBody)body, ssl, (Boolean)false).byteStream();
    }

    public static String postForm(String url, Map<String, String> map, Boolean ssl) throws IOException {
        Builder builder = new Builder();
        map.forEach((key, value) -> {
            builder.add(key, value);
        });
        RequestBody body = builder.build();
        LOGGER.info("对外请求url:{}\tcontentType:{}\tbody:{}", new Object[]{url, body.contentType(), com.alibaba.fastjson.JSON.toJSONString(map)});
        String result = post(url, (RequestBody)body, ssl, (Boolean)false).string();
        LOGGER.info("对外请求返回:{}", result);
        return result;
    }

    public static String get(String url, Map<String, Object> params, Boolean ssl) throws IOException {
        if (params != null && params.size() > 0) {
            url = url + "?" + getParamsByMap(params);
        }

        LOGGER.info("对外请求url:{}", url);
        String result = get(url, ssl);
        LOGGER.info("对外请求返回:{}", result);
        return result;
    }

    private static String getParamsByMap(Map<String, Object> params) {
        if (params != null && params.size() > 0) {
            StringBuffer sb = new StringBuffer();
            params.forEach((key, value) -> {
                sb.append(key).append("=").append(value).append("&");
            });
            String rs = sb.toString();
            if (rs.endsWith("&")) {
                rs = rs.substring(0,rs.length()-1);
            }

            return rs;
        } else {
            return "";
        }
    }

    private static String get(String url, Boolean ssl) throws IOException {
        Request request = (new okhttp3.Request.Builder()).url(url).build();
        Response response;
        if (ssl.booleanValue()) {
            response = sslClient.newCall(request).execute();
        } else {
            response = client.newCall(request).execute();
        }

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static String postBytes(String url, byte[] bytes, Boolean ssl) throws IOException {
        RequestBody body = RequestBody.create(JSON, bytes);
        LOGGER.info("对外请求url:{}\tcontentType:{}\tbody:{}", new Object[]{url, body.contentType(), bytes.toString()});
        String result = post(url, body, ssl, false).string();
        LOGGER.info("对外请求返回:{}", result);
        return result;
    }

    private static ResponseBody post(String url, RequestBody body, Boolean ssl, Boolean acceptJson) throws IOException {
        Request request;
        if (acceptJson.booleanValue()) {
            request = (new okhttp3.Request.Builder()).addHeader("Accept", "application/json;charset=utf-8").url(url).post(body).build();
        } else {
            request = (new okhttp3.Request.Builder()).url(url).post(body).build();
        }

        Response response;
        if (ssl.booleanValue()) {
            response = sslClient.newCall(request).execute();
        } else {
            response = client.newCall(request).execute();
        }

        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static byte[] getBytes(String url, Boolean ssl) throws IOException {
        Request request = (new okhttp3.Request.Builder()).url(url).build();
        Response response;
        if (ssl.booleanValue()) {
            response = sslClient.newCall(request).execute();
        } else {
            response = client.newCall(request).execute();
        }

        if (response.isSuccessful()) {
            return response.body().bytes();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private static OkHttpClient getSslClient() {
        X509TrustManager xtm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };

        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init((KeyManager[])null, new TrustManager[]{xtm}, (SecureRandom)null);
        } catch (Exception var4) {
            LOGGER.warn(var4.getMessage(), var4);
            return null;
        }

        HostnameVerifier trustAll = (s, sslSession) -> {
            return true;
        };
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        return (new okhttp3.OkHttpClient.Builder()).sslSocketFactory(sslSocketFactory, xtm).hostnameVerifier(trustAll).connectTimeout(60L, TimeUnit.SECONDS).readTimeout(600L, TimeUnit.SECONDS).writeTimeout(60L, TimeUnit.SECONDS).build();
    }

    public static String postXml(String url, String xml, Boolean ssl) throws IOException {
        return post(url, xml, ssl, XML);
    }

    public static String post(String url, String param, Boolean ssl, MediaType mediaType) throws IOException {
        RequestBody body = RequestBody.create(mediaType, param);
        LOGGER.info("对外请求url:{}\tcontentType:{}\tbody:{}", new Object[]{url, body.contentType(), param});
        String result = post(url, body, ssl, false).string();
        LOGGER.info("对外请求返回:{}", result);
        return result;
    }

    public static String postAndGetJson(String url, String param, Boolean ssl, MediaType mediaType) throws IOException {
        RequestBody body = RequestBody.create(mediaType, param);
        LOGGER.info("对外请求url:{}\tcontentType:{}\tbody:{}", new Object[]{url, body.contentType(), param});
        String result = post(url, body, ssl, true).string();
        LOGGER.info("对外请求返回:{}", result);
        return result;
    }

    static {
        client = (new okhttp3.OkHttpClient.Builder()).connectTimeout(60L, TimeUnit.SECONDS).writeTimeout(60L, TimeUnit.SECONDS).readTimeout(600L, TimeUnit.SECONDS).build();
        sslClient = getSslClient();
    }
}
