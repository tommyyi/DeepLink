package com.laser.flowmanager.ui;

import java.lang.reflect.Field;
import java.util.List;

import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.SimpleViewSwitcher;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.laser.flowcommon.IAdModel;
import com.laser.flowcommon.IBaseBean;
import com.laser.flowcommon.IFlowModel;
import com.laser.flowmanager.R;
import com.laser.flowmanager.contract.IFlowPresenter;
import com.laser.flowmanager.contract.IFlowView;
import com.laser.flowmanager.ui.widget.DividerItemDecoration;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Administrator
 * @version 1.0
 * @created 13-六月-2017 14:43:54
 */
public class FlowArea implements IFlowView
{
    private DividerItemDecoration mItemDecoration;
    
    private FlowAdapter mFlowAdapter;
    
    private IFlowPresenter mIFlowPresenter;
    
    private XRecyclerView mXRecyclerView;
    
    private boolean mIsItemDecorationAdded = false;
    private Runnable mRunnableAfterShow;
    private Runnable mRunnableAfterClick;
    private View mLoadingView;

    /**
     * @param activity
     * @param XRecyclerView
     * @param iAdModel
     * @param iFlowModel
     * @param runnableAfterShow
     * @param runnableAfterClick
     */
    public FlowArea(Activity activity, XRecyclerView XRecyclerView, IAdModel iAdModel, IFlowModel iFlowModel, Runnable runnableAfterShow, Runnable runnableAfterClick)
    {
        mIFlowPresenter = new FlowPresenter(this, iAdModel, iFlowModel);
        mRunnableAfterShow=runnableAfterShow;
        mRunnableAfterClick=runnableAfterClick;
        init(activity, XRecyclerView);
    }

    private void init(Activity activity, XRecyclerView XRecyclerView)
    {
        mFlowAdapter = new FlowAdapter(mRunnableAfterShow,mRunnableAfterClick);
        mItemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST);
        mItemDecoration.setHeadlineSearch(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mXRecyclerView = XRecyclerView;
        mXRecyclerView.setLoadingListener(new LoadingListener());
        mXRecyclerView.setLayoutManager(layoutManager);
        //mXRecyclerView.addOnScrollListener(new PicassoHelper.PicassoOnScrollListener());

        mXRecyclerView.setAdapter(mFlowAdapter);
        setLoadingImageView(mXRecyclerView);

        mLoadingView = activity.findViewById(R.id.flowLoadingView);
        setUIState(true);
        /*加载缓存，加载缓存完成后，会执行自动刷新操作*/
        mIFlowPresenter.loadCache(activity.getApplicationContext());
    }

    private void setUIState(boolean isLoading)
    {
        if(isLoading)
        {
            mXRecyclerView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE);
        }
        else
        {
            mXRecyclerView.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);
        }
    }

    private void setLoadingImageView(XRecyclerView recyclerView)
    {
        try
        {
            Field field_refresh_header = getField(XRecyclerView.class, recyclerView, new Checker()
            {
                public boolean check(Object fieldInstance)
                {
                    return fieldInstance instanceof ArrowRefreshHeader;
                }
            },false);
            if (field_refresh_header == null)
                return;
            
            field_refresh_header.setAccessible(true);
            Object refresh_header = field_refresh_header.get(recyclerView);
            setProgressBarView(refresh_header, ArrowRefreshHeader.class, recyclerView);
            setTextColor(ArrowRefreshHeader.class,refresh_header,true);

            Field field_footer = getField(XRecyclerView.class, recyclerView, new Checker()
            {
                @Override
                public boolean check(Object fieldInstance)
                {
                    return fieldInstance instanceof LoadingMoreFooter;
                }
            },false);
            if (field_footer == null)
                return;
            
            field_footer.setAccessible(true);
            LoadingMoreFooter footer = (LoadingMoreFooter)field_footer.get(recyclerView);
            setProgressBarView(footer, LoadingMoreFooter.class, recyclerView);
            setTextColor(LoadingMoreFooter.class,footer,false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private Field getField(Class instanceClass, Object instance, Checker checker, boolean requestSecond)
        throws Exception
    {
        Field[] declaredFields = instanceClass.getDeclaredFields();
        for (Field field : declaredFields)
        {
            field.setAccessible(true);
            Object fieldInstance = field.get(instance);
            if (checker.check(fieldInstance))
            {
                if(requestSecond)
                {
                    requestSecond=false;
                }
                else
                    return field;
            }
        }
        return null;
    }
    
    private void setProgressBarView(Object instance, Class<?> instanceClass, XRecyclerView recyclerView)
        throws Exception
    {
        Field field_progressbar = getField(instanceClass, instance, new Checker()
        {
            public boolean check(Object fieldInstance)
            {
                return fieldInstance instanceof SimpleViewSwitcher;
            }
        },false);
        if (field_progressbar == null)
            return;
        
        field_progressbar.setAccessible(true);
        SimpleViewSwitcher progressBar = (SimpleViewSwitcher)field_progressbar.get(instance);
        View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.progress_circle,
            (ViewGroup)recyclerView.getParent(),
            false);
        progressBar.setView(view);
    }

    private void setTextColor(Class<?> instanceClass, Object instance, boolean requestSecond) throws Exception
    {
        Field field_mText=getField(instanceClass, instance, new Checker()
        {
            @Override
            public boolean check(Object fieldInstance)
            {
                return fieldInstance instanceof TextView;
            }
        },requestSecond);
        if(field_mText==null)
            return;

        field_mText.setAccessible(true);
        TextView textView=(TextView)field_mText.get(instance);
        textView.setTextColor(0xffB2B2B2);
    }

    private void addItemDecoration(XRecyclerView xRecyclerView)
    {
        if (!mIsItemDecorationAdded)
        {
            mIsItemDecorationAdded = true;
            xRecyclerView.addItemDecoration(mItemDecoration);
        }
    }

    private interface Checker
    {
        boolean check(Object fieldInstance);
    }
    
    @Override
    public void onLoadMoreReturn(@Nullable List<IBaseBean> newsList)
    {
        mXRecyclerView.loadMoreComplete();
        if (newsList != null && !newsList.isEmpty())
        {
            mFlowAdapter.addList(newsList);
        }
    }
    
    @Override
    public void onRefreshReturn(@Nullable List<IBaseBean> newsList)
    {
        setUIState(false);
        mXRecyclerView.refreshComplete();
        if (newsList != null && !newsList.isEmpty())
        {
            addItemDecoration(mXRecyclerView);
            mFlowAdapter.setList(newsList);
        }
    }
    
    @Override
    public void onCacheReturn(@Nullable List<IBaseBean> newsList)
    {
        //缓存加载完毕后，执行刷新操作
        mXRecyclerView.refresh();
        if (newsList != null && !newsList.isEmpty())
        {
            addItemDecoration(mXRecyclerView);
            mFlowAdapter.setList(newsList);
        }
    }
    
    private class LoadingListener implements XRecyclerView.LoadingListener
    {
        /**
         * 用户下拉刷新的捕获
         */
        @Override
        public void onRefresh()
        {
            mIFlowPresenter.refresh();
        }
        
        /**
         * 用户上拉加载操作的捕获
         */
        @Override
        public void onLoadMore()
        {
            mIFlowPresenter.loadMore();
        }
    }

    public void onDestroy()
    {
        if (mIFlowPresenter!=null)
            mIFlowPresenter.quit();
        if(mFlowAdapter!=null)
            mFlowAdapter.destroy();
    }
}