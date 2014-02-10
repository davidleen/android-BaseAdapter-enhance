
package com.example.improvedbaseadapter.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.improvedbaseadapter.Log;
import com.example.improvedbaseadapter.R;

/**
 * 支持下来刷新的布局。
 * 
 * @author davidleen29
 */
public class DownPullImplLinearView extends DownPullLinearView {
    private final static String TAG = "DownPullImplLinearView";

    @SuppressLint("NewApi")
    public DownPullImplLinearView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public DownPullImplLinearView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public DownPullImplLinearView(Context context) {
        super(context);

    }

    private View headView;

    private TextView tipsTextview;

    private TextView lastUpdatedTextView;

    private ImageView arrowImageView;

    private ProgressBar progressBar;

    // 用来设置箭头图标动画效果
    private RotateAnimation animation;

    private RotateAnimation reverseAnimation;

    @Override
    protected View getHeadView(Context context) {
        // 设置滑动效果
        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(100);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(100);
        reverseAnimation.setFillAfter(true);

        headView = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_head, null);

        arrowImageView = (ImageView)headView.findViewById(R.id.head_arrowImageView);
        arrowImageView.setMinimumWidth(50);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar)headView.findViewById(R.id.head_progressBar);
        tipsTextview = (TextView)headView.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView)headView.findViewById(R.id.head_lastUpdatedTextView);

        return headView;

    }

    @Override
    protected void enterRefrshState() {
        Log.d(TAG, "enterRefrshState");
        progressBar.setVisibility(View.VISIBLE);
        arrowImageView.clearAnimation();
        arrowImageView.setVisibility(View.GONE);
        tipsTextview.setText("正在刷新，请稍候");
        lastUpdatedTextView.setVisibility(View.GONE);

    }

    @Override
    protected void enterPullState()
    {
        tipsTextview.setVisibility(View.VISIBLE);
        lastUpdatedTextView.setVisibility(View.VISIBLE);
        arrowImageView.clearAnimation();
        arrowImageView.setVisibility(View.VISIBLE);
        arrowImageView.clearAnimation();
        arrowImageView.startAnimation(reverseAnimation);

        tipsTextview.setText("下拉刷新");
    }

    @Override
    protected void enterReleaseState() {
        Log.d(TAG, "enterReleaseState");

        arrowImageView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        tipsTextview.setVisibility(View.VISIBLE);
        lastUpdatedTextView.setVisibility(View.VISIBLE);

        arrowImageView.clearAnimation();
        arrowImageView.startAnimation(animation);

        tipsTextview.setText("松开开始刷新");

    }

    @Override
    protected void enterNormalState() {

        tipsTextview.setVisibility(View.VISIBLE);
        lastUpdatedTextView.setVisibility(View.VISIBLE);
        arrowImageView.clearAnimation();
        arrowImageView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        tipsTextview.setText("下拉刷新");

    }

}
