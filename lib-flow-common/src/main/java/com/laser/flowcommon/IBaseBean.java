package com.laser.flowcommon;


import android.support.v7.widget.RecyclerView;

/**
 * @author Administrator
 * @version 1.0
 * @created 13-六月-2017 17:53:52
 */
public interface IBaseBean {

	/**
	 * 
	 * @param viewHolder    viewHolder
	 */
	public void bindViewHolder(RecyclerView.ViewHolder viewHolder);

	/**
	 * 使用viewholder xml的资源Id作为type
	 */
	public int getType();

	/**
	 * 
	 * @param runnable    runnable
	 */
	public void setOnClickRunnable(Runnable runnable);

	/**
	 * 
	 * @param runnable    runnable
	 */
	public void setOnShowRunnable(Runnable runnable);

}