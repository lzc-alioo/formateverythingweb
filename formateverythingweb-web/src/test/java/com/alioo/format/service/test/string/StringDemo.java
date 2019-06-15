package com.alioo.format.service.test.string;

import java.nio.ByteBuffer;

public class StringDemo {

    final int aaa = 111;

    public int getAaa(){
        return this.aaa;
    }

    public static void main(String[] args) {

        boolean flag = "hello" == "hello";
        System.out.println(flag);

        boolean flag2 = "hello" == new String("hello");
        System.out.println(flag2);


//        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
//        buffer.putChar('a');
//        System.out.println(buffer);
//        buffer.putChar('c');
//        System.out.println(buffer);
//        buffer.putInt(10);
//        System.out.println(buffer);


        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        buffer.putChar('a');
        buffer.putChar('c');
        System.out.println("插入完数据 " + buffer);
        buffer.mark();// 记录mark的位置
        buffer.position(30);// 设置的position一定要比mark大，否则mark无法重置
        System.out.println("reset前 " + buffer);
        buffer.reset();// 重置reset ，reset后的position=mark
        System.out.println("reset后 " + buffer);
        buffer.rewind();//清除标记，position变成0，mark变成-1
        System.out.println("清除标记后 " + buffer);


        char[] chars = new char[]{23, 55};
        new String(chars);


    }
}
