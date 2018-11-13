package com.alioo.format.domain.base;

import java.util.List;

/**
 * @author yujiayuan
 * Date: 2016年7月14日 下午7:47:26
 */
public class Page<T> {
    private List<T> objectList;
    private int page; //页码，第一页从1开始
    private int size; //每页条数
    private int total;//总数据行数

    public Page() {
    }

    public Page(List<T> data, int total) {
        this.objectList = data;
        this.total = total;
    }

    public Page(List<T> data, int page, int size, int total) {
        this.objectList = data;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public List<T> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<T> objectList) {
        this.objectList = objectList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
