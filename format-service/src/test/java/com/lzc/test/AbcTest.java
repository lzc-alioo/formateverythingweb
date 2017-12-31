package com.lzc.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import  java.lang.System;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by alioo on 2017/9/7.
 */
public class AbcTest {
    
    public static void main(String [] args) throws ExecutionException, InterruptedException, TimeoutException {
//        ExecutorService executorService = Executors.newFixedThreadPool(3);

        ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 5, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("aaa");
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("bbb");
            }
        });


        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor=new ScheduledThreadPoolExecutor(3);
        scheduledThreadPoolExecutor.schedule(new Runnable() {
            @Override
            public void run() {

            }
        },5000,TimeUnit.SECONDS);


        ReentrantLock reentrantLock;

        ReadWriteLock readWriteLock;


//        Set<Callable<String>> callables = new HashSet<Callable<String>>();
//
//        callables.add(new Callable<String>() {
//            public String call() throws Exception {
//                Thread.sleep(50);
//                System.out.println("Task 1");
//                return "Task 1";
//            }
//        });
//        callables.add(new Callable<String>() {
//            public String call() throws Exception {
//                System.out.println("Task 2");
//                return "Task 2";
//            }
//        });
//        callables.add(new Callable<String>() {
//            public String call() throws Exception {
//                System.out.println("Task 3");
//                return "Task 3";
//            }
//        });
//
////        String result = executorService.invokeAny(callables);
////        System.out.println("result = " + result);
////        executorService.shutdown();
//
//        List<Future<String>> futures= executorService.invokeAll(callables);
//
//        for(Future<String> future : futures){
//            System.out.println("future.get = " + future.get());
//            future.get(4,TimeUnit.MINUTES);
//
//        }
//
//
//        //ScheduledThreadPoolExecutor
//        ScheduledExecutorService scheduledExecutorService=
//        Executors.newScheduledThreadPool(5);
//
//        scheduledExecutorService.schedule(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },5000,TimeUnit.MINUTES);
//
//
//        LinkedBlockingQueue queue=new LinkedBlockingQueue();
//        queue.add(2);

    }
}
