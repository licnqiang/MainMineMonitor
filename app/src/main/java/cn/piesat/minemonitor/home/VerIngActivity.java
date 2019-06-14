package cn.piesat.minemonitor.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.adapter.TaskListAdapter;
import cn.piesat.minemonitor.entity.TaskListEntity;


public class VerIngActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private List<String> spData;
    private ArrayAdapter<String> arrAdapter;
    private TaskListAdapter adapter;
    @BindView(R.id.sp_show)
    Spinner spinner;
    @BindView(R.id.tv_task_id)
    TextView tvTaskId;
    @BindView(R.id.ls_home_task)
    ListView lsHomeTask;
    private List<TaskListEntity> ing;
    private String chooseNumber;
    private List<TaskListEntity> chooseList = new ArrayList<>();
    private List<TaskListEntity> chooseALL = new ArrayList<>();
    private Intent intent;
    @OnClick({R.id.iv_back, R.id.ll_task_details, R.id.ll_figure_spot, R.id.ll_mission_locus, R.id.iv_content_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_task_details:
                intent = new Intent(this, CurrentTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_figure_spot:
                intent = new Intent(this, FigureSpotActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_mission_locus:
                intent = new Intent(this, MissionLocusActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_content_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_content_menu), Gravity.RIGHT, 0, 0);
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ing);
        ButterKnife.bind(this);
        ing = (List<TaskListEntity>) getIntent().getSerializableExtra(
                "ing");
        chooseALL.addAll(ing);
        adapter = new TaskListAdapter(VerIngActivity.this, R.layout.item_task_list_content, chooseALL);
        lsHomeTask.setAdapter(adapter);
        lsHomeTask.setOnItemClickListener(this);
        initSp();
        spinner.setOnItemSelectedListener(new OnItemSelectedListenerImpl());
    }
    private void initSp() {
        spData = new ArrayList<>();
        for (int i = 0; i < chooseALL.size(); i++) {
            spData.add(chooseALL.get(i).getTaskNumber());
        }
        arrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spData);
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrAdapter);
        ((BaseAdapter) spinner.getAdapter()).notifyDataSetChanged();
        chooseNumber = spinner.getSelectedItem().toString();
        tvTaskId.setText("请选择" + chooseNumber + "任务图斑");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TaskListEntity listEntity = chooseALL.get(position);
        String taskNumber = listEntity.getTaskNumber();
        String mapNumber = listEntity.getMapNumber();
        intent = new Intent(this, ContentListActivity.class);
        intent.putExtra("taskNumber", taskNumber);
        intent.putExtra("mapNumber", mapNumber);
        startActivity(intent);
    }
    //    下拉框选择事件
    private class OnItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            chooseNumber = parent.getItemAtPosition(position).toString();
            tvTaskId.setText("请选择" + chooseNumber + "任务图斑");
            listAllChange();
        }

        /**
         * 验证中，未验证，以验证集合。点击下拉列表改变list集合。
         */
        private void listAllChange() {
            if (chooseALL.size() == 0 || chooseALL.size() == ing.size() / 2) {
                chooseALL.clear();
                chooseALL.addAll(ing);
            }
            chooseList.clear();
            for (int i = 0; i < chooseALL.size(); i++) {
                if (chooseALL.get(i).getTaskNumber().equals(chooseNumber)) {
                    chooseList.add(chooseALL.get(i));
                }
            }
            chooseALL.clear();
            chooseALL.addAll(chooseList);
            adapter.notifyDataSetChanged();


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }


    }
}
