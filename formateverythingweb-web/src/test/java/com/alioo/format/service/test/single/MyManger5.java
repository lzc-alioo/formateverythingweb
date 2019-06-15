package com.alioo.format.service.test.single;

import java.io.Serializable;

/**
 * @ClassName: com.alioo.format.service.test.single.MyManger5
 * @Description:
 * @author: liuzhichong@didichuxing.com
 * @date: 2019/02/16
 * @Copyright: 2019 www.didichuxing.com Inc. All rights reserved.
 */
public class MyManger5 implements Serializable{
    
  private static volatile MyManger5 instance;
  private MyManger5() {
  }
  private static class SingletonHolder {
    private static MyManger5 instance = new MyManger5();
  }
  public static MyManger5 getInstance( ) {
    return SingletonHolder.instance;
  }

}
