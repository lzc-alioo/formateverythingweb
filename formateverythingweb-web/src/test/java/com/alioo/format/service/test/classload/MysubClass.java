package com.alioo.format.service.test.classload;

/**
 * @ClassName: com.alioo.format.service.test.classload.MysubClass
 * @Description:
 * @author: liuzhichong@didichuxing.com
 * @date: 2019/02/15
 * @Copyright: 2019 www.didichuxing.com Inc. All rights reserved.
 */
public class MysubClass  extends MysuperClass{

    public MysubClass() {
        System.out.printf("MysubClassMysubClass init");
    }

    static {
        System.out.println("MysubClass init!");
    }
}
