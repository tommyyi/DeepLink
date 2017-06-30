package com.laser.flowmanager.contract;

import android.content.Context;

/**
 * @author Administrator
 * @version 1.0
 * @created 13-六月-2017 15:13:43
 */
public interface IFlowPresenter
{
    void loadMore();
    void refresh();
    void loadCache(Context context);
    /*退出处理*/
    void quit();
}