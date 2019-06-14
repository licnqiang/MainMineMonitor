package cn.piesat.minemonitor.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.SettingActivity;

/**
 * Created by yjl on 2018/3/8.
 */

public class SettingTitleLayout extends LinearLayout implements View.OnClickListener {


    private final ImageView back;

    public SettingTitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_setting_title, this);
        back = findViewById(R.id.iv_setting_back);
        back.setOnClickListener(this);
        LinearLayout llBack = findViewById(R.id.ll_back);
        llBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_back:
                ((Activity) getContext()).finish();
                break;
            case R.id.ll_back:
                ((Activity) getContext()).finish();
                break;
        }
    }
}
