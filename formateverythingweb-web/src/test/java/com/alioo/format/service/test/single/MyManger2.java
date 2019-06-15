package com.alioo.format.service.test.single;

/**
 * @ClassName: com.alioo.format.service.test.single.MyManger2
 * @Description:
 * @author: liuzhichong@didichuxing.com
 * @date: 2019/02/16
 * @Copyright: 2019 www.didichuxing.com Inc. All rights reserved.
 */
public class MyManger2 {
  private static  MyManger2 instance ;
  private MyManger2() {
      instance=new MyManger2();
  }
  public static synchronized MyManger2 getInstance() {
    if(instance==null){
      instance=new MyManger2();
    }
    return instance;
  }

} 