package com.alioo.format.service.test.single;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

/**
 * @ClassName: com.alioo.format.service.test.single.ReflectAndSerialSingleton
 * @Description:
 * @author: liuzhichong@didichuxing.com
 * @date: 2019/02/16
 * @Copyright: 2019 www.didichuxing.com Inc. All rights reserved.
 */

public class ReflectAndSerialSingleton {
    public static void main(String[] args) throws Exception {
        MyManger7 sc1 = MyManger7.getInstance();
        MyManger7 sc2 = MyManger7.getInstance();
        System.out.println(sc1);
        System.out.println(sc2);
        System.out.println(sc1.equals(sc2)); // sc1，sc2是同一个对象

        /**
         * 通过反序列化的方式构造多个对象（类需要实现Serializable接口）
         */
        // 1. 把对象sc1写入硬盘文件
        FileOutputStream fos = new FileOutputStream("object.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(sc1);
        oos.close();
        fos.close();

        // 2. 把硬盘文件上的对象读出来
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("object.txt"));
        // 如果对象定义了readResolve()方法，readObject()会调用readResolve()方法。从而解决反序列化的漏洞
        MyManger7 sc5 = (MyManger7) ois.readObject();
        // 反序列化出来的对象，和原对象，不是同一个对象。如果对象定义了readResolve()方法，可以解决此问题。
        System.out.println("序列化出来的对象："+sc5);

        ois.close();

        /**
         * 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
         */
        Class<MyManger7> clazz = MyManger7.class;
        Constructor<MyManger7> c = clazz.getDeclaredConstructor(null);
        c.setAccessible(true); // 跳过权限检查
        MyManger7 sc3 = c.newInstance();
        MyManger7 sc4 = c.newInstance();
        System.out.println(sc3);  // sc3，sc4不是同一个对象
        System.out.println(sc4);


    }
}
