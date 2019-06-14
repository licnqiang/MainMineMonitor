package cn.piesat.minemonitor.home.contentlist;


import android.os.Bundle;

import com.baidu.mapapi.map.MapView;

import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.fragment.OutlineFragment;
import cn.piesat.minemonitor.util.SystemUtils;

public class ChangeMapActivity extends BaseActivity {

    private OutlineFragment locfrag;
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_map);
        mMapView = findViewById(R.id.map_change_map);
        mMapView.showZoomControls(false);
        locfrag = new OutlineFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_add, locfrag).commitAllowingStateLoss();
        SystemUtils in = new SystemUtils();
        in.initMap(mMapView,41.37,111.68,"flat_map");
    }
}
