package cn.piesat.minemonitor.mapdata.view;

import java.util.List;

import cn.piesat.minemonitor.mapdata.entity.DtAttrEntity;
import pie.core.FieldInfo;
import pie.core.FieldType;
import pie.core.Geometry;

/**
 * Created by admin on 2017/6/6.
 */

public interface DtAttrDataSource {

    List<FieldInfo> getAttr();

    List<DtAttrEntity> getAttrValueById(int id);

    Geometry getGeometryById(int id);

    List<List<DtAttrEntity>> getAttrlistFromDataset();

    boolean updateAttrByEntity(int id, DtAttrEntity dtAttrEntity);

    FieldType getAttrType(int type);
}
