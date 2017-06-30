package com.laser.flowmanager.ui;

import java.util.ArrayList;
import java.util.List;

import com.laser.events.ItemRemoveEvent;
import com.laser.flowcommon.IBaseBean;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author Administrator
 * @version 1.0
 */
class FlowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<IBaseBean> mIBaseBeanList;
    private Runnable mRunnableAfterShow;
    private Runnable mRunnableAfterClick;

    public FlowAdapter(Runnable runnableAfterShow, Runnable runnableAfterClick)
    {
        mRunnableAfterShow = runnableAfterShow;
        mRunnableAfterClick = runnableAfterClick;
        EventBus.getDefault().register(this);
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        if (viewType == 0)
            view = new View(parent.getContext());
        else
            view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerView.ViewHolder(view)
        {
        };
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        mIBaseBeanList.get(position).setOnShowRunnable(mRunnableAfterShow);
        mIBaseBeanList.get(position).setOnClickRunnable(mRunnableAfterClick);
        mIBaseBeanList.get(position).bindViewHolder(holder);
    }

    @Override
    public int getItemViewType(int position)
    {
        return mIBaseBeanList.get(position).getType();
    }

    @Override
    public int getItemCount()
    {
        return mIBaseBeanList == null ? 0 : mIBaseBeanList.size();
    }
    
    public void addList(List<IBaseBean> baseBeanList)
    {
        if (mIBaseBeanList == null)
        {
            mIBaseBeanList = new ArrayList<>();
        }
        int oldSize = mIBaseBeanList.size();
        mIBaseBeanList.addAll(baseBeanList);
        notifyItemRangeChanged(oldSize + 1, mIBaseBeanList.size());
    }

    public void setList(List<IBaseBean> baseBeanList)
    {
        mIBaseBeanList = baseBeanList;
        notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRemoveItem(ItemRemoveEvent itemRemoveEvent)
    {
        if(itemRemoveEvent==null||itemRemoveEvent.getIBaseBean()==null)
            return;

        int indexOf = mIBaseBeanList.indexOf(itemRemoveEvent.getIBaseBean());
        IBaseBean iBaseBean = mIBaseBeanList.remove(indexOf);
        /*因为recyclerview的第一个item是下拉加载的item，所以需要加1*/
        if(iBaseBean!=null)
            notifyItemRemoved(indexOf+1);
    }

    public void destroy()
    {
        EventBus.getDefault().unregister(this);
    }
}