package com.laser.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laser.flowcommon.R;
import com.laser.tools.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/30.
 */

public class NewsDislikePopupWindow extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {

    private Context context;
    //动画持续时间
    private final static int ANIMATION_IN_TIME = 400;
    private final static int ANIMATION_OUT_TIME = 400;
    private View mPopContent;
    private TextView mTvDislikeReason;
    private TextView mTvDislikeCommit;
    private LinearLayout mFilterWordsContainer;
    private View mRootView;
    private static final int TAG_COMMIT_VIEW = -1;
    private List<String> selectedFilterWords = new ArrayList<>();
    private RelativeLayout mRootLayout;
    private ImageView mPopCorner;
    private OnDislikeCommitLitener commitListener;


    private int mPopWidth;


    public void setOnCommitListener(OnDislikeCommitLitener commitListener) {
        this.commitListener = commitListener;
    }


    public NewsDislikePopupWindow(Context context) {
        super(null, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.flow_view_popup_news_dislike, null);
        int[] screenSize = DisplayUtil.getScreenSize(context);
        int screenWidth = screenSize[0];
        mPopWidth = screenWidth - DisplayUtil.dip2px(context, 30);
        setWidth(mPopWidth);
        setContentView(contentView);
        initView(contentView);
        setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
        setOutsideTouchable(true);
        setOnDismissListener(this);
    }

    private void initView(View contentView) {
        mRootLayout = (RelativeLayout) contentView.findViewById(R.id.news_dislike_pop_root);
        mPopContent = contentView.findViewById(R.id.news_dislike_pop_content);
        mPopCorner = (ImageView) contentView.findViewById(R.id.news_dislike_pop_corner);
        mTvDislikeReason = (TextView) contentView.findViewById(R.id.tv_news_dislike_reason);
        mTvDislikeCommit = (TextView) contentView.findViewById(R.id.tv_news_dislike_commit);
        mFilterWordsContainer = (LinearLayout) contentView.findViewById(R.id.news_dislike_filter_words_container);
        mTvDislikeCommit.setOnClickListener(this);
        mTvDislikeCommit.setTag(TAG_COMMIT_VIEW);
    }


    public void setFilterWords(List<String> filterWords) {
        if (filterWords == null) {
            return;
        }
        LinearLayout linearLayout = null;
        int size = filterWords.size();
        for (int i = 0; i < size; i++) {
            String filterWord = filterWords.get(i);
            TextView textView = getTextView(filterWord);
            int index = i % 2;
            if (index == 0) {
                linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int llMarginTop = DisplayUtil.dip2px(context, 8);
                layoutParams.setMargins(0, llMarginTop, 0, 0);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                mFilterWordsContainer.addView(linearLayout);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            int marginLeft = 0;
            int marginRight = 0;
            int marginTop = 0;
            int marginBottom = 0;
            if (index == 0) {
                marginRight = DisplayUtil.dip2px(context, 8);
            }
            if (i < size - 2) {
                marginBottom = DisplayUtil.dip2px(context, 8);
            }
            layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
            if (linearLayout == null) {
                continue;
            }
            linearLayout.addView(textView, index, layoutParams);
            if (i == size - 1 && index == 0) {
                textView = getTextView(filterWord);
                layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                layoutParams.setMargins(0, 0, 0, 0);
                textView.setVisibility(View.INVISIBLE);
                textView.setEnabled(false);
                linearLayout.addView(textView, index + 1, layoutParams);
            }
        }
    }

    @NonNull
    private TextView getTextView(String filterWord) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));

        int paddingLeft = DisplayUtil.dip2px(context, 8);
        int paddingTop = DisplayUtil.dip2px(context, 8);
        int paddingRight = DisplayUtil.dip2px(context, 8);
        int paddingBottom = DisplayUtil.dip2px(context, 8);
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        textView.setText(filterWord);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setBackground(context.getResources().getDrawable(R.drawable.flow_filter_words_bg_normal));
        textView.setTextColor(context.getResources().getColor(R.color.news_filter_words_color_nor));
        textView.setOnClickListener(this);
        textView.setTag(false);
        return textView;
    }


    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof Integer && (int) tag == TAG_COMMIT_VIEW) {
            if (commitListener != null) {
                commitListener.onCommit(selectedFilterWords);
            }
            dismiss();
        } else if (tag instanceof Boolean) {
            TextView textView = (TextView) v;
            boolean selected = !(boolean) textView.getTag();
            textView.setTag(selected);
            if (selected) {
                textView.setBackground(context.getResources().getDrawable(R.drawable.flow_filter_words_bg_select));
                textView.setTextColor(context.getResources().getColor(R.color.news_filter_words_color_selected));
                selectedFilterWords.add(textView.getText().toString());
            } else {
                textView.setBackground(context.getResources().getDrawable(R.drawable.flow_filter_words_bg_normal));
                textView.setTextColor(context.getResources().getColor(R.color.news_filter_words_color_nor));
                selectedFilterWords.remove(textView.getText().toString());
            }
            if (!selectedFilterWords.isEmpty()) {
                mTvDislikeCommit.setText("确定");
                String string = "已选" + selectedFilterWords.size() + "理由";
                SpannableString spannableString = new SpannableString(string);
                spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.news_filter_words_color_selected)), 2, string.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTvDislikeReason.setText(spannableString);
            } else {
                mTvDislikeCommit.setText("不感兴趣");
                mTvDislikeReason.setText("可选内容，精确屏蔽");
            }
        }
    }


    public void show(View anchor, View deleteIcon) {
        update();

        int[] deleteIconLocations = new int[2];
        deleteIcon.getLocationOnScreen(deleteIconLocations);
        int deleteIconX = deleteIconLocations[0];
        int deleteIconY = deleteIconLocations[1];
        int[] screenSize = DisplayUtil.getScreenSize(context);
        int screenWidth = screenSize[0];
        int screenHeight = screenSize[1];
        if (deleteIconY < screenHeight / 2) {//popupwindow显示在anchor下方
            mPopCorner.setImageResource(R.drawable.flow_ic_pop_corner_up);
            mPopCorner.measure(0, 0);
            int cornerWidth = mPopCorner.getMeasuredWidth();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.news_dislike_pop_corner);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = deleteIconX - (screenWidth - mPopWidth) / 2 - cornerWidth + deleteIcon.getMeasuredWidth() / 2;
            mPopCorner.setLayoutParams(params);
            mPopContent.setLayoutParams(layoutParams);
            int yoff = getYoffForBelow(anchor, deleteIcon, deleteIconY);
            showAsDropDown(anchor, (anchor.getMeasuredWidth() - mPopWidth) / 2, -yoff);
        } else {//popupwindow显示在anchor上方
            mPopCorner.setImageResource(R.drawable.flow_ic_pop_corner_down);
            mPopCorner.measure(0, 0);
            int cornerWidth = mPopCorner.getMeasuredWidth();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.news_dislike_pop_content);
            layoutParams.leftMargin = deleteIconX - (screenWidth - mPopWidth) / 2 - cornerWidth + deleteIcon.getMeasuredWidth() / 2;
            mPopCorner.setLayoutParams(layoutParams);
            int yoff = getYoffForAbove(anchor, deleteIconY);
            showAsDropDown(anchor, (anchor.getMeasuredWidth() - mPopWidth) / 2, -yoff);
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 0.7f;
            window.setAttributes(lp);
        } else {
            mRootView = anchor.getRootView();
            mRootView.setAlpha(0.7f);
        }
    }


    private int getYoffForBelow(View anchor, View deleteIcon, int deleteIconY) {
        int[] anchorLocations = new int[2];
        anchor.getLocationOnScreen(anchorLocations);
        int anchorY = anchorLocations[1];
        int anchorHeight = anchor.getMeasuredHeight();
        int deleteIconHeight = deleteIcon.getMeasuredHeight();
        return anchorHeight - (deleteIconY - anchorY) - deleteIconHeight;
    }

    private int getYoffForAbove(View anchor, int deleteIconY) {
        getContentView().measure(0, 0);
        int popHeight = getContentView().getMeasuredHeight();
        int[] anchorLocations = new int[2];
        anchor.getLocationOnScreen(anchorLocations);
        int anchorY = anchorLocations[1];
        int anchorHeight = anchor.getMeasuredHeight();
        return anchorHeight - (deleteIconY - anchorY) + popHeight;
    }

    @Override
    public void onDismiss() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 1f;
            window.setAttributes(lp);
        } else {
            if (mRootView != null) {
                mRootView.setAlpha(1f);
                mRootView = null;
            }
        }
    }

    private Animation createInAnimation(Context context) {
        AnimationSet set = new AnimationSet(context, null);
        set.setFillAfter(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(scaleAnimation);
        set.setDuration(ANIMATION_IN_TIME);
        return set;
    }

    private Animation createOutAnimation(Context context) {
        AnimationSet set = new AnimationSet(context, null);
        set.setFillAfter(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(scaleAnimation);
        set.setDuration(ANIMATION_OUT_TIME);
        return set;
    }


    public interface OnDislikeCommitLitener {

        void onCommit(List<String> selectedFilterWords);

    }

}
