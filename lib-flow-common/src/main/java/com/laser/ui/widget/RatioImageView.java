package com.laser.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.laser.flowcommon.R;


/**
 * @author Administrator
 * @description
 * @data 2017/6/9
 */

public class RatioImageView extends ImageView
{
    
    private float mRatio = 1f;
    
    public RatioImageView(Context context)
    {
        this(context, null);
    }
    
    public RatioImageView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        
    }
    
    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        if (attrs != null)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RatioImageView);
            mRatio = a.getFloat(R.styleable.RatioImageView_ratio, 1f);
            a.recycle();
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = (int)(widthSize * mRatio + 0.5f);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void setRatio(float ratio) {
        mRatio = ratio;
    }
}
