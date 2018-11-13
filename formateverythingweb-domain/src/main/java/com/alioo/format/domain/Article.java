package com.alioo.format.domain;

import java.util.Date;
public class  Article {

    private int id; //

    private String title; //标题

    private String content; //内容，属于富文本

    private int readCount; //在csdn阅读量

    private int commentCount; //在csdn评论量

    private String csdnLink; //在csdn中的原始链接

    private String graspTime; //抓取时间戳，格式yyyyMMddHHmmss

    private int contentType; //内容类型1：原创；2，转载

    private String contentDesc; //内容简介

    private Date createTime; //创建时间

    private String createPin; //创建人

    private Date updateTime; //更新时间

    private String updatePin; //更新人

    private int sysVersion; //数据版本号

    private int yn; //删除标示

    private Date ts; //时间戳

    public Article() {
    }

    public Article(String title, String content, int readCount, int commentCount, String csdnLink, String graspTime,
                   int contentType, String contentDesc, Date updateTime) {
        this.title = title;
        this.content = content;
        this.readCount = readCount;
        this.commentCount = commentCount;
        this.csdnLink = csdnLink;
        this.graspTime = graspTime;
        this.contentType = contentType;
        this.contentDesc = contentDesc;
        this.updateTime = updateTime;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }

    public void setReadCount(int readCount){
        this.readCount = readCount;
    }
    public int getReadCount(){
        return this.readCount;
    }

    public void setCommentCount(int commentCount){
        this.commentCount = commentCount;
    }
    public int getCommentCount(){
        return this.commentCount;
    }

    public void setCsdnLink(String csdnLink){
        this.csdnLink = csdnLink;
    }
    public String getCsdnLink(){
        return this.csdnLink;
    }

    public void setGraspTime(String graspTime){
        this.graspTime = graspTime;
    }
    public String getGraspTime(){
        return this.graspTime;
    }

    public void setContentType(int contentType){
        this.contentType = contentType;
    }
    public int getContentType(){
        return this.contentType;
    }

    public void setContentDesc(String contentDesc){
        this.contentDesc = contentDesc;
    }
    public String getContentDesc(){
        return this.contentDesc;
    }

    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }
    public Date getCreateTime(){
        return this.createTime;
    }

    public void setCreatePin(String createPin){
        this.createPin = createPin;
    }
    public String getCreatePin(){
        return this.createPin;
    }

    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }
    public Date getUpdateTime(){
        return this.updateTime;
    }

    public void setUpdatePin(String updatePin){
        this.updatePin = updatePin;
    }
    public String getUpdatePin(){
        return this.updatePin;
    }

    public void setSysVersion(int sysVersion){
        this.sysVersion = sysVersion;
    }
    public int getSysVersion(){
        return this.sysVersion;
    }

    public void setYn(int yn){
        this.yn = yn;
    }
    public int getYn(){
        return this.yn;
    }

    public void setTs(Date ts){
        this.ts = ts;
    }
    public Date getTs(){
        return this.ts;
    }

}