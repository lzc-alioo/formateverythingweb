package com.alioo.format.service.test.skiplist;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class SkipListDemo {

    public static void skipListMapshow(){
        Map<Integer,String> map= new ConcurrentSkipListMap<>();

        map.put(1, "1");
        map.put(23, "23");
        map.put(3, "1");
        map.put(2, "2");


        System.out.println(map);
        /*输出是有序的，从小到大。
         * output
         * 1
         * 2
         * 3
         * 23
         *
         */
        for(Integer key : map.keySet()){
            System.out.println(map.get(key));
        }
    }

    public static void main(String[] args) {
        skipListMapshow();
    }
}
