package cn.piesat.minemonitor.home.contentlist;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.adapter.PicVidAdapter;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.entity.CheckInfoEntiy;
import cn.piesat.minemonitor.media.DialogUtils;

public class ShowMediaActivity extends BaseActivity implements View.OnClickListener, PicVidAdapter.InnerItemOnclickListener {
    private String observationPoints;
    private String mapNumber;
    private List<CheckInfoEntiy> cie = new ArrayList<>();
    //顶部title文字
    private TextView topTitle;
    private ImageView menu;
    private CustomSQLTools s;
    private PicVidAdapter adapter;
    private ListView list;
    private String imgDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_media);
        s = new CustomSQLTools();
        //根据观察点编号，获取所有的该编号下视频（图片）
        Intent intent = getIntent();
        observationPoints = intent.getStringExtra("taskId");
        mapNumber = intent.getStringExtra("mapId");
        CustomSQLTools s = new CustomSQLTools();
        cie.addAll(s.thereIsA(ShowMediaActivity.this, observationPoints));
        initView();
        adapter = new PicVidAdapter(ShowMediaActivity.this, R.layout.item_pic_vid, cie);
        adapter.setOnInnerItemOnClickListener(this);
        list = findViewById(R.id.ls_show_pic_vid);
        onClickListener();
        list.setAdapter(adapter);
        if (!observationPoints.isEmpty() && !mapNumber.isEmpty()) {
            init();
        }
    }

    private void onClickListener() {
        menu.setOnClickListener(this);
    }

    private void initView() {
        topTitle = findViewById(R.id.tv_setting_title);
        topTitle.setText("当前页面:" + mapNumber + "图斑验证图片（视频）");
        menu = findViewById(R.id.iv_setting_children_menu);
    }

    private void init() {
//        List<Media> media1 = DataSupport.where("mapId =? and taskId =?", mapNumber, observationPoints).find(Media.class);
//        if (media1.size() == 0) {
//            Toast.makeText(this, "暂无图片", Toast.LENGTH_SHORT).show();
//        } else {
//            for (int i = 0; i < media1.size(); i++) {
//                CheckInfoEntiy picVid = new CheckInfoEntiy(media1.get(i).getFilePath(), media1.get(i).getTextPath());
//                cie.add(picVid);
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_children_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_setting_children_menu), Gravity.RIGHT, 0, 0);
                break;
        }
    }

    Dialog descDialog;

    @Override
    public void itemClick(View v) {
        final int position;

        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.bj_content:
                descDialog = DialogUtils.showCameraDesDialog(ShowMediaActivity.this, "确定", "取消", "", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText m_etDesc = descDialog.findViewById(R.id.et_camera_desc);
                        String descContent = m_etDesc.getText().toString();
                        imgDesc = (TextUtils.isEmpty(descContent) ? "暂无描述内容" : descContent);
                        s.updatePicContent(ShowMediaActivity.this, cie.get(position).getCheckItemId(), imgDesc);
                        cie.clear();
                        cie.addAll(s.thereIsA(ShowMediaActivity.this, observationPoints));
                        adapter.setOnInnerItemOnClickListener(ShowMediaActivity.this);
                        list.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (descDialog != null) {
                            Toast.makeText(ShowMediaActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            descDialog.dismiss();
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (descDialog != null) {
                            descDialog.dismiss();
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (descDialog != null) {
                            descDialog.dismiss();
                        }
                    }
                });
                break;
        }
    }
}
