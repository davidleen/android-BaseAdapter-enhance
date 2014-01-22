
package com.example.improvedbaseadapter.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

 

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * 列表 不提供滚动功能 完全展示的listview 仅适用于 列表项目不多，需要嵌套展示的列表（即外围是scrollView 类似的滚动控件。）
 * 
 * @author davidleen29
 */
public class LinearView extends LinearLayout {

    private List<View> viewPool = new ArrayList<View>();

    private BaseAdapter mAdapter;

    private OnItemClickListener mOnItemClickListener;

//    @SuppressLint("NewApi")
//    public LinearView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//
//    }

    public LinearView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public LinearView(Context context) {
        super(context);

    }

    private DataSetObserver observer = new DataSetObserver()
    {

        @Override
        public void onChanged() {

            super.onChanged();
            initView();
        }

        @Override
        public void onInvalidated() {

            super.onInvalidated();
            initView();
        }

    };

    private void initView()
    {
        if (mAdapter != null)
        {
            int childCount = getChildCount();

            int count = mAdapter.getCount();
            // Log.d("LinearView" + "initView" + childCount + ",count:" +
            // count);
            if (childCount > count)
            {
                for (int index = count; index < childCount; index++)
                {
                    View tempView = getChildAt(index);
                    viewPool.add(tempView);

                }
                removeViews(count, childCount - count);
            }

            childCount = getChildCount();
            for (int i = 0; i < count; i++) {
                View convertView;
                if (i < childCount)
                {
                    convertView = getChildAt(i);
                } else
                    convertView = viewPool.size() > 0 ? viewPool.remove(0) : null;

                View newView = mAdapter.getView(i, convertView, this);
                newView.setOnClickListener(listener);
                if (newView.getParent() == null)
                {
                    addView(newView);
                }

            }

        }

    }

    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (mAdapter != null && mOnItemClickListener != null)
            {
                int index = LinearView.this.indexOfChild(v);
                mOnItemClickListener.onItemClick(LinearView.this, v, index,
                        mAdapter.getItemId(index));

            }
        }
    };

    /**
     * 设置适配器
     * 
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter)
    {
        if (mAdapter != null)
        {
            mAdapter.unregisterDataSetObserver(observer);
        }
        mAdapter = adapter;
        viewPool.clear();

        if (mAdapter != null)
        {
            mAdapter.registerDataSetObserver(observer);
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 返回指定位置上的数据
     * 
     * @param position
     * @return
     */
    public Object getItemAtPosition(int position)
    {
        if (position < 0)
            return null;
        if (mAdapter == null)
            return null;
        if (mAdapter.getCount() <= position)
            return null;
        return mAdapter.getItem(position);
    }

    /**
     * 设置行点击事件。
     * 
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener
    {

        public void onItemClick(LinearView parent, View v, int position, long id);
    }
}
