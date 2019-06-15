package com.alioo.format.service.test.cache;

import java.util.LinkedHashMap;

public class LinkedHashMapDemo {
    public static void main(String[] args) {

        LinkedHashMap map=new LinkedHashMap(10,0.75f,true);
        map.put(1, 1);
        map.put(2, 22);
        map.put(3, 3);

        System.out.println(map);

        map.get(2);
        System.out.println(map);
    }
}
