package com.alioo.format.service.test.map;

import java.util.HashMap;
import java.util.Map;

 class $MapDemo {

     {
         System.out.println("i'm $MapDemo class ");
     }
     static {
         System.out.println("static $MapDemo ");
     }

    public static void main(String[] args) {

        new $MapDemo();

        Map map = new HashMap<>();

        System.out.println( map.get("aaa")+"bbb");
    }
}


