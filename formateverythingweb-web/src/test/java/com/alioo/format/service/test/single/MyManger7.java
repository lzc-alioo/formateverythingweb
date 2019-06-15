package com.alioo.format.service.test.single;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * @ClassName: com.alioo.format.service.test.single.MyManger7
 * @Description:
 * @author: liuzhichong@didichuxing.com
 * @date: 2019/02/16
 * @Copyright: 2019 www.didichuxing.com Inc. All rights reserved.
 */
public class MyManger7 implements Serializable {
    //这里两行的顺序非常关键，当前行(public static boolean flag = false;)一定要在(private static MyManger7 instance;)的前面
    //因为下面这一行new MyManger7()其中的构造器会修改flag的值，如果顺序被调整，new MyManger7()中修改过的值后来
    //又会被static boolean flag = false;覆盖，从而没有目的
    public static boolean flag = false;
    private static volatile MyManger7 instance;

    private MyManger7() {
        synchronized (MyManger7.class) {
            if (flag == false) {
                flag = true;
            } else {
                throw new RuntimeException("正在遭受反射攻击");
            }
        }
    }

    public static MyManger7 getInstance() {
        if (instance == null) {
            synchronized (MyManger7.class) {
                if (instance == null) {
                    instance = new MyManger7();
                }
            }
        }
        return instance;
    }

    // 防止反序列化获取多个对象的漏洞。
    // 无论是实现Serializable接口，或是Externalizable接口，当从I/O流中读取对象时，readResolve()方法都会被调用到。
    // 实际上就是用readResolve()中返回的对象直接替换在反序列化过程中创建的对象。
    private Object readResolve() throws ObjectStreamException {
        return instance;
    }

}
