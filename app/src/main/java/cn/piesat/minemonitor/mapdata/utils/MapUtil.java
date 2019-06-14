package cn.piesat.minemonitor.mapdata.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import java.io.File;

import pie.core.DataSource;
import pie.core.Dataset;
import pie.core.DatasetVector;
import pie.core.DimensionMode;
import pie.core.GeoPoint;
import pie.core.Geometry;
import pie.core.GeometryType;
import pie.core.GisNative;
import pie.core.MapPanAnimationListener;
import pie.core.MapView;
import pie.core.Point2D;
import pie.core.PrjCoordSys;
import pie.core.QueryDef;
import pie.core.Recordset;
import pie.core.Rect2D;
import pie.core.SceneMode;
import pie.core.Workspace;

/**
 * 地图帮助类，打开关闭工作空间，地图。<br>
 * 打开地图的步骤：<br>
 * 1.初始化工作空间资源（mapController.initWork(context, initWorkPath)）；<br>
 * 2.打开工作空间（workspace.open(workspacePath)）；<br>
 * 3.关联工作空间（mapController.attachWorkspace(Workspace)）；<br>
 * 4.打开地图，前提是工作空间打开成功（mapController.openBasemapFromWorkspace(mapName)）。<br>
 * 注意：地图可多次打开关闭，工作空间最好只打开一次，当程序退出时再关闭。
 *
 * @author YueShanShan
 */
public class MapUtil {

    private static final class MapInitHelperlHandle {

        private static final MapUtil NEWINSTANC = new MapUtil();
    }

    public static MapUtil getInstace() {
        return MapInitHelperlHandle.NEWINSTANC;
    }

    public interface MapChangeListener {

        void onMapChange();
    }

    // 工作空间
    private Workspace m_workspace;
    // 要打开的地图的名称
    private boolean m_isOpenWorkspace;
    private final String TAG = "MapUtil";
    private MapChangeListener m_mapChangeListener;
    private boolean isPan = false;

    public void setMapChangeListener(MapChangeListener mapChangeListener) {
        this.m_mapChangeListener = mapChangeListener;
    }

    /**
     * 初始化资源
     */
    private boolean initMapRes(Context context, String path) {

        // 资源文件路径
        String root = path;
        File file = new File(root);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        // 初始化工作空间资源
        GisNative.init(context, root);
        return true;
    }

    /**
     * 打开工作空间 工作空间不能重复打开。
     *
     * @return true表示工作空间打开成功，false表示工作空间打开失败。
     */
    public boolean openWorkspace(String path) {
        String workspacePath = path;
        File file = new File(workspacePath);
        if (!file.exists() || !file.isFile()) {
            return false;
        }
//        if ((m_workspace != null && m_workspace.getHandle() != 0) || m_workspace == null) {
//            m_workspace = new Workspace();
//        }
        if (m_workspace == null || m_workspace.getHandle() ==0){
            m_workspace = new Workspace();
        }
        else{
            m_workspace.close();
            m_workspace = new Workspace();
        }
        m_isOpenWorkspace = m_workspace.open(workspacePath);
        return m_isOpenWorkspace;
    }



    // 在地图控件初始化之前调用
    public boolean init(Context context, String path) {
        return initMapRes(context, path);
    }

    /**
     * 打开地图
     *
     * @return true表示地图打开成功，false表示地图打开失败。
     */
    public boolean openMap(MapView mapView, String mapName) {
        if (!m_isOpenWorkspace || m_workspace == null || m_workspace.getHandle() == 0) {
//			openWorkspace();
            return false;
        }
        if (m_isOpenWorkspace) {
            // 设置平面或3D模式一定要在打开地图之前，否则会出问题。
            mapView.setDimensionMode(SysConstant.MapConfig.defalutDimensionMode);
            mapView.setSceneMode(SysConstant.MapConfig.defalutSceneMode);
            mapView.attachWorkspace(m_workspace);
            boolean isOpen = mapView.openMap(mapName);
            mapView.setAutoProjection(true);
            mapView.setMapBkgColor(SysConstant.MapConfig.defalutMapBkgColor);
            mapView.setRotate(SysConstant.MapConfig.defalutRotateStatus);
            mapView.setPitch(SysConstant.MapConfig.defalutPitchStatus);
            Point2D point = SysConstant.MapConfig.defaultPoint;
            point = mapView.getPrjCoordSys().latLngToProjection(point);
            if (mapView.getViewBounds().contains(point)) {
                mapView.setMapCenter(point);// 三维球暂时有bug,必须设置两次地图中心点才起作用
            }
            mapView.setRollAngle(0);
//			initScale(mapView, SysConstant.MapConfig.defaultScale);
//			mapView.viewEntire();
            return isOpen;
        }
        return false;
    }

    /**
     * 切换地图。
     *
     * @param mapView 地图。
     * @param mapName 要打开的地图名称。
     */
    public void switchMap(MapView mapView, String mapName) {
        // 切换地图前，先记录下地图的比例尺、中心点、俯仰角和旋转角等信息
        Point2D center = mapView.getMapCenter();
        center = mapView.getPrjCoordSys().projectionTolatLng(center);
        double scale = mapView.getScale();
        double angle = mapView.getRollAngle();
        double pitchAngle = mapView.getPitchAngle();
        switchMap(mapView, mapName, center, scale, angle, pitchAngle, null, null);
    }

    public void switchMap(MapView mapView, String mapName, double angle, double pitchAngle, DimensionMode mode, SceneMode sceneMode) {
        Point2D center = mapView.getMapCenter();
        center = mapView.getPrjCoordSys().projectionTolatLng(center);
        double scale = mapView.getScale();
        switchMap(mapView, mapName, center, scale, angle, pitchAngle, mode, sceneMode);
    }

    public void switchMap(MapView mapView, String mapName, DimensionMode mode, SceneMode sceneMode) {
        Point2D center = mapView.getMapCenter();
        center = mapView.getPrjCoordSys().projectionTolatLng(center);
        double scale = mapView.getScale();
        double angle = mapView.getRollAngle();
        double pitchAngle = mapView.getPitchAngle();
        switchMap(mapView, mapName, center, scale, angle, pitchAngle, mode, sceneMode);
    }

    /**
     * 切换模式，二维和三维。
     *
     * @param mapView   地图。
     * @param mode      二三维模式。
     * @param sceneMode 平面、球面模式。
     */
    public void switchMap(MapView mapView, DimensionMode mode, SceneMode sceneMode) {
        // 切换模式前，先记录下地图的名称、比例尺、中心点、俯仰角和旋转角等信息
        String mapName = mapView.getName();
        Point2D center = mapView.getMapCenter();
        center = mapView.getPrjCoordSys().projectionTolatLng(center);
        double scale = mapView.getScale();
        double angle = mapView.getRollAngle();
        double pitchAngle = mapView.getPitchAngle();
        switchMap(mapView, mapName, center, scale, angle, pitchAngle, mode, sceneMode);
    }

    public void switchMap(MapView mapView, String mapName, Point2D center, double scale, double angle, double pitchAngle, DimensionMode mode, SceneMode sceneMode) {
        // 切换模式前，先记录下地图的名称、比例尺、中心点、俯仰角和旋转角等信息
        if (TextUtils.isEmpty(mapName)) {
            mapName = mapView.getName();
        }
        if (center == null) {
            center = mapView.getMapCenter();
        } else {
            center = mapView.getPrjCoordSys().latLngToProjection(center);
        }
        if (scale < 0) {
            scale = 0;
        }
        if (angle < 0) {
            angle = 0;
        }
        if (pitchAngle < 0) {
            pitchAngle = 0;
        }
        if (mode == null) {
            mode = mapView.getDimensionMode();
        }
        if (sceneMode == null) {
            sceneMode = mapView.getSceneMode();
        }
        // 先关闭地图再打开地图
        mapView.closeMap();

        // 设置地图模式
        mapView.setDimensionMode(mode);
        mapView.setSceneMode(sceneMode);

        // 打开地图
        mapView.openMap(mapName);

        // 设置可旋转可俯仰
        mapView.setAutoProjection(true);
        // mapView.setRotate(true);

        mapView.setMapCenter(center);
        mapView.refreshCamera();
        // 再将地图的比例尺、中心点、俯仰角和旋转角等信息设置到切换的地图
        mapView.setScale(scale);
        mapView.refreshCamera();
        mapView.setRollAngle(angle);
        mapView.refreshCamera();
        if (mode != null) {
            switch (mode) {
                case D2DMode:
                    // 设置背景颜色
                    mapView.setMapBkgColor(Color.WHITE);
                    mapView.setPitchAngle(pitchAngle);// xumw
                    break;
                case D3DMode:
                    // 设置背景颜色
                    mapView.setMapBkgColor(Color.BLACK);
                    mapView.setPitch(true);
                    mapView.setPitchAngle(pitchAngle);
                    break;
                default:
                    // 设置背景颜色
                    mapView.setMapBkgColor(Color.BLACK);
                    mapView.setPitchAngle(pitchAngle);
                    break;
            }
        }

        mapView.refreshCamera();

        mapView.setIsSelectedGeometry(true);
        mapView.setSelected(true);
        setMinZoom(mapView, SysConstant.MapConfig.defaultMinScale);
        if (m_mapChangeListener != null) {
            m_mapChangeListener.onMapChange();
        }
    }

    public void closeWorkspace() {
        if (m_workspace != null && m_workspace.getHandle() != 0) {
            m_isOpenWorkspace = false;
            m_workspace.close();
        }
    }

    public Workspace getWorkspace() {
        if (m_workspace == null || m_workspace.getHandle() == 0) {
            // initMapRes();
//			openWorkspace();
        }
        return m_workspace;
    }

    public void setMinZoom(MapView mapView, int minLevel) {
        if (mapView != null) {
            double[] scaleLevels = mapView.getScaleLevels();
            if (scaleLevels != null && mapView != null) {
                mapView.setMinScale(scaleLevels[minLevel]);
            }
        }
    }

    /**
     * 获取数据集对象 dsName 数据源名称 dtName 数据集名称
     **/
    public Dataset getDataset(String dsName, String dtName) {
        Dataset dt = null;
        DataSource ds = getWorkspace().getDataSource(dsName);
        if (ds != null) {
            dt = ds.getDataset(dtName);
        }
        return dt;
    }

    public int saveGeometryToDt(Geometry geometry, Dataset set) {
        int i = -1;
        if (geometry == null || set == null) {
            return i;
        }
        DatasetVector dVector = (DatasetVector) set;
        Recordset recordset = dVector.query(new QueryDef());
        if (recordset == null) {
            return i;
        }
        recordset.edit();
        i = recordset.addNew(geometry);
        recordset.update();
        dVector.releaseRecordset(recordset);
        return i;
    }

    public Dataset getDataset(String dsName) {
        Dataset dt = null;
        DataSource ds = getWorkspace().getDataSource(dsName);
        if (ds != null) {
            dt = ds.getDataset(0);
        }
        return dt;
    }

    public Point2D mapToDt(MapView mapview, String dsName, Point2D mapPnt) {
        Dataset dt = getDataset(dsName);
        // SysConstant.SEARCH_QUANGUO_COUNTRY.DATASOURCE_NAME,
        // SysConstant.SEARCH_QUANGUO_COUNTRY.DATASET_TOWN_NAME);
        if (dt != null) {
            // 把几何对象从地图坐标转换为数据集坐标系
            PrjCoordSys projSysDataSet = dt.getPrjCoordSys();
            // 设置 数据集的投影为源投影
            // PrjCoordSys iCoodPrj = mapview.getPriCoordSys();
            mapview.setProjCoordSysSrc(projSysDataSet);
            GeoPoint geoPoint = new GeoPoint();
            geoPoint.make(mapPnt.x, mapPnt.y);
            // false 表示 从目标投影转到 源投影
            mapview.prjConvert(geoPoint, false);
            Point2D lonLat = geoPoint.getPoint();
            return lonLat;
        }
        return null;
    }

    public Rect2D dtToMap(MapView mapview, String dsName, Rect2D rect) {
        if (rect == null) {
            return null;
        }
        Dataset dt = getDataset(dsName);
        return dtToMap(mapview, dt, rect);
    }

    public Geometry mapToDt(MapView mapview, Dataset dt, Geometry geometry) {
        if (dt != null) {
            // 把几何对象从地图坐标转换为数据集坐标系
            PrjCoordSys projSysDataSet = dt.getPrjCoordSys();
            // 设置 数据集的投影为源投影
            mapview.setProjCoordSysSrc(projSysDataSet);
            // false 表示 从目标投影转到 源投影
            mapview.prjConvert(geometry, false);
            return geometry;
        }
        return null;
    }

    public Rect2D dtToMap(MapView mapview, Dataset dt, Rect2D rect) {
        if (rect == null) {
            return null;
        }
        Point2D leftTop = new Point2D(rect.left, rect.top);
        Point2D rightBottom = new Point2D(rect.right, rect.bottom);

        Point2D leftTopPrj = dtToMap(mapview, dt, leftTop);
        Point2D rightBottomPrj = dtToMap(mapview, dt, rightBottom);
        return new Rect2D(leftTopPrj.x, leftTopPrj.y, rightBottomPrj.x, rightBottomPrj.y);
    }

    public Point2D dtToMap(MapView mapview, String dsName, Point2D point) {
        if (point == null) {
            return null;
        }
        Dataset dt = getDataset(dsName);
        return dtToMap(mapview, dt, point);
    }

    public Point2D dtToMap(MapView mapview, Dataset dt, Point2D point) {
        if (point == null) {
            return null;
        }
        GeoPoint geoPoint = new GeoPoint(point.x, point.y);
        Geometry temp = dtToMap(mapview, dt, geoPoint);
        if (temp != null && temp.getType() == GeometryType.GeoPoint) {
            GeoPoint result = (GeoPoint) temp;
            Point2D point2d = result.getPoint();
            return point2d;
        }
        return null;
    }

    public Geometry dtToMap(MapView mapview, String dsName, Geometry geometry) {
        Dataset dt = getDataset(dsName);
        return dtToMap(mapview, dt, geometry);
    }

    public Geometry dtToMap(MapView mapview, Dataset dt, Geometry geometry) {
        if (dt != null) {
            // 把几何对象从地图坐标转换为数据集坐标系
            PrjCoordSys projSysDataSet = dt.getPrjCoordSys();
            // 设置 数据集的投影为源投影
            mapview.setProjCoordSysSrc(projSysDataSet);
            // false 表示 从目标投影转到 源投影
            mapview.prjConvert(geometry, true);
            return geometry;
        }
        return null;
    }

    /**
     * 移动地图到某一个中心点（不建议用该方法）。
     *
     * @param mapView  地图。
     * @param toCenter 目的地点。
     * @param scale    具体的比例尺。
     */
    public void moveToCenter(final MapView mapView, Point2D toCenter, final double scale) {
        Point2D startPnt = mapView.getMapCenter();
        Point2D endPnt = mapView.getPrjCoordSys().projectionTolatLng(toCenter);
        mapView.startPanAnimation(startPnt, endPnt);
        mapView.setMapPanAnimationListener(new MapPanAnimationListener() {

            @Override
            public void preAnimation() {

            }

            @Override
            public void postAnimation() {
                mapView.startZoomAnimation(scale);
            }
        });
    }

    /**
     * 移动地图到某一个中心点。
     *
     * @param mapView  地图。
     * @param toCenter 目的地点。
     * @param zoom     级别，是指切片缓存的数据级别。
     */
    public void moveToCenter(final MapView mapView, Point2D toCenter, final int zoom) {
        Point2D startPnt = mapView.getMapCenter();
        Point2D endPnt = mapView.getPrjCoordSys().projectionTolatLng(toCenter);
        isPan = true;
        mapView.startPanAnimation(startPnt, endPnt);
        mapView.setMapPanAnimationListener(new MapPanAnimationListener() {

            @Override
            public void preAnimation() {

            }

            @Override
            public void postAnimation() {
                if (isPan) {
                    double[] scales = mapView.getScaleLevels();
                    int count = scales.length;
                    if (count == 0) {
                        return;
                    }
                    double newScale = scales[count - 1];
                    if (count >= zoom) {
                        newScale = scales[zoom];
                    }
                    mapView.startZoomAnimation(newScale);
                    isPan = false;
                }
            }
        });
    }

    /**
     * 移动地图到某一个中心点。
     *
     * @param mapView  地图。
     * @param toCenter 目的地点。
     */
    public void moveToCenter(final MapView mapView, Point2D toCenter) {
        Point2D startPnt = mapView.getMapCenter();
        Point2D endPnt = mapView.getPrjCoordSys().latLngToProjection(toCenter);
        if (Math.abs(startPnt.x - endPnt.x) > 1 || Math.abs(startPnt.y - endPnt.y) > 1) {
            mapView.startPanAnimation(startPnt, endPnt);
        }
    }

    /**
     * 指北针复位
     */
    public void resetCompass(MapView mapview) {
        int angle = (int) mapview.getRollAngle();
        int currAngle = (int) mapview.getRollAngle();
        if (currAngle > 180) {
            mapview.startRotateAnimation(angle, 360);
        } else {
            mapview.startRotateAnimation(angle, 0);
        }
    }

}
