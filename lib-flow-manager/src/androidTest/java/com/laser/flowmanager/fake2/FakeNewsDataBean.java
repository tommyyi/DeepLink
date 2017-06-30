package com.laser.flowmanager.fake2;

import com.laser.flowcommon.IBaseBean;

import android.support.v7.widget.RecyclerView;

/**
 * Created by 易剑锋 on 2017/6/13.
 */

public class FakeNewsDataBean implements IBaseBean
{
    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder)
    {

    }

    @Override
    public int getType()
    {
        return com.ulegendtimes.mobile.plugin.ttt.R.layout.holder2;
    }

    @Override
    public void setOnClickRunnable(Runnable runnable)
    {

    }

    @Override
    public void setOnShowRunnable(Runnable runnable)
    {

    }
}
