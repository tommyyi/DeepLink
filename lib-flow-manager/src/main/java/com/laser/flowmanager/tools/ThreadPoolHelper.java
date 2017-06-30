package com.laser.flowmanager.tools;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 易剑锋 on 2017/3/14.
 * 【初始化线程池】
 * 【获取线程池单例】
 */

public class ThreadPoolHelper
{
    /**
     * 线程池单例
     */
    private static ThreadPoolExecutor mThreadPoolExecutor;

    public static ThreadPoolExecutor getThreadPoolExecutor()
    {
        return mThreadPoolExecutor;
    }

    /**
     * 初始化线程池
     * @param queueCapacity 任务队列容量
     * @param threadNamePrefix 线程名字前缀
     * @param corePoolSize 核心池大小，核心池在没有任务时不会退出，除非设置了allowCoreThreadTimeOut
     * @param maximumPoolSize 本线程池总的最大容量
     * @param keepAliveTime 非核心线程的没有任务可执行时的存活时间
     * @returnThreadPoolExecutor单例
     */
    public static ThreadPoolExecutor init(int queueCapacity, String threadNamePrefix, int corePoolSize, int maximumPoolSize, int keepAliveTime)
    {
        if (mThreadPoolExecutor == null)
        {
            synchronized (ThreadPoolHelper.class)
            {
                if (mThreadPoolExecutor == null)
                    initThreadPool(queueCapacity, threadNamePrefix, corePoolSize, maximumPoolSize, keepAliveTime);
            }
        }
        return mThreadPoolExecutor;
    }

    private static void initThreadPool(final int queueCapacity, final String threadNamePrefix, final int corePoolSize, int maximumPoolSize, int keepAliveTime)
    {
        TimeUnit unit = TimeUnit.MILLISECONDS;//单位
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(queueCapacity);//阻塞队列
        ThreadFactory threadFactory = new ThreadFactory()
        {
            private final AtomicInteger THREAD_NUM = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable runnable)
            {
                return new Thread(runnable, threadNamePrefix + THREAD_NUM.getAndIncrement());
            }
        };
        RejectedExecutionHandler handler = getPolicy(threadNamePrefix);
        mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    private static RejectedExecutionHandler getPolicy(final String threadNamePrefix)
    {
        return new RejectedExecutionHandler()
        {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
            {
                Log.i("", threadNamePrefix + "线程池不够用");
            }
        };
    }

    /**
     * 销毁线程池
     */
    public static void destroy()
    {
        if (mThreadPoolExecutor != null)
        {
            synchronized (ThreadPoolHelper.class)
            {
                if (mThreadPoolExecutor != null)
                {
                    mThreadPoolExecutor.allowCoreThreadTimeOut(true);
                    mThreadPoolExecutor.shutdown();
                    mThreadPoolExecutor = null;
                }
            }
        }
    }
}
