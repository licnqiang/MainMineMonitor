package cn.piesat.minemonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.fragment.OffLineAllFrgt;
import cn.piesat.minemonitor.fragment.OffLineDownLoadedFrgt;
import cn.piesat.minemonitor.views.TopIndicatorView;

public class OffLineMapActivity extends BaseActivity implements OnClickListener {
    private List<Fragment> mFragments;
    private int flag;

    private TopIndicatorView m_topIndicator;
    private int m_index;
    private ImageView m_back;
    private TextView m_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }


    private void initView() {
        setContentView(R.layout.activity_off_line_map);
        m_topIndicator = (TopIndicatorView) findViewById(R.id.task_top);
        String[] lable = { "下载管理","已下载"};
        m_topIndicator.setLabels(lable);
        m_back = (ImageView) findViewById(R.id.img_back);
        m_back.setVisibility(View.VISIBLE);
        m_title = (TextView) findViewById(R.id.tv_title);
        m_title.setText("离线地图");
        setClickListener();

    }

    private void initTabHost() {
        m_topIndicator
                .setOnTopIndicatorListener(new TopIndicatorView.OnTopIndicatorListener() {

                    @Override
                    public void onIndicatorSelected(int index) {
                        // TODO Auto-generated method stub
                        m_topIndicator.setTabsDisplay(
                                OffLineMapActivity.this, index);
                        setCurrentFrag(index);
                    }
                });

    }

    protected void initData() {
        // TODO Auto-generated method stub
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new OffLineAllFrgt());
        mFragments.add(new OffLineDownLoadedFrgt());
        initTabHost();
        setCurrentFrag(0);
    }


    private void setClickListener() {
        // TODO Auto-generated method stub
        m_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

            default:
                break;
        }
    }

    public void setCurrentFrag(int currentFrag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, mFragments.get(currentFrag));
        transaction.commit();
    }


}
