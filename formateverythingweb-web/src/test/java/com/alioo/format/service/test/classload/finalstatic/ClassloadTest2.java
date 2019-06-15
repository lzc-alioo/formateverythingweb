package com.alioo.format.service.test.classload.finalstatic;

import com.alioo.format.service.test.classload.MysubClass;

/**
 * @ClassName: com.alioo.format.service.test.classload.ClassloadTest
 * @Description:
 * @author: liuzhichong@didichuxing.com
 * @date: 2019/02/15
 * @Copyright: 2019 www.didichuxing.com Inc. All rights reserved.
 */
public class ClassloadTest2 {
    public static void main(String[] args){

//        System.out.println(MySuperClass2.value);
        System.out.println(MySuperClass2.HELLO);
    }
}
