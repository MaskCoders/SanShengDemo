package com.example.demo.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadPoolFactory implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    public CustomThreadPoolFactory(String poolName){
        namePrefix = poolName;
    }
    public Thread newThread(Runnable r) {
        return new Thread(r, namePrefix +" #" + threadNumber.getAndIncrement());
    }
}
