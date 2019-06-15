package com.alioo.format.service.test.single;

/**
 * @ClassName: com.alioo.format.service.test.single.MyManger6
 * @Description:
 * @author: liuzhichong@didichuxing.com
 * @date: 2019/02/16
 * @Copyright: 2019 www.didichuxing.com Inc. All rights reserved.
 */
public enum MyManger6 {
  INSTANCE;
  private MyResource instance;
  MyManger6() {
    instance = new MyResource();
  }
  public MyResource getInstance() {
    return instance;
  }
}
class MyResource {
  public void doMethod() {
    System.out.println("枚举类型的单例类资源");
  }
}
