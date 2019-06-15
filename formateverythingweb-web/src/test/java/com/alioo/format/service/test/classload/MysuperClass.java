package com.alioo.format.service.test.classload;

/**
 * @ClassName: com.alioo.format.service.test.classload.MysuperClass
 * @Description:
 * @author: liuzhichong@didichuxing.com
 * @date: 2019/02/15
 * @Copyright: 2019 www.didichuxing.com Inc. All rights reserved.
 */
public class MysuperClass {
    public MysuperClass() {
        System.out.printf("MysuperClassMysuperClass init");
    }

    static {
        System.out.println("MysuperClass init");
    }

    public static int value=111;

}
