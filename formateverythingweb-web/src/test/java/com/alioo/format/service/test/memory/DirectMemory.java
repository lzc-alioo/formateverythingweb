package com.alioo.format.service.test.memory;


//-XX:MaxDirectMemorySize

public class DirectMemory {
    public static void main(String[] args) {

//        https://blog.csdn.net/moakun/article/details/85377879

//方法1
        long a= Runtime.getRuntime().maxMemory();
        System.out.println(a);

//方法2
        long b=sun.misc.VM.maxDirectMemory();
        System.out.println("maxMemoryValue:"+b);

    }

}
