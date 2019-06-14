package cn.piesat.minemonitor.home;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.R;
/**
 * 验证图斑分布图
 * */
public class FigureSpotActivity extends BaseActivity implements View.OnClickListener {
    //顶部title文字
    private TextView topTitle;
    private ImageView menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_figure_spot);
        initView();
        onClickListener();
    }
    private void onClickListener() {
        menu.setOnClickListener(this);
    }

    private void initView() {
        topTitle = findViewById(R.id.tv_setting_title);
        topTitle.setText("图斑分布图");
        menu = findViewById(R.id.iv_setting_children_menu);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_children_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_setting_children_menu), Gravity.RIGHT, 0, 0);
                break;
        }
    }
}
