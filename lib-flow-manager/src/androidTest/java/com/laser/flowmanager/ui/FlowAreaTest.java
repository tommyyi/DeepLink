package com.laser.flowmanager.ui;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.laser.flowmanager.MainActivity;
import com.laser.flowmanager.TestBase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by 易剑锋 on 2017/6/13.
 */
@RunWith(AndroidJUnit4.class)
public class FlowAreaTest extends TestBase
{
    @Rule
    public ActivityTestRule<MainActivity> mRule;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Test
    public void launch() throws Exception
    {
        mRule = new ActivityTestRule<>(MainActivity.class);
        Intent intent=new Intent();
        intent.setClass(mContext,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mRule.launchActivity(intent);
        waiting();
    }
}