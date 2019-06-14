package cn.piesat.minemonitor.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.FileListActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.adapter.MyFileAdapter;
import cn.piesat.minemonitor.entitys.FileEntity;

public class ImportActivity extends BaseActivity implements View.OnClickListener {

    private TextView topTitle;
    private Intent intent;
    private ImageView menu;
    @BindView(R.id.tv_import)
    TextView btn_import;
    @BindView(R.id.tv_file)
    TextView result;
    private ListView mListView;
    private Button btnComfirm;
    private MyFileAdapter mAdapter;
    private Context mContext;
    private File currentFile;
    String sdRootPath;

    private ArrayList<FileEntity> mList;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        ButterKnife.bind(this);
        initView();
        onClickListener();
    }

    private void onClickListener() {
        menu.setOnClickListener(this);
    }

    private void initView() {
        topTitle = findViewById(R.id.tv_setting_title);
        topTitle.setText("当前页面:设置->任务导入");
        menu = findViewById(R.id.iv_setting_children_menu);
        btn_import.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_children_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_setting_children_menu), Gravity.RIGHT, 0, 0);
                break;
            case R.id.tv_import:
                //打开文件夹
                startActivity(new Intent(this, FileListActivity.class));
                break;
        }
    }
}
