package com.laser.flowmanager.contract;

import java.util.List;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.laser.flowcommon.IBaseBean;

/**
 * @author Administrator
 * @version 1.0
 * @created 13-六月-2017 16:07:41
 */
public interface IFlowView
{
    @MainThread
    void onLoadMoreReturn(@Nullable List<IBaseBean> newsList);
    
    @MainThread
    void onRefreshReturn(@Nullable List<IBaseBean> newsList);
    
    @MainThread
    void onCacheReturn(@Nullable List<IBaseBean> newsList);
}