package com.lzc.home.article.launcher;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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


    public static String httpGet(String url, Map<String, Object> requestParams, Map<String, Object> headerMap) {

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
            //if (cookieStr != null) {
            //    httpGet.setHeader("Cookie", cookieStr);
            //}
            for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
                httpGet.setHeader(entry.getKey(), (String)entry.getValue());
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
//

    public static String httpPost(String url, Map<String, Object> requestParams, Map<String, Object> headerMap, String urlEncode) {

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
                httpPost.setHeader(entry.getKey(), (String)entry.getValue());
            }

//            logger.debug("httpPost request " + httpPost.getURI());

//            if (cookieStr != null) {
//                httpPost.setHeader("Cookie", cookieStr);
//            }

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
