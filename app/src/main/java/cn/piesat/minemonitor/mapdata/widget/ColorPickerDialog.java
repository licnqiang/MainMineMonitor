package cn.piesat.minemonitor.mapdata.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.rxbus.RxBus;
import cn.piesat.minemonitor.rxbus.event.ColorEvent;


/**
 * Created by Administrator on 2017/5/18.
 */

public class ColorPickerDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnColorPickerClickListener mListener;
    private int mColor;

    public ColorPickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public void setOnButtonClickListener(OnColorPickerClickListener listener) {
        mListener = listener;
    }

    public interface OnColorPickerClickListener {
        void cancel();

        void confirm(int color);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(mContext, R.layout.color_picker_dialog, null);
        setContentView(contentView);
        TextView tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        setCanceledOnTouchOutside(false);

        ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
//        SVBar svBar = (SVBar) findViewById(R.id.svbar);
//        OpacityBar opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
//        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);

//        picker.addSVBar(svBar);
//        picker.addOpacityBar(opacityBar);
//        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
        picker.setOldCenterColor(picker.getColor());
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                mColor = color;
            }
        });
        picker.setShowOldCenterColor(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {
            this.dismiss();
            if (mListener == null) return;
            mListener.cancel();
        } else if (v.getId() == R.id.tv_confirm) {
             this.dismiss();
            RxBus.getDefault().post(new ColorEvent(""));
             if (mListener == null) return;
            mListener.confirm(mColor);
        }
    }
}
