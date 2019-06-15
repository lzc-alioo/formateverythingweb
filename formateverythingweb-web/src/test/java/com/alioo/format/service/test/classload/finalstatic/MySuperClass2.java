package com.alioo.format.service.test.classload.finalstatic;

/**
 * @ClassName: com.alioo.format.service.test.classload.finalstatic.MySuperClass2
 * @Description:
 * @author: liuzhichong@didichuxing.com
 * @date: 2019/02/15
 * @Copyright: 2019 www.didichuxing.com Inc. All rights reserved.
 */
public class MySuperClass2 {
    public MySuperClass2() {
        System.out.printf("MySuperClass2MySuperClass2 init");
    }

    static {
        System.out.println("MySuperClass2 init");
    }

    public static final int value = 111;
    public static final String HELLO = "hello world";

}
