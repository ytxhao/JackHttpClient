package com.jeck.http;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPoolManager {
    private static final String TAG = "ThreadPoolManager";
    private static ThreadPoolManager instance = new ThreadPoolManager();
    private LinkedBlockingQueue<Future<?>> taskQueue = new LinkedBlockingQueue<>();
    private ThreadPoolExecutor threadPoolExecutor;

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    private ThreadPoolManager() {
        threadPoolExecutor = new ThreadPoolExecutor(4, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), handler);
        threadPoolExecutor.execute(runnable);
    }

    public <T> boolean removeTask(FutureTask futureTask) {
        boolean result = false;
        /**
         * 阻塞式队列是否含有线程
         */
        if (taskQueue.contains(futureTask)) {
            taskQueue.remove(futureTask);
        } else {
            result = threadPoolExecutor.remove(futureTask);
        }
        return result;
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                FutureTask mFutureTask = null;

                try {
                    /**
                     * 阻塞式函数
                     */
                    Log.i(TAG, "等待队列" + taskQueue.size());
                    mFutureTask = (FutureTask) taskQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mFutureTask != null) {
                    threadPoolExecutor.execute(mFutureTask);
                }
                Log.i(TAG, "线程池大小" + threadPoolExecutor.getPoolSize());
            }
        }
    };

    public <T> void execute(FutureTask<T> futureTask) throws InterruptedException {
        taskQueue.put(futureTask);
    }

    private RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                taskQueue.put(new FutureTask<Object>(r, null) {
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
