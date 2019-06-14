package cn.piesat.minemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import cn.piesat.minemonitor.mapdata.utils.Config;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;
import cn.piesat.minemonitor.setting.CleanActivity;
import cn.piesat.minemonitor.setting.DownloadActivity;
import cn.piesat.minemonitor.setting.ImportActivity;
import cn.piesat.minemonitor.setting.UserManagmentActivity;
import cn.piesat.minemonitor.util.ToastUtil;

/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView clean;
    private ImageView taskImport;
    private ImageView mapDownload;
    private Intent intent;
    //顶部title文字
    private TextView topTitle;
    private TextView text_name;
    private ImageView menu;
    private ImageView ivMy;
    private LinearLayout ll_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        onClickListener();
    }

    private void onClickListener() {
        clean.setOnClickListener(this);
        taskImport.setOnClickListener(this);
        mapDownload.setOnClickListener(this);
        menu.setOnClickListener(this);
        ivMy.setOnClickListener(this);
        ll_setting.setOnClickListener(this);

    }

    private void initView() {
        topTitle = findViewById(R.id.tv_setting_title);
        topTitle.setText("设置");
        clean = findViewById(R.id.iv_setting_directory_clean);
        taskImport = findViewById(R.id.iv_setting_task_import);
        mapDownload = findViewById(R.id.iv_setting_map_download);
        ll_setting = findViewById(R.id.ll_setting_directory_clean);
        text_name = findViewById(R.id.text_name);
        menu = findViewById(R.id.iv_setting_children_menu);
        ivMy = findViewById(R.id.iv_my);
        if(!TextUtils.isEmpty(SpHelper.getStringValue("USERNAME"))){
            text_name.setText(SpHelper.getStringValue("USERNAME"));}
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_setting_directory_clean:
                /*intent = new Intent(this, CleanActivity.class);
                startActivity(intent);*/
               /* String path = Config.getProjectPath() + "/default/config";
                deleteAllFiles(new File(path));*/
            break;
            case R.id.iv_setting_task_import:
                /*intent = new Intent(this, ImportActivity.class);
                startActivity(intent);*/
                break;
            case R.id.iv_setting_map_download:
                startActivity(new Intent(this,DownloadActivity.class));
//                startActivity(new Intent(this, OffLineMapActivity.class));
                break;
            case R.id.iv_setting_children_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_setting_children_menu), Gravity.RIGHT, 0, 0);
                break;
            case R.id.iv_my:
                startActivity(new Intent(this, UserManagmentActivity.class));
                break;
        }

    }

    void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        ToastUtil.show(SettingActivity.this,"清理完成");
      }
}
