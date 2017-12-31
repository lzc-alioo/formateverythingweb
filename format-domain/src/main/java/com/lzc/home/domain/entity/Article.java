package com.lzc.home.domain.entity;

import com.jd.o2o.commons.domain.BaseTableEntity;

import java.util.Date;

/**
 * 员工表
 */
public class Article extends BaseTableEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Article() {
    }

    public Article(String title, String content, int readCount, int commentCount, String csdnLink,String updateTimeStr, String graspTime,int contentType,String contentDesc) {
        this.title = title;
        this.content = content;
        this.readCount = readCount;
        this.commentCount = commentCount;
        this.csdnLink = csdnLink;
        this.graspTime = graspTime;
        this.contentType = contentType;
        this.contentDesc = contentDesc;


        Date updateTime=DateTimeUtil.toDateFromStr(updateTimeStr, "yyyy-MM-dd HH:mm");
        this.setUpdateTime(updateTime);
        this.setCreatePin("lzc");
        this.setUpdatePin("lzc");

    }

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容，属于富文本
     */
    private String content;

    /**
     * 在csdn阅读量
     */
    private int readCount;

    /**
     * 在csdn评论量
     */
    private int commentCount;

    /**
     * 在csdn中的原始链接
     */
    private String csdnLink;

    /**
     * 抓取时间戳，格式yyyyMMddHHmmss
     */
    private String graspTime;


    /**
     * 内容类型1：原创；2，转载
     */
    private int contentType;


    /**
     * 内容简介
     */
    private String contentDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCsdnLink() {
        return csdnLink;
    }

    public void setCsdnLink(String csdnLink) {
        this.csdnLink = csdnLink;
    }

    public String getGraspTime() {
        return graspTime;
    }

    public void setGraspTime(String graspTime) {

        this.graspTime = graspTime;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", readCount=" + readCount +
                ", commentCount=" + commentCount +
                ", csdnLink='" + csdnLink + '\'' +
                ", graspTime='" + graspTime + '\'' +
                ", contentType=" + contentType +
                ", contentDesc='" + contentDesc + '\'' +
                '}';
    }
}