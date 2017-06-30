package com.laser.flowmanager.tools;


import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by 易剑锋 on 2017/3/29.
 */

public class PresenterBase
{
    protected void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor)
    {
        mThreadPoolExecutor = threadPoolExecutor;
    }

    private ThreadPoolExecutor mThreadPoolExecutor;

    protected PresenterBase()
    {
    }

    public void quit()
    {
        if (mThreadPoolExecutor != null)
        {
            ThreadPoolHelper.destroy();
        }
    }

    protected void start(final RunnableInWorkThread runnableInWorkThread)
    {
        mThreadPoolExecutor.execute(runnableInWorkThread);
    }

    /**
     * Created by 易剑锋 on 2017/3/30.
     */
    public static class RunnableInMainThread<T> implements Runnable
    {
        protected List<T> mList;

        @Override
        public void run()
        {
        }

        public Runnable setData(List<T> newsList)
        {
            mList = newsList;
            return this;
        }
    }

    /**
     * Created by 易剑锋 on 2017/3/30.
     */
    protected static class RunnableInWorkThread<T> implements Runnable
    {
        protected RunnableInMainThread<T> mRunnableInMainThread;
        protected Handler mHandler;

        protected RunnableInWorkThread(RunnableInMainThread<T> RunnableInMainThread)
        {
            mHandler = new Handler(Looper.getMainLooper());
            mRunnableInMainThread = RunnableInMainThread;
        }

        @Override
        public void run()
        {
        }
    }
}
