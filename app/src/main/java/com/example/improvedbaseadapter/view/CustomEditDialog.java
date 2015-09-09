
package com.example.improvedbaseadapter.view;

import com.example.improvedbaseadapter.utils.StringUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 封装文本输入对话框
 * 
 * @author davidleen29
 */
public abstract class CustomEditDialog {

    private AlertDialog.Builder builder;

    protected Context mContext;

    private String title;

    private String initText = "";

    private int inputType = -1;

    private String positive = "确认";

    private String negative = "取消";

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public CustomEditDialog(Context context) {

        this(context, null);

    }

    public CustomEditDialog(Context context, String title) {

        this(context, title, null);
    }

    public CustomEditDialog(Context context, String title, String initText) {
        this(context, title, initText, -1);

    }

    /**
     * @param context
     * @param title
     * @param initText
     * @param inputType itc InputType.TYPE_TEXT_VARIATION_PASSWORD
     */
    public CustomEditDialog(Context context, String title, String initText, int inputType) {

        mContext = context;

        this.title = title;
        this.initText = initText;
        this.inputType = inputType;
    }

    public AlertDialog create() {
        builder = new AlertDialog.Builder(mContext);
        final EditText editText = new EditText(mContext);
        if (null != initText)
            editText.setText(initText);
        if (inputType > -1)
            editText.setInputType(inputType);
        builder.setView(editText);
        if (title != null)
            builder.setTitle(title);

        builder.setPositiveButton(positive,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String text = editText.getText().toString().trim();

                        if (StringUtil.isEmptyString(text))
                        {
                            Toast.makeText(mContext, "请输入文本", Toast.LENGTH_LONG).show();
                            return;
                        }

                        doPositive(dialog, text);
                    }
                });

        builder.setNegativeButton(negative,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        doNegative(dialog);
                    }
                });
        return builder.create();
    }

    /**
     * 点击确定处理
     */
    protected abstract void doPositive(DialogInterface dialog, String newText);

    /**
     * 点击取消处理
     */
    protected void doNegative(DialogInterface dialog)
    {
        dialog.dismiss();

    }

}
