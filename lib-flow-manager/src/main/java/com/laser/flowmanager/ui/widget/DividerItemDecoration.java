package com.laser.flowmanager.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.laser.flowmanager.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration
{
    
    private static final int[] ATTRS = new int[] {android.R.attr.listDivider};
    
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    
    private Drawable mDivider;
    
    public void setHeadlineSearch(boolean headlineSearch)
    {
        isHeadlineSearch = headlineSearch;
    }
    
    private boolean isHeadlineSearch = false;
    
    public void setHeadlineRecommend(boolean headlineRecommend)
    {
        isHeadlineRecommend = headlineRecommend;
    }
    
    private boolean isHeadlineRecommend = false;
    
    private int mOrientation;
    
    private Context mContext;
    
    private int mItemSize = 1;
    
    public DividerItemDecoration(Context context, int orientation)
    {
        mContext = context;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }
    
    public void setOrientation(int orientation)
    {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)
        {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }
    
    @Override
    public void onDraw(Canvas c, RecyclerView parent)
    {
        if (mOrientation == VERTICAL_LIST)
        {
            drawVertical(c, parent);
        }
        else
        {
            drawHorizontal(c, parent);
        }
    }
    
    public void drawVertical(Canvas c, RecyclerView parent)
    {
        int padding = (int)mContext.getResources().getDimension(R.dimen.activity_horizontal_margin);
        final int left = parent.getPaddingLeft() + padding;
        final int right = parent.getWidth() - parent.getPaddingRight() - padding;
        
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            final View child = parent.getChildAt(i);
            //RecyclerView v = new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            /*【头条搜索界面】去掉header和footer的分割线，
            * 【头条推荐界面】去掉footer的分割线*/
            if(isHeadlineSearch||isHeadlineRecommend){
                if(params.getViewAdapterPosition() == 0){
                    continue;
                }
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            /*if (isHeadlineSearch||isHeadlineRecommend)
            {

                if (i > 0 && i < childCount - 1)
                {
                    mDivider.setBounds(left, top, right, top + 1);
                    mDivider.draw(c);
                }
            }
            else if (isHeadlineRecommend)
            {
                if (i < childCount - 1)
                {
                    mDivider.setBounds(left, top, right, top + 1);
                    mDivider.draw(c);
                }
            }
            else
            {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }*/
        }
    }
    
    public void drawHorizontal(Canvas c, RecyclerView parent)
    {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
    
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent)
    {
        //mItemSize = mDivider.getIntrinsicHeight();
        if (mOrientation == VERTICAL_LIST)
        {
            outRect.set(0, 0, 0, mItemSize);
        }
        else
        {
            outRect.set(0, 0, mItemSize, 0);
        }
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        if (mOrientation == VERTICAL_LIST)
        {
            outRect.set(0, 0, 0, mItemSize);
        }
        else
        {
            outRect.set(0, 0, mItemSize, 0);
        }
    }
}