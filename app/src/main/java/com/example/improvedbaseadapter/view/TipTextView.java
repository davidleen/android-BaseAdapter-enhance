package com.example.improvedbaseadapter.view;

 

import com.example.improvedbaseadapter.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 标题文本 带横向间隔
 */
public class TipTextView extends LinearLayout {

    String text;

    int textPosition = -1;

    int color = Color.GRAY;

    // 可以设置成属性加入
    private int lineBackground =  R.drawable.cutline_across;

    public TipTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /* 这里取得declare-styleable集合 */
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.TipTextView);
        /* 这里从集合里取出相对应的属性值,第二参数是如果使用者没用配置该属性时所用的默认值 */
        text = typeArray.getString(R.styleable.TipTextView_text);

        textPosition = typeArray.getInt(R.styleable.TipTextView_textPosition, textPosition);
        // float textSize = typeArray.getDimension(R.styleable.MyView_textSize,
        // 36);
        // mString = typeArray.getString(R.styleable.MyView_text);
        // /* 设置自己的类成员变量 */
        // mPaint.setTextSize(textSize);
        // mPaint.setColor(textColor);
        /* 关闭资源 */
        typeArray.recycle();
        LinearLayout.LayoutParams params;
        int lineHeight = (int)(getResources().getDisplayMetrics().density * 3);

        if (textPosition >= 0)
        {
            params = new LayoutParams(0,
                    lineHeight);

            View view = new View(context);
            if(lineBackground>-1)
            view.setBackgroundResource(lineBackground);
            params.weight = 1;

            this.addView(view, params);
        }
        TextView tv = new TextView(context);
        tv.setText(text);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        this.addView(tv, params);

        if (textPosition <= 0)
        {
            params = new LayoutParams(0,
                    lineHeight);

            View view = new View(context);
            if(lineBackground>-1)
            view.setBackgroundResource(lineBackground);
            params.weight = 1;

            this.addView(view, params);
        }
        this.setGravity(Gravity.CENTER);

    }

    public TipTextView(Context context) {
        this(context, null);

    }

}
