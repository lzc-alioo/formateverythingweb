package com.alioo.format.service.test.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.RandomAccess;

public class FileDemo {
    public static void main(String[] args) throws Exception {

        try {
            RandomAccessFile in = new RandomAccessFile("/Users/alioo/work/gitstudy/formateverythingweb/formateverythingweb-web/src/test/resources/application.yml","r");
            in.skipBytes(9);
            int a=in.read();
            char b=(char)a;
            System.out.println("a="+a);
            System.out.println("b="+b);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileInputStream in2 = new FileInputStream("/Users/alioo/work/gitstudy/formateverythingweb/formateverythingweb-web/src/test/resources/application.yml");
        in2.skip(8);
        int a=in2.read();
        char b=(char)a;
        System.out.println("a="+a);
        System.out.println("b="+b);
    }
}
