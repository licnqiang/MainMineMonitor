package cn.piesat.minemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import cn.piesat.minemonitor.util.ActivityCollector;

/**
 * 任务列表页面
 */
public class TaskListActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private ListView list;
    private Button setting;
    private Button selfTask;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        initView();
        onClickListener();

    }

    private void onClickListener() {
        mBack.setOnClickListener(this);
        setting.setOnClickListener(this);
        selfTask.setOnClickListener(this);
    }

    private void initView() {
        ActivityCollector.addActivity(this);
        mBack = findViewById(R.id.iv_back);
        mBack.setVisibility(View.VISIBLE);
        list = findViewById(R.id.ls_list);
        setting = findViewById(R.id.btn_setting);
        selfTask = findViewById(R.id.btn_self_task);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_self_task:
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
