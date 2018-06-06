package com.duodian.youhui.admin.utils;

import com.duodian.youhui.data.http.HttpBackBean;
import com.duodian.youhui.enums.http.SdkHttpCodeEnum;
import com.duodian.youhui.enums.http.SdkLogicCodeEnum;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by qingxu on 2016/11/17.
 * sdk 使用的http帮助类 勿动
 */
public class SdkHttpHelper {

    public static final String ENCODING_UTF8 = "UTF-8";
    public static final String ENCODING_GBK = "GBK";

    public static final String POST = "POST";
    public static final String GET  = "GET";

    private static Logger logger = LoggerFactory.getLogger(SdkHttpHelper.class);

    public static final int OVERTIME = 2000;

    private static RequestConfig defaultRequestConfig(Integer over_time) {
        if(over_time == null){
            return RequestConfig.custom().setSocketTimeout(OVERTIME).setConnectTimeout(OVERTIME).setConnectionRequestTimeout(OVERTIME).build();
        }else{
            return RequestConfig.custom().setSocketTimeout(over_time).setConnectTimeout(over_time).setConnectionRequestTimeout(over_time).build();
        }
    }


    public static HttpBackBean handleGet(String uri, Map<String,String> headers, Map<String,String> params, Integer over_time){
        HttpBackBean backBean = new HttpBackBean();
        CloseableHttpClient httpclient = null;
        HttpGet request = null;
        HttpResponse response = null;
        try {
            uri = StringUtils.trim(uri);
            if(uri.startsWith("https://")){
                SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
                sslcontext.init(null, new TrustManager[]{new SdkHttpHelper.MyTrustManager()}, null);
                httpclient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(sslcontext)).build();
            }else{
                httpclient = HttpClients.custom().build();
            }
            StringBuilder sb = new StringBuilder(uri);
            if(params !=null){
                if(uri.indexOf("?") == -1){
                    sb.append("?");
                }else{
                    sb.append("&");
                }
                Iterator it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String,String> entry = (Map.Entry) it.next();
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    if(it.hasNext()) sb.append("&");
                }
            }
            request = new HttpGet(sb.toString());
            request.setHeader("User-Agent","admore");
            if(headers != null){
                for(Map.Entry<String,String> entry:headers.entrySet()){
                    request.setHeader(entry.getKey(),entry.getValue());
                }
            }
            request.setConfig(defaultRequestConfig(over_time));
            backBean.setStart(System.currentTimeMillis());
            response = httpclient.execute(request);
            String result =  IOUtils.toString(response.getEntity().getContent(),ENCODING_UTF8);
            backBean.setResult(result);
            backBean.setStatusCode(response.getStatusLine().getStatusCode());
            backBean.setLogicCode(SdkLogicCodeEnum.正常.code);
        } catch (SocketTimeoutException e) {
            backBean.setStatusCode(SdkHttpCodeEnum.超时.code);
            backBean.setLogicCode(SdkLogicCodeEnum.超时.code);
            backBean.setResult(e.getMessage());
        } catch (ConnectTimeoutException e) {
            backBean.setStatusCode(SdkHttpCodeEnum.超时.code);
            backBean.setLogicCode(SdkLogicCodeEnum.超时.code);
            backBean.setResult(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            backBean.setResult(e.getMessage());
            if(response == null){
                backBean.setStatusCode(SdkHttpCodeEnum.系统内部错误.code);
                backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
            }else{
                backBean.setStatusCode(response.getStatusLine().getStatusCode());
                backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
            }
        } finally {
            backBean.setEnd(System.currentTimeMillis());
            IOUtils.closeQuietly(httpclient);
        }
        return backBean;
    }

    public static HttpBackBean handleGet(String uri, Map<String,String> headers, Map<String,String> params){
        return handleGet(uri,headers,params,OVERTIME);
    }

    public static HttpBackBean handlePostFormData(String uri, Map<String,String> headers, LinkedHashMap<String,String> params,Integer over_time){
        HttpBackBean backBean = new HttpBackBean();
        CloseableHttpClient httpclient = null;
        HttpPost request = null;
        HttpResponse response = null;
        try {
            request = new HttpPost(StringUtils.trim(uri));
            request.setConfig(defaultRequestConfig(over_time));
            if(uri.startsWith("https://")){
                SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
                sslcontext.init(null, new TrustManager[]{new SdkHttpHelper.MyTrustManager()}, null);
                httpclient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(sslcontext)).build();
            }else{
                httpclient = HttpClients.custom().build();
            }
            if(params != null){
                List<NameValuePair> formValue = new ArrayList<>();
                Iterator it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String,String> entry = (Map.Entry) it.next();
                    formValue.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
                }
                request.setEntity(new UrlEncodedFormEntity(formValue,"UTF-8"));
            }

            if(headers != null){
                for(Map.Entry<String,String> entry:headers.entrySet()){
                    request.setHeader(entry.getKey(),entry.getValue());
                }
            }
            request.setHeader("User-Agent","admore");
            backBean.setStart(System.currentTimeMillis());
            response = httpclient.execute(request);
            String result = IOUtils.toString(response.getEntity().getContent(),ENCODING_UTF8);
            backBean.setResult(result);
            backBean.setStatusCode(response.getStatusLine().getStatusCode());
            backBean.setLogicCode(SdkLogicCodeEnum.正常.code);
        } catch (ConnectTimeoutException e) {
            backBean.setStatusCode(SdkHttpCodeEnum.超时.code);
            backBean.setLogicCode(SdkLogicCodeEnum.超时.code);
            backBean.setResult(e.getMessage());
        } catch (Exception e) {
            backBean.setResult(e.getMessage());
            if(response == null){
                backBean.setStatusCode(SdkHttpCodeEnum.系统内部错误.code);
                backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
            }else{
                backBean.setStatusCode(response.getStatusLine().getStatusCode());
                backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
            }
        } finally {
            backBean.setEnd(System.currentTimeMillis());
            IOUtils.closeQuietly(httpclient);
        }
        return backBean;
    }

    public static HttpBackBean handlePostFormData(String uri, Map<String,String> headers, LinkedHashMap<String,String> params){
        return handlePostFormData(uri,headers,params,OVERTIME);
    }

    public static HttpBackBean handlePostURLEncodedData(String uri, Map<String,String> headers, LinkedHashMap<String,String> params,Integer over_time){
        HttpBackBean backBean = new HttpBackBean();
        CloseableHttpClient httpclient = null;
        HttpPost request = null;
        HttpResponse response = null;
        try {
            request = new HttpPost(StringUtils.trim(uri));
            if(uri.startsWith("https://")){
                SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
                sslcontext.init(null, new TrustManager[]{new SdkHttpHelper.MyTrustManager()}, null);
                httpclient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(sslcontext)).build();
            }else{
                httpclient = HttpClients.custom().build();
            }

            if(headers != null){
                for(Map.Entry<String,String> entry:headers.entrySet()){
                    request.setHeader(entry.getKey(),entry.getValue());
                }
            }

            if(params != null){
                List<NameValuePair> formValue = new ArrayList<>();
                Iterator it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String,String> entry = (Map.Entry) it.next();
                    formValue.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
                }
                request.setEntity(new UrlEncodedFormEntity(formValue,"UTF-8"));
            }
            request.setConfig(defaultRequestConfig(over_time));
            request.setHeader("User-Agent","admore");
            backBean.setStart(System.currentTimeMillis());
            response = httpclient.execute(request);
            String result = IOUtils.toString(response.getEntity().getContent(),ENCODING_UTF8);
            backBean.setResult(result);
            backBean.setStatusCode(response.getStatusLine().getStatusCode());
            backBean.setLogicCode(SdkLogicCodeEnum.正常.code);
        } catch (ConnectTimeoutException e) {
            backBean.setStatusCode(SdkHttpCodeEnum.超时.code);
            backBean.setResult(e.getMessage());
            backBean.setLogicCode(SdkLogicCodeEnum.超时.code);
        } catch (Exception e) {
            if(response == null){
                backBean.setStatusCode(SdkHttpCodeEnum.系统内部错误.code);
                backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
            }else{
                backBean.setStatusCode(response.getStatusLine().getStatusCode());
                backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
            }
            backBean.setResult(e.getMessage());
        } finally {
            backBean.setEnd(System.currentTimeMillis());
            IOUtils.closeQuietly(httpclient);
        }
        return backBean;
    }

    public static HttpBackBean handlePostURLEncodedData(String uri, Map<String,String> headers, LinkedHashMap<String,String> params){
        return handlePostURLEncodedData(uri,headers,params,OVERTIME);
    }

    public static HttpBackBean handlePostRaw(String url, Map<String,String> headers, LinkedHashMap<String,String> params,Integer over_time){
        HttpBackBean backBean = new HttpBackBean();
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String content = "";
        if(params != null){
            JSONObject object = JSONObject.fromObject(params);
            content = object.toString();
        }
        content = StringUtils.defaultIfEmpty(content,"");

        try {
            backBean.setStart(System.currentTimeMillis());
            URL request = new URL(StringUtils.trim(url));
            if(url.startsWith("https")){
                // 创建SSLContext对象，并使用我们指定的信任管理器初始化
                TrustManager[] tm = { new SdkHttpHelper.MyTrustManager() };
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                sslContext.init(null, tm, new java.security.SecureRandom());
                connection = (HttpsURLConnection) request.openConnection();
            }else{
                connection = (HttpURLConnection) request.openConnection();
            }
            connection.setRequestMethod(POST);
            connection.setRequestProperty("User-Agent","admore");
            connection.setDoOutput(true);
            if(headers != null){
                for(Map.Entry<String,String> entry:headers.entrySet()){
                    connection.setRequestProperty(entry.getKey(),entry.getValue());
                }
            }
            if(over_time == null){
                connection.setConnectTimeout(OVERTIME);
            }else{
                connection.setConnectTimeout(over_time);
            }
            output = connection.getOutputStream();
            output.write(content.getBytes(ENCODING_UTF8));
            input = connection.getInputStream();
            String result =  IOUtils.toString(input, ENCODING_UTF8);
            backBean.setResult(result);
            backBean.setStatusCode(connection.getResponseCode());
        } catch (SocketTimeoutException e) {
            try {
                backBean.setResult(e.getMessage());
                backBean.setStatusCode(connection.getResponseCode());
                backBean.setLogicCode(SdkLogicCodeEnum.超时.code);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            try {
                backBean.setResult(e.getMessage());
                backBean.setStatusCode(connection.getResponseCode());
                backBean.setLogicCode(SdkLogicCodeEnum.系统内部错误.code);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            backBean.setResult(e.getMessage());
            backBean.setStatusCode(SdkHttpCodeEnum.请求未发出.code);
            backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
        } catch (KeyManagementException e) {
            backBean.setResult(e.getMessage());
            backBean.setStatusCode(SdkHttpCodeEnum.请求未发出.code);
            backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
        } catch (NoSuchProviderException e) {
            backBean.setResult(e.getMessage());
            backBean.setStatusCode(SdkHttpCodeEnum.请求未发出.code);
            backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
        } finally {
            backBean.setEnd(System.currentTimeMillis());
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
            if(connection != null) {
                connection.disconnect();
            }
        }
        return backBean;
    }

    public static HttpBackBean handlePostRaw(String url, Map<String,String> headers, LinkedHashMap<String,String> params){
        return handlePostRaw(url,headers,params,OVERTIME);
    }


    public static HttpBackBean handlePostRaw(String url, Map<String,String> headers, String content,Integer over_time){
        HttpBackBean backBean = new HttpBackBean();
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        content = StringUtils.defaultIfEmpty(content,"");

        try {
            backBean.setStart(System.currentTimeMillis());
            URL request = new URL(StringUtils.trim(url));
            if(url.startsWith("https")){
                // 创建SSLContext对象，并使用我们指定的信任管理器初始化
                TrustManager[] tm = { new SdkHttpHelper.MyTrustManager() };
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                sslContext.init(null, tm, new java.security.SecureRandom());
                connection = (HttpsURLConnection) request.openConnection();
            }else{
                connection = (HttpURLConnection) request.openConnection();
            }
            connection.setRequestMethod(POST);
            connection.setRequestProperty("User-Agent","admore");
            connection.setDoOutput(true);
            if(headers != null){
                for(Map.Entry<String,String> entry:headers.entrySet()){
                    connection.setRequestProperty(entry.getKey(),entry.getValue());
                }
            }
            if(over_time == null){
                connection.setConnectTimeout(OVERTIME);
            }else{
                connection.setConnectTimeout(over_time);
            }
            output = connection.getOutputStream();
            output.write(content.getBytes(ENCODING_UTF8));
            input = connection.getInputStream();
            String result =  IOUtils.toString(input, ENCODING_UTF8);
            backBean.setResult(result);
            backBean.setStatusCode(connection.getResponseCode());
        } catch (SocketTimeoutException e) {
            try {
                backBean.setResult(e.getMessage());
                backBean.setStatusCode(connection.getResponseCode());
                backBean.setLogicCode(SdkLogicCodeEnum.超时.code);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            try {
                backBean.setResult(e.getMessage());
                backBean.setStatusCode(connection.getResponseCode());
                backBean.setLogicCode(SdkLogicCodeEnum.系统内部错误.code);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            backBean.setResult(e.getMessage());
            backBean.setStatusCode(SdkHttpCodeEnum.请求未发出.code);
            backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
        } catch (KeyManagementException e) {
            backBean.setResult(e.getMessage());
            backBean.setStatusCode(SdkHttpCodeEnum.请求未发出.code);
            backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
        } catch (NoSuchProviderException e) {
            backBean.setResult(e.getMessage());
            backBean.setStatusCode(SdkHttpCodeEnum.请求未发出.code);
            backBean.setLogicCode(SdkLogicCodeEnum.请求未发出.code);
        } finally {
            backBean.setEnd(System.currentTimeMillis());
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
            if(connection != null) {
                connection.disconnect();
            }
        }
        return backBean;
    }

    public static HttpBackBean handlePostRaw(String url, Map<String,String> headers, String content){
        return handlePostRaw(url,headers,content,OVERTIME);
    }


    public static class MyTrustManager extends X509ExtendedTrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {

        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {

        }
    }


    /**
     * post 发送json数据请求
     * @param urlPath
     * @param Json
     * @return
     */
    public static String doJsonPost(String urlPath, String Json) {
        // HttpClient 6.0被抛弃了
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept","application/json");
            // 往服务器里面发送数据
            if (Json != null && !TextUtils.isEmpty(Json)) {
                byte[] writebytes = Json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(Json.getBytes());
                outwritestream.flush();
                outwritestream.close();
            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }



    public static void main(String[] args) {
        System.out.println("==============start===============");
        String uri = "http://advert.jianlc.com/batchvalidateidfa.shtml?idfa_list=1&appid=1";
        System.out.println("1"+SdkHttpHelper.handleGet(uri,null,null).getResult());
        System.out.println("===============end==============");
    }
}

