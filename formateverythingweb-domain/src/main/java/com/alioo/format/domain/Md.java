package com.alioo.format.domain;

public class Md {

    private int id; //

    private String b32; //32位md5值

    private String b16; //16位md5值

    private String data; //字符串

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    public void setB32(String b32){
        this.b32 = b32;
    }
    public String getB32(){
        return this.b32;
    }

    public void setB16(String b16){
        this.b16 = b16;
    }
    public String getB16(){
        return this.b16;
    }

    public void setData(String data){
        this.data = data;
    }
    public String getData(){
        return this.data;
    }

}