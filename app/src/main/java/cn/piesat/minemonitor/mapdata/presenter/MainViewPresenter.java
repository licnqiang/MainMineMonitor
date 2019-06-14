package cn.piesat.minemonitor.mapdata.presenter;

import android.graphics.Color;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.mapdata.entity.DtAttrEntity;
import cn.piesat.minemonitor.mapdata.entity.LayerEntity;
import cn.piesat.minemonitor.mapdata.entity.ProjectEntity;
import cn.piesat.minemonitor.mapdata.mode.DtAttrRepository;
import cn.piesat.minemonitor.mapdata.mode.ProjectRepository;
import cn.piesat.minemonitor.mapdata.presenter.contract.MainViewContract;
import cn.piesat.minemonitor.mapdata.utils.Config;
import cn.piesat.minemonitor.mapdata.utils.FileUtils;
import cn.piesat.minemonitor.mapdata.utils.MapUtil;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;
import cn.piesat.minemonitor.mapdata.utils.SysConstant;
import cn.piesat.minemonitor.mapdata.view.LayerConstants;
import pie.core.DataSource;
import pie.core.Dataset;
import pie.core.EngineType;
import pie.core.FieldType;
import pie.core.GeoLine;
import pie.core.GeoPoint;
import pie.core.GeoRegion;
import pie.core.Layer;
import pie.core.LayerSet;
import pie.core.MapView;
import pie.core.Point2D;
import pie.core.PrjCoordSys;
import pie.core.Recordset;
import pie.core.Rect2D;
import pie.core.Style;
import pie.core.Workspace;

import static cn.piesat.minemonitor.mapdata.utils.MapUtil.getInstace;


public class MainViewPresenter implements MainViewContract.Presenter {

    private final String KEY_PROJECT_NAME = "project_name";
    private ProjectRepository m_projectMode;
    private MainViewContract.View m_view;
    private ProjectEntity m_project;

    public ProjectEntity getProject() {
        return m_project;
    }


    public MainViewPresenter(MainViewContract.View view) {
        this.m_view = view;
        m_projectMode = new ProjectRepository(Config.getProjectPath());
    }


    @Override
    public void start() {
        //openMap();
    }

    public void start(String number) {
        openMap(number);
    }


    private void openMap(String number) {
        //如果无历史工作空间记录，则打开第一个工程
        List<ProjectEntity> projects = m_projectMode.getProjects();
        if (projects != null && projects.size() > 0) {
            //如果有缓存记录则打开上一次的工作空间，否则打开第一个工程
            ProjectEntity entity = projects.get(0);
            String projectName = getProjectNameFromCache();
            if (projectName != null && projectName.length() > 0) {
                for (ProjectEntity project : projects) {
                    if (project.name.equals(projectName)) {
                        entity = project;
                        openProject(entity, number);
                        break;
                    }
                }
            } else {
                openProject(entity, number);
            }
        } else {
            //如果无工程，则提示拷贝数据并退出程序
        }
    }

    /**
     * 打开工程
     * 创建人：YuanYuhua
     * 创建时间：2017/6/22
     * 修改人：
     * 修改时间：
     * 修改备注：
     *
     * @param entity 要打开的工程对象
     */
    private void openProject(ProjectEntity entity, String number) {
        if (entity == null) {
            return;
        }
        String path = Config.getProjectPath() + "/default/config";
//        deleteAllFiles(new File(path));
        m_project = entity;
        MapView mapView = m_view.getMapView();
        //获取workspace路径
        String workspacePath = m_projectMode.getWorkspacePathByProject(entity);
        File file = new File(workspacePath);
        if (file.exists()) {
//            MapUtil.getInstace().closeWorkspace();
            MapUtil.getInstace().openWorkspace(workspacePath);
            //添加工程中的DataSource至workspace
            if (MapUtil.getInstace().getWorkspace().getMapCount() != 0) {
                boolean isOpen = MapUtil.getInstace().openMap(mapView, MapUtil.getInstace().getWorkspace().getMapNameAt(0));
                Log.d("", "");
            }
            String vectorRootPath = entity.path + "/config/vector";
            String vectorFilePath = vectorRootPath + "/" + number.substring(number.length() - 3, number.length()) + "vector.gsf";
            File file1 = new File(vectorFilePath);
            if (!file1.exists() || !file1.isFile()) {
                //添加工程中的DataSource至workspace
                List<DataSource> dataSources = m_projectMode.getDataSourcesByProject(entity, number);
                //添加Dataset至MapView
                addLayersToMap(dataSources);
                //保存地图，保存工作空间
                mapView.save();
                getInstace().getWorkspace().save();
            } else {
                DataSource vectorDs = new DataSource();
                vectorDs.open(vectorFilePath);
            }

        } else {
            //无workspace，则拷贝配置config文件夹至工程目录下
            FileUtils.copyFolder(Config.getConfgiPath(), entity.path + "/config");
            //打开工作空间
//            MapUtil.getInstace().closeWorkspace();
            getInstace().openWorkspace(workspacePath);
            if (getInstace().getWorkspace().getMapCount() != 0) {
                getInstace().openMap(mapView, getInstace().getWorkspace().getMapNameAt(0));
            }
            //添加工程中的DataSource至workspace
            List<DataSource> dataSources = m_projectMode.getDataSourcesByProject(entity, number);
            //添加Dataset至MapView
            addLayersToMap(dataSources);
            //保存地图，保存工作空间
            mapView.save();
            getInstace().getWorkspace().save();
        }

    }

    /**
     * 添加数据集到图层
     * 创建人：YuanYuhua
     * 创建时间：2017/6/22
     * 修改人：YuanYuhua
     * 修改时间：2017/6/22
     * 修改备注：添加到地图上增加分组
     *
     * @param dataSources 所有的数据源
     */
    public void addLayersToMap(List<DataSource> dataSources) {
        if (dataSources == null) {
            return;
        }
        //添加图层的顺序，底图、影像、矢量
        //第一个影像DataSource为底图
        //剩余所有影像DataSource添加到影像图层集
        //所有的矢量DataSource添加到矢量图层集
        boolean isBaseMap = true;
        ArrayList<DataSource> objects = new ArrayList<>();
        ArrayList<DataSource> objects1 = new ArrayList<>();
        ArrayList<Dataset> objects2 = new ArrayList<>();
        for (int i = 0; i < dataSources.size(); i++) {
            DataSource ds = dataSources.get(i);
            String dsName = ds.getName();
            Log.e("-------", "----dsName-----" + dsName);
            if (null != dsName && !dsName.isEmpty()) {
                dsName = dsName.substring(dsName.lastIndexOf("/") + 1, dsName.lastIndexOf("."));
            }
            getInstace().getWorkspace().addDataSource(ds);
            int type = ds.getEngineType();
            if (type == 5) {
                objects.add(ds);
            }
            if (type == 219) {
                objects1.add(ds);
            }
            if (type == EngineType.Tile.getValue()) {
                Dataset imgeDt = ds.getDataset(0);
                String dtName = imgeDt.getName();
                String layerName = dtName + "+@" + dsName;
                String layerSetName = null;
                String groupName = null;
                if (isBaseMap) {
                    layerSetName = SysConstant.PropertyName.DTTC;
                    groupName = "BaseMapGroup";
                    isBaseMap = false;
                    //避免刷新工程时添加重复的Layer
                }
                addLayerToMap(imgeDt, layerSetName, layerName, groupName);
            }
        }

        if (objects == null) {
            return;
        }

        for (int i = 0; i < objects.size(); i++) {
            DataSource ds = objects.get(i);
            String dsName = ds.getName();
            try {
                dsName = dsName.substring(dsName.lastIndexOf("/") + 1, dsName.lastIndexOf("."));
            } catch (Exception e) {
                e.printStackTrace();
            }
            getInstace().getWorkspace().addDataSource(ds);
            int type = ds.getEngineType();
            if (type == EngineType.Plugin.getValue()) {
                Dataset imgeDt = ds.getDataset(0);
                String dtName = imgeDt.getName();
                String layerName = dtName + "@" + dsName;
                String layerSetName;
                String groupName;

                layerSetName = SysConstant.PropertyName.YXTC;
                groupName = "ImageGroup";
                addLayerToMap(imgeDt, layerSetName, layerName, groupName);
            }
        }

        if (objects1 == null) {
            return;
        }

        for (int i = 0; i < objects1.size(); i++) {
            DataSource ds = objects1.get(i);
            String dsName = ds.getName();
            try {
                dsName = dsName.substring(dsName.lastIndexOf("/") + 1, dsName.lastIndexOf("."));
            } catch (Exception e) {
                e.printStackTrace();
            }
            getInstace().getWorkspace().addDataSource(ds);
            int count = ds.getDatasetCount();
            for (int j = 0; j < count; j++) {
                Dataset dt = ds.getDataset(j);
                String dtName = dt.getName();
                if (dtName.substring(0, 1).equals("T")) {
                    objects2.add(dt);
                } else {
                    String layerName = dtName + "@" + dsName;
                    String layerSetName = SysConstant.PropertyName.SLTCJ;
                    String groupName = "VectorGroup";
                    //避免刷新工程时添加重复的Layer
                    addLayerToMap(dt, layerSetName, layerName, groupName);
                }
            }
        }

        if (objects2 == null) {
            return;
        }
        for (int i = 0; i < objects2.size(); i++) {
            Dataset dt = objects2.get(i);
            String dtName = dt.getName();
            String layerName = dtName + "@" + dt;
            String layerSetName = SysConstant.PropertyName.XZQH;
            String groupName = "Vector";
            //避免刷新工程时添加重复的Layer
            addLayerToMap(dt, layerSetName, layerName, groupName);
        }
    }

    /**
     * 添加图层至地图
     * 创建人：YuanYuhua
     * 创建时间：2017/6/22
     * 修改人：
     * 修改时间：
     * 修改备注：
     *
     * @param dt           数据集
     * @param layerSetName 图层集名称
     * @param layerName    图层名称
     * @param groupName    图层组名称
     */
    public void addLayerToMap(Dataset dt, String layerSetName, String layerName, String groupName) {
        //避免刷新工程时添加重复的Layer
        LayerSet layerSet = m_view.getMapView().getLayerSet(layerSetName);
        Layer layer = null;
        if (layerSet != null) {
            layer = layerSet.getLayer(layerName);
        }
        if (layer == null || layer.getHandle() == 0) {
            m_view.getMapView().addLayer(dt, layerSetName, groupName);
//            m_view.getMapView().addLayer(dt, layerSetName);
        }
    }


    @Override
    public void saveProjectNameToCache() {
        if (m_project != null) {
            SpHelper.setStringValue(KEY_PROJECT_NAME, m_project.name);
        }
    }

    @Override
    public String getProjectNameFromCache() {
        return SpHelper.getStringValue(KEY_PROJECT_NAME);
    }

    @Override
    public LayerSet getLayerSetByName(String layerName) {
        return m_view.getMapView().getLayerSet(layerName);
    }

    @Override
    public List<LayerEntity> getLayersByLayerSet(LayerSet layerSet) {
        List<LayerEntity> list;
        if (layerSet != null && layerSet.getLayerCount() > 0) {
            list = new ArrayList<>();
            int layerCount = layerSet.getLayerCount();
            for (int i = 0; i < layerCount; i++) {
                Layer layer = layerSet.getLayer(i);
                LayerEntity entity = new LayerEntity(layer.getName(), layer.isVisible(), layer.getDataset().getType());
                list.add(entity);
            }
            return list;
        }
        return null;
    }

    @Override
    public List<LayerEntity> getLayersByLayerSetName(String layerSetName) {
        LayerSet layerSetByName = getLayerSetByName(layerSetName);
        List<LayerEntity> layersByLayerSet = getLayersByLayerSet(layerSetByName);
        return layersByLayerSet;
    }

    @Override
    public void setCurrentEditLayer(Layer currentLayer, Layer layer) {
        boolean isSelected = false;
        boolean isEdited = false;
        if (currentLayer != null) {
            cleanEditedLayerSelection(currentLayer);
            isSelected = currentLayer.isSelectable();
            isEdited = currentLayer.isEditable();
        }
        if (layer == null) {
            return;
        }
        currentLayer = layer;
        currentLayer.setSelectable(isSelected);
        currentLayer.setEditable(isEdited);
        Rect2D rect = layer.getDataset().getBounds();
        rect = getInstace().dtToMap(m_view.getMapView(), layer.getDataset(), rect);
        Rect2D mapBounds = m_view.getMapView().getViewBounds();
        if (!rect.contains(mapBounds)) {
            m_view.getMapView().setViewBounds(rect);
            m_view.getMapEditView().submitEditObject();
        }
    }

    @Override
    public void refreshCurrentProject() {
        ProjectEntity entity = getProject();
        if (entity == null) {
            return;
        }
        List<DataSource> dataSourceCurrent = new ArrayList<>();
        Workspace wk = getInstace().getWorkspace();
        if (wk == null) {
            return;
        }
        int count = wk.getDataSourceCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                DataSource ds = wk.getDataSource(i);
                dataSourceCurrent.add(ds);
            }
        }
        List<DataSource> dataSourcesNew = m_projectMode.getDataSourcesByProject(entity);
        if (dataSourcesNew != null) {
            dataSourceCurrent.removeAll(dataSourcesNew);
            dataSourceCurrent.addAll(dataSourcesNew);
            //添加Dataset至MapView
            addLayersToMap(dataSourceCurrent);
            //保存地图，保存工作空间
            m_view.getMapView().save();
            wk.save();
        }

    }

    @Override
    public void addGpsRecordsToTrackerLayer(List<Point2D> list, String tag) {
        m_view.removeGeometryByTag(tag);
        int type = m_view.getEditLayer().getLayerType();
        Point2D[] points = new Point2D[list.size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = list.get(i);
        }
        Style style = new Style();
        style.markerSize = 15;
        style.markerWidth = 15;
        style.markerHeight = 15;
        style.fillForeColor = Color.GREEN;
        style.markerType = Style.MarkerType_Null;
        style.lineColor = Color.GREEN;
        style.lineWidth = 6;
        if (type == LayerConstants.LayerType.LAYER_POINT) {
            for (int i = 0; i < points.length; i++) {
                GeoPoint geoPoint = new GeoPoint();
                geoPoint.make(points[i].x, points[i].y);
                geoPoint.setStyle(style);
                m_view.getMapView().getTrackingLayer().addGeometry(tag, geoPoint);
            }
        } else if (type == LayerConstants.LayerType.LAYER_LINE) {
            GeoLine geoLine = new GeoLine();
            geoLine.make(points);
            geoLine.setStyle(style);
            m_view.getMapView().getTrackingLayer().addGeometry(tag, geoLine);
        } else {
            GeoRegion geoRegion = new GeoRegion();
            geoRegion.make(points);
            geoRegion.setStyle(style);
            m_view.getMapView().getTrackingLayer().addGeometry(tag, geoRegion);
        }
    }

    /**
     * 获取属性值
     *
     * @param dataset
     * @param id
     * @return
     */
    @Override
    public List<DtAttrEntity> getAttrsValueByID(Dataset dataset, int id) {
        if (dataset == null || id <= 0) {
            return null;
        }
        DtAttrRepository repository = new DtAttrRepository(dataset);
        List<DtAttrEntity> list = repository.getAttrValueById(id);
        return list;
    }

    /**
     * 更新属性
     *
     * @param
     * @param id
     * @param listAttr
     * @return
     */
    @Override
    public boolean updateAttrsByID(Recordset query, int id, List<DtAttrEntity> listAttr) {
        boolean isUpdate = false;
        if (listAttr == null || listAttr.size() == 0) {
            return isUpdate;
        }
        if (id <= 0) {
            return isUpdate;
        }
        for (int j = 0; j < listAttr.size(); j++) {
            DtAttrEntity dtAttrEntity = listAttr.get(j);
            switch (FieldType.valueOf(dtAttrEntity.fieldInfo.type)) {
                case UNKNOWN:
                    break;
                case BOOLEAN:
                    boolean b;
                    try {
                        b = Boolean.getBoolean(dtAttrEntity.object.toString());
                    } catch (Exception e) {
                        b = false;
                    }
                    isUpdate = query.setFieldValueBool(dtAttrEntity.fieldInfo.foreignName, b);
                    break;
                case BYTE:
                case BINARY:
                case TEXT:
                case CHAR:
                    isUpdate = query.setFieldValueString(dtAttrEntity.fieldInfo.foreignName, dtAttrEntity.object.toString());
                    break;
                case INT16:
                case INT32:
                case INT64:
                    int i;
                    try {
                        i = Integer.valueOf(dtAttrEntity.object.toString());
                    } catch (Exception e) {
                        i = -1;
                    }
                    isUpdate = query.setFieldValueInt(dtAttrEntity.fieldInfo.foreignName, i);

                    break;
                case FLOAT:
                    float f;
                    try {
                        f = Float.valueOf(dtAttrEntity.object.toString());
                    } catch (Exception e) {
                        f = -1;
                    }
                    isUpdate = query.setFieldValueFloat(dtAttrEntity.fieldInfo.foreignName, f);
                    break;
                case DOUBLE:
                    double d;
                    try {
                        d = Double.valueOf(dtAttrEntity.object.toString());
                    } catch (Exception e) {
                        d = -1;
                    }
                    isUpdate = query.setFieldValueDouble(dtAttrEntity.fieldInfo.foreignName, d);
                    break;
                case DATE:
                    break;
                case LONGBINARY:
                    break;
                case TIME:
                case TIMESTAMP:
                case NTEXT:
                case GEOMETRY:
                    break;
                default:
                    isUpdate = false;
                    break;

            }
        }
//        query.update();
//        dv.releaseRecordset(query);
        return isUpdate;
    }

    @Override
    public void switchProject(ProjectEntity project) {
        if (project == null) {
            return;
        }
        Log.d("handle", "0pre handle:" + getInstace().getWorkspace().getHandle());
        //首先保存地图何工作空间
        m_view.getMapView().save();
        getInstace().getWorkspace().save();
        //关闭工作空间和地图
        m_view.getMapView().closeMap();
        getInstace().closeWorkspace();
        //打开新的工程
        openProject(project, "");
        Log.d("handle", "next handle:" + getInstace().getWorkspace().getHandle());
    }

    public void cleanEditedLayerSelection(Layer layer) {
        if (layer != null) {
            layer.getSelection().removeAll();
        }
    }


    static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

}
