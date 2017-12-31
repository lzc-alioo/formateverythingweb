package com.lzc.home.article;

import java.util.concurrent.SynchronousQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuzhichong on 2016/11/4.
 */
public class Test2 {
    public static void main(String[] args) {

        SynchronousQueue queue = new SynchronousQueue(true);

        queue.add("a");
        queue.add("a");

        System.out.println(queue);
    }
}
