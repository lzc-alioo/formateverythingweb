//package com.alioo.format.service.test.threadstate;
//
//import org.junit.Test;
//
///**
// * @ClassName: com.alioo.format.service.test.threadstate.ThreadStateTest
// * @Description:
// * @author: liuzhichong@didichuxing.com
// * @date: 2019/02/13
// * @Copyright: 2019 www.didichuxing.com Inc. All rights reserved.
// */
//public class ThreadStateTest {
//
//    @Test
//    public void NEW() {
//        Thread t = new Thread();
//        System.out.println(t.getState());
//    }
//
//    @Test
//    public void RUNNABLE() {
//
//        final int[] j = {0};
//
//        Thread t = new Thread("mythread-runnable") {
//
//
//            public void run() {
//                for (int i = 0; i < Integer.MAX_VALUE; i++) {
////                    System.out.println(i);
//                    j[0] = i ;
//                    try {
//                        Thread.sleep(100L);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        };
//
//        t.start();
//
//        for (int i = 0; i < 5000; i++) {
//            System.out.println(t.getState()+"="+ j[0]);
//
//            try {
//                Thread.sleep(1L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//}
