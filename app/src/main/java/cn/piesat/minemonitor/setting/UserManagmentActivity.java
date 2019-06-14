package cn.piesat.minemonitor.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.MainActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.home.Constant;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;
import cn.piesat.minemonitor.util.ActivityCollector;

public class UserManagmentActivity extends BaseActivity implements View.OnClickListener {
    //顶部title文字
    private TextView topTitle;
    private ImageView menu;
    private TextView tvClose;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_pwd)
    TextView tv_pwd;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private CustomSQLTools s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_managment);
        s = new CustomSQLTools();
        ButterKnife.bind(this);
        initView();
        onClickListener();
    }

    private void onClickListener() {
        menu.setOnClickListener(this);
        tvClose.setOnClickListener(this);
    }

    private void initView() {
        topTitle = findViewById(R.id.tv_setting_title);
        topTitle.setText("用户信息");
        menu = findViewById(R.id.iv_setting_children_menu);
        tvClose = findViewById(R.id.tv_close);
        tv_user_name.setText(SpHelper.getStringValue("USERNAME"));
        tv_pwd.setText(SpHelper.getStringValue("PASSWORD"));
        sp = getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_children_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_setting_children_menu), Gravity.RIGHT, 0, 0);
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.tv_close:
                ActivityCollector.finishAll();
                List<String> trackXY = new ArrayList<>();
                trackXY.addAll(xY());
                s.AddTrack(UserManagmentActivity.this,s.getUUID(),"",devinceNo,trackXY.get(0),trackXY.get(1),
                        Constant.TRACK_EVENT_QUIT,s.getCurrTime(),SpHelper.getStringValue("USERNAME"),"",Constant.GPS);
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }
}
