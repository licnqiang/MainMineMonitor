package cn.piesat.minemonitor.mapdata.entity;

/**
 * Created by Administrator on 2017/5/27.
 */

public class LayerEntity {

    public String name;
    public boolean visible;
    public int type;

    public LayerEntity(String name, boolean visible, int type) {
        this.name = name;
        this.visible = visible;
        this.type = type;
    }

    @Override
    public String toString() {
        return "LayerEntity{" + "name='" + name + '\'' + ", visible=" + visible + ", type=" + type + '}';
    }
}
