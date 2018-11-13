package com.alioo.util;


import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzhichong on 2016/9/8.
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static HttpClient httpClient = new DefaultHttpClient();

    //    private static String cookieStr;
    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new SSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);
            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }


    public static String httpGet(String url, Map<String, Object> requestParams, String cookieStr) {

        HttpGet httpGet = null;
        String result = null;
        try {
            // 参数设置
            StringBuilder builder = new StringBuilder(url);
            builder.append("?");
            for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                builder.append((String) entry.getKey());
                builder.append("=");
                builder.append((String) entry.getValue());
                builder.append("&");
            }

            String tmpUrl = builder.toString();
            tmpUrl = tmpUrl.substring(0, tmpUrl.length() - 1);

            httpGet = new HttpGet(tmpUrl);

            logger.debug("executing request " + httpGet.getURI());
            if (cookieStr != null) {
                httpGet.setHeader("Cookie", cookieStr);
            }
            HttpResponse response = httpClient.execute(httpGet);

            // reponse header
//            System.out.println(response.getStatusLine().getStatusCode());

            // 网页内容
            HttpEntity httpEntity = response.getEntity();

            result = EntityUtils.toString(httpEntity);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpGet != null) {
                httpGet.abort();
            }
        }
        return result;
    }


    public static String httpGet(String url, Map<String, Object> requestParams, Map<String, Object> headerMap, String
            urlEncode) throws Exception {

        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = null;
        try {
            sslcontext = createIgnoreVerifySSL();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"},
                null, new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                hostname = "*.xueqiu.com";
//                return SSLConnectionSocketFactory.getDefaultHostnameVerifier().verify(hostname, session);
                return true;
            }

        });


        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
//                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .register("https", sf)
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();


        HttpGet httpGet = null;
        String result = null;
        try {
            String tmpUrl = null;
            // 参数设置
            StringBuilder builder = new StringBuilder(url);
            if (requestParams != null && requestParams.size() > 0) {
                builder.append("?");
                for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                    builder.append((String) entry.getKey());
                    builder.append("=");
                    builder.append((String) entry.getValue());
                    builder.append("&");
                }
                builder.deleteCharAt(builder.length() - 1);
            }
            tmpUrl = builder.toString();
            httpGet = new HttpGet(tmpUrl);

//            logger.debug("executing request " + httpGet.getURI());
            //if (cookieStr != null) {
            //    httpGet.setHeader("Cookie", cookieStr);
            //}
            for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
                httpGet.setHeader(entry.getKey(), (String) entry.getValue());
            }


            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build
                    ();//设置请求和传输超时时间
            httpGet.setConfig(requestConfig);


            HttpResponse response = client.execute(httpGet);

            // reponse header
//            System.out.println(response.getStatusLine().getStatusCode());

            // 网页内容
            HttpEntity httpEntity = response.getEntity();

            result = EntityUtils.toString(httpEntity, urlEncode);

            Header [] setCookieHeaders=response.getHeaders("Set-Cookie");
            if(setCookieHeaders!=null&&setCookieHeaders.length>0){
                headerMap.put("Set-Cookie", setCookieHeaders);
            }


        } catch (Exception e) {
            throw e;
        } finally {
            if (httpGet != null) {
                httpGet.abort();
            }
        }
        return result;
    }

    //
    public static String httpPostBody(String url, Map<String, Object> headerMap, String body, String urlEncode) {

        HttpPost httpPost = null;
        String result = null;
        try {
//        // 参数设置
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
//            Object obj = entry.getValue();
//            if (obj instanceof String[]) {
//                String[] tmp = (String[]) obj;
//                for (int i = 0; i < tmp.length; i++) {
//                    params.add(new BasicNameValuePair(entry.getKey(),
//                            tmp[i]));
//                }
//            } else {
//                params.add(new BasicNameValuePair(entry.getKey(),
//                        (String) entry.getValue()));
//            }
//        }


            httpPost = new HttpPost(url);
//        httpPost.setEntity(new UrlEncodedFormEntity(params, urlEncode));
            httpPost.setEntity(new StringEntity(body, urlEncode));


            for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), (String) entry.getValue());
            }

//            logger.debug("httpPost request " + httpPost.getURI());

//            if (cookieStr != null) {
//                httpPost.setHeader("Cookie", cookieStr);
//            }

            HttpHost proxy = new HttpHost("192.168.191.1", 8888, "https");
            RequestConfig config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(1 * 1000 * 60).build();

            httpPost.setConfig(config);

            // reponse header
            HttpResponse response = httpClient.execute(httpPost);
//            System.out.println(response.getStatusLine().getStatusCode());

            // 网页内容
            HttpEntity httpEntity = response.getEntity();

            result = EntityUtils.toString(httpEntity);
//            System.out.println(result);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
        return result;
    }


    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    public static String httpPost(String url, Map<String, Object> requestParams, Map<String, Object> headerMap, String urlEncode) {

//        httpClient=getNewHttpClient();
//        try {
//            //Secure Protocol implementation.
//            SSLContext ctx = SSLContext.getInstance("SSL");
//            //Implementation of a trust manager for X509 certificates
//            X509TrustManager tm = new X509TrustManager() {
//
//                public void checkClientTrusted(X509Certificate[] xcs,
//                                               String string) throws CertificateException {
//
//                }
//
//                public void checkServerTrusted(X509Certificate[] xcs,
//                                               String string) throws CertificateException {
//                }
//
//                public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//            };
//            ctx.init(null, new TrustManager[] { tm }, null);
//            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
//            ClientConnectionManager ccm = httpClient.getConnectionManager();
//            //register https protocol in httpclient's scheme registry
//            SchemeRegistry sr = ccm.getSchemeRegistry();
//            sr.register(new Scheme("https", 443, ssf));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//采用绕过验证的方式处理https请求
        SSLContext sslcontext = null;
        try {
            sslcontext = createIgnoreVerifySSL();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();


        HttpPost httpPost = null;
        String result = null;
        try {
            // 参数设置
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                Object obj = entry.getValue();
                if (obj instanceof String[]) {
                    String[] tmp = (String[]) obj;
                    for (int i = 0; i < tmp.length; i++) {
                        params.add(new BasicNameValuePair(entry.getKey(),
                                tmp[i]));
                    }
                } else {
                    params.add(new BasicNameValuePair(entry.getKey(),
                            (String) entry.getValue()));
                }
            }

            httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, urlEncode));

            for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), (String) entry.getValue());
            }

//            logger.debug("httpPost request " + httpPost.getURI());

//            if (cookieStr != null) {
//                httpPost.setHeader("Cookie", cookieStr);
//            }

            // reponse header
//            HttpResponse response = httpClient.execute(httpPost);
            HttpResponse response = client.execute(httpPost);


//            System.out.println(response.getStatusLine().getStatusCode());

            // 网页内容
            HttpEntity httpEntity = response.getEntity();

            result = EntityUtils.toString(httpEntity);
//            System.out.println(result);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
        return result;
    }

    public static String httpPost(String url, Map<String, Object> requestParams, String cookieStr, String urlEncode) {

        HttpPost httpPost = null;
        String result = null;
        try {
            // 参数设置
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                Object obj = entry.getValue();
                if (obj instanceof String[]) {
                    String[] tmp = (String[]) obj;
                    for (int i = 0; i < tmp.length; i++) {
                        params.add(new BasicNameValuePair(entry.getKey(),
                                tmp[i]));
                    }
                } else {
                    params.add(new BasicNameValuePair(entry.getKey(),
                            (String) entry.getValue()));
                }
            }

            httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, urlEncode));

            logger.debug("executing request " + httpPost.getURI());

            if (cookieStr != null) {
                httpPost.setHeader("Cookie", cookieStr);
            }

            // reponse header
            HttpResponse response = httpClient.execute(httpPost);
//            System.out.println(response.getStatusLine().getStatusCode());

            // 网页内容
            HttpEntity httpEntity = response.getEntity();

            result = EntityUtils.toString(httpEntity);
//            System.out.println(result);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
        return result;
    }

    public static String httpPost(HttpPost httpPost, String cookieStr) {

        String result = null;
        try {
            if (cookieStr != null) {
                httpPost.setHeader("Cookie", cookieStr);
            }
            // reponse header
            HttpResponse response = httpClient.execute(httpPost);
//            System.out.println(response.getStatusLine().getStatusCode());

            // 网页内容
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
//            System.out.println(result);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
        return result;
    }
}
