package cn.piesat.minemonitor.mapdata.presenter.contract;

import java.util.List;
import cn.piesat.minemonitor.base.BasePresenter;
import cn.piesat.minemonitor.base.BaseView;
import cn.piesat.minemonitor.mapdata.entity.DtAttrEntity;
import cn.piesat.minemonitor.mapdata.entity.LayerEntity;
import cn.piesat.minemonitor.mapdata.entity.ProjectEntity;
import pie.core.Dataset;
import pie.core.Layer;
import pie.core.LayerSet;
import pie.core.MapEditView;
import pie.core.MapView;
import pie.core.Point2D;
import pie.core.Recordset;

/**
 * Created by Administrator on 2017/5/22.
 */

public interface MainViewContract {

    interface View extends BaseView<Presenter> {

        void switchFragment(String flag);

        MapView getMapView();

        MapEditView getMapEditView();

        void removeGeometryByTag(String name);

        Layer getEditLayer();

        int saveGpsRecordToDataset(List<Point2D> m_gpsPointList);
    }

    interface Presenter extends BasePresenter {

        /**
         * 保存工程名称至缓存
         */
        void saveProjectNameToCache();

        /**
         * 从缓存中获取工程名称
         * @return
         */
        String getProjectNameFromCache();

        /**
         * 切换工程
         * @param project 要打开的工程
         */
        void switchProject(ProjectEntity project);

        LayerSet getLayerSetByName(String layerName);

        List<LayerEntity> getLayersByLayerSet(LayerSet layerSet);

        List<LayerEntity> getLayersByLayerSetName(String layerSetName);

        void setCurrentEditLayer(Layer currentLayer, Layer layer);
        /**
         * 刷新工程
         */
        void refreshCurrentProject();
        void addGpsRecordsToTrackerLayer(List<Point2D> list, String tag);

        List<DtAttrEntity> getAttrsValueByID(Dataset dataset, int id);

        boolean updateAttrsByID(Recordset query, int id, List<DtAttrEntity> listAttr);
    }
}
