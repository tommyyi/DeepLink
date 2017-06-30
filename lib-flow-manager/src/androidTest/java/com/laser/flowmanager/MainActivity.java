package com.laser.flowmanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by 易剑锋 on 2017/6/13.
 */

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //IScreenFlow flow360 = new Flow360(this, null, com.ulegendtimes.mobile.plugin.ttt.R.id.rl_root);

        setContentView(com.ulegendtimes.mobile.plugin.ttt.R.layout.main);
        //flow360.initView(com.ulegendtimes.mobile.plugin.ttt.R.id.rl_root);
    }
}
