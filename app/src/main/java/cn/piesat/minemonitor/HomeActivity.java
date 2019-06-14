package cn.piesat.minemonitor;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.huace.gnssserver.IGnssListener;
import com.huace.gnssserver.IGnssServiceBinder;
import com.huace.gnssserver.IReceiver;
import com.huace.gnssserver.IReceiverListener;
import com.huace.gnssserver.gnss.data.GnssInfo;
import com.huace.gnssserver.gnss.data.SatelliteInfo;
import com.huace.gnssserver.gnss.data.receiver.BaseParams;
import com.huace.gnssserver.gnss.data.receiver.BasePositionInfoArray;
import com.huace.gnssserver.gnss.data.receiver.Baudrate;
import com.huace.gnssserver.gnss.data.receiver.CorsInfo;
import com.huace.gnssserver.gnss.data.receiver.Course;
import com.huace.gnssserver.gnss.data.receiver.CsdInfo;
import com.huace.gnssserver.gnss.data.receiver.DataFrequency;
import com.huace.gnssserver.gnss.data.receiver.DataFrequencyArray;
import com.huace.gnssserver.gnss.data.receiver.DataSourceList;
import com.huace.gnssserver.gnss.data.receiver.DopsInfo;
import com.huace.gnssserver.gnss.data.receiver.EbubbleInfo;
import com.huace.gnssserver.gnss.data.receiver.ExpireDate;
import com.huace.gnssserver.gnss.data.receiver.FileRecordInfo;
import com.huace.gnssserver.gnss.data.receiver.FileRecordStatus;
import com.huace.gnssserver.gnss.data.receiver.GeoidModelInfo;
import com.huace.gnssserver.gnss.data.receiver.GnssDataConfigList;
import com.huace.gnssserver.gnss.data.receiver.GprsInfo;
import com.huace.gnssserver.gnss.data.receiver.MagnetometerInfo;
import com.huace.gnssserver.gnss.data.receiver.ModemBandMode;
import com.huace.gnssserver.gnss.data.receiver.ModemCommunicationMode;
import com.huace.gnssserver.gnss.data.receiver.ModemDialParams;
import com.huace.gnssserver.gnss.data.receiver.ModemDialStatus;
import com.huace.gnssserver.gnss.data.receiver.ModemSignal;
import com.huace.gnssserver.gnss.data.receiver.NetworkStatus;
import com.huace.gnssserver.gnss.data.receiver.NmeaData;
import com.huace.gnssserver.gnss.data.receiver.Position;
import com.huace.gnssserver.gnss.data.receiver.PositionInfo;
import com.huace.gnssserver.gnss.data.receiver.PowerStatus;
import com.huace.gnssserver.gnss.data.receiver.ProjectionInfo;
import com.huace.gnssserver.gnss.data.receiver.RadioChannelArray;
import com.huace.gnssserver.gnss.data.receiver.RadioInfo;
import com.huace.gnssserver.gnss.data.receiver.ReceiverInfo;
import com.huace.gnssserver.gnss.data.receiver.ReceiverWifiInfo;
import com.huace.gnssserver.gnss.data.receiver.SatelliteNumber;
import com.huace.gnssserver.gnss.data.receiver.TransformInfo;
import com.huace.gnssserver.gnss.data.receiver.WorkWay;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.entity.TaskEntity;
import cn.piesat.minemonitor.fragment.MostFragment;
import cn.piesat.minemonitor.home.Constant;
import cn.piesat.minemonitor.home.VerNoActivity;
import cn.piesat.minemonitor.home.service.LongRunningService;
import cn.piesat.minemonitor.mapdata.presenter.MainViewPresenter;
import cn.piesat.minemonitor.mapdata.presenter.contract.MainViewContract;
import cn.piesat.minemonitor.mapdata.utils.Config;
import cn.piesat.minemonitor.mapdata.utils.MapUtil;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;
import cn.piesat.minemonitor.mapdata.utils.SysConstant;
import cn.piesat.minemonitor.rxbus.RxBus;
import cn.piesat.minemonitor.rxbus.event.ColorEvent;
import cn.piesat.minemonitor.rxbus.event.LayerSetEvent;
import cn.piesat.minemonitor.rxbus.event.RefreProject;
import cn.piesat.minemonitor.rxbus.event.ThemeLabelEntityEvent;
import cn.piesat.minemonitor.rxbus.event.ThemeLayerEvent;
import cn.piesat.minemonitor.rxbus.event.ThemepostlayerEvent;
import cn.piesat.minemonitor.util.CompressOperate_zip4j;
import cn.piesat.minemonitor.util.FileUtil;
import cn.piesat.minemonitor.util.LoadingDialogTools;
import cn.piesat.minemonitor.util.LocationUtil2;
import cn.piesat.minemonitor.util.RarDecompressionUtil;
import cn.piesat.minemonitor.util.ToastUtil;
import pie.core.DataSource;
import pie.core.Dataset;
import pie.core.DatasetVector;
import pie.core.Geometry;
import pie.core.Layer;
import pie.core.LayerSet;
import pie.core.MapEditView;
import pie.core.MapScaleChangedListener;
import pie.core.MapView;
import pie.core.Point2D;
import pie.core.QueryDef;
import pie.core.Recordset;
import pie.core.Rect2D;
import pie.core.RenderType;
import pie.core.Style;
import pie.core.TextStyle;
import pie.core.ThemeLabel;
import pie.core.Workspace;
import pie.map.MapLayoutParams;
import pie.map.MapViews;
import pie.map.enums.TextAlignmentType;
import pie.map.enums.TextAlongLineType;
import pie.map.enums.TextDirectionType;
import pie.map.gesture.MapGestureController;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static cn.piesat.minemonitor.mapdata.utils.MapUtil.getInstace;


/**
 * 主页面
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, MainViewContract.View {
    private CompressOperate_zip4j zip4j;
    public String encryptDir = "VerifyData";
    private List<File> list;
    private static final String TAG = "smzq";
    private ImageView ivMenu;
    private List<TaskEntity> listEntitiesIng = new ArrayList<>();
    private List<TaskEntity> listEntitiesNo = new ArrayList<>();
    private List<TaskEntity> listEntitiesEnd = new ArrayList<>();
    private RadioButton llVerEnd;
    private RadioButton llVerNo;
    private RadioButton llVerIng;
    private Intent intent;
    private MostFragment mostFrag;
    private double longitude;
    private double latitude;
    private LocationClient mLocationClient;
    private MyLocationListener myListener = new MyLocationListener();
    private MapViews mMapViews;
    private pie.core.MapView mSwitchMapView;
    private ImageButton ib_pie_zoom_in;
    private ImageButton ib_pie_zoom_out;
    private ImageButton ib_pie_location_to_dem;
    private TextView tv_pie_scale_value;
    private double demX;
    private double demY;
    private Point2D endPnt;
    private long exitTime = 0;
    private MapLayoutParams pararms;
    private LinearLayout homeVerAll;
    private MainViewPresenter m_presenter;
    private MapEditView m_mapEditView;
    private CustomSQLTools s;
    private SharedPreferences sper;
    private SharedPreferences.Editor edit;
    private Subscription m_subscription;
    private int layerCount;
    private int layerCount1;
    private double x;
    private double y;
    private boolean markSwitch = false;
    private boolean isBind = false;
    private String key = "";
    private Rect2D bounds;
    private LayerSet xgData;
    private String prjName;
    private List<Layer> layerSet;
    private Layer layerla;
    private Workspace workspace;
    String provider = "";
    private IGnssServiceBinder mService;
    private ImageView image;
    List<View> layerVeiw = new ArrayList<>();
    String path = Config.getProjectPath() + "/default/config";
    private String runXy;

    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

        }
    }

    public String taskID() {
        String taskID = sper.getString("TASK_ID", "Null");
        return taskID;
    }

    public String getID() {
        String taskID = sper.getString("TASKID", "Null");
        return taskID;
    }

    public List<String> getTitles() {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(demX));
        list.add(String.valueOf(demY));
        return list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String path = Config.getMapResoucePath();
        MapUtil.getInstace().init(this, path);
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate");
//        onBindClick();
        initProject();
        SpHelper.setStringValue("LON", String.valueOf(longitude));
        SpHelper.setStringValue("LAT", String.valueOf(latitude));
        Intent intent = new Intent(HomeActivity.this, LongRunningService.class);
        startService(intent);
    }

    private void initProject() {
        s = new CustomSQLTools();
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
        sper = getSharedPreferences("User", Context.MODE_PRIVATE);
        SpHelper.setStringValue("VerificationStatus", "WYZ");
        SpHelper.setStringValue("DefaultTag", "default");
//        LoadingDialogTools.showDialog(this);
        m_presenter = new MainViewPresenter(this);
        mSwitchMapView.setMapGestureController(new MapGestureController());
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataNum();
                Message message = new Message();
                handler.sendMessage(message);
            }
        }).start();
        onClickListener();
        mostFrag = new MostFragment();
        mostFrag.setActivity(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_most, mostFrag).commitAllowingStateLoss();
    }

    private void initView() {
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        llVerIng = findViewById(R.id.rb_1);
        llVerNo = findViewById(R.id.rb_2);
        llVerEnd = findViewById(R.id.rb_3);
        ivMenu = findViewById(R.id.iv_menu);
        mMapViews = findViewById(R.id.mvs_pie_basic_map);
        homeVerAll = findViewById(R.id.ll_home_ver_all);
        mSwitchMapView = mMapViews.getMapView();
        m_mapEditView = mMapViews.getMapEditView();
        m_mapEditView.setEditHandleSize(500);
        m_mapEditView.setSelectionTolerance(100);
        ib_pie_zoom_in = (ImageButton) findViewById(R.id.ib_pie_zoom_in);
        ib_pie_zoom_out = (ImageButton) findViewById(R.id.ib_pie_zoom_out);
        ib_pie_location_to_dem = (ImageButton) findViewById(R.id.ib_pie_location_to_dem);
        tv_pie_scale_value = (TextView) findViewById(R.id.tv_pie_scale_value);
        ib_pie_zoom_in.setOnClickListener(this);
        ib_pie_zoom_out.setOnClickListener(this);
        ib_pie_location_to_dem.setOnClickListener(this);
        mSwitchMapView.setMapScaleChangedListener(new ScaleValueChangedListener());
    }

    private boolean yesOrNO(String prjName, List<TaskEntity> priList) {
        boolean isNo = false;
        for (int i = 0; i < priList.size(); i++) {
            if (priList.get(i).getTaskNumber().equals(prjName)) {
                isNo = true;
            }
        }
        return isNo;
    }

    /**
     * 获取任务数据编号
     */
    public void getDataNum() {
        queryIng();
        queryNo();
        queryEnd();
      /*  prjName = taskID();
        if (!prjName.equals("Null")) {
            SpHelper.setStringValue("ID", prjName);
            if (listEntitiesNo.size() > 0 && (yesOrNO(prjName, listEntitiesNo)==true)){
                llVerNo.setChecked(true);
            } else if (listEntitiesIng.size() > 0&& (yesOrNO(prjName, listEntitiesIng)==true)) {
                llVerIng.setChecked(true);
            } else if (listEntitiesEnd.size() > 0&& (yesOrNO(prjName, listEntitiesEnd)==true)) {
                llVerEnd.setChecked(true);
            }
            openMap(prjName);
        } else {*/
        if (listEntitiesNo.size() > 0) {
            llVerNo.setChecked(true);
            String taskNumber = listEntitiesNo.get(0).getTaskNumber();
            SpHelper.setStringValue("ID", taskNumber);
            openMap(taskNumber);
        } else if (listEntitiesIng.size() > 0) {
            llVerIng.setChecked(true);
            String taskNumber = listEntitiesIng.get(0).getTaskNumber();
            SpHelper.setStringValue("ID", taskNumber);
            openMap(taskNumber);
        } else if (listEntitiesEnd.size() > 0) {
            llVerEnd.setChecked(true);
            String taskNumber = listEntitiesEnd.get(0).getTaskNumber();
            SpHelper.setStringValue("ID", taskNumber);
            openMap(taskNumber);
        } else {
            openMap("T250220190419001");
        }
    }
    // }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoadingDialogTools.dismissDialog();
            setMap();
            setColor();
            openNumber();
            subscribe();
            hideMap();
           /* if(markSwitch == true){
                DataSource googleimageVect = MapUtil.getInstace().getWorkspace().getDataSource("googleimageVect");
                if(googleimageVect != null){
                Dataset googleimageVect1 = googleimageVect.getDataset("googleimageVect");
                mSwitchMapView.addLayer(googleimageVect1, "GOOGLEIMAGE");
                mSwitchMapView.getLayerSet(mSwitchMapView.getLayerSetCount()-1).setVisible(true);
               }
            }*/
        }
    };

    /**
     * 根据四方位确定加载范围
     */
    private void setMap() {
        LayerSet layerSet = mSwitchMapView.getLayerSet(SysConstant.PropertyName.DTTC);
        if (layerSet != null) {
            int layerCount = layerSet.getLayerCount();
            for (int i = 0; i < layerCount; i++) {
                layerla = layerSet.getLayer(i);
                Rect2D bounds = layerla.getDataset().getBounds();
                double bottom = bounds.getBottom();
                double top = bounds.getTop();
                double left = bounds.getLeft();
                double right = bounds.getRight();
                //mSwitchMapView.setViewBounds(new Rect2D(left,top,right,bottom));
                mSwitchMapView.setViewBounds(new Rect2D(95.509959745, 52.285209651, 129.278257814, 37.059176949));
                mSwitchMapView.setScale(mSwitchMapView.getMinScale() * 40);//设置默认的加载比例尺
                mSwitchMapView.setMapCenter(bounds.getCenter());
                mSwitchMapView.setMinScale(mSwitchMapView.getMinScale());
            }
        } else {
            ToastUtil.show(this, "请选择任务数据");
        }
    }


    /**
     * 隐藏当前任务图斑以外的所有图斑
     */
    private void hideMap() {
        String defaultTag = SpHelper.getStringValue("DefaultTag");
        String s = null;
        String id1 = getID();
        //String substring1 = taskID().substring(taskID().length() - 3, taskID().length())+ "vector";
        String id = SpHelper.getStringValue("ID");
        if (!id1.equals("Null")) {
            s = id1.substring(id1.length() - 3, id1.length()) + "vector";
        } else if (id != null) {
            s = id.substring(id.length() - 3, id.length()) + "vector";
        } else {
            ToastUtil.show(this, "请选择验证任务");
        }
        LayerSet labelLayerset = mSwitchMapView.getLayerSet("LabelLayerset");
        if (labelLayerset != null) {
            int layerCount3 = labelLayerset.getLayerCount();
            if (layerCount3 > 0) {
                for (int i = 0; i < layerCount3; i++) {
                    Layer layer = labelLayerset.getLayer(i);
                    String name = layer.getName();
                    String substring = name.substring(name.indexOf("@") + 1);
                    if (!"default".equals(defaultTag)) {
                        if ("Null".equals(id1)) {
                            layer.setVisible(false);
                            layerla.setVisible(false);
                        } else {
                            if (!s.equals(substring)) {
                                layer.setVisible(false);
                                layerla.setVisible(false);
                            } else {
                                layer.setVisible(true);
                                layerla.setVisible(true);
                            }
                        }
                    } else {
                        if (!s.equals(substring)) {
                            layer.setVisible(false);
                            layerla.setVisible(false);
                        } else {
                            layer.setVisible(true);
                            layerla.setVisible(true);
                        }
                    }
                }
            }
        }


        LayerSet layerSet = mSwitchMapView.getLayerSet(SysConstant.PropertyName.SLTCJ);
        if (layerSet != null) {
            int layerCount = layerSet.getLayerCount();
            if (layerCount > 0) {
                for (int i = 0; i < layerCount; i++) {
                    Layer layer = layerSet.getLayer(i);
                    String name = layer.getName();
                    String substring = name.substring(name.indexOf("@") + 1);
                    if (!"default".equals(defaultTag)) {
                        if ("Null".equals(id1)) {
                            layer.setVisible(false);
                            layerla.setVisible(false);
                        } else {
                            if (!s.equals(substring)) {
                                layer.setVisible(false);
                                layerla.setVisible(false);
                            } else {
                                layer.setVisible(true);
                                layerla.setVisible(true);
                            }
                        }
                    } else {
                        if (!s.equals(substring)) {
                            layer.setVisible(false);
                            layerla.setVisible(false);
                        } else {
                            layer.setVisible(true);
                            layerla.setVisible(true);
                        }
                    }
                }
            }
        }
        LayerSet layerSet1 = mSwitchMapView.getLayerSet(SysConstant.PropertyName.XZQH);
        if (layerSet1 != null) {
            int layerCount2 = layerSet1.getLayerCount();
            if (layerCount2 > 0) {
                for (int i = 0; i < layerCount2; i++) {
                    Layer layer = layerSet1.getLayer(i);
                    String name = layer.getName();
                    String substring = name.substring(name.indexOf("@") + 1);
                    if (!"default".equals(defaultTag)) {
                        if ("Null".equals(id1)) {
                            layer.setVisible(false);
                            layerla.setVisible(false);
                        } else {
                            if (!s.equals(substring)) {
                                layer.setVisible(false);
                                layerla.setVisible(false);
                            } else {
                                layer.setVisible(true);
                                layerla.setVisible(true);
                                setXzqhColor();
                            }
                        }
                    } else {
                        if (!s.equals(substring)) {
                            layer.setVisible(false);
                            layerla.setVisible(false);
                        } else {
                            layer.setVisible(true);
                            layerla.setVisible(true);
                            setXzqhColor();
                        }
                    }
                }
            }
        }
    }

    private void setColor() {
        LayerSet SLData;
        Layer layer = null;
        List<Layer> layerSets = new ArrayList<>();
        SLData = mSwitchMapView.getLayerSet(SysConstant.PropertyName.SLTCJ);
        try {
            if (null != SLData && !SLData.equals("")) {
                layerCount = SLData.getLayerCount();
            }
        } catch (Exception e) {
            e.printStackTrace();

            return;
        }
        if (layerCount != 0) {
            for (int i = 0; i < layerCount; i++) {
                try {
                    if (SLData != null || !SLData.equals("")) {
                        layer = SLData.getLayer(i);
                    }
                    if (layer != null || !layer.equals("")) {
                        layerSets.add(layer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    return;
                }
            }
            for (int k = 0; k < layerSets.size(); k++) {
                String name = layerSets.get(k).getName();
                Style style = layerSets.get(k).getStyle();
                String splitData = splitData(name, "矿", "_");
                if (!TextUtils.isEmpty(splitData)) {
                    if (splitData.equals("权数据_面")) {
                        style.lineColor = Color.rgb(0, 0, 205);
                    } else if (splitData.equals("山地质环境治理_面")) {
                        style.lineColor = Color.rgb(50, 205, 50);
                    } else if (splitData.equals("山地质灾害_线")) {
                        style.lineColor = Color.rgb(28, 28, 28);
                    } else if (splitData.equals("山地质灾害_面")) {
                        style.lineColor = Color.rgb(255, 0, 255);
                    } else if (splitData.equals("山开发占地_井口_点")) {
                        Dataset dataset1 = layerSets.get(k).getDataset();
                        DatasetVector dataset = (DatasetVector) dataset1;
                        Recordset query = dataset.query(new QueryDef());
                        int recordCount = query.getRecordCount();
                        for (int i = 0; i < recordCount; i++) {
                            query.moveTo(i);
                            Geometry geometry = query.getGeometry();
                            Point2D center = geometry.getBounds().getCenter();
                            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_point);
                            Point2D point2D = MapUtil.getInstace().dtToMap(mSwitchMapView, dataset, center);
                            image = new ImageView(this);
                            image.setImageBitmap(bitmap);
                            MapLayoutParams pararms = new MapLayoutParams(20, 20, point2D, 1);
                            layerVeiw.add(image);
                            mMapViews.addView(image, 1, pararms);
                        }
                    } else if (splitData.equals("山开发占地_面")) {
                        style.lineColor = Color.rgb(255, 255, 0);
                    } else {
                        style.lineColor = Color.rgb(255, 165, 0);
                    }
                }
                style.fillForeColor = Color.argb(0, 0, 0, 0);
                style.lineWidth = 2;
                layerSets.get(k).setStyle(style);
            }
        }
    }

    private void setXzqhColor() {
        layerSet = new ArrayList<>();
        xgData = mSwitchMapView.getLayerSet(SysConstant.PropertyName.XZQH);
        String color = SpHelper.getStringValue("COLOR");
        if ("null".equals(color) || null == color) {
            color = "TWO";
        }
        if (color.equals("ONE")) {
            //验证中
            setXzColor(255, 165, 0);
        } else if (color.equals("TWO")) {
            //未验证
            setXzColor(255, 0, 0);
        } else if (color.equals("THREE")) {
            //已验证
            setXzColor(0, 255, 0);
        }
    }

    private void setXzColor(int R, int G, int B) {
        if (xgData != null) {
            layerCount1 = xgData.getLayerCount();
            if (xgData.isVisible()) {
                xgData.setVisible(false);
            }
        }
        if (layerCount1 != 0) {
            for (int j = 0; j < layerCount1; j++) {
                layerSet.add(xgData.getLayer(j));
            }
            for (int a = 0; a < layerSet.size(); a++) {
                Style style = layerSet.get(a).getStyle();
                style.fillForeColor = Color.argb(0, 0, 0, 0);
                style.lineColor = Color.rgb(R, G, B);
                style.lineWidth = 4;
                layerSet.get(a).setStyle(style);
            }
        }
    }

    public String splitData(String str, String strStart, String strEnd) {
        String tempStr;
        tempStr = str.substring(str.indexOf(strStart) + 1, str.lastIndexOf(strEnd));
        return tempStr;
    }


    private void openNumber() {
        ThemeLabel theme = new ThemeLabel();
        //获取所有的矢量数据
        LayerSet layerSet = mSwitchMapView.getLayerSet(SysConstant.PropertyName.SLTCJ);
        if (layerSet != null) {
            //获取矢量数据的条目数
            int layerCount = layerSet.getLayerCount();
            //根据条目索引获取Layer；
            for (int y = 0; y < layerCount; y++) {
                Layer layer = layerSet.getLayer(y);
                Dataset dataset = layer.getDataset();
                theme.makeDefault(dataset, SysConstant.PropertyName.JYBH);
                theme.setAlignmentType(TextAlignmentType.Horizontal);
                theme.setDirectionType(TextDirectionType.LeftTopToRightBottom);
                theme.setAlongLineType(TextAlongLineType.Simple);
                mSwitchMapView.addThemeLayer(dataset, RenderType.RenderLabel, "LabelLayerset", theme);
                mSwitchMapView.refresh(true, true);
            }
        }
    }


    private void subscribe() {
        m_subscription = RxBus.getDefault().toObserverable(Object.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                //在主线程进行观察，可做UI更新操作
                .observeOn(AndroidSchedulers.mainThread())
                //观察的对象
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event instanceof RefreProject) {
                            m_presenter.refreshCurrentProject();
                            List<LayerSet> layerSets = new ArrayList<LayerSet>();
                            int layerSetCount = mSwitchMapView.getLayerSetCount();
                            for (int i = 0; i < layerSetCount; i++) {
                                layerSets.add(mSwitchMapView.getLayerSet(i));
                            }
                            RxBus.getDefault().post(new LayerSetEvent(layerSets));
                        } else if (event instanceof ThemeLabelEntityEvent) {//标签专题图设置标注字段，标注位置监听
                            ThemeLabelEntityEvent entity = (ThemeLabelEntityEvent) event;
                            if (entity != null) {
                                String s = entity.layerjson;
                                String fieldname = entity.field;
                                Layer layer = new Gson().fromJson(s, Layer.class);
                                if (layer != null) {
                                    Dataset dset = layer.getDataset();
                                    //如果存在相同的标签专题图，先删除在添加
                                    Layer dslayer = null;
                                    LayerSet labellayset = mSwitchMapView.getLayerSet("LabelLayerset");
                                    if (labellayset != null) {
                                        int labelcount = labellayset.getLayerCount();
                                        for (int i = 0; i < labelcount; i++) {
                                            Layer ly = labellayset.getLayer(i);
                                            if (ly != null && ly.getName().startsWith(dset.getName())) {
                                                dslayer = labellayset.getLayer(i);
                                            }
                                        }
                                    }
                                    if (dslayer != null) {
                                        labellayset.removeLayer(dslayer.getName());
                                    }
                                    ThemeLabel theme = new ThemeLabel();
                                    theme.makeDefault(dset, fieldname);
                                    theme.setAlignmentType(entity.type);
                                    theme.setDirectionType(TextDirectionType.LeftTopToRightBottom);
                                    theme.setAlongLineType(TextAlongLineType.Simple);
                                    LayerSet themeLayerset = mSwitchMapView.addThemeLayer(dset, RenderType.RenderLabel, "LabelLayerset", theme);
                                    int layercount = themeLayerset.getLayerCount();
                                    if (layercount > 0) {
                                        Layer themeLayer = themeLayerset.getLayer(layercount - 1);
                                        TextStyle textStyle = new TextStyle();
                                        textStyle.align = 1;
                                        textStyle.foreColor = entity.forecolor;
                                        textStyle.width = entity.fontsize;
                                        textStyle.height = entity.fontsize;
                                        themeLayer.setTextStyle(textStyle);
                                        mSwitchMapView.refresh(true, true);
                                        //1、通知配置图层列表界面刷新list, 2、将新添加的标签专题图传递到配置图层详情界面
                                        RxBus.getDefault().post(new ThemepostlayerEvent(themeLayer));
//                                        RxBus.getDefault().post(getAllLayerset());
                                    }
                                }
                            }

                        } else if (event instanceof ThemeLayerEvent) {//标签专题图开关按钮监听，如果打开时不存在此标签专题图，则至此主界面添加
                            ThemeLayerEvent layer = (ThemeLayerEvent) event;
                            ThemeLabel theme = new ThemeLabel();
                            theme.makeDefault(layer.m_layer.getDataset(), "");
                            mSwitchMapView.addThemeLayer(layer.m_layer.getDataset(), RenderType.RenderLabel, "LabelLayerset", theme);
                            mSwitchMapView.refresh(true, true);
                            LayerSet layset = mSwitchMapView.getLayerSet("LabelLayerset");
                            int layerCount = layset.getLayerCount();
                            for (int i = 0; i < layerCount; i++) {
                                Layer ly = layset.getLayer(i);
                                if (layer.m_layer.getName().startsWith(ly.getName())) {
                                    //1、通知配置图层列表界面刷新list, 2、将新添加的标签专题图传递到配置图层详情界面
                                    RxBus.getDefault().post(new ThemepostlayerEvent(ly));
//                                    RxBus.getDefault().post(getAllLayerset());
                                }
                            }
                        } else if (event instanceof ColorEvent) {
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void onClickListener() {
        ivMenu.setOnClickListener(this);
        llVerEnd.setOnClickListener(this);
        llVerNo.setOnClickListener(this);
        llVerIng.setOnClickListener(this);
    }


    /**
     * 验证中数据初始化
     */
    private void queryIng() {
        String sql = "验证中";
        listEntitiesIng.clear();
        listEntitiesIng.addAll(s.getAllocated(sql, HomeActivity.this));
    }

    /**
     * 未验证数据初始化
     */
    private void queryNo() {
        String sql = "已导出";
        listEntitiesNo.clear();
        listEntitiesNo.addAll(s.getAllocated(sql, HomeActivity.this));
    }

    /**
     * 以验证数据初始化
     */
    private void queryEnd() {
        String sql = "已验证";
        listEntitiesEnd.clear();
        listEntitiesEnd.addAll(s.getAllocated(sql, HomeActivity.this));
    }


    private void openMap(String taskNumber) {
        if (MapUtil.getInstace().getWorkspace() == null) {
            m_presenter.start(taskNumber);
        } else {
            mSwitchMapView.closeMap();
            m_presenter.start(taskNumber);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * 地图切换
     */
    public void switchMapSon(Boolean mark) {
        markSwitch = mark;
        if (mark == true) {
            String vectorRootPath = "/storage/emulated/0/PIEMapData/projectData/default/googleimageVect/googleimageVect.xml";
            DataSource googleimage = MapUtil.getInstace().getWorkspace().getDataSource("googleimageVect");
            if (googleimage == null) {
                DataSource dataSource = new DataSource();
                boolean open = dataSource.open(vectorRootPath);
                if (open) {
                    workspace = MapUtil.getInstace().getWorkspace();
                    workspace.addDataSource(dataSource);
                    workspace.save();
                }
                Dataset imgeDt = dataSource.getDataset("googleimageVect");
                if (imgeDt != null) {
                    mSwitchMapView.addLayer(imgeDt, "GOOGLEIMAGE");
                } else {
                    ToastUtil.show(this, "2d底图为空");
                }
            } else {
                DataSource googleimageVect = MapUtil.getInstace().getWorkspace().getDataSource("googleimageVect");
                if (googleimageVect != null) {
                    Dataset googleimageVect1 = googleimageVect.getDataset("googleimageVect");
                    mSwitchMapView.addLayer(googleimageVect1, "GOOGLEIMAGE");
                    mSwitchMapView.getLayerSet(mSwitchMapView.getLayerSetCount() - 1).setVisible(true);
                }
            }

        } else {
            DataSource googleimageVect = MapUtil.getInstace().getWorkspace().getDataSource("googleimageVect");
            if (googleimageVect != null) {
                Dataset googleimageVect1 = googleimageVect.getDataset("googleimageVect");
                mSwitchMapView.addLayer(googleimageVect1, "GOOGLEIMAGE");
                mSwitchMapView.getLayerSet(mSwitchMapView.getLayerSetCount() - 1).setVisible(false);
            }
        }
    }

    /**
     * 地图缩放回调
     *
     * @author pie
     */
    private class ScaleValueChangedListener implements MapScaleChangedListener {

        @Override
        public void onScaleChanged(double scale) {
            setScaleTextView();
        }

    }

    /**
     * 设置显示缩放比例 TextView
     */
    private void setScaleTextView() {
        int value = getCurrentScaleValue() / 100;// 厘米 换算成米
        if (xgData != null) {
            if (value < 60000) {

                if (!xgData.isVisible()) {
                    xgData.setVisible(true);
                }
            } else {
                if (xgData.isVisible()) {
                    xgData.setVisible(false);
                }
            }
        }
        if (value < 1000) {
            tv_pie_scale_value.setText(value + "m");
        } else {
            double valueF = value / 1000.00;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String dfValue = decimalFormat.format(valueF);
            tv_pie_scale_value.setText(dfValue + "km");
        }
    }

    /**
     * 获取当前比例尺 对应 1：value
     *
     * @return 1:value,value值（单位 cm）
     */
    private int getCurrentScaleValue() {
        double scale = mSwitchMapView.getScale();
        int scaleValue = (int) (1 / scale);
        return scaleValue;
    }


    @SuppressLint("CommitPrefEdits")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_menu), Gravity.RIGHT, 0, 0);
                break;
            case R.id.rb_1:
                //初始化任务列表数据
                removeView();
                //deleteFile(new File(path));
                edit = sper.edit().putString("TASKID", "Null");
                edit.commit();
                SpHelper.setStringValue("DefaultTag", "tag");
                intent = new Intent(this, VerNoActivity.class);
                intent.putExtra("from", 1);
                startActivity(intent);
                break;
            case R.id.rb_2:
                removeView();
                //deleteFile(new File(path));
                edit = sper.edit().putString("TASKID", "Null");
                edit.commit();
                SpHelper.setStringValue("DefaultTag", "tag");
                intent = new Intent(this, VerNoActivity.class);
                intent.putExtra("from", 2);
                startActivity(intent);
                break;
            case R.id.rb_3:
                removeView();
                //deleteFile(new File(path));
                edit = sper.edit().putString("TASKID", "Null");
                edit.commit();
                SpHelper.setStringValue("DefaultTag", "tag");
                intent = new Intent(this, VerNoActivity.class);
                intent.putExtra("from", 3);
                startActivity(intent);
                break;
            case R.id.ib_pie_zoom_in:
                //放大
                mapZoomIn();
                break;

            case R.id.ib_pie_zoom_out:
                //缩小
                mapZoomOut();
                break;

            case R.id.ib_pie_location_to_dem:
                //定位
                locationToDem();
                break;

        }
    }


    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    private void removeView() {
        if (layerVeiw.size() >= 0) {
            for (int i = 0; i < layerVeiw.size(); i++) {
                mMapViews.removeView(layerVeiw.get(i));
            }
        }
        layerVeiw.clear();
    }

    /**
     * 地图放大
     */
    private void mapZoomIn() {
        mSwitchMapView.zoomIn();
        Log.i("zrc", mSwitchMapView.getScale() + "");
    }


    /**
     * 地图缩小
     */
    private void mapZoomOut() {
        mSwitchMapView.zoomOut();
    }


    /**
     * 定位到有Dem数据处
     */
    private void locationToDem() {
        demX = 0;
        demY = 0;
        if (latitude > 0 && longitude > 0) {
            demX = longitude;
            demY = latitude;
        } else {
            Location location = LocationUtil2.getInstance(this).showLocation();
            if (location != null) {
                demX = location.getLongitude(); // 经度 DEMTest
                demY = location.getLatitude(); // 纬度 DEMTest
            }
        }
        if ((int) demX <= 0 || demY <= 0) {
            ToastUtil.show(this, "正在定位中...");
        } else {
            Point2D startPoint = mSwitchMapView.getMapCenter();
            endPnt = mSwitchMapView.getPrjCoordSys().latLngToProjection(new Point2D(demX, demY));
            if (Math.abs(startPoint.x - endPnt.x) > 1 || Math.abs(startPoint.y - endPnt.y) > 1) {
                mSwitchMapView.startPanAnimation(startPoint, endPnt);
                mSwitchMapView.setScale(2.105484291333595E-5);
            }
            setPositioning();
        }
    }

    private void setPositioning() {
        /*添加定位图标方法一*/
        ImageView image = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_position);
        image.setImageBitmap(bitmap);
        if (pararms == null) {
            pararms = new MapLayoutParams(30, 30, endPnt, 1);
        }
        mMapViews.removeViewAt(1);
        mMapViews.addView(image, 1, pararms);
    }

    @Override
    protected void onDestroy() {
     /*   //保存最后的工程名称
        m_presenter.saveProjectNameToCache();
        if (m_subscription.isUnsubscribed()) {
            m_subscription.unsubscribe();
        }*/
        mSwitchMapView.closeMap();
        MapUtil.getInstace().getWorkspace().close();
        onStopClick();
        Intent intent1 = new Intent(this, LongRunningService.class);
        stopService(intent1);
        super.onDestroy();

    }

    public void jieYa() {
        encryptDir = SpHelper.getStringValue("FIELD", "VerifyData");
        if (encryptDir.equals("")) {
            encryptDir = "VerifyData";
        }
        zip4j = new CompressOperate_zip4j();
        list = new ArrayList<>();
        list.add(0, new File(FileUtil.encrypt + encryptDir + FileUtil.encryptDB));
        list.add(1, new File(FileUtil.encrypt + encryptDir + FileUtil.encryptDBJ));
        list.add(2, new File(FileUtil.encrypt + encryptDir + FileUtil.encryptTEX));
        zip4j.compressZip4j(list, FileUtil.encrypt + encryptDir, "123456");
        FileUtil.deletefile(list);
        /**
         * 压缩BO2,BO3文件
         * 解压BO2,BO3文件
         *
         * */
        List<String> alldir = new ArrayList<>();
        alldir.addAll(FileUtil.getFilesAllName(FileUtil.encrypt + encryptDir));
        zip4j = new CompressOperate_zip4j();
        list = new ArrayList<>();
        for (int i = 0; i < alldir.size(); i++) {
            File file = new File(alldir.get(i) + FileUtil.bt);
            if (file.exists()) {
                zip4j.compressZip4j(alldir.get(i) + FileUtil.bt, alldir.get(i), "123456");
                zip4j.compressZip4j(alldir.get(i) + FileUtil.bs, alldir.get(i), "123456");
            }

        }
        for (int i = 0; i < alldir.size(); i++) {
            File file = new File(alldir.get(i) + FileUtil.bt);
            if (file.exists()) {
                FileUtil.deleteDir(alldir.get(i) + FileUtil.bt);
                FileUtil.deleteDir(alldir.get(i) + FileUtil.bs);
            }

        }
        SpHelper.setStringValue("RUNXY", "2");
    }

    @Override
    protected void onRestart() {
        if (!taskID().equals("Null")) {
            String vectorRootPath = "/storage/emulated/0/PIEMapData/projectData/default" + "/config/vector";
            String vectorFilePath = vectorRootPath + "/" + taskID().substring(taskID().length() - 3, taskID().length()) + "vector.gsf";
            if (SpHelper.getStringValue("SHOWPOINT").equals("show")) {
                showPoint(vectorFilePath);
                SpHelper.setStringValue("SHOWPOINT", "");
            }
            File file1 = new File(vectorFilePath);
            if (!taskID().equals(SpHelper.getStringValue("ID"))) {
                DataSource vectorDs = new DataSource();
                if (!file1.exists() || !file1.isFile()) {
                    initProject();
                } else {
                    vectorDs.open(vectorFilePath);
                }
            }
            hideMap();
        }
        if (SpHelper.getStringValue("MARK") != null) {
            if (SpHelper.getStringValue("MARK").equals("mark")) {
                initProject();
                SpHelper.setStringValue("MARK", "");
            }
        }
        hideMap();
        super.onRestart();
    }

    private void showPoint(String path) {
        DataSource dataSource = new DataSource();
        dataSource.open(path);
        int count = dataSource.getDatasetCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String name = dataSource.getDataset(i).getName();
                Log.v("***", name);
                if (name.contains("矿山开发占地_井口_点_")) {
                    Dataset dataset = dataSource.getDataset(i);
                    DatasetVector dataset1 = (DatasetVector) dataset;
                    Recordset query = dataset1.query(new QueryDef());
                    int recordCount = query.getRecordCount();
                    if (recordCount > 0) {
                        for (int y = 0; y < recordCount; y++) {
                            query.moveTo(y);
                            Geometry geometry = query.getGeometry();
                            Point2D center = geometry.getBounds().getCenter();
                            Point2D point2D = MapUtil.getInstace().dtToMap(mSwitchMapView, dataset, center);
                            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_point);
                            image = new ImageView(this);
                            image.setImageBitmap(bitmap);
                            MapLayoutParams pararms = new MapLayoutParams(20, 20, point2D, 1);
                            layerVeiw.add(image);
                            mMapViews.addView(image, 1, pararms);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {

            List<String> trackXY = new ArrayList<>();
            trackXY.addAll(xY());
            runXy = SpHelper.getStringValue("RUNXY", "1");
            if (!runXy.equals("1")) {

            } else {
                if (trackXY.size() > 0) {
                    s.AddTrack(HomeActivity.this, s.getUUID(), "", devinceNo, trackXY.get(0), trackXY.get(1),
                            Constant.TRACK_EVENT_QUIT, s.getCurrTime(), SpHelper.getStringValue("USERNAME"), "", Constant.GPS);
                }
            }

            onStopClick();
            Intent intent1 = new Intent(this, LongRunningService.class);
            stopService(intent1);
            if (null != mSwitchMapView) {
                mSwitchMapView.destroyMapWindow();
            }
            LoadingDialogTools.showDialog(this);
            LoadingDialogTools.setMessage("数据删除中，请稍后");
            deleFile(new DataCallBack() {
                @Override
                public void succeed() {
                    LoadingDialogTools.dismissDialog();
                    System.exit(0);
                    finish();
                }

                @Override
                public void failed() {
                    LoadingDialogTools.dismissDialog();
                }
            });
//            jieYa()
        }

        return true;
    }




    private void deleFile(final DataCallBack dataCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> fileList = FileUtil.getFilesAllName(FileUtil.encrypt + SpHelper.getStringValue("FIELD"));
                    for (int i = 0; i < fileList.size(); i++) {
                        if (new File(fileList.get(i) + FileUtil.bt + ".rar").exists()) {
                            deleteFile(new File(fileList.get(i) + FileUtil.bt));
                        }
                        if (new File(fileList.get(i) + FileUtil.bs + ".rar").exists()) {
                            deleteFile(new File(fileList.get(i) + FileUtil.bs));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dataCallBack.failed();
                }
                dataCallBack.succeed();
            }
        }).start();
    }


    interface DataCallBack {
        void succeed();

        void failed();
    }


    /**
     * 集成图斑
     */

    @Override
    public void switchFragment(String flag) {
    }

    @Override
    public MapView getMapView() {
        return mSwitchMapView;
    }

    @Override
    public MapEditView getMapEditView() {
        return m_mapEditView;
    }

    @Override
    public void removeGeometryByTag(String geometryRegionTag) {
        mSwitchMapView.getTrackingLayer().removeGeometryByName(geometryRegionTag);

    }

    @Override
    public Layer getEditLayer() {
        return null;
    }

    @Override
    public int saveGpsRecordToDataset(List<Point2D> m_gpsPointList) {
        return 0;
    }


    @Override
    public void setPresenter(MainViewContract.Presenter presenter) {

    }

    @Override
    public void showToast(String msg) {

    }


    /**
     * 绑定GnssService服务
     */
    public void onBindClick() {
        try {
            Intent intent = new Intent("com.huace.gnssserver.GnssService");
            intent.setPackage("com.huace.gnssserver");
            isBind = bindService(intent, mConnection, BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v("********", "绑定成功");
            mService = IGnssServiceBinder.Stub.asInterface(service);
            try {
                mService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
                //下面的这句监听，主要是给智能RTK和GNSS RTK使用的，其他设备可不需要这个监听
                mService.requestReceiverListener(mIReceiverListener);
                //下面的这句监听，主要是给智能RTK和GNSS RTK使用的，其他设备可不需要这个监听
                IReceiver mReceiver = IReceiver.Stub.asInterface(mService.getReceiver());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v("********", "绑定失败");
            mService = null;
        }

    };

    IGnssListener listener = new IGnssListener.Stub() {
        @Override
        public void onLocationChanged(Location location) throws RemoteException {
            Log.v("********", longitude + "" + "---------1" + latitude);
        }

        /***
         *
         * @param provider 目前有Unknow,Local，Wifi，Bluetooth
         * @param status 状态 0：接收机和Cors都已断开
         *                     1:接收机连接，Cors未连接
         *                     2：接收机和Cors都已连接
         *                     10:已经10秒没有收到数据
         * @throws RemoteException
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) throws RemoteException {
            HomeActivity.this.provider = provider;
        }

        /***
         * 主要获取数据的接口
         * @param gnssinfo Gnss数据
         * @throws RemoteException
         */
        @Override
        public void onGnssInfoChanged(GnssInfo gnssinfo) throws RemoteException {
            longitude = gnssinfo.longitude;
            latitude = gnssinfo.latitude;
            SpHelper.setStringValue("LON", String.valueOf(longitude));
            SpHelper.setStringValue("LAT", String.valueOf(latitude));
            Log.v("********", longitude + "" + "---------2" + latitude);
        }

        /***
         * 当连接上接收机时会回调
         * @param provider 目前有Unknow,Local，Wifi，Bluetooth
         * @throws RemoteException
         */
        @Override
        public void onProviderEnabled(String provider) throws RemoteException {
            HomeActivity.this.provider = provider;
        }

        /***
         * 当断开接收机时会回调
         * @param provider 目前有Unknow,Local，Wifi，Bluetooth
         * @throws RemoteException
         */
        @Override
        public void onProviderDisabled(String provider) throws RemoteException {
            HomeActivity.this.provider = provider;
        }
    };

    IReceiverListener mIReceiverListener = new IReceiverListener.Stub() {


        @Override
        public void getSatelliteConstellations(int i) throws RemoteException {

        }

        @Override
        public void getSatelliteUsedNums(SatelliteNumber satelliteNumber) throws RemoteException {

        }

        @Override
        public void getSatelliteInfos(SatelliteInfo[] satelliteInfos) throws RemoteException {

        }

        @Override
        public void getPositionEx(PositionInfo positionInfo, Course course) throws RemoteException {

        }

        @Override
        public void getBasePosition(Position position) throws RemoteException {

        }

        @Override
        public void getGnssDops(DopsInfo dopsInfo) throws RemoteException {

        }

        @Override
        public void getGpggaData(byte[] bytes) throws RemoteException {

        }

        @Override
        public void getBaseParams(BaseParams params, WorkWay way) throws RemoteException {
        }

        @Override
        public void getIoEnable(boolean[] ioEnable) throws RemoteException {
        }

        @Override
        public void getIoData(GnssDataConfigList list) throws RemoteException {
        }

        @Override
        public void getNmeaOutputList(NmeaData[] nmeaArray) throws RemoteException {
        }

        @Override
        public void getGnssPdopMask(int i) throws RemoteException {

        }

        @Override
        public void getGnssElevmask(int i) throws RemoteException {

        }

        @Override
        public void getEbubbleInfo(EbubbleInfo ebubbleInfo) throws RemoteException {

        }

        @Override
        public void getMagnetometerInfo(MagnetometerInfo magnetometerInfo) throws RemoteException {

        }

        @Override
        public void getBasePositionList(BasePositionInfoArray basePositionInfoArray) throws RemoteException {

        }

        @Override
        public void getBasePositionDifference(float v) throws RemoteException {

        }

        @Override
        public void getTransmissionInfo(byte[] bytes) throws RemoteException {

        }

        @Override
        public void getPosDataFrequency(DataFrequency dataFrequency) throws RemoteException {

        }


        @Override
        public void getReceiverInfo(ReceiverInfo info) throws RemoteException {
        }

        @Override
        public void getRegCode(String regCode) throws RemoteException {
        }

        @Override
        public void getFileRecordStatus(FileRecordStatus status) throws RemoteException {
        }

        @Override
        public void getFileRecordParams(FileRecordInfo info) throws RemoteException {
        }

        @Override
        public void getFileRecordAutoStart(boolean isAuto) throws RemoteException {
        }

        @Override
        public void getComBaudrate(Baudrate baudrate) throws RemoteException {
        }

        @Override
        public void getExpireDate(ExpireDate expireDate) throws RemoteException {

        }

        @Override
        public void getBattteyLife(int i) throws RemoteException {

        }


        @Override
        public void getModemCommunicationMode(ModemCommunicationMode mode) throws RemoteException {
        }

        @Override
        public void getModemDialStatus(ModemDialStatus status) throws RemoteException {
        }

        @Override
        public void getCsdDialStatus(ModemDialStatus status) throws RemoteException {
        }

        @Override
        public void getCsdInfo(CsdInfo info) throws RemoteException {
        }

        @Override
        public void getModemAutoDialParams(ModemDialParams params) throws RemoteException {
        }

        @Override
        public void getModemAutoDial(boolean b) throws RemoteException {

        }

        @Override
        public void getModemAutoPowerOn(boolean b) throws RemoteException {

        }

        @Override
        public void getModemPowerStatus(PowerStatus powerStatus) throws RemoteException {

        }

        @Override
        public void getWifiAutoPowerOn(boolean auto) throws RemoteException {
        }

        @Override
        public void getWifiInfo(ReceiverWifiInfo info) throws RemoteException {
        }

        @Override
        public void getWifiStatus(PowerStatus status) throws RemoteException {
        }

        @Override
        public void getModemSignal(ModemSignal modemSignal) throws RemoteException {

        }

        @Override
        public void getModemBandMode(ModemBandMode modemBandMode) throws RemoteException {

        }


        @Override
        public void getRadioPowerStatus(PowerStatus status) throws RemoteException {
        }

        @Override
        public void getRadioChannelList(RadioChannelArray channelList) throws RemoteException {
        }

        @Override
        public void getRadioInfo(RadioInfo info) throws RemoteException {
        }

        @Override
        public void getGprsStatus(NetworkStatus status) throws RemoteException {
        }

        @Override
        public void getGprsInfo(GprsInfo info) throws RemoteException {
        }

        @Override
        public void getCorsInfo(CorsInfo info) throws RemoteException {
        }

        @Override
        public void getSourceTable(DataSourceList data) throws RemoteException {
        }

        @Override
        public void getRadioAutoPowerOn(boolean b) throws RemoteException {

        }

        @Override
        public void getGprsLoginMdl(boolean b) throws RemoteException {

        }

        @Override
        public void UpdateFileRecordParamsError() throws RemoteException {

        }

        @Override
        public void GetCmdUpdateError() throws RemoteException {

        }

        @Override
        public void getDiffDataTip() throws RemoteException {

        }

        @Override
        public void getTransformInfo(TransformInfo transformInfo) throws RemoteException {

        }

        @Override
        public void getGeoidModelInfo(GeoidModelInfo geoidModelInfo) throws RemoteException {

        }

        @Override
        public void getProjectionInfo(ProjectionInfo projectionInfo) throws RemoteException {

        }

        @Override
        public void getFileRecordFrequencyList(DataFrequencyArray dataFrequencyArray) throws RemoteException {

        }

    };

    /***
     * 解绑GnssServer服务
     */
    private void onStopClick() {
        if (isBind) {
            unbindService(mConnection);
            isBind = false;
        }
        if (mService == null) {
            return;
        }
        try {
            mService.removeUpdates(listener);//移除监听
            mService.removeReceiverListener(mIReceiverListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
