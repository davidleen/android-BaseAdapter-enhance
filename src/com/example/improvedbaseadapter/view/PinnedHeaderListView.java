package com.example.improvedbaseadapter.view;

/**
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/***
 * A ListView that maintains a header pinned at the top of the list. The pinned header can be pushed up and dissolved as needed.
 */
public class PinnedHeaderListView extends ListView {

	/***
	 * Adapter interface. The list adapter must implement this interface.
	 */
	public interface PinnedHeaderAdapter {

		/***
		 * Pinned header state: don't show the header.
		 */
		public static final int PINNED_HEADER_GONE = 0;

		/***
		 * Pinned header state: show the header at the top of the list.
		 */
		public static final int PINNED_HEADER_VISIBLE = 1;

		/***
		 * Pinned header state: show the header. If the header extends beyond the bottom of the first shown element, push it up and clip.
		 */
		public static final int PINNED_HEADER_PUSHED_UP = 2;

		/***
		 * Computes the desired state of the pinned header for the given position of the first visible list item. Allowed return values are
		 * {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE} or {@link #PINNED_HEADER_PUSHED_UP}.
		 */
		int getPinnedHeaderState(int position);

		/***
		 * Configures the pinned header view to match the first visible list item.
		 * 
		 * @param header pinned header view.
		 * @param position position of the first visible list item.
		 * @param alpha fading of the header view, between 0 and 255.
		 */
		void configurePinnedHeader(View header, int position, int alpha);

		/**
		 * 配置headview
		 */
		public View getHeadView();
	}

	private static final int MAX_ALPHA = 255;

	protected static final String TAG = PinnedHeaderListView.class.getName();

	private PinnedHeaderAdapter mAdapter;
	private View mHeaderView;
	private boolean mHeaderViewVisible;

	private int mHeaderViewWidth;

	private int mHeaderViewHeight;
	private OnScrollListener mOnScrollListener;

	public PinnedHeaderListView(Context context) {
		super(context);
	}

	public PinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PinnedHeaderListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	private void setPinnedHeaderView(View view) {
		mHeaderView = view;

		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}

		/**
		 * 注册默认的滚动监听
		 */
		if (mOnScrollListener == null) {
			setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				}
			});
		}
		layoutHeadView(0);

	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = null;
		mHeaderView = null;

		if (adapter instanceof PinnedHeaderAdapter) {
			mAdapter = (PinnedHeaderAdapter) adapter;
			setPinnedHeaderView(mAdapter.getHeadView());
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// Log.e(TAG, "onMeasure:" + widthMeasureSpec + "--" +
		// heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	private void layoutHeadView(int firstVisiblePosition) {

		if (mHeaderView != null && getAdapter().getCount() > 0) {

			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			configureHeaderView(firstVisiblePosition);
			// invalidate();

		}

	}

	public void configureHeaderView(int position) {
		if (mHeaderView == null) {
			return;
		}

		int state = mAdapter.getPinnedHeaderState(position);
		switch (state) {
		case PinnedHeaderAdapter.PINNED_HEADER_GONE: {
			mHeaderViewVisible = false;
			break;
		}

		case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE: {

			if (mHeaderView.getTop() != 0) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}
			mAdapter.configurePinnedHeader(mHeaderView, position, MAX_ALPHA);
			mHeaderViewVisible = true;
			break;
		}

		case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			int bottom = firstView.getBottom();
			int headerHeight = mHeaderView.getHeight();
			int y;
			int alpha;
			if (bottom + getDividerHeight() < headerHeight) {
				y = (bottom - headerHeight + getDividerHeight());
				alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				alpha = MAX_ALPHA;
			}
			mAdapter.configurePinnedHeader(mHeaderView, position, alpha);
			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
			}
			mHeaderViewVisible = true;
			break;
		}
		}

		// Log.e(TAG, "state:" + state);
		// mHeaderView.measure(mHeaderViewWidth, mHeaderViewHeight);

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		if (mHeaderView != null && mHeaderViewVisible) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}

	}

	/**
	 * 重载滚动监听
	 */
	@Override
	public void setOnScrollListener(final OnScrollListener listener) {

		mOnScrollListener = new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				listener.onScrollStateChanged(view, scrollState);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				layoutHeadView(firstVisibleItem); // 为实现滚动时重新布局
				listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

			}
		};
		super.setOnScrollListener(mOnScrollListener);

	}

	/**
	 * 直接停止listview 滚动
	 * 
	 * @author Administrator
	 */
	static class StopListFling {

		private static Field mFlingEndField = null;
		private static Method mFlingEndMethod = null;

		static {
			try {
				mFlingEndField = AbsListView.class.getDeclaredField("mFlingRunnable");
				mFlingEndField.setAccessible(true);
				mFlingEndMethod = mFlingEndField.getType().getDeclaredMethod("endFling");
				mFlingEndMethod.setAccessible(true);
			} catch (Exception e) {
				mFlingEndMethod = null;
			}
		}

		public static void stop(ListView list) {
			if (mFlingEndMethod != null) {
				try {
					mFlingEndMethod.invoke(mFlingEndField.get(list));
				} catch (Exception e) {
				}
			}
		}
	}
}