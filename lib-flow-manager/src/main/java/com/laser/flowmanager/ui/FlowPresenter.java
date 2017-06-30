package com.laser.flowmanager.ui;

import java.util.ArrayList;
import java.util.List;

import com.laser.flowcommon.IAdModel;
import com.laser.flowcommon.IBaseBean;
import com.laser.flowcommon.IFlowModel;
import com.laser.flowmanager.contract.IFlowPresenter;
import com.laser.flowmanager.contract.IFlowView;
import com.laser.flowmanager.tools.PresenterBase;
import com.laser.flowmanager.tools.ThreadPoolHelper;

import android.content.Context;

/**
 * @author Administrator
 * @version 1.0
 * @created 13-六月-2017 14:58:18
 */
class FlowPresenter extends PresenterBase implements IFlowPresenter
{
    private IFlowView mIFlowView;
    private IAdModel mIAdModel;
    private IFlowModel mIFlowModel;

    /**
     * @param IFlowView
     * @param iAdModel
     * @param iFlowModel
     */
    public FlowPresenter(IFlowView IFlowView, IAdModel iAdModel, IFlowModel iFlowModel)
    {
        super();
        init(IFlowView);
        mIAdModel = iAdModel;
        mIFlowModel = iFlowModel;
    }

    private void init(IFlowView IFlowView)
    {
        mIFlowView = IFlowView;
        ThreadPoolHelper.init(1, "flow", 2, 2, 3000);
        setThreadPoolExecutor(ThreadPoolHelper.getThreadPoolExecutor());
    }

    @Override
    public void loadMore()
    {
        RunnableInMainThread<IBaseBean> runnableInMainThread = new RunnableInMainThread<IBaseBean>()
        {
            @Override
            public void run()
            {
                mIFlowView.onLoadMoreReturn(mList);
            }
        };
        RunnableInWorkThread<IBaseBean> runnableInWorkThread = new RunnableInWorkThread<IBaseBean>(runnableInMainThread)
        {
            @Override
            public void run()
            {
                List<IBaseBean> infoList = mIFlowModel.getInfoList();
                fillAd(infoList);
                mRunnableInMainThread.setData(infoList);
                mHandler.post(mRunnableInMainThread);
            }
        };
        start(runnableInWorkThread);
    }
    
    @Override
    public void refresh()
    {
        RunnableInMainThread<IBaseBean> runnableInMainThread = new RunnableInMainThread<IBaseBean>()
        {
            @Override
            public void run()
            {
                mIFlowView.onRefreshReturn(mList);
            }
        };
        RunnableInWorkThread<IBaseBean> runnableInWorkThread = new RunnableInWorkThread<IBaseBean>(runnableInMainThread)
        {
            @Override
            public void run()
            {
                List<IBaseBean> infoList = mIFlowModel.refresh();
                fillAd(infoList);
                mRunnableInMainThread.setData(infoList);
                mHandler.post(mRunnableInMainThread);
            }
        };
        start(runnableInWorkThread);
    }

    private void fillAd(List<IBaseBean> infoList)
    {
        if(infoList!=null)
        {
            for(int index=0;index<infoList.size();index++)
            {
                index+=2;
                if(index>infoList.size())
                    break;

                IBaseBean ad = mIAdModel.getAd();
                if (ad!=null)
                {
                    infoList.add(index, ad);
                }
            }
        }
    }

    @Override
    public void loadCache(Context context)
    {
        RunnableInMainThread<IBaseBean> runnableInMainThread = new RunnableInMainThread<IBaseBean>()
        {
            @Override
            public void run()
            {
                mIFlowView.onCacheReturn(mList);
            }
        };
        RunnableInWorkThread<IBaseBean> runnableInWorkThread = new RunnableInWorkThread<IBaseBean>(runnableInMainThread)
        {
            @Override
            public void run()
            {
                List<IBaseBean> infoList = mIFlowModel.getCacheList();
                mRunnableInMainThread.setData(infoList);
                mHandler.post(mRunnableInMainThread);
            }
        };
        start(runnableInWorkThread);
    }
}