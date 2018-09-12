package com.duodian.admore.core.helper;

import com.duodian.admore.core.encrypt.SHAEncrypt;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by j.sh on 2015/6/26.
 */
public class HttpHelper {

    public static final String ENCODING_UTF8 = "UTF-8";
    public static final String ENCODING_GBK = "GBK";

    public static final String POST = "POST";
    public static final String GET = "GET";


    private static RequestConfig defaultRequestConfig() {
        return RequestConfig.custom().setSocketTimeout(1500).setConnectTimeout(1500).setConnectionRequestTimeout(1500).build();
    }

    public static String handleGet(String uri) {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        try {
            HttpGet request = new HttpGet(uri);
            request.setHeader("User-Agent", "admore");
            request.setConfig(defaultRequestConfig());
            HttpResponse response = httpclient.execute(request);
            return IOUtils.toString(response.getEntity().getContent(), ENCODING_UTF8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(httpclient);
        }
    }

    public static String handleGetHttps(String uri) {
        CloseableHttpClient httpclient = null;
        try {
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
            sslcontext.init(null, new TrustManager[]{new MyTrustManager()}, null);

            httpclient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(sslcontext)).build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("User-Agent", "admore");
            request.setConfig(defaultRequestConfig());
            HttpResponse response = httpclient.execute(request);
            return IOUtils.toString(response.getEntity().getContent(), ENCODING_UTF8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(httpclient);
        }
    }

    public static String handlePost(String url, Map<String, String> formParams) {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        try {
            HttpPost request = new HttpPost(url);
            List<NameValuePair> formValue = new ArrayList<>();
            for (Map.Entry<String, String> entry : formParams.entrySet()) {
                formValue.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            request.setEntity(new UrlEncodedFormEntity(formValue, "UTF-8"));
            request.setHeader("User-Agent", "admore");
            request.setConfig(defaultRequestConfig());
            HttpResponse response = httpclient.execute(request);
            return IOUtils.toString(response.getEntity().getContent(), ENCODING_UTF8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(httpclient);
        }
    }

    public static String handlePost(String url, Map<String, String> formParams, Integer timout) {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        try {
            HttpPost request = new HttpPost(url);
            List<NameValuePair> formValue = new ArrayList<>();
            for (Map.Entry<String, String> entry : formParams.entrySet()) {
                formValue.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            request.setEntity(new UrlEncodedFormEntity(formValue, "UTF-8"));
            request.setHeader("User-Agent", "admore");
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timout) //连接超时
                    .setSocketTimeout(timout) //读超时
                    .build();
            request.setConfig(config);
           // request.setConfig(defaultRequestConfig());
            HttpResponse response = httpclient.execute(request);
            return IOUtils.toString(response.getEntity().getContent(), ENCODING_UTF8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(httpclient);
        }
    }

    public static String handlePost(String url, String content) {
        return handlePost(url, content, ENCODING_UTF8);
    }

    public static String handlePost(String url, String content, String encoding) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        content = StringUtils.defaultIfEmpty(content, "");

        try {
            URL request = new URL(url);
            connection = (HttpURLConnection) request.openConnection();
            connection.setRequestMethod(POST);
            connection.setDoOutput(true);
            output = connection.getOutputStream();
            output.write(content.getBytes(encoding));
            input = connection.getInputStream();
            return IOUtils.toString(input, encoding);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
            if (connection != null) {
                connection.disconnect();
            }
        }
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


}

