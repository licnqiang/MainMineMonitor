package cn.piesat.minemonitor.util;


import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import cn.piesat.minemonitor.R;

/**
 * Created by yjl on 2018/3/16.
 */

public class SystemUtils {
    private BaiduMap mBaidumap;
    private BitmapDescriptor bitmap;

    public void initMap(MapView mMapView, Double lon, Double lan , String tag) {
        mBaidumap = mMapView.getMap();
        if(tag.equals("satellite_map")){
            mBaidumap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//开启卫星图
        }else if(tag.equals("flat_map")){
            mBaidumap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//平面地图
        }
        LatLng cenpt = new LatLng(lon, lan);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(15)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaidumap.setMapStatus(mMapStatusUpdate);
        if(tag.equals("")){
            //定义Maker坐标点
            LatLng cenp = new LatLng(lon, lan);
            //构建Marker图标
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_position);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(cenp)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mBaidumap.addOverlay(option);
        }
    }
}
