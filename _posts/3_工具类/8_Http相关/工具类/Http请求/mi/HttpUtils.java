package com.hlj.util.Z016_Http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * OKHttp3为基础的http调用工具
     <!-- OkHttp3 -->
         <dependency>
             <groupId>com.squareup.okhttp3</groupId>
             <artifactId>okhttp</artifactId>
             <version>3.11.0</version>
         </dependency>
 */
@Slf4j
public class HttpUtils {

    private static OkHttpClient client = null;


    public static final MediaType JSON  = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType XML   = MediaType.parse("application/xml;charset=utf-8");
    public static final MediaType FROM  = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
    public static final MediaType FROM_DATA  = MediaType.parse("multipart/form-data;charset=utf-8");

    static {

        client = new OkHttpClient.Builder()

                // 1、目前是信任所有证书
                .sslSocketFactory(createSSLSocketFactory(),new TrustAllCerts())
                .hostnameVerifier(new TrustAllHostnameVerifier())

                // 2、添加自己的证书
                //.sslSocketFactory(createSSLSocketFactory())

                //超时配置
                .readTimeout(50, TimeUnit.SECONDS)
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool())
                .build();
        client.dispatcher().setMaxRequests(400);
        client.dispatcher().setMaxRequestsPerHost(20);
    }




    /**
     *  Get调用
     * @param url
     * @param params 发送内容
     * @return string
     */
    public static HttpEntity doGet (String url , Map<String,String> params, Map<String, String> headersParams)  {
        //  url中添加参数
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder builder = httpUrl.newBuilder();
        if(params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        //url添加参数完成
        httpUrl = builder.build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .headers(setHeaders(headersParams))
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            return new HttpEntity(response.code(),response.body().string());
        } catch (Exception e) {
            log.error("HTTP调用失败,", e);
            throw new RuntimeException("HTTP调用失败", e);
        }
    }

    /**
     *  Get异步调用
     * @param url
     * @param params 发送内容
     * @return string
     */
    public static void doGetAsyn (String url ,  Map<String,String> headersParams, Map<String,String> params, Consumer<HttpEntity> consumer)  {
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder builder = httpUrl.newBuilder();
        for(Map.Entry<String,String> entry : params.entrySet()){
            builder.addQueryParameter(entry.getKey(),entry.getValue());
        }
        httpUrl = builder.build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .headers(setHeaders(headersParams))
                .get()
                .build();
        //异步调用
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("HTTP调用失败,", e);
                throw new RuntimeException("HTTP调用失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                HttpEntity httpEntity = new HttpEntity(response.code(), response.body().string());
                consumer.accept(httpEntity);
            }
        });
    }


    /**
     *  Post调用 map提交
     * @param url
     * @param params 发送参数
     * @return string
     */
    public static HttpEntity doPostFrom (String url,  Map<String, String> params, Map<String, String> headersParams)  {
        FormBody.Builder builder = new FormBody.Builder();
        if(params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .headers(setHeaders(headersParams))
                .build();
        try {
            Response response = client.newCall(request).execute();
            return new HttpEntity(response.code(),response.body().string());
        } catch (Exception e) {
            log.error("HTTP调用失败,", e);
            throw new RuntimeException("HTTP调用失败", e);
        }
    }



    /**
     * Post调用（支持上传文件）
     *
     * @param url
     * @param params 发送参数
     * @return string
     */
    public HttpEntity doPostFrom(String url, Map<String, String> params, List<HttpFileEntity> files, Map<String, String> headersParams) {
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
        multiBuilder.setType(MultipartBody.FORM);
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                multiBuilder.addFormDataPart(key, value);
            }
        }
        if (files != null && !files.isEmpty()) {
            for (HttpFileEntity entry : files) {
                multiBuilder.addFormDataPart(entry.getFileKey(), entry.getFile().getName(),
                        MultipartBody.create(MediaType.parse(entry.getMime()), entry.getFile()));
            }
        }
        if (headersParams == null) {
            headersParams = new HashMap<>(2);
        }
        headersParams.put("charset", "utf-8");
        Request request = new Request.Builder()
                .url(url)
                .post(multiBuilder.build())
                .headers(setHeaders(headersParams))
                .build();
        try {
            Response response = client.newCall(request).execute();
            return new HttpEntity(response.code(), response.body().string());
        } catch (Exception e) {
            log.error("HTTP调用失败", e);
            return null;
        }
    }


    /**
     * Post调用（支持上传文件）
     */
    public HttpEntity doPostFromFile(String url, Map<String, String> params, List<HttpFileEntity> files, Map<String, String> headersParams) {
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
        multiBuilder.setType(MultipartBody.FORM);
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                multiBuilder.addFormDataPart(key, value);
            }
        }
        if (files != null && !files.isEmpty()) {
            for (HttpFileEntity entry : files) {
                multiBuilder.addFormDataPart(entry.getFileKey(), entry.getFile().getName(),
                        MultipartBody.create(MediaType.parse(entry.getMime()), entry.getFile()));
            }
        }
        if (headersParams == null) {
            headersParams = new HashMap<>(2);
        }
        headersParams.put("charset", "utf-8");
        Request request = new Request.Builder()
                .url(url)
                .post(multiBuilder.build())
                .headers(setHeaders(headersParams))
                .build();
        try {
            Response response = client.newCall(request).execute();
            return new HttpEntity(response.code(), response.body().string());
        } catch (Exception e) {
            log.error("HTTP调用失败", e);
            return null;
        }
    }


    /**
     *  Post调用 根据 xml 或 json 调用
     * @param type  类型
     * @param url
     * @param content 发送内容
     * @return string
     */
    public static HttpEntity doPost (MediaType type, String url, String content, Map<String, String> headersParams)  {
        RequestBody body = RequestBody.create(type, content);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(setHeaders(headersParams))
                .build();
        try {
            Response response = client.newCall(request).execute();
            return new HttpEntity(response.code(),response.body().string());
        } catch (Exception e) {
            log.error("HTTP调用失败,", e);
            throw new RuntimeException("HTTP调用失败", e);
        }
    }



    /**
     *  Put调用 map提交
     * @param url
     * @param params 发送参数
     * @return string
     */
    public static HttpEntity doPutForm ( String url,  Map<String, String> params, Map<String, String> headersParams)  {
        FormBody.Builder builder = new FormBody.Builder();
        if(params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .put(formBody)
                .headers(setHeaders(headersParams))
                .build();
        try {
            Response response = client.newCall(request).execute();
            return new HttpEntity(response.code(),response.body().string());
        } catch (Exception e) {
            log.error("HTTP调用失败,", e);
            throw new RuntimeException("HTTP调用失败", e);
        }
    }



    /**
     *  Put调用 根据 xml 或 json 等不同类型调用 调用
     * @param type  类型
     * @param url
     * @param content 发送内容
     * @return string
     */
    public static HttpEntity doPut (MediaType type, String url, String content, Map<String, String> headersParams)  {
        RequestBody body = RequestBody.create(type, content);
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeaders(headersParams))
                .put(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return new HttpEntity(response.code(),response.body().string());
        } catch (Exception e) {
            log.error("HTTP调用失败,", e);
            throw new RuntimeException("HTTP调用失败", e);
        }
    }


    /**
     *  Delete调用
     * @param url
     * @param params 发送参数
     * @return string
     */
    public static HttpEntity doDeleteForm ( String url,  Map<String, String> params, Map<String, String> headersParams)  {
        FormBody.Builder builder = new FormBody.Builder();
        if(params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .delete(formBody)
                .headers(setHeaders(headersParams))
                .build();
        try {
            Response response = client.newCall(request).execute();
            return new HttpEntity(response.code(),response.body().string());
        } catch (Exception e) {
            log.error("HTTP调用失败,", e);
            throw new RuntimeException("HTTP调用失败", e);
        }
    }


    /**
     *  Delete调用
     * @param type  类型
     * @param url
     * @param content 发送内容
     * @return string
     */
    public static HttpEntity doDelete (MediaType type, String url, String content, Map<String, String> headersParams)  {
        RequestBody body = RequestBody.create(type, content);
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeaders(headersParams))
                .delete(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return new HttpEntity(response.code(),response.body().string());
        } catch (Exception e) {
            log.error("HTTP调用失败,", e);
            throw new RuntimeException("HTTP调用失败", e);
        }
    }


    /**
     * 设置请求头
     * @return
     */
    private static Headers setHeaders(Map<String, String> headersParams){
        okhttp3.Headers.Builder headersbuilder=new okhttp3.Headers.Builder();
        if(headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                headersbuilder.add(key, headersParams.get(key));
            }
        }
        Headers headers=headersbuilder.build();
        return headers;
    }






    /**
     * 使用okttp3访问https时不配置证书或者忽略证书会报错：java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
     * 可以采用两种方式：
     * 第一种：根据自己的证书服务器来配置，达到一对一的效果，每个商业app都应该有自己的证书设置，这样能保证访问的安全性。
     * 第二种：在okhttp中设置信任所有证书 (如下)
     */

    /**
     *
     * HTTPS  --- 证书处理
     * 1、信任所有证书
     * 2、获取证书
     */
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            //1、信任所有证书
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()},new SecureRandom());
            //2、添加自己的证书
            //SSLContext  sc = getSSLContext("resource路径","证书密码");

             ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
        }
        return ssfFactory;
    }
    /**
     * HTTPS  --- 证书处理 信任所有证书
     * 1、信任所有证书
     */
    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }

    /**
     * HTTPS  --- 证书处理 信任所有证书
     * 1、信任所有证书
     */
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    /**
     * 获取Https证书
     * @return
     */
    private static SSLContext getSSLContext(String certPath, String certPass) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {

            // 证书 密码默认是商户号（仿微信支付）
            char[] password = certPass.toCharArray();

            ClassPathResource resource = new ClassPathResource(certPath);//cert/wechat/apiclient_cert.p12
            InputStream certinputStream = resource.getInputStream();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certinputStream, password);

            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
            return sslContext;
    }
}
