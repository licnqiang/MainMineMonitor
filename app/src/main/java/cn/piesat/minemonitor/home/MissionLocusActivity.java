package cn.piesat.minemonitor.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.entity.CheckTrackEntiy;

/**
 * 任务轨迹
 */
public class MissionLocusActivity extends BaseActivity implements View.OnClickListener {
    //顶部title文字
    private TextView topTitle;
    private ImageView menu;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private List<String> list = new ArrayList<>();
    private CustomSQLTools s;
    private List<CheckTrackEntiy> checkTrackEntiyList = new ArrayList<>();
    private List<LatLng> points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_mission_locus);
        s = new CustomSQLTools();
        initView();
        onClickListener();

    }

    private void onClickListener() {
        menu.setOnClickListener(this);
    }

    private void initView() {
        topTitle = findViewById(R.id.tv_setting_title);
        topTitle.setText("任务轨迹");
        menu = findViewById(R.id.iv_setting_children_menu);
        mMapView = (MapView) findViewById(R.id.mmap);
        mBaiduMap = mMapView.getMap();
        //指南针图标默认的是打开的
        mBaiduMap.getUiSettings().setCompassEnabled(true);
        float rotate = -1.0f;
        MapStatus mapStatus = new MapStatus.Builder(mBaiduMap.getMapStatus()).rotate(rotate).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mapStatusUpdate);

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
//        trackTest();
        trackSetting();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_children_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_setting_children_menu), Gravity.RIGHT, 0, 0);
                break;
        }
    }

    public void trackTest() {
        List<String> textShow = new ArrayList<>();
        textShow.add("登陆 2018.7.30 9.00");
        textShow.add("拍照 2018.7.30 9.32.24");
        textShow.add("心跳 2018.7.30 10.00 ");
        textShow.add("心跳 2018.7.30 11.00");
        textShow.add("拍照 2018.7.30 11.30");
        textShow.add("心跳 2018.7.30 12.00");
        textShow.add("心跳 2018.7.30 13.00");
        textShow.add("退出 2018.7.30 13.11");
        points = new ArrayList<LatLng>();
        points.add(new LatLng(41.8451, 109.836));
        points.add(new LatLng(41.7557, 109.8864));
        points.add(new LatLng(41.7637, 109.868));
        points.add(new LatLng(41.7733, 109.9392));
        points.add(new LatLng(41.7784, 109.9132));
        points.add(new LatLng(41.7793, 109.9124));
        points.add(new LatLng(41.7757, 109.9095));
        points.add(new LatLng(41.7333, 109.8664));
        for (int i = 0; i < points.size(); i++) {
            OverlayOptions textOption = new TextOptions()
                    .bgColor(Color.WHITE)
                    .fontSize(24)
                    .fontColor(Color.BLACK)
                    .text(textShow.get(i))
                    .rotate(0)
                    .position(points.get(i));
            mBaiduMap.addOverlay(textOption);
        }
        if (points.size() > 0) {
            LatLng cenpt = new LatLng(points.get(0).latitude, points.get(0).longitude);
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(18)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            List<Integer> colors = new ArrayList<>();
//            colors.add(Integer.valueOf(Color.BLUE));
//            colors.add(Integer.valueOf(Color.RED));
            colors.add(Integer.valueOf(Color.YELLOW));
//            colors.add(Integer.valueOf(Color.GREEN));
//            colors.add(Integer.valueOf(Color.BLUE));
//            colors.add(Integer.valueOf(Color.RED));
//            colors.add(Integer.valueOf(Color.YELLOW));
//            colors.add(Integer.valueOf(Color.GREEN));
            if (points.size() > 2) {
                OverlayOptions ooPolyline = new PolylineOptions().width(10)
                        .colorsValues(colors).points(points);
                Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
            }
//
        }

    }

    /**
     * GPS To baidu  GPS转百度
     *
     * @param sourceLatLng
     * @return
     */
    public LatLng convertGPSToBaidu(LatLng sourceLatLng) {
// 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
// sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    public void trackSetting() {
        checkTrackEntiyList.addAll(s.getAllTrackInfo(MissionLocusActivity.this));
        points = new ArrayList<LatLng>();
        points.clear();
        for (int i = 0; i < checkTrackEntiyList.size(); i++) {
            points.add(convertGPSToBaidu(new LatLng(Double.parseDouble(checkTrackEntiyList.get(i).getPlaceX()), Double.parseDouble(checkTrackEntiyList.get(i).getPlaceY()))));
            //构建文字Option对象，用于在地图上添加文字
            OverlayOptions textOption = new TextOptions()
                    .bgColor(Color.WHITE)
                    .fontSize(24)
                    .fontColor(Color.BLACK)
                    .text(checkTrackEntiyList.get(i).getEventType() + checkTrackEntiyList.get(i).getEventTime())
                    .rotate(0)
                    .position(points.get(i));
            mBaiduMap.addOverlay(textOption);
        }
        if (points.size() > 0) {
            LatLng cenpt = new LatLng(points.get(0).latitude, points.get(0).longitude);
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(18)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            List<Integer> colors = new ArrayList<>();
            colors.add(Integer.valueOf(Color.BLUE));
            colors.add(Integer.valueOf(Color.RED));
            colors.add(Integer.valueOf(Color.YELLOW));
            colors.add(Integer.valueOf(Color.GREEN));
            if (points.size() > 2) {
                OverlayOptions ooPolyline = new PolylineOptions().width(10)
                        .colorsValues(colors).points(points);
                Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
            }
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mMapView.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
