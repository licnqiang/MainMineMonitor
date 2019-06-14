package cn.piesat.minemonitor.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.Toast;


import cn.piesat.minemonitor.HomeActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.SettingActivity;

/**
 * Created by yjl on 2018/3/2.
 * 自定义返回注册
 */

public class HomeBottomLayout extends LinearLayout implements View.OnClickListener {


    private final Button setting;
    private final Button selfTask;
    private boolean oknot = false;

    public HomeBottomLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_home_task_bottom, this);
        setting = findViewById(R.id.btn_setting);
        setting.setOnClickListener(this);
        selfTask = findViewById(R.id.btn_self_task);
        selfTask.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting:
                if (oknot == false) {
                    v.getContext().startActivity(new Intent(v.getContext(), SettingActivity.class));
                    oknot = true;
                } else {
                    Toast.makeText(getContext(), "" + getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                    if (getClass().getSimpleName().equals("SettingActivity")) {

                    } else {

                    }
                }


                break;
            case R.id.btn_self_task:
                v.getContext().startActivity(new Intent(v.getContext(), HomeActivity.class));
                break;
        }
    }
}
