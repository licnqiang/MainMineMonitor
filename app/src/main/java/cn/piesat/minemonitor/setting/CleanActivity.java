package cn.piesat.minemonitor.setting;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.R;


public class CleanActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    //顶部title文字
    @BindView(R.id.tv_setting_title)
    TextView topTitle;

    @OnClick(R.id.iv_setting_children_menu)
    public void setMenu(View view) {
        popupWindow.showAtLocation(findViewById(R.id.iv_setting_children_menu), Gravity.RIGHT, 0, 0);

    }

    /**
     * 清理全部
     */
    @BindView(R.id.tv_setting_clean_all_clean)
    TextView tvclean;
    /**
     * 清理照片
     */
    @BindView(R.id.tv_setting_clean_photo)
    TextView tvphoto;
    /**
     * 清理视频
     */
    @BindView(R.id.tv_setting_clean_video)
    TextView tvvideo;
    /**
     * 清理解译数据
     */
    @BindView(R.id.tv_setting_clean_parse_data)
    TextView tvparse;
    /**
     * 清理验证成果
     */
    @BindView(R.id.tv_setting_clean_validation)
    TextView tvvalidation;
    /**
     * 清理任务数据。
     */
    @BindView(R.id.iv_setting_clean_task_data)
    TextView tvdata;

    @OnClick({R.id.tv_setting_clean_all_clean, R.id.tv_setting_clean_photo, R.id.tv_setting_clean_video,
            R.id.tv_setting_clean_parse_data, R.id.tv_setting_clean_validation, R.id.iv_setting_clean_task_data})
    public void onClick(TextView view) {
        Toast.makeText(this, "清理完成", Toast.LENGTH_SHORT).show();
    }

    @OnCheckedChanged({R.id.tb_all_clean, R.id.tb_photo, R.id.tb_video, R.id.tb_parse_data, R.id.tb_validation, R.id.tb_task_data})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_all_clean:
                if (isChecked) {
                    tvclean.setVisibility(View.GONE);
                } else {
                    tvclean.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tb_photo:
                if (isChecked) {
                    tvphoto.setVisibility(View.GONE);

                } else {
                    tvphoto.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tb_video:
                if (isChecked) {
                    tvvideo.setVisibility(View.GONE);
                } else {
                    tvvideo.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.tb_parse_data:
                if (isChecked) {
                    tvparse.setVisibility(View.GONE);

                } else {
                    tvparse.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.tb_validation:
                if (isChecked) {
                    tvvalidation.setVisibility(View.GONE);
                } else {
                    tvvalidation.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tb_task_data:
                if (isChecked) {
                    tvdata.setVisibility(View.GONE);
                } else {
                    tvdata.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);
        ButterKnife.bind(this);
        topTitle.setText("目录清理");
    }
}
