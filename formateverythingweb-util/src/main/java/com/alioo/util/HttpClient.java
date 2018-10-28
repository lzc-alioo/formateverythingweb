package com.alioo.util;

import com.alioo.format.domain.base.HttpClientResponse;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http工具类
 *
 * @author caohx
 */
public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger("client");

    private static final String UTF8 = "UTF-8";
    private static final int DEFAULT_MAX_CONNECTIONS = 100;
    private static final int DEFAULT_SO_TIMEOUT = 60000;
    private static CloseableHttpClient httpClient = null;
    private static final String GITF_UPLOAD_FILE_PARAMS = "filecontent";

    public final static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    private static final ConnectionKeepAliveStrategy strategy =
        new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch (NumberFormatException ignore) {
                            logger.error("number format error", ignore);
                        }
                    }
                }
                // default keep alive 30s
                return 30 * 1000;
            }

        };

    static {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(DEFAULT_SO_TIMEOUT)
            .setSocketTimeout(DEFAULT_SO_TIMEOUT)
            .setConnectTimeout(DEFAULT_SO_TIMEOUT)
            .setConnectionRequestTimeout(DEFAULT_SO_TIMEOUT)
            .build();
        logger.info("socketTimeOut={}, connecteTimeOut={}", requestConfig.getSocketTimeout(), requestConfig
            .getConnectTimeout());
        SocketConfig socketConfig = SocketConfig.custom()
            .setTcpNoDelay(true).build();
        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setMaxTotal(DEFAULT_MAX_CONNECTIONS);
        connManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS);
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .build();
        connManager.setDefaultConnectionConfig(connectionConfig);
        //connMrg.setMaxPerRoute(new HttpRoute(new HttpHost(host, port)), maxTotal);

        httpClient = HttpClients.custom()
            .evictIdleConnections(1, TimeUnit.SECONDS)
            .setConnectionTimeToLive(DEFAULT_SO_TIMEOUT, TimeUnit.SECONDS)
            .setConnectionManager(connManager)
            .setKeepAliveStrategy(strategy)
            .setDefaultRequestConfig(requestConfig)
            .build();
    }

    public static HttpClientResponse get(String url, int timeout) {
        return get(url, null, UTF8, timeout);
    }

    public static HttpClientResponse get(String url) {
        return get(url, null);
    }

    public static HttpClientResponse get(String url, Map<String, String> params) {
        return get(url, params, UTF8, DEFAULT_SO_TIMEOUT);
    }

    public static HttpClientResponse get(String url, Map<String, String> params, Map<String, String> headers) {
        return get(url, params, headers, UTF8, DEFAULT_SO_TIMEOUT);
    }

    public static HttpClientResponse get(String url, Map<String, String> params, String charset, int timeout) {
        return get(url, params, null, charset, timeout);
    }

    public static HttpClientResponse get(String url, Map<String, String> params,
                                         Map<String, String> headers, String charset, int timeout) {
        HttpGet get = generateCommonGetRequest(url, params, timeout);
        HttpClientResponse response = null;
        try {
            if (headers != null) {
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    get.addHeader(e.getKey(), e.getValue());
                }
            }

            response = fillResponse(get, charset);
        } catch (Exception e) {
            logger.error("error post data to " + url, e);
        } finally {
            logger.info("url:" + url + "," + params);
            get.releaseConnection();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("response=" + response);
        }
        return response;
    }

    public static HttpClientResponse post(String url, String body,
                                          Map<String, String> headers, int timeout) {
        return post(url, null, body, headers, timeout, null);
    }

    public static HttpClientResponse post(String url, String body,
                                          Map<String, String> headers) {
        return post(url, null, body, headers, 3000, null);
    }

    public static HttpClientResponse post(String url, Map<String, String> params,
                                          Map<String, String> headers, int timeout) {
        return post(url, params, null, headers, timeout, null);
    }

    public static HttpClientResponse post(String url, String body,
                                          Map<String, String> headers, int timeout, String contentType) {
        return post(url, null, body, headers, timeout, contentType);
    }

    public static HttpClientResponse post(String url, Map<String, String> params, String body,
                                          Map<String, String> headers, int timeout, String contentType) {
        HttpPost post = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(timeout)
            .setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout).build();
        post.setConfig(requestConfig);
        generateParamOrBody(post, params, body, contentType);
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                post.addHeader(e.getKey(), e.getValue());
            }
        }
        HttpClientResponse response = null;
        try {
            response = fillResponse(post);
        } catch (Exception e) {
            logger.error("error post data to " + url, e);
        } finally {
            post.releaseConnection();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("response=" + response);
        }
        return response;
    }


    public static HttpClientResponse put(String url, String body, Map<String, String> headers) {
        return put(url, null, body, headers, 3000, ContentType.APPLICATION_JSON.getMimeType());
    }

    public static HttpClientResponse put(String url, Map<String, String> param, Map<String, String> headers) {
        return put(url, param, null, headers, 3000, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
    }


    public static HttpClientResponse put(String url, Map<String, String> param, String body,
                                         Map<String, String> headers, int timeout, String contentType) {
        HttpPut put = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(timeout)
            .setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout).build();
        put.setConfig(requestConfig);
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                put.addHeader(e.getKey(), e.getValue());
            }
        }
        generateParamOrBody(put, param, body, contentType);
        HttpClientResponse response = null;
        try {
            response = fillResponse(put);
        } catch (Exception e) {
            logger.error("error put data to " + url, e);
        } finally {
            put.releaseConnection();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("response=" + response);
        }

        return response;
    }

    public static HttpClientResponse patch(String url, String body, Map<String, String> headers) {
        return patch(url, null, body, headers, 3000, ContentType.APPLICATION_JSON.getMimeType());
    }

    public static HttpClientResponse patch(String url, Map<String, String> param, Map<String, String> headers) {
        return patch(url, param, null, headers, 3000, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
    }


    public static HttpClientResponse patch(String url, Map<String, String> param, String body,
                                           Map<String, String> headers, int timeout, String contentType) {
        HttpEntityEnclosingRequestBase patch = new HttpPatch(url);
        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(timeout)
            .setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout).build();
        patch.setConfig(requestConfig);
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                patch.addHeader(e.getKey(), e.getValue());
            }
        }
        generateParamOrBody(patch, param, body, contentType);
        HttpClientResponse response = null;
        try {
            response = fillResponse(patch);
        } catch (Exception e) {
            logger.error("error patch data to " + url, e);
        } finally {
            patch.releaseConnection();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("response=" + response);
        }

        return response;
    }

    public static HttpClientResponse delete(String url, Map<String, String> headers) {
        return delete(url, headers, DEFAULT_SO_TIMEOUT);
    }

    public static HttpClientResponse delete(String url, Map<String, String> headers, int timeout) {
        HttpDelete delete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(timeout)
            .setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout).build();
        delete.setConfig(requestConfig);
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                delete.addHeader(e.getKey(), e.getValue());
            }
        }

        HttpClientResponse response = null;
        try {
            response = fillResponse(delete);
        } catch (Exception e) {
            logger.error("error delete data to " + url, e);
        } finally {
            delete.releaseConnection();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("response=" + response);
        }

        return response;
    }

    private static HttpGet generateCommonGetRequest(String uri, Map<String, String> params, int timeout) {
        HttpGet get = new HttpGet(uri);
        if (params != null
            && !params.isEmpty()) {
            URIBuilder uriBuilder = new URIBuilder(get.getURI());
            for (String key : params.keySet()) {
                String value = params.get(key);
                uriBuilder.addParameter(key, value);
            }
            try {
                get.setURI(uriBuilder.build());
            } catch (URISyntaxException e) {
                logger.error("generateCommonGetRequest " + uri, e);
            }
        }
        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(timeout)
            .setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout).build();
        get.setConfig(requestConfig);
        return get;
    }

    private static void generateParamOrBody(HttpEntityEnclosingRequestBase heb,
                                            Map<String, String> param, String body, String contentType) {
        try {
            if (null != param) {
                List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity he = new UrlEncodedFormEntity(nvps, UTF8);
                he.setContentType(contentType == null ? ContentType.APPLICATION_FORM_URLENCODED.getMimeType()
                    : contentType);
                heb.setEntity(he);
            }
            if (null != body) {
                BasicHttpEntity requestBody = new BasicHttpEntity();
                requestBody.setContent(new ByteArrayInputStream(body.getBytes(UTF8)));
                requestBody.setContentLength(body.getBytes(UTF8).length);
                requestBody.setContentType(contentType == null ? ContentType.APPLICATION_JSON.getMimeType()
                    : contentType);
                heb.setEntity(requestBody);
            }
        } catch (UnsupportedEncodingException impossiable) {
            logger.error("UTF-8 is not surportted", impossiable);
        }
    }

    public static String getParams(Map<String, String> map) {

        if (map == null) return "";
        StringBuilder str = new StringBuilder();
        for (Map.Entry entry : map.entrySet()) {

            str.append(entry.getKey());
            str.append("=");
            str.append(entry.getValue());
            str.append("&");
        }
        String param = str.toString();

        if (StringUtil.isNotBlank(param)) {

            param = param.substring(0, param.lastIndexOf("&"));
        }
        return param;

    }

    private static HttpClientResponse fillResponse(HttpUriRequest request, String charset) throws IOException {
        HttpEntity entity = null;
        try {
            long start = System.currentTimeMillis();

            CloseableHttpResponse closeableHttpResponse = httpClient.execute(request);
            entity = closeableHttpResponse.getEntity();
            String responseData = entity == null ? "" : EntityUtils.toString(entity, charset);
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();

            logger.info("httpclientget response url={}, params={}, header={}, cost={}, status={}, response={}",
                request.getURI().toString(), request.getAllHeaders(),
                (System.currentTimeMillis() - start), statusCode, responseData);

            return new HttpClientResponse(statusCode, responseData, request.getURI().toString());
        } finally {
            if (entity != null) {
                EntityUtils.consume(entity);
            }
        }
    }

    private static HttpClientResponse fillResponse(HttpUriRequest request) throws IOException {
        return fillResponse(request, "UTF-8");
    }

    /**
     * 上传文件
     *
     * @param url
     * @param file
     * @return
     */
    public static String uploadFile(String url, File file) throws Exception {
        String response = null;
        HttpPost post = new HttpPost(url);
        try {
            //文件必须存在
            if (file != null && file.exists() && file.isFile()) {
                FileBody fileBody = new FileBody(file);
                HttpEntity reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addPart(GITF_UPLOAD_FILE_PARAMS, fileBody).setCharset(CharsetUtils.get("UTF-8")).build();
                post.setEntity(reqEntity);
                response = EntityUtils.toString(httpClient.execute(post).getEntity(), CharsetUtils.get("UTF-8"));
            }
        } catch (Exception e) {
            logger.info("【HttpClient uploadFile】:" + e.getMessage());
            throw new Exception("error post data to " + url, e);
        } finally {
            post.releaseConnection();
        }
        return response;
    }

}
