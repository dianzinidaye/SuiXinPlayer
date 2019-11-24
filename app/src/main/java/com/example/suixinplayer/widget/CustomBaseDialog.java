package com.example.suixinplayer.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.suixinplayer.R;
import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.widget.base.BaseDialog;

public class CustomBaseDialog extends BaseDialog<CustomBaseDialog> {
    private TextView tv_cancel;
    private TextView tv_exit;
    private Context context;

    public CustomBaseDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        showAnim(new Swing());

        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(context, R.layout.dialog_custom_base, null);
        tv_cancel = inflate.findViewById( R.id.tv_cancel);
        tv_exit = inflate.findViewById( R.id.tv_exit);
        tv_cancel.setText("CANCEL");
        tv_exit.setText("EXIT");
        inflate.setBackgroundColor(Color.parseColor("#ffffff"));

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

       // return false;
    }
}