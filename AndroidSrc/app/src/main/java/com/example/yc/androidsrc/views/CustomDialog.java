package com.example.yc.androidsrc.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.yc.androidsrc.R;

/**
 * 自定义ProgressDialog
 *
 * @author RebornC
 * Created on 2018/12/12.
 */

public class CustomDialog extends ProgressDialog {

    public CustomDialog(Context context)
    {
        super(context);
    }

    public CustomDialog(Context context, int theme)
    {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = dipToPx(100);
        params.height = dipToPx(100);
        getWindow().setAttributes(params);
    }

    @Override
    public void show()
    {
        super.show();
    }

    /**
     * dip 转换成 px
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

}
