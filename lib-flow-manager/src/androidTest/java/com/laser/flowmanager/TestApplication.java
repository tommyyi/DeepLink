package com.laser.flowmanager;

import android.app.Application;

/**
 * Created by 易剑锋 on 2017/6/13.
 */

public class TestApplication extends Application
{
    public static TestApplication testApplication;
    @Override
    public void onCreate()
    {
        super.onCreate();
        testApplication=this;
    }
}
