package com.example.cricketApp.Service;
import com.example.cricketApp.Utility.CustomRejectedHandler;
import com.example.cricketApp.Utility.CustomThreadFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ThreadPool {
    public static final ReentrantLock lock = new ReentrantLock();
    public static final Condition matchAvailable = lock.newCondition();
    public static ThreadPoolExecutor matchExecutor = new ThreadPoolExecutor(2, 4, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<>(2), new CustomThreadFactory(), new CustomRejectedHandler());
}
