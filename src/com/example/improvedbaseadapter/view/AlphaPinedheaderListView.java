package com.example.improvedbaseadapter.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;

/**
 * 带字母索引的listView
 * 
 * @author davidleen29
 */
public class AlphaPinedheaderListView extends PinnedHeaderListView {

	private SectionIndexer sectionIndexer;
	private int currentSection = -1;
	private Rect drawRect = null;
	private Rect touchRect = null;
	private static final int MINSECTIONCOUNT = 5;

	public AlphaPinedheaderListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		ini(context);

	}

	public AlphaPinedheaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ini(context);

	}

	public AlphaPinedheaderListView(Context context) {
		super(context);
		ini(context);

	}

	private void ini(Context context) {

	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (this.getAdapter() != null) {
			getAdapter().unregisterDataSetObserver(observer);
		}
		this.setFastScrollEnabled(true);

		if (adapter instanceof SectionIndexer) {
			sectionIndexer = (SectionIndexer) adapter;
			adapter.registerDataSetObserver(observer);
			this.setFastScrollEnabled(false);

		}

		super.setAdapter(adapter);

	}

	private DataSetObserver observer = new DataSetObserver() {

		@Override
		public void onChanged() {

			super.onChanged();
			Log.d(TAG, "dataChanged");

			// form new groupIndex;
			// List<Integer> indexList = new ArrayList<Integer>();
			// indexList.add(0);
			// for (int i = 1, count = group.length; i < count; i++) {
			// if (group[i] != group[i - 1])
			// indexList.add(i);
			// }
			// int indexCount = indexList.size();
			// groupIndex = new int[indexCount];
			// for (int i = 0; i < indexCount; i++) {
			// groupIndex[i] = indexList.get(i);
			// }
			// indexList.clear();
			invalidate();

		}

		@Override
		public void onInvalidated() {

			super.onInvalidated();

		}

	};

	private Paint textPaint;

	@Override
	public void draw(Canvas canvas) {

		super.draw(canvas);
		if (sectionIndexer == null)
			return;
		Object[] section = sectionIndexer.getSections();
		if (section != null && section.length > MINSECTIONCOUNT) {

			if (textPaint == null)
				textPaint = new TextPaint();
			textPaint.setStyle(Style.FILL_AND_STROKE);

			textPaint.setColor(Color.GRAY);
			textPaint.setAlpha(60);
			canvas.drawRect(drawRect, textPaint);
			canvas.save();
			int width = drawRect.width();

			int size = section.length;
			int part = drawRect.height() / size;
			textPaint.setStyle(Style.STROKE);
			canvas.translate(drawRect.left + width / 3, part * 3 / 4);
			for (int i = 0; i < size; i++) {
				if (i == currentSection) {

					canvas.translate(-width / 8, 0);
					textPaint.setTextSize(width * 3 / 4);
					textPaint.setColor(Color.GREEN);
				} else {
					textPaint.setTextSize(width / 2);
					textPaint.setColor(Color.BLACK);
				}
				canvas.drawText(String.valueOf(section[i]), 0, 0, textPaint);
				canvas.translate(i == currentSection ? width / 8 : 0, part);
			}

		}
		canvas.restore();
	}

	/**
	 * 重载滚动监听
	 */
	@Override
	public void setOnScrollListener(final OnScrollListener listener) {

		OnScrollListener mOnScrollListener = new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				listener.onScrollStateChanged(view, scrollState);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (sectionIndexer != null)
					currentSection = sectionIndexer
							.getSectionForPosition(firstVisibleItem);
				listener.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);

			}
		};
		super.setOnScrollListener(mOnScrollListener);

	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		drawRect = new Rect(w * 9 / 10, 0, w, h);

		touchRect = new Rect(drawRect.left - 10, drawRect.top,
				drawRect.right + 10, drawRect.bottom);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Object[] section = sectionIndexer == null ? null : sectionIndexer
				.getSections();
		if (section != null && section.length > MINSECTIONCOUNT
				&& touchRect.contains((int) event.getX(), (int) event.getY())) {

			StopListFling.stop(this);
			int y = (int) event.getY();

			// find current in which section
			int part = touchRect.height() / section.length;

			int sectionIndex = y / part;
			Log.d(TAG, "sectionIndex:" + sectionIndex);
			if (sectionIndex >= section.length)
				sectionIndex = section.length - 1;

			int position = sectionIndexer.getPositionForSection(sectionIndex);

			// SL.d(TAG, "sectionIndex:" + sectionIndex + ",position:" +
			// position);

			if (position != -1 && getFirstVisiblePosition() != position) {
				setSelection(position);
			}

			return true;
		}

		return super.onTouchEvent(event);
	}
}
