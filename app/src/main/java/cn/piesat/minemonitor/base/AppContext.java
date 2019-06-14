package cn.piesat.minemonitor.base;


import android.app.Application;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import java.util.Observer;

import cn.piesat.minemonitor.mapdata.utils.SpHelper;
import cn.piesat.minemonitor.mapdata.utils.SysConstant;
import cn.piesat.minemonitor.util.LocationWatcher;

public class AppContext extends Application {

    private static AppContext instance;
    private String m_currentLayerSetName;
    private BDLocation m_pntRef;

    public static AppContext getInstance() {
        return instance;
    }

    private LocationClient mLocationClient = null;
    private BDLocation m_location;
    private LocationWatcher m_locationWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        instance = this;
        m_currentLayerSetName = SysConstant.MapConfig.vectorLayerset;
        SpHelper.init(this);
        m_locationWatcher = new LocationWatcher();
        m_pntRef = new BDLocation();
        initLocation();
        startLocation();

    }

    public void addLocationObserver(Observer o) {
        if (o != null) {
            m_locationWatcher.addObserver(o);
        }
    }

    public void removeLocationObserver(Observer o) {
        if (o != null) {
            m_locationWatcher.deleteObserver(o);
        }
    }

    public String getCurrentLayerSetName() {
        return m_currentLayerSetName;
    }

    public void setCurrentLayerSetName(String currentLayerSetName) {
        if (TextUtils.isEmpty(currentLayerSetName)) {
            return;
        }
        m_currentLayerSetName = currentLayerSetName;
    }

    public void startLocation() {
        if (mLocationClient == null) {
            initLocation();
        }
        //mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }

    public void stopLocation() {
        if (mLocationClient != null) {
            //mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
        }
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");//1. gcj02：国测局坐标；2. bd09：百度墨卡托坐标；3. bd09ll：百度经纬度坐标；
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 2000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    /*public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            m_location = location;
            if (location.getLongitude() <= Double.MIN_VALUE || location.getLatitude() <= Double.MIN_VALUE) {
                return;
            }
            if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位
                // 离线定位误差大不使用该位置
                return;
            }
//            LogUtils.i("wbb", "lon:" + location.getLongitude() + "--lat:" + location.getLatitude());
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            if (m_pntRef != null && (Math.abs(m_pntRef.getLongitude() - lng) < 0.000001) && (Math.abs(m_pntRef.getLatitude() - lat)) < 0.000001) {
                // 如果和前一个点相同,则直接过滤掉.
                return;
            }
            m_pntRef = m_location;
            Gps wgs84 = CoordSysConverter.bd09_To_Gps84(m_location.getLongitude(), m_location.getLatitude());
//            Gps wgs84 = CoordSysConverter.gcj02_To_Gps84(m_location.getLongitude(), m_location.getLatitude());
            m_location.setLongitude(wgs84.getWgLon());
            m_location.setLatitude(wgs84.getWgLat());
            m_locationWatcher.setLocation(m_location);
        }*/
    }

