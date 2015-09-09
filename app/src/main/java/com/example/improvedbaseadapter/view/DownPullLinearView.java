
package com.example.improvedbaseadapter.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.improvedbaseadapter.BuildConfig;

/**
 * 支持下来刷新的布局。
 * 
 * @author davidleen29
 */
public abstract class DownPullLinearView extends LinearLayout {
    private final static String TAG = "DownPullLinearView";

    @SuppressLint("NewApi")
    public DownPullLinearView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public DownPullLinearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DownPullLinearView(Context context) {
        super(context);
        initView(context);
    }

    private float headContentHeight;

    private float headContentOriginalTopPadding;

    private float stateChangeLength;

    private float maxDragLength;

    private View headView;

    private void initView(Context context)
    {
        headView = getHeadView(context);
        if (headView != null)
        {
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
                    ,
                    LayoutParams.WRAP_CONTENT);

            this.addView(headView, 0, p);
            measureView(headView);
            headContentHeight = headView.getMeasuredHeight();

            headContentOriginalTopPadding = headView.getPaddingTop();
            stateChangeLength = headContentHeight * 2;
            maxDragLength = stateChangeLength * 2;
            // Log.d(TAG, "headContentHeight:" + headContentHeight);
            resetState();

        }
    }

    private void updateHeadViewPadding(float f) {

        headView.setPadding(headView.getPaddingLeft(),
                (int)f,
                headView.getPaddingLeft(),
                headView.getPaddingBottom());
    }

    // 计算headView的width及height值
    protected void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    protected abstract View getHeadView(Context context);

    // 下拉事件
    public class DragEvent {
        // 拉动类型。
        public int dragType;

        public float dragLength;

        // 下拉刷新标志
        public final static int PULL_To_REFRESH = 0;

        // 松开刷新标志
        public final static int RELEASE_To_REFRESH = 1;

        // 正在刷新标志
        public final static int REFRESHING = 2;

        // 正在刷新标志
        public final static int NORMAL = 3;

    }

    private float startX;

    private float startY;

    private int currentState = DragEvent.NORMAL;

    boolean isRecord = false;

    public boolean dispatchTouchEvent(MotionEvent event)
    {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "dispatchTouchEvent:" + event);
        // float tempY = event.getY();
        if (headView == null)
            return super.dispatchTouchEvent(event);

        // 刷新过程中拦截所有事件事件。
        if (currentState == DragEvent.REFRESHING)
            return true;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // if (firstItemIndex == 0 && !isRecored) {
                startX = event.getX();
                startY = event.getY();
                isRecord = true;
                // isRecored = true;
                // // System.out.println("当前-按下高度-ACTION_DOWN-Y："+startY);
                // }
                break;

            case MotionEvent.ACTION_CANCEL:// 失去焦点&取消动作
            case MotionEvent.ACTION_UP:
                isRecord = false;
                if (currentState == DragEvent.RELEASE_To_REFRESH
                        && mOnEnterRefreshStateListener != null)
                {

                    headView.setPadding(headView.getPaddingLeft(),
                            0,
                            headView.getPaddingLeft(),
                            headView.getPaddingBottom());
                    currentState = DragEvent.REFRESHING;
                    enterRefrshState();

                    mOnEnterRefreshStateListener.onEnterRefreshState(this);
                } else
                {
                    resetState();
                }

                // if (state == DONE || state == NONE) {
                // // System.out.println("当前-抬起-ACTION_UP：DONE什么都不做");
                // } else if (state == PULL_To_REFRESH) {
                // state = NONE; // 恢复初始状态
                // changeHeaderViewByState();
                // //
                // System.out.println("当前-抬起-ACTION_UP：PULL_To_REFRESH-->DONE-由下拉刷新状态到刷新完成状态");
                // } else if (state == RELEASE_To_REFRESH) {
                // state = REFRESHING;
                // changeHeaderViewByState();
                // onRefresh();
                // //
                // System.out.println("当前-抬起-ACTION_UP：RELEASE_To_REFRESH-->REFRESHING-由松开刷新状态，到刷新完成状态");
                // }
                // }
                //
                // break;

            case MotionEvent.ACTION_MOVE:

                if (!isRecord)
                {
                    startX = event.getX();
                    startY = event.getY();
                    isRecord = true;
                } else
                {

                    int veticalDragLeng = (int)Math.min(event.getY() - startY,
                            maxDragLength);
                    int horizentalDragLeng = (int)(event.getX() - startX);
                    if (currentState == DragEvent.NORMAL)
                    {
                        // 判断纵向 横向 拖动
                        if (veticalDragLeng > 10 && Math.abs(horizentalDragLeng) < 50)
                        {
                            currentState = DragEvent.PULL_To_REFRESH;
                            enterPullState();
                        }

                    } else
                    {
                        if (veticalDragLeng > stateChangeLength
                                && (currentState == DragEvent.PULL_To_REFRESH))
                        {

                            enterReleaseState();
                            // changState;
                            currentState = DragEvent.RELEASE_To_REFRESH;
                        } else

                        if (veticalDragLeng < stateChangeLength
                                && currentState == DragEvent.RELEASE_To_REFRESH)
                        {
                            enterPullState();
                            currentState = DragEvent.PULL_To_REFRESH;
                        }

                        headView.setPadding(headView.getPaddingLeft(),
                                (int)Math.pow(veticalDragLeng - headContentHeight, 0.8),
                                headView.getPaddingLeft(),
                                headView.getPaddingBottom());
                    }
                }

                //
                // if(tempY-startY)>0
                //
                // if (state != REFRESHING && isRecored) {
                // // 可以松开刷新了
                // if (state == RELEASE_To_REFRESH) {
                // // 往上推，推到屏幕足够掩盖head的程度，但还没有全部掩盖
                // if ((tempY - startY < headContentHeight + 20) && (tempY -
                // startY) > 0) {
                // state = PULL_To_REFRESH;
                // changeHeaderViewByState();
                // //
                // System.out.println("当前-滑动-ACTION_MOVE：RELEASE_To_REFRESH--》PULL_To_REFRESH-由松开刷新状态转变到下拉刷新状态");
                // }
                // // 一下子推到顶
                // else if (tempY - startY <= 0) {
                // state = NONE;
                // changeHeaderViewByState();
                // //
                // System.out.println("当前-滑动-ACTION_MOVE：RELEASE_To_REFRESH--》DONE-由松开刷新状态转变到done状态");
                // }
                // // 往下拉，或者还没有上推到屏幕顶部掩盖head
                // else {
                // // 不用进行特别的操作，只用更新paddingTop的值就行了
                // }
                // }
                // // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                // else if (state == PULL_To_REFRESH) {
                // // 下拉到可以进入RELEASE_TO_REFRESH的状态
                // if (tempY - startY >= headContentHeight + 20
                // && currentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
                // state = RELEASE_To_REFRESH;
                // isBack = true;
                // changeHeaderViewByState();
                // //
                // System.out.println("当前-滑动-PULL_To_REFRESH--》RELEASE_To_REFRESH-由done或者下拉刷新状态转变到松开刷新");
                // }
                // // 上推到顶了
                // else if (tempY - startY <= 0) {
                // state = NONE;
                // changeHeaderViewByState();
                // //
                // System.out.println("当前-滑动-PULL_To_REFRESH--》DONE-由Done或者下拉刷新状态转变到done状态");
                // }
                // }
                // // done状态下
                // else if (state == DONE || state == NONE) {
                // if (tempY - startY > 0) {
                // state = PULL_To_REFRESH;
                // changeHeaderViewByState();
                // //
                // System.out.println("当前-滑动-DONE--》PULL_To_REFRESH-由done状态转变到下拉刷新状态");
                // }
                // }
                //
                // // 更新headView的size
                // if (state == PULL_To_REFRESH) {
                // refreshingPadding = (int)((-1 * headContentHeight + (tempY -
                // startY)));
                // updateHeadViewPadding(refreshingPadding);
                // // headView.setPadding(headView.getPaddingLeft(),
                // // refreshingPadding, headView.getPaddingRight(),
                // // headView.getPaddingBottom());
                // // headView.invalidate();
                // //
                // System.out.println("当前-下拉刷新PULL_To_REFRESH-TopPad："+topPadding);
                // }
                //
                // // 更新headView的paddingTop
                // if (state == RELEASE_To_REFRESH) {
                // refreshingPadding = (int)((tempY - startY -
                // headContentHeight));
                //
                // updateHeadViewPadding(refreshingPadding);
                // //
                // System.out.println("当前-释放刷新RELEASE_To_REFRESH-TopPad："+topPadding);
                // }
                // }
                break;
        }

        // return super.onTouchEvent(event);

        // super.dispatchTouchEvent(ev)
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onInterceptTouchEvent:" + event);
        if (currentState != DragEvent.NORMAL)
            return true;
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onTouchEvent:" + event);
        return super.onTouchEvent(event);
    }

    protected abstract void enterRefrshState();

    protected abstract void enterPullState();

    public void resetState()
    {

        // release to normal
        headView.setPadding(headView.getPaddingLeft(),
                (int)-headContentHeight,
                headView.getPaddingLeft(),
                headView.getPaddingBottom());

        currentState = DragEvent.NORMAL;
        enterNormalState();

    }

    protected abstract void enterNormalState();

    protected abstract void enterReleaseState();

    // private List<DragEvent> dragEvents = new ArrayList<DragEvent>();
    //
    // protected DragEvent getNewDragEvent()
    // {
    // int size = dragEvents.size();
    // if (size > 0)
    // return dragEvents.remove(size - 1);
    // else
    // return new DragEvent();
    // }
    //
    // protected void releaseDragEvent(DragEvent event)
    // {
    // dragEvents.add(event);
    // }

    /**
     * 执行刷新的接口
     * 
     * @author davidlee29
     */
    public interface OnEnterRefreshStateListener
    {
        public void onEnterRefreshState(DownPullLinearView view);
    }

    private OnEnterRefreshStateListener mOnEnterRefreshStateListener = null;

    public void setOnEnterRefreshStateListener(OnEnterRefreshStateListener listener)
    {
        this.mOnEnterRefreshStateListener = listener;
    }
}
