
package com.example.improvedbaseadapter.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 带*号文本 将 带*转换成 自定义的黄色
 * 
 * @author 138191
 */
public class TextWithStarView extends TextView {

    // 判断是否是必填字段。
    // 默认为真， 可代码控制。。
    private boolean isNeceField = true;

    public TextWithStarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        resetText();

    }

    public TextWithStarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        resetText();

    }

    public TextWithStarView(Context context) {
        super(context, null);

    }

    public void resetText()
    {

        if (!isInEditMode())
        {
            String text = getText().toString();
            int lastIndex = text.lastIndexOf("*");

            StringBuilder sb = new StringBuilder();
            if (lastIndex >= 0)
            {
                sb.append(text.substring(0, lastIndex));
            } else
            {
                sb.append(text.substring(0));
            }
            if (isNeceField)
            {
                sb.append("<font color=\"").append(Color.RED)
                        .append("\">*");
            }

            setText(Html.fromHtml(sb.toString()));
        }

    }

    public void setIsNeceField(boolean isNeceField)
    {
        this.isNeceField = isNeceField;
        resetText();
    }

}
