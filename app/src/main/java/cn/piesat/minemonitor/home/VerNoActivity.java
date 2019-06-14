package cn.piesat.minemonitor.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.adapter.TaskListAdapter;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.entity.TaskEntity;
import cn.piesat.minemonitor.entity.TaskListEntity;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;


public class VerNoActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private List<String> spData;
    private ArrayAdapter<String> arrAdapter;
    private TaskListAdapter adapter;
    @BindView(R.id.sp_show)
    Spinner spinner;
    @BindView(R.id.tv_task_id)
    TextView tvTaskId;
    @BindView(R.id.ls_home_task)
    ListView lsHomeTask;
    @BindView(R.id.ll_figure_spot)
    LinearLayout lfs;
    @BindView(R.id.ll_mission_locus)
    LinearLayout lml;
    @BindView(R.id.ll_header)
    LinearLayout llheader;
    @BindView(R.id.ll_task_details)
    LinearLayout dqrw;
    @BindView(R.id.tv_no_data)
    TextView nodate;

    @BindView(R.id.ll_dqrwxq_gary)
    LinearLayout dqrwGary;
    @BindView(R.id.ll_xztb_gray)
    LinearLayout xztbGray;
    @BindView(R.id.ll_rwgj_gray)
    LinearLayout rwgiGray;

    private List<TaskEntity> no;
    private List<TaskEntity> xuan = new ArrayList<>();
    private String chooseNumber;
    private List<TaskListEntity> chooseList = new ArrayList<>();
    private List<TaskListEntity> chooseALL = new ArrayList<>();
    private Intent intent;
    private CustomSQLTools s;
    private Bundle mBundle;
    private List<TaskEntity> rwxq;
    private TaskListEntity listEntity;
    private SharedPreferences.Editor edit;
    private SharedPreferences sper;
    private List<TaskEntity> listEntitiesIng = new ArrayList<>();
    private List<TaskEntity> listEntitiesNo = new ArrayList<>();
    private List<TaskEntity> listEntitiesEnd = new ArrayList<>();
    private int from;
    private Bundle bundle;
    private LoadingDialog ld;
    private String tiNum = "null";
    private String w;
    private TaskListEntity yzfbt;
    private List<TaskListEntity> yzfbt1;
    @BindView(R.id.iv_content_menu)
    ImageView menu;

    @OnClick({R.id.iv_back, R.id.ll_task_details, R.id.ll_figure_spot, R.id.ll_mission_locus, R.id.iv_content_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_task_details:
                if (rwxq.size() > 0) {
                    intent = new Intent(this, CurrentTaskActivity.class);
                    mBundle = new Bundle();
                    mBundle.putSerializable("list", (Serializable) rwxq);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                } else {
                    dqrw.setClickable(false);
                }

                break;
            case R.id.ll_figure_spot:
//                intent = new Intent(VerNoActivity.this, HomeActivity.class);
//                mBundle = new Bundle();
//                mBundle.putSerializable("yzfbt", (Serializable) yzfbt1);
//                intent.putExtras(mBundle);
//                intent.putExtra("key", "Mark");
//                startActivity(intent);
                intent = new Intent(VerNoActivity.this, AddMapActivity.class);
                intent.putExtra("rwbh", rwxq.get(0).getTaskNumber());
                intent.putExtra("rwzt", rwxq.get(0).getState());
                startActivityForResult(intent, 1);
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

    /**
     * 验证中数据初始化
     */
    private void queryIng() {
        String sql = Constant.IMPLEMENT;
        listEntitiesIng.clear();
        listEntitiesIng.addAll(s.getAllocated(sql, VerNoActivity.this));
    }

    /**
     * 未验证数据初始化
     */
    private void queryNo() {
        String sql = Constant.EXPORT;
        listEntitiesNo.clear();
        listEntitiesNo.addAll(s.getAllocated(sql, VerNoActivity.this));
    }

    /**
     * 以验证数据初始化
     */
    private void queryEnd() {
        String sql = Constant.VERIFIED;
        listEntitiesEnd.clear();
        listEntitiesEnd.addAll(s.getAllocated(sql, VerNoActivity.this));
    }

    //创建Handler对象
    private MyHandler handler = new MyHandler(this);

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        //使该内部类持有对外部类的弱引用
        private WeakReference<VerNoActivity> weakReference;

        //构造器中完成弱引用初始化
        MyHandler(VerNoActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VerNoActivity activity = weakReference.get();
            switch (msg.what) {
                case 1:
                    if (no.size() > 0) {
                        SpHelper.setStringValue("SHOWPOINT","show");
                        xuan.clear();
                        for (int i = 0; i < listEntitiesIng.size(); i++) {
                            if (listEntitiesIng.get(i).getTaskNumber().equals(sper.getString("TASK_ID", "null"))) {
                                xuan.add(listEntitiesIng.get(i));
                            }
                        }
                        for (int i = 0; i < listEntitiesIng.size(); i++) {
                            if (listEntitiesIng.get(i).getTaskNumber().equals(sper.getString("TASK_ID", "null"))) {

                            } else {
                                xuan.add(listEntitiesIng.get(i));
                            }
                        }
                        no = xuan;
                        if (activity.no.size() > 0) {
                            activity.add();
                        } else {
                            activity.lfs.setClickable(false);
                            activity.lml.setClickable(false);
                        }
                    } else {
//                        Toast.makeText(activity, "暂无数据", Toast.LENGTH_SHORT).show();
                        activity.lfs.setClickable(false);
                        activity.lml.setClickable(false);
                        ld.loadSuccess();
                    }

                    break;
                case 2:
                    if (no.size() > 0) {
                        SpHelper.setStringValue("SHOWPOINT","show");
                        xuan.clear();
                        for (int i = 0; i < listEntitiesNo.size(); i++) {
                            if (listEntitiesNo.get(i).getTaskNumber().equals(sper.getString("TASK_ID", "null"))) {
                                xuan.add(listEntitiesNo.get(i));
                            }
                        }
                        for (int i = 0; i < listEntitiesNo.size(); i++) {
                            if (listEntitiesNo.get(i).getTaskNumber().equals(sper.getString("TASK_ID", "null"))) {

                            } else {
                                xuan.add(listEntitiesNo.get(i));
                            }
                        }
                        no = xuan;
                        if (activity.no.size() > 0) {
                            activity.add();
                        } else {
                            activity.lfs.setClickable(false);
                            activity.lml.setClickable(false);
                        }
                    } else {
                        activity.lfs.setClickable(false);
                        activity.lml.setClickable(false);
                        ld.loadSuccess();
                    }
                    break;
                case 3:
                    if (no.size() > 0) {
                        SpHelper.setStringValue("SHOWPOINT","show");
                        xuan.clear();
                        for (int i = 0; i < listEntitiesEnd.size(); i++) {
                            if (listEntitiesEnd.get(i).getTaskNumber().equals(sper.getString("TASK_ID", "null"))) {
                                xuan.add(listEntitiesEnd.get(i));
                            }
                        }
                        for (int i = 0; i < listEntitiesEnd.size(); i++) {
                            if (listEntitiesEnd.get(i).getTaskNumber().equals(sper.getString("TASK_ID", "null"))) {

                            } else {
                                xuan.add(listEntitiesEnd.get(i));
                            }
                        }
                        no = xuan;
                        if (activity.no.size() > 0) {
                            activity.add();
                        } else {
                            activity.lfs.setClickable(false);
                            activity.lml.setClickable(false);
                        }
                    } else {
                        activity.lfs.setClickable(false);
                        activity.lml.setClickable(false);
                        ld.loadSuccess();
                    }
                    break;

            }
        }
    }


    @Override
    protected void onResume() {
//        ld = new LoadingDialog(this);
        Intent intent = getIntent();
        from = intent.getIntExtra("from", 0);
//        from = 2;
        if (from == 0) {
            edit = sper.edit();
            edit.putInt("FROM", sper.getInt("FROM", 0));
            edit.commit();
        } else {
            edit = sper.edit();
            edit.putInt("FROM", from);
            edit.commit();
        }
        switch (from) {
            case 1:
//                no = (List<TaskEntity>) getIntent().getSerializableExtra("ing");
//                if (no != null) {
//                    if (no.size() > 0) {
//                        add();
//                    } else {
//                        lfs.setClickable(false);
//                        lml.setClickable(false);
//                        ld.loadSuccess();
//                    }
//                } else {
//                tiNum = sper.getString("TASK_ID", "null");
//                ld.setLoadingText("加载中...")
//                        .setSuccessText("加载成功")//设置loading时显示的文字
//                        .show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        queryIng();
                        no = listEntitiesIng;
                        Message message = new Message();
                        bundle = new Bundle();
                        message.what = from;
                        handler.sendMessage(message);
                    }
                }).start();
//                }
                break;
            case 2:
//                no = (List<TaskEntity>) getIntent().getSerializableExtra("no");
//                if (no != null) {
//                    if (no.size() > 0) {
//                        add();
//                    } else {
//                        lfs.setClickable(false);
//                        lml.setClickable(false);
//                        ld.loadSuccess();
//                    }
//                } else {
//                tiNum = sper.getString("TASK_ID", "null");
//                ld.setLoadingText("加载中...")
//                        .setSuccessText("加载成功")//设置loading时显示的文字
//                        .show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        queryNo();
                        no = listEntitiesNo;
                        Message message = new Message();
                        message.what = from;
                        handler.sendMessage(message);
                    }
                }).start();
//                }
                break;
            case 3:
//                no = (List<TaskEntity>) getIntent().getSerializableExtra("end");
//                if (no != null) {
//                    if (no.size() > 0) {
//                        add();
//                    } else {
//                        lfs.setClickable(false);
//                        lml.setClickable(false);
//                        ld.loadSuccess();
//                    }
//                } else {
//                tiNum = sper.getString("TASK_ID", "null");
//                ld.setLoadingText("加载中...")
//                        .setSuccessText("加载成功")//设置loading时显示的文字
//                        .show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        queryEnd();
                        no = listEntitiesEnd;
                        Message message = new Message();
                        message.what = from;
                        handler.sendMessage(message);
                    }
                }).start();
//                }
                break;
        }
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_no);
        ButterKnife.bind(this);
        sper = getSharedPreferences("User", Context.MODE_PRIVATE);
        s = new CustomSQLTools();
        rwxq = new ArrayList<>();
        llheader.setVisibility(View.GONE);
        nodate.setVisibility(View.VISIBLE);
        ld = new LoadingDialog(this);
        dqrw.setVisibility(View.VISIBLE);
        menu.setVisibility(View.GONE);
//        ld.setLoadingText("加载中...")
//                .setSuccessText("加载成功")//设置loading时显示的文字
//                .show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        ld.loadSuccess();
        super.onNewIntent(intent);
    }

    @Override
    protected void onRestart() {
        if (rwxq.size() > 0) {
            if (!rwxq.get(0).getTaskNumber().equals("")) {
                adapter.clear();
                chooseALL.addAll(s.getAllPoints(chooseNumber, VerNoActivity.this));
                adapter = new TaskListAdapter(VerNoActivity.this, R.layout.item_task_list_content, chooseALL);
                lsHomeTask.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                dqrw.setVisibility(View.GONE);
            }
        }


        super.onRestart();
    }

    private void add() {
        String taskid = no.get(0).getTaskNumber();
        spinner.setOnItemSelectedListener(new OnItemSelectedListenerImpl());
        chooseALL.addAll(s.getAllPoints(taskid, this));
        adapter = new TaskListAdapter(VerNoActivity.this, R.layout.item_task_list_content, chooseALL);
        lsHomeTask.setAdapter(adapter);
        lsHomeTask.setOnItemClickListener(this);
         initSp();
        ld.loadSuccess();
        llheader.setVisibility(View.VISIBLE);
        nodate.setVisibility(View.GONE);
    }

    private void initSp() {
        spData = new ArrayList<>();
        spData.clear();
        for (int i = 0; i < no.size(); i++) {
            if (!tiNum.equals("null")) {
                if (i == 0 && tiNum != no.get(0).getTaskNumber()) {
                    w = no.get(0).getTaskNumber();
                    spData.add(tiNum);
                } else if (tiNum.equals(no.get(i).getTaskNumber())) {
                    spData.add(w);
                } else if (i != 0 && tiNum != no.get(i).getTaskNumber()) {
                    spData.add(no.get(i).getTaskNumber());
                }
            } else {
                spData.add(no.get(i).getTaskNumber());
            }

        }
        arrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spData);
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrAdapter);
        ((BaseAdapter) spinner.getAdapter()).notifyDataSetChanged();
        chooseNumber = spinner.getSelectedItem().toString();
        if (rwxq.size() > 0) {
            tvTaskId.setText(rwxq.get(0).getTaskName() + "图斑列表");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listEntity = chooseALL.get(position);
//        String taskNumber = listEntity.getTaskNumber();
//        String mapNumber = listEntity.getMapNumber();
        mBundle = new Bundle();
        intent = new Intent(this, ContentListActivity.class);
        mBundle.putSerializable("item", listEntity);
        intent.putExtra("from", 2);
        intent.putExtras(mBundle);
        edit = sper.edit();
        edit.clear();
        edit.putString("TASK_ID", listEntity.getTaskNumber());
        edit.commit();
        startActivity(intent);
    }

    //    下拉框选择事件
    private class OnItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Intent intent = getIntent();
            int from = intent.getExtras().getInt("from");
            if (from == 1) {
                SpHelper.setStringValue("COLOR", "ONE");
            } else if (from == 2) {
                SpHelper.setStringValue("COLOR", "TWO");
            } else if (from == 3) {
                SpHelper.setStringValue("COLOR", "THREE");
            }
//            if (!tiNum.equals("null")){
//                chooseNumber = tiNum;
//                chooseNumber = parent.getItemAtPosition(position).toString();
//            }else{

            chooseNumber = parent.getItemAtPosition(position).toString();
            edit = sper.edit();
            edit.putString("TASK_ID", chooseNumber);
            edit.putString("TASKID", chooseNumber);
            edit.commit();
//            }
//            rwxq = new ArrayList<>();
            rwxq.clear();
            for (int i = 0; i < no.size(); i++) {
                if (no.get(i).getTaskNumber().equals(chooseNumber)) {
                    rwxq.add(no.get(i));
                }
            }
            if (rwxq.size() > 0) {
                tvTaskId.setText(rwxq.get(0).getTaskName() + "图斑列表");
            }
//            Toast.makeText(VerNoActivity.this, "rwxq集合长度" + rwxq.size(), Toast.LENGTH_SHORT).show();
            listAllChange();
        }

        /**
         * 验证中，未验证，以验证集合。点击下拉列表改变list集合。
         */
        private void listAllChange() {
            List<TaskEntity> notask = new ArrayList<>();
            notask.addAll(no);
            chooseList.clear();
            yzfbt = new TaskListEntity();
            yzfbt1 = new ArrayList<>();
            for (int i = 0; i < no.size(); i++) {
                if (notask.get(i).getTaskNumber().equals(chooseNumber)) {
                    chooseList.addAll(s.getAllPoints(chooseNumber, VerNoActivity.this));

                }
            }
            for (int i = 0; i < chooseList.size(); i++) {
                yzfbt.setMapNumber(chooseList.get(i).getMapNumber());
                yzfbt.setState(chooseList.get(i).getState());
                yzfbt1.add(yzfbt);
            }
            chooseALL.clear();
            chooseALL.addAll(chooseList);
//            yzfbt1.size();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    }
}
