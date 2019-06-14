package cn.piesat.minemonitor.mapdata.mode;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.mapdata.entity.DtAttrEntity;
import cn.piesat.minemonitor.mapdata.view.DtAttrDataSource;
import pie.core.Dataset;
import pie.core.DatasetVector;
import pie.core.FieldInfo;
import pie.core.FieldType;
import pie.core.Geometry;
import pie.core.QueryDef;
import pie.core.Recordset;

/**
 * Created by admin on 2017/6/6.
 */

public class DtAttrRepository implements DtAttrDataSource {
    private Dataset m_dataset;

    public DtAttrRepository(Dataset dataset) {
        this.m_dataset = dataset;
    }

    @Override
    public List<FieldInfo> getAttr() {
        if (m_dataset == null || m_dataset.getHandle() == 0) {
            return null;
        }
        DatasetVector dv = (DatasetVector) m_dataset;
        int count = dv.getFieldCount();
        List<FieldInfo> fields = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FieldInfo info = dv.getFieldInfoAt(i);
            //过滤系统字段
            if (info.fieldName.startsWith("PIE")) {
                continue;
            }
            fields.add(info);
        }
        return fields;
    }

    @Override
    public List<DtAttrEntity> getAttrValueById(int id) {
        if (m_dataset == null || m_dataset.getHandle() == 0) {
            return null;
        }
        List<DtAttrEntity> list;
        DatasetVector dv = (DatasetVector) m_dataset;
        Recordset rd = dv.query(" PIEID = " + id);
        if (rd != null) {
            list = new ArrayList<>();
            int count = rd.getFieldCount();
            for (int i = 0; i < count; i++) {
                FieldInfo info = rd.getFieldInfoAt(i);
                //过滤系统字段
                if (info.fieldName.startsWith("PIE")) {
                    continue;
                }

                //获取字段值
                switch (FieldType.valueOf(info.type)) {
                    case UNKNOWN:
                        break;
                    case BOOLEAN:
                        list.add(new DtAttrEntity(info, rd.getFieldValueBool(info.fieldName)));
                        break;
                    case BYTE:
                        list.add(new DtAttrEntity(info, rd.getFieldValueByte(info.fieldName)));
                        break;
                    case INT16:
                    case INT32:
                    case INT64:
                        list.add(new DtAttrEntity(info, rd.getFieldValueInt(info.fieldName)));
                        break;
                    case FLOAT:
                        list.add(new DtAttrEntity(info, rd.getFieldValueFloat(info.fieldName)));
                        break;
                    case DOUBLE:
                        list.add(new DtAttrEntity(info, rd.getFieldValueDouble(info.fieldName)));
                        break;
                    case DATE:
                        list.add(new DtAttrEntity(info, rd.getFieldValueDate(info.fieldName)));
                        break;
                    case BINARY:
                        break;
                    case TEXT:
                        list.add(new DtAttrEntity(info, rd.getFieldValueString(info.fieldName)));
                        break;
                    case LONGBINARY:
                        list.add(new DtAttrEntity(info, rd.getFieldValueInt(info.fieldName)));
                        break;
                    case CHAR:
                        list.add(new DtAttrEntity(info, rd.getFieldValueString(info.fieldName)));
                        break;
                    case TIME:
                    case TIMESTAMP:
                    case NTEXT:
                    case GEOMETRY:
                        break;
                    default:
                        break;
                }
            }
            return list;
        }
        return null;
    }

    @Override
    public Geometry getGeometryById(int id) {
        DatasetVector dv = (DatasetVector) m_dataset;
        Recordset rd = dv.query(" PIEID = " + id);
        if (rd != null) {
            return rd.getGeometry();
        }
        return null;
    }

    @Override
    public List<List<DtAttrEntity>> getAttrlistFromDataset() {
        if (m_dataset == null || m_dataset.getHandle() == 0) {
            return null;
        }
        DatasetVector dv = (DatasetVector) m_dataset;
        Recordset rd = dv.queryByDef(new QueryDef());
        if (rd != null) {
            List<List<DtAttrEntity>> lists = new ArrayList<>();
            int recordCount = rd.getRecordCount();
            int filedcount = rd.getFieldCount();
            for (int j = 0; j < recordCount; j++) {
                List<DtAttrEntity> list = new ArrayList<>();
                rd.moveTo(j);
                for (int i = 0; i < filedcount; i++) {
                    FieldInfo info = rd.getFieldInfoAt(i);
                    //过滤系统字段
                    if (info.fieldName.startsWith("PIE")) {
                        continue;
                    }
                    //获取字段值
                    switch (FieldType.valueOf(info.type)) {
                        case UNKNOWN:
                            break;
                        case BOOLEAN:
                            list.add(new DtAttrEntity(info, rd.getFieldValueBool(info.fieldName)));
                            break;
                        case BYTE:
                            list.add(new DtAttrEntity(info, rd.getFieldValueByte(info.fieldName)));
                            break;
                        case INT16:
                        case INT32:
                        case INT64:
                            list.add(new DtAttrEntity(info, rd.getFieldValueInt(info.fieldName)));
                            break;
                        case FLOAT:
                            list.add(new DtAttrEntity(info, rd.getFieldValueFloat(info.fieldName)));
                            break;
                        case DOUBLE:
                            list.add(new DtAttrEntity(info, rd.getFieldValueDouble(info.fieldName)));
                            break;
                        case DATE:
                            list.add(new DtAttrEntity(info, rd.getFieldValueDate(info.fieldName)));
                            break;
                        case BINARY:
                            break;
                        case TEXT:
                            list.add(new DtAttrEntity(info, rd.getFieldValueString(info.fieldName)));
                            break;
                        case LONGBINARY:
                            list.add(new DtAttrEntity(info, rd.getFieldValueInt(info.fieldName)));
                            break;
                        case CHAR:
                            list.add(new DtAttrEntity(info, rd.getFieldValueString(info.fieldName)));
                            break;
                        case TIME:
                        case TIMESTAMP:
                        case NTEXT:
                        case GEOMETRY:
                            break;
                        default:
                            break;
                    }
                }
                lists.add(list);
            }

            return lists;
        }

        return null;
    }

    @Override
    public boolean updateAttrByEntity(int id, DtAttrEntity dtAttrEntity) {
        boolean isUpdate = false;
        if (m_dataset == null || m_dataset.getHandle() == 0) {
            return isUpdate;
        }
        DatasetVector dv = (DatasetVector) m_dataset;
        Recordset query = dv.query(" PIEID = " + id);
        query.edit();
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
        query.update();
        dv.releaseRecordset(query);

        return isUpdate;
    }

    @Override
    public FieldType getAttrType(int type) {
        return FieldType.valueOf(type);
    }
}
