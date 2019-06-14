package cn.piesat.minemonitor.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.OffLineMapActivity;
import cn.piesat.minemonitor.R;

public class DownloadActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ly_download)
    LinearLayout ly_download;
    @BindView(R.id.ly_import)
    LinearLayout ly_import;
    private TextView topTitle;
    private Boolean download_switch = false;
    private Boolean import_switch = false;
    private ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topTitle = findViewById(R.id.tv_setting_title);
        ly_download.setOnClickListener(this);
        ly_import.setOnClickListener(this);
        topTitle.setText("当前页面:设置->地图下载");
        menu = findViewById(R.id.iv_setting_children_menu);
        menu.setOnClickListener(this);
        setOnClick();
    }


    private void setOnClick() {
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ly_download:
                startActivity(new Intent(this, OffLineMapActivity.class));
                break;

            case R.id.ly_import:
                break;
            case R.id.iv_setting_children_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_setting_children_menu), Gravity.RIGHT, 0, 0);
                break;
        }
    }
}
