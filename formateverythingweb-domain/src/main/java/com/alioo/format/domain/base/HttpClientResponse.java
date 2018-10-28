package com.alioo.format.domain.base;

import org.springframework.http.HttpStatus;

/**
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/1/29
 * Time: 下午2:47
 */
public class HttpClientResponse {

    private int statusCode;

    private String data;

    private boolean isSuccess;

    private String url;

    public HttpClientResponse(int status, String data, String url) {
        this.statusCode = status;
        this.data = data;
        this.url = url;
    }

    public HttpClientResponse() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess() {
        return this.statusCode == HttpStatus.OK.value();
    }

    @Override
    public String toString() {
        return "{'statusCode' : " + statusCode + "; 'data':'" + data + "'; 'url':'" + url + "'}";
    }
}
