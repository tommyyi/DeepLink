package com.laser.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;

import com.laser.tools.DisplayUtil;


/**
 * Created by Administrator on 2017/4/5.
 */

public class LargeTouchableAreasImageView extends AppCompatImageView {


    private int mTouchAdditionBottom;
    private int mTouchAdditionLeft;
    private int mTouchAdditionRight;
    private int mTouchAdditionTop;
    private int mPreviousLeft = -1;
    private int mPreviousRight = -1;
    private int mPreviousBottom = -1;
    private int mPreviousTop = -1;
    private Rect mRect;


    public LargeTouchableAreasImageView(Context context) {
        this(context, null);
    }

    public LargeTouchableAreasImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LargeTouchableAreasImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRect = new Rect();
        mTouchAdditionBottom = DisplayUtil.dip2px(context, 24);
        mTouchAdditionLeft = DisplayUtil.dip2px(context, 24);
        mTouchAdditionRight = DisplayUtil.dip2px(context, 24);
        mTouchAdditionTop = DisplayUtil.dip2px(context, 24);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (left != mPreviousLeft || top != mPreviousTop || right != mPreviousRight || bottom != mPreviousBottom) {
            mPreviousLeft = left;
            mPreviousTop = top;
            mPreviousRight = right;
            mPreviousBottom = bottom;
            mRect.set(left - mTouchAdditionLeft, top - mTouchAdditionTop, right + mTouchAdditionRight, bottom + mTouchAdditionBottom);
            TouchDelegate touchDelegate = new TouchDelegate(mRect, this);
            final View parent = (View) this.getParent();
            if (parent != null) {
                parent.setTouchDelegate(touchDelegate);
            }
        }
    }
}
