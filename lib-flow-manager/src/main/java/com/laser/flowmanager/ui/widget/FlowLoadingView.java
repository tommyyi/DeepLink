package com.laser.flowmanager.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.laser.flowmanager.R;
import com.laser.tools.DisplayUtil;

/**
 * @author Administrator
 * @description
 * @data 2017/6/20
 */

public class FlowLoadingView extends RelativeLayout {

    private View mIvScanner;
    private View mIvText;
    private TranslateAnimation mAnimation;

    public FlowLoadingView(Context context) {
        this(context, null);
    }

    public FlowLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.flow_view_loading, this, true);
        mIvText = findViewById(R.id.iv_text);
        mIvScanner = findViewById(R.id.iv_scanner);
        initAnimator();
    }

    private void initAnimator() {
        mIvText.measure(0, 0);
        int _20dp = DisplayUtil.dip2px(getContext(), 20);
        int scannerActiveArea = mIvText.getMeasuredWidth() + _20dp;
        mAnimation = new TranslateAnimation(-scannerActiveArea / 2, scannerActiveArea / 2, 0, 0);
        mAnimation.setDuration(1200);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setRepeatCount(Animation.INFINITE);
    }


    @Override
    public void setVisibility(int visibility) {
        if (visibility == VISIBLE) {
            mIvScanner.startAnimation(mAnimation);
        } else if (visibility == GONE || visibility == INVISIBLE) {
            mAnimation.cancel();
        }
        super.setVisibility(visibility);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIvScanner.startAnimation(mAnimation);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimation.cancel();
    }
}
