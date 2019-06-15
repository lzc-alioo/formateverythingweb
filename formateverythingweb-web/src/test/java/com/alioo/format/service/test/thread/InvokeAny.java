package com.alioo.format.service.test.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvokeAny {


    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();


        MyTask taska = new MyTask(5000L, "aaa", lock);
        MyTask taskb = new MyTask(1L, "bbb", lock);
        MyTask taskc = new MyTask(8000L, "ccc", lock);

        taska.start();
        taskb.start();
        taskc.start();

        synchronized (lock) {
            lock.wait();
        }
        if (taska.result != null) {
            System.out.println(taska.result);
        }
        if (taskb.result != null) {
            System.out.println(taskb.result);
        }
        if (taskc.result != null) {
            System.out.println(taskc.result);
        }


    }
}

class MyTask extends Thread {
    private static Logger logger = LoggerFactory.getLogger(MyTask.class);
    private long sleepTime;
    private String str;
    private Object lock;

    public String result;

    public MyTask(long sleepTime, String str, Object lock) {
        this.sleepTime = sleepTime;
        this.str = str;
        this.lock = lock;
    }

    @Override
    public void run() {
        logger.info("==" + str);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("==" + str);
        synchronized (lock) {
            lock.notifyAll();
        }
        result = (str + str);
    }


}
