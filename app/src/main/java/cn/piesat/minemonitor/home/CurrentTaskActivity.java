package cn.piesat.minemonitor.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.MapRelatedActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.entity.TaskEntity;

/**
 * 当前任务详情页面
 */
public class CurrentTaskActivity extends BaseActivity implements View.OnClickListener {
    //顶部title文字
    private TextView topTitle;
    private ImageView menu;
    private TextView loc;
    private TextView trajectory;
    private Intent intent;


    @BindView(R.id.tv_task_number)
    TextView tbbh;
    @BindView(R.id.tv_task_name)
    TextView rwmc;
    @BindView(R.id.tv_create_time)
    TextView cjsj;
    @BindView(R.id.tv_cover_area)
    TextView fgqy;
    @BindView(R.id.tv_task_content)
    TextView rwnr;
    @BindView(R.id.tv_task_note)
    TextView rwbz;
    @BindView(R.id.tv_task_state)
    TextView rwzt;
    @BindView(R.id.tv_rwzt)
    TextView rwztCode;
    private List<TaskEntity> taskEntities;
    private String stateCode;
    private CustomSQLTools s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_current_task);
        ButterKnife.bind(this);
        s = new CustomSQLTools();
        Bundle bundle = this.getIntent().getExtras();
        taskEntities = (List<TaskEntity>) bundle.getSerializable("list");
        if (taskEntities != null && taskEntities.size() > 0) {
            add();
        }
        initView();
        onClickListener();
    }

    private void add() {
        tbbh.setText(taskEntities.get(0).getTaskNumber());
        rwmc.setText(taskEntities.get(0).getTaskName());
        cjsj.setText(taskEntities.get(0).getTaskCreateDate());
        CustomSQLTools s = new CustomSQLTools();
        fgqy.setText(s.getXZQName(taskEntities.get(0).getTaskXZQ(), CurrentTaskActivity.this));
        rwnr.setText(taskEntities.get(0).getTaskContend());
        rwbz.setText(taskEntities.get(0).getTaskNote());
        stateCode = taskEntities.get(0).getState();
        if (stateCode.equals(Constant.EXPORT)) {
            rwzt.setText(Constant.UNVERIFIED);
            rwztCode.setTextColor(Color.parseColor("#EE0000"));
            rwzt.setTextColor(Color.parseColor("#EE0000"));
        } else if (stateCode.equals(Constant.IMPLEMENT)) {
            rwzt.setText(Constant.IN_THE_EXECUTION);
            rwztCode.setTextColor(Color.parseColor("#FFA500"));
            rwzt.setTextColor(Color.parseColor("#FFA500"));
        } else if (stateCode.equals(Constant.VERIFIED)) {
            rwztCode.setTextColor(Color.parseColor("#00cc66"));
            rwzt.setTextColor(Color.parseColor("#00cc66"));
            rwzt.setText(Constant.VERIFIED);
        }
    }

    private void onClickListener() {
        menu.setOnClickListener(this);
        loc.setOnClickListener(this);
        trajectory.setOnClickListener(this);
    }

    private void initView() {
        topTitle = findViewById(R.id.tv_setting_title);
        topTitle.setText("任务详情 ");
        menu = findViewById(R.id.iv_setting_children_menu);
        loc = findViewById(R.id.btn_loc);
        trajectory = findViewById(R.id.btn_trajectory);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_children_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_setting_children_menu), Gravity.RIGHT, 0, 0);
                break;
            case R.id.btn_loc:
                intent = new Intent(this, MapRelatedActivity.class);
                intent.putExtra("AddMapX", s.taskWork(taskEntities.get(0).getTaskNumber(), CurrentTaskActivity.this).getKsCenterCoordX());
                intent.putExtra("AddMapY", s.taskWork(taskEntities.get(0).getTaskNumber(), CurrentTaskActivity.this).getKsCenterCoordY());
                intent.putExtra("Code", 24);
                startActivity(intent);
                break;
            case R.id.btn_trajectory:
                intent = new Intent(this, MissionLocusActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
}

