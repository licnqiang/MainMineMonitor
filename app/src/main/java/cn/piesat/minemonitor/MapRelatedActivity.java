package cn.piesat.minemonitor;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.entity.TaskEntity;
import cn.piesat.minemonitor.home.AddMapActivity;
import cn.piesat.minemonitor.home.Constant;
import cn.piesat.minemonitor.home.ContentListActivity;
import cn.piesat.minemonitor.mapdata.presenter.MainViewPresenter;
import cn.piesat.minemonitor.mapdata.presenter.contract.MainViewContract;
import cn.piesat.minemonitor.mapdata.utils.Config;
import cn.piesat.minemonitor.mapdata.utils.MapUtil;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;
import cn.piesat.minemonitor.mapdata.utils.SysConstant;
import cn.piesat.minemonitor.util.LoadingDialogTools;
import cn.piesat.minemonitor.util.LocationUtil2;
import cn.piesat.minemonitor.util.LogUtils;
import cn.piesat.minemonitor.util.ToastUtil;
import pie.core.DataExchange;
import pie.core.DataSource;
import pie.core.Dataset;
import pie.core.DatasetLayer;
import pie.core.DatasetVector;
import pie.core.EditAction;
import pie.core.EditLayer;
import pie.core.EditMode;
import pie.core.Geometry;
import pie.core.Layer;
import pie.core.LayerSet;
import pie.core.MapClickListener;
import pie.core.MapEditView;
import pie.core.MapScaleChangedListener;
import pie.core.MapView;
import pie.core.Point2D;
import pie.core.QueryDef;
import pie.core.Recordset;
import pie.core.Rect2D;
import pie.core.RenderType;
import pie.core.Style;
import pie.core.ThemeLabel;
import pie.core.UserAction;
import pie.map.MapLayoutParams;
import pie.map.MapViews;
import pie.map.enums.TextAlignmentType;
import pie.map.enums.TextAlongLineType;
import pie.map.enums.TextDirectionType;
import pie.map.gesture.MapGestureController;
import pie.map.interfaces.MapCenterChangeListener;


/**
 * 地图二级页面
 */
public class MapRelatedActivity extends BaseActivity implements View.OnClickListener, MainViewContract.View, MapClickListener, Observer {

    private static final String TAG = "smzq";
    private List<TaskEntity> listEntitiesIng = new ArrayList<>();
    private List<TaskEntity> listEntitiesNo = new ArrayList<>();
    private List<TaskEntity> listEntitiesEnd = new ArrayList<>();
    private Intent intent;
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
    private MapLayoutParams pararms;
    private MainViewPresenter m_presenter;
    private MapEditView m_mapEditView;
    private CustomSQLTools s;
    private Layer m_editLayer;
    private SharedPreferences sper;
    private SharedPreferences.Editor edit;
    private boolean mark = false;
    private int layerCount;
    private int layerCount1;
    private double x;
    private double y;
    private int countEditMap = 1;
    List<View> layerVeiw = new ArrayList<>();
    //2018-6-20后期新增地图页面实现业务分离
//    @BindView(R.id.rl_gcdsz)
//    RelativeLayout rlgcdsz;
    @BindView(R.id.btn_save)//观察点保存
            ImageButton gcdSave;
    @BindView(R.id.img_see_point)//观察中心点
            ImageView gcdPoint;
    @BindView(R.id.tv_setting_title)//标题
            TextView mapTitle;
    @BindView(R.id.ll_add_tb)//添加图版版块
            LinearLayout addtb;
    @BindView(R.id.ll_changed_tb)//图斑编辑版块
            LinearLayout changeTB;

    private int code;
    private List<String> mapList;
    private double cx;
    private double cy;
    private String cn;
    private String plaquesNum;
    private DataSource m_dataSource;
    private String dataType;
    private String layername;
    private List<String> layerNameList;
    private String layername1;
    private ImageView image;

    @Override
    public void update(Observable o, Object arg) {

    }

    class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                double x_pi = Math.PI * 3000.0 / 180.0;
                x = location.getLongitude() - 0.0065;
                y = location.getLatitude() - 0.006;
                double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
                double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
                longitude = z * Math.cos(theta);
                latitude = z * Math.sin(theta);
                Log.d("lon", "locationToDem: " + demX);
                Log.d("lon", "locationToDem: " + demY);
            }
            switch (code) {
                case 21:
                case 23:
                case 24:
                    zoomCenter(cx, cy);
                    break;
                case 22:
                case 20:
                    if (x==-0.0065&&y==-0.006){
                        zoomCenter(118.963, 45.2641);
                    }else{
                        zoomCenter(x, y);

                    }

                    break;
                default:
            }
            mSwitchMapView.setMapCenterChangeListener(new MapCenterChangeListener() {
                @Override
                public void onMapCenterChange(Point2D point2D) {
                    Point2D test2 = mSwitchMapView.getPrjCoordSys().projectionTolatLng(point2D);
                    mapList.clear();
                    mapList.add(String.valueOf(test2.x));
                    mapList.add(String.valueOf(test2.y));
//                    Toast.makeText(HomeActivity.this, "" +test2.x+"\n"+test2.y, Toast.LENGTH_SHORT).show();
                }
            });
            mSwitchMapView.setMapClickListener(new MapClickListener() {
                @Override
                public void onMapClick(Point2D point2D) {
                }
            });

        }
    }


    public String taskID() {
        String taskID = sper.getString("TASK_ID", "Null");
        return taskID;
    }

    @OnClick({R.id.btn_save, R.id.ll_back, R.id.btn_save_map, R.id.btn_undo, R.id.ib_changed, R.id.ib_changed_save, R.id.ib_changed_undo})
    public void onMapView(View view) {
        switch (view.getId()) {
            case R.id.btn_save_map:
                if (mapList.size() > 0) {
                    intent = new Intent(this, AddMapActivity.class);
                    intent.putExtra("mapGCDX", mapList.get(0));
                    intent.putExtra("mapGCDY", mapList.get(1));
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
            case R.id.btn_undo:
                finish();
                break;
            case R.id.ll_back:
                removeView();
                finish();
                break;
            case R.id.btn_save:
                if (code == 20) {//保存观察点
                    if (mapList.size() > 0) {
                        intent = new Intent(this, ContentListActivity.class);
                        intent.putExtra("mapGCDX", mapList.get(0));
                        intent.putExtra("mapGCDY", mapList.get(1));
                        setResult(RESULT_OK, intent);
                    } else {
                        Toast.makeText(this, "未能获取经纬度，请重新设置观察点!", Toast.LENGTH_SHORT).show();
                    }
                } else if (code == 21) {//修改观察点
                    if (mapList.size() > 0) {
                        s.updateXYNO(cn, MapRelatedActivity.this, mapList.get(0), mapList.get(1));
                        intent = new Intent(this, ContentListActivity.class);
                        intent.putExtra("mapGCDXG", 21);
                        setResult(RESULT_OK, intent);
                    } else {
                        Toast.makeText(this, "修改失败，请重新修改", Toast.LENGTH_SHORT).show();
                    }

                }
                finish();
                break;
            case R.id.ib_changed:
                if (TextUtils.isEmpty(msPoint())) {
                    LogUtils.e("nullPointException", "Line:94");
                    return;
                }
                setLayerEditable(msPoint());
                m_mapEditView.setEditHandleSize(20);
                Toast.makeText(this, "请选择需要编辑的图斑", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_changed_save:
                if (EditLayer.getEditLayer(mSwitchMapView) == null) {
                    return;
                }
//                export();
                saveLayer();
                Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_changed_undo:
                unDoEdit();
                break;
            default:


        }
    }

    public void export() {
        DataSource dataSource = new DataSource();
        String dsets = msPoint();
        String dsets1 = dsets.substring(0, dsets.length() - 10);
        String dsets2 = dsets.substring(dsets.length() - 9);
        boolean open = dataSource.open("/storage/emulated/0/PIEMapData/projectData/default/config/vector/" + dsets2 + ".gsf");
        String shppath = s.export + "export/";
        File file = new File(shppath);
        if (!file.exists()) {
            file.mkdir();
        }
        Dataset dset = dataSource.getDataset(1);
        boolean bexport = DataExchange.toShpFromGsf(dataSource, dset, shppath + "/" + dsets1 + ".shp");
        if (!bexport) {
//            Toast.makeText(this, "导出shp失败", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "导出shp成功", Toast.LENGTH_SHORT).show();
        }
//        String m_proPath = s.export + "export/";
//        File file = new File(m_proPath);
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        Workspace workspace = MapUtil.getInstace().getWorkspace();
//        if (workspace != null) {
//            int dsourceCount = workspace.getDataSourceCount();
//            if (dsourceCount > 0) {
//                for (int i = 0; i < dsourceCount; i++) {
//                    DataSource dataSource = workspace.getDataSource(i);
//                    String dataSource1 = workspace.getName();
//                    Log.d("shit", "initWork: " + dataSource1);
//                    if (dataSource != null && dataSource.getEngineType() == EngineType.GSF.getValue()) {
//                        m_dataSource = dataSource;
//                    }
//                }
//            }
//        }
//        DataSource dataSource = new DataSource();
//        m_dataSource = dataSource;
//        String dsets = msPoint();
//        String dsets1 = dsets.substring(0, dsets.length() - 10);
//        Dataset dset = (m_dataSource.getDataset(dsets1));
//        String dset = String.valueOf((m_dataSource.getDataset(dsets1)));
//        String shppath = getIntent().getStringExtra("TASKID");
//        boolean bexport = DataExchange.toSHapeFIlefromGSF(m_dataSource, dset, s.sspath + "/" + m_proPath + shppath + ".shp");
//        boolean bexport = DataExchange.toGsfFromShp(s.sspath + "/" + m_proPath + shppath + ".shp",m_dataSource, dset );
//        if (!bexport) {
//            Toast.makeText(this, "导出shp失败", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "导出shp成功", Toast.LENGTH_SHORT).show();
//        }
    }

    public String msPoint() {
//        dataType
        //获取所有的矢量数据
        LayerSet layerSet = mSwitchMapView.getLayerSet(SysConstant.PropertyName.SLTCJ);
        if (layerSet != null) {
            layerNameList = new ArrayList<>();
            //获取矢量数据的条目数
            int layerCount = layerSet.getLayerCount();
            for (int i = 0; i < layerCount; i++) {
                m_editLayer = layerSet.getLayer(i);
                layername = m_editLayer.getName();
                layername1 = layername.substring(0, layername.length() - 10);
                if (layername1.equals(dataType)) {
                    layername = m_editLayer.getName();
                    break;
                } else {
                    layername = "数据暂无";
                }
            }
        } else {
            Toast.makeText(this, "暂无图斑数据", Toast.LENGTH_SHORT).show();
        }

//        m_editLayer = layerSet.getLayer(1);

        //根据条目索引获取Layer；
//        for (int y = 1; y < layerCount-1; y++) {
//            Layer layer = layerSet.getLayer(y);
//            Log.d("tbbh", "msPoint: "+layer.getName());
//            Dataset dataset = layer.getDataset();
//            dataset.open();
//            DatasetVector vector = (DatasetVector) dataset;
//            Log.d("tbbh", "getObjectCount: "+ vector.getObjectCount());
//            for (int i = 0;i<vector.getObjectCount();i++){
//                String sql = " PIEID  = " + (i+1);
//                Recordset recordset = vector.query(sql);
//               String test436 = recordset.getFieldValueString(SysConstant.PropertyName.JYBH);
//                Log.d("tbbh", "msPoint: "+test436);
//            }
//                            String sql = " OBJECTID  = " + (y+1);
//        }
        return layername;
    }

    public void setLayerEditable(String layerName) {
        setLayerEditByName(layerName, true);
        setLayerSelectedByName(layerName, true);
        m_mapEditView.setUserAction(UserAction.EDIT, EditAction.DEFAULT);
        m_mapEditView.setEditHandleSize(20);
        m_mapEditView.setEditMode(EditMode.GEOMETRY);
    }

    /**
     * 撤销
     */
    public void unDoEdit() {
        m_mapEditView.setUserAction(UserAction.EDIT, EditAction.DEFAULT);
        EditLayer.getEditLayer(mSwitchMapView).undo();
        mSwitchMapView.refresh(false, true);
    }

    /**
     * 保存
     */
    public void saveLayer() {
        if (EditLayer.getEditLayer(mSwitchMapView) == null) {
            return;
        }
        m_mapEditView.submitEditObject();
        m_mapEditView.setUserAction(UserAction.EDIT, EditAction.DEFAULT);
        mSwitchMapView.refresh(false, true);
        Layer editor = getEditLayer();
        if (editor != null) {
            setLayoutManyou(msPoint());
        }
//        export();
    }

    public void setLayoutManyou(String layoutManyou) {
        setLayerEditByName(layoutManyou, false);
        setLayerSelectedByName(layoutManyou, false);
        getMapEditView().setUserAction(UserAction.DEFAULT, EditAction.DEFAULT);
    }

    public void setLayerEditByName(String layerName, boolean editable) {
        if (m_editLayer != null) {
            m_editLayer.setEditable(editable);
        }
    }

    public void setLayerSelectedByName(String layerName, boolean selectable) {
        if (m_editLayer != null) {
            m_editLayer.setSelectable(selectable);
        }
    }

    public void deleteGeometry() {
        EditLayer eLayer = EditLayer.getEditLayer(mSwitchMapView);
        DatasetLayer layer = new DatasetLayer(eLayer.getDataSetLayer());
        layer.deleteGeometry();
        mSwitchMapView.refresh(false, true);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String path = Config.getMapResoucePath();
        MapUtil.getInstace().init(this, path);
        super.onCreate(savedInstanceState);
        initProject();
    }

    private void mapAllWork() {
        mapList = new ArrayList<>();
        switch (code) {
            case 20://观察点设置
            case 21://观察点修改
            case 23://图版编辑
                cx = Double.valueOf(getIntent().getStringExtra("CX"));
                cy = Double.valueOf(getIntent().getStringExtra("CY"));
                plaquesNum = getIntent().getStringExtra("PlaquesNum");
                if (code == 20) {
                    mapTitle.setText(plaquesNum + "观察点设置");
                    gcdPoint.setVisibility(View.VISIBLE);
                    gcdSave.setVisibility(View.VISIBLE);
                } else if (code == 21) {
                    cn = getIntent().getStringExtra("CN");
                    mapTitle.setText(plaquesNum + "观察点修改");
                    gcdPoint.setVisibility(View.VISIBLE);
                    gcdSave.setVisibility(View.VISIBLE);
                } else if (code == 23) {
                    dataType = getIntent().getStringExtra("DataType");
                    mapTitle.setText(plaquesNum + "图斑编辑");
                    changeTB.setVisibility(View.VISIBLE);
                }
                break;
            case 22://添加图斑
                mapTitle.setText("添加图斑定位点");
                addtb.setVisibility(View.VISIBLE);
                gcdPoint.setVisibility(View.VISIBLE);
                break;
            case 24://添加图斑
                cx = Double.valueOf(getIntent().getStringExtra("AddMapX"));
                cy = Double.valueOf(getIntent().getStringExtra("AddMapY"));
                mapTitle.setText("任务图斑");
                break;
            default:
        }
    }

    private void initProject() {
        s = new CustomSQLTools();
        setContentView(R.layout.activity_map_related);
        ButterKnife.bind(this);
        code = getIntent().getIntExtra("Code", -1);
        mapAllWork();
        initView();
        sper = getSharedPreferences("User", Context.MODE_PRIVATE);
        m_presenter = new MainViewPresenter(this);
        mSwitchMapView.setMapGestureController(new MapGestureController());
//        LoadingDialogTools.showDialog(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataNum();
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //这里是进行的更新UI操作
//            LoadingDialogTools.dismissDialog();
            setColor();
            openNumber();
            setMap();
        }
    };

    /**
     * 获取任务数据编号
     */
    public void getDataNum() {
        queryIng();
        queryNo();
        queryEnd();
        String prjName = taskID();
        if (!prjName.equals("Null")) {
            SpHelper.setStringValue("ID", prjName);
            openMap(prjName);
        } else {
            if (listEntitiesIng.size() > 0) {
                String taskNumber = listEntitiesIng.get(0).getTaskNumber();
                SpHelper.setStringValue("ID", taskNumber);
                openMap(taskNumber);
            } else if (listEntitiesNo.size() > 0) {
                String taskNumber = listEntitiesNo.get(0).getTaskNumber();
                SpHelper.setStringValue("ID", taskNumber);
                openMap(taskNumber);
            } else if (listEntitiesEnd.size() > 0) {
                String taskNumber = listEntitiesEnd.get(0).getTaskNumber();
                SpHelper.setStringValue("ID", taskNumber);
                openMap(taskNumber);
            } else {
                openMap("T060220180529001");
            }
        }
    }

    private void setColor() {
        List<Layer> layerSets = new ArrayList<>();
        List<Layer> layerSet = new ArrayList<>();
        LayerSet SLData = mSwitchMapView.getLayerSet(SysConstant.PropertyName.SLTCJ);
        LayerSet XGData = mSwitchMapView.getLayerSet(SysConstant.PropertyName.XZQH);
        if (SLData != null) {
            layerCount = SLData.getLayerCount();
        }
        if (XGData != null) {
            layerCount1 = XGData.getLayerCount();
        }

        if (layerCount != 0) {
            for (int i = 0; i < layerCount; i++) {
                layerSets.add(SLData.getLayer(i));
                Log.d("name", "setColor: " + layerSets.get(i).getDataset().getName());
            }
            for (int k = 0; k < layerSets.size(); k++) {
                String name = layerSets.get(k).getName();
                Style style = layerSets.get(k).getStyle();
                String splitData = splitData(name, "矿", "_");
                Log.e("++++++", splitData(name, "矿", "_"));
                if (!TextUtils.isEmpty(splitData)) {
                    if (splitData.equals("权数据_面")) {
                        style.lineColor = Color.rgb(0, 0, 205);
                    } else if (splitData.equals("山地质坏境恢复治理_面")) {
                        style.lineColor = Color.rgb(50, 205, 50);
                    } else if (splitData.equals("山地质灾害_线")) {
                        style.lineColor = Color.rgb(28, 28, 28);
                    } else if (splitData.equals("山地质灾害_面")) {
                        style.lineColor = Color.rgb(255, 0, 255);
                    } else if (splitData.equals("山开发占地_井口_点")) {
                        Dataset dataset1 = layerSets.get(k).getDataset();
                        DatasetVector dataset = (DatasetVector)dataset1;
                        Recordset query = dataset.query(new QueryDef());
                        int recordCount = query.getRecordCount();
                        for (int i = 0; i <recordCount ; i++) {
                            query.moveTo(i);
                            Geometry geometry = query.getGeometry();
                            Point2D center = geometry.getBounds().getCenter();
                            Point2D point2D = MapUtil.getInstace().dtToMap(mSwitchMapView, dataset, center);
                            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_point);
                            image = new ImageView(this);
                            image.setImageBitmap(bitmap);
                            MapLayoutParams pararms = new MapLayoutParams(20, 20, point2D, 1);
                            layerVeiw.add(image);
                            mMapViews.addView(image,1, pararms);
                        }
                    } else if (splitData.equals("山开发占地_面")) {
                        style.lineColor = Color.rgb(255, 255, 0);
                    } else {
                        style.lineColor = Color.rgb(255, 0, 255);
                    }
                }
                style.fillForeColor = Color.argb(0, 0, 0, 0);
                style.lineWidth = 2;
                layerSets.get(k).setStyle(style);
            }
        }

        if (layerCount1 != 0) {
            for (int j = 0; j < layerCount1; j++) {
                layerSet.add(XGData.getLayer(j));
            }
            for (int a = 0; a < layerSet.size(); a++) {

                Style style = layerSet.get(a).getStyle();
                style.fillForeColor = Color.argb(0, 0, 0, 0);
                style.lineColor = Color.rgb(0, 0, 0);
                style.lineWidth = 4;
                layerSet.get(a).setStyle(style);
            }
        }
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

    /**
     * 根据四方位确定加载范围
     */
    private void setMap() {
        LayerSet layerSet = mSwitchMapView.getLayerSet(SysConstant.PropertyName.DTTC);
        if (layerSet != null) {
            int layerCount = layerSet.getLayerCount();
            for (int i = 0; i < layerCount; i++) {
                Layer layer = layerSet.getLayer(i);
                Rect2D bounds = layer.getDataset().getBounds();
                double bottom = bounds.getBottom();
                double top = bounds.getTop();
                double left = bounds.getLeft();
                double right = bounds.getRight();
                mSwitchMapView.setViewBounds(new Rect2D(left, top, right, bottom));
                mSwitchMapView.setScale(mSwitchMapView.getMinScale() * 1100);//设置默认的加载比例尺
                //mSwitchMapView.setMinScale(0.046204291333595E-5);//设置最小缩放比例
            }
        } else {
            ToastUtil.show(this, "请添加瓦片数据");
        }
    }

    /**
     * 验证中数据初始化
     */
    private void queryIng() {
        String sql = Constant.IMPLEMENT;
        listEntitiesIng.clear();
        listEntitiesIng.addAll(s.getAllocated(sql, MapRelatedActivity.this));
    }

    /**
     * 未验证数据初始化
     */
    private void queryNo() {
        String sql = Constant.EXPORT;
        listEntitiesNo.clear();
        listEntitiesNo.addAll(s.getAllocated(sql, MapRelatedActivity.this));
    }

    /**
     * 以验证数据初始化
     */
    private void queryEnd() {
        String sql = Constant.VERIFIED;
        listEntitiesEnd.clear();
        listEntitiesEnd.addAll(s.getAllocated(sql, MapRelatedActivity.this));
    }

    private void initView() {
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
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
        mMapViews = findViewById(R.id.mvs_pie_basic_map);
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

    private int pointx = 1;

    public void zoomCenter(double dex, double dey) {
        double cx = dex;
        double cy = dey;


        Point2D point2D = new Point2D(cx, cy);
        Point2D startPoint = mSwitchMapView.getMapCenter();
        if (startPoint.x > 0) {
            pointx++;
        }
        if (code == 24) {
            if (pointx == 2 || pointx == 3) {
                endPnt = mSwitchMapView.getPrjCoordSys().forward(point2D);
                mSwitchMapView.startPanAnimation(startPoint, endPnt);
                mSwitchMapView.setScale(0.105484291333595E-5);
            }
        } else {
            if (pointx == 2 || pointx == 3) {
                endPnt = mSwitchMapView.getPrjCoordSys().forward(point2D);
                mSwitchMapView.startPanAnimation(startPoint, endPnt);
                mSwitchMapView.setScale(2.105484291333595E-5);
            }
        }


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_most, fragment);
        transaction.commit();

    }

    //定位
    public void getPositioning() {
        //开启定位
        locationToDem("LOCATION");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                getPositioning();
                break;

        }
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
    private void locationToDem(String tag) {
        longitude = Double.parseDouble(SpHelper.getStringValue("LON"));
        latitude = Double.parseDouble(SpHelper.getStringValue("LAT"));
        if (latitude > 0 && longitude > 0) {
            demX = longitude;
            demY = latitude;
        } else {
            Location location = LocationUtil2.getInstance(this).showLocation();
            if (location != null) {
                demX = location.getLongitude();
                demY = location.getLatitude();
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
        mMapViews.addView(image, pararms);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSwitchMapView.closeMap();
        SpHelper.setStringValue("MARK", "mark");
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
        return m_editLayer;
    }


    @Override
    public int saveGpsRecordToDataset(List<Point2D> list) {
        if (list == null) {
            return -1;
        }
        Geometry geometry = mSwitchMapView.getTrackingLayer().getGeometryFromTag(SysConstant.MapConfig.GPS_RECORD_TAG);
        Dataset set = m_editLayer.getDataset();
        if (geometry != null && set != null) {
            int i = MapUtil.getInstace().saveGeometryToDt(geometry, set);
            return i;
        }
        mSwitchMapView.refresh(false, true);
        return -1;
    }

    @Override
    public void setPresenter(MainViewContract.Presenter presenter) {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void onMapClick(Point2D point2D) {

    }

    public String splitData(String str, String strStart, String strEnd) {
        String tempStr;
        tempStr = str.substring(str.indexOf(strStart) + 1, str.lastIndexOf(strEnd));
        return tempStr;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        removeView();
        return super.onKeyDown(keyCode, event);
    }

    private void removeView() {
        if(layerVeiw.size() >= 0){
            for (int i = 0; i < layerVeiw.size() ; i++) {
                mMapViews.removeView(layerVeiw.get(i));
            }
        }
        layerVeiw.clear();
    }
}
