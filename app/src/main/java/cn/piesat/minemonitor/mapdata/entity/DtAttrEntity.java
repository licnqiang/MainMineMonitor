package cn.piesat.minemonitor.mapdata.entity;

import pie.core.FieldInfo;

/**
 * Created by Administrator on 2017/6/8.
 */

public class DtAttrEntity {

    public FieldInfo fieldInfo;
    public Object object;

    public DtAttrEntity(FieldInfo fieldInfo, Object object) {
        this.fieldInfo = fieldInfo;
        this.object = object;
    }
}
