package cn.piesat.minemonitor.entitys;

import java.io.Serializable;

/**
 * Created by licancan on 2016/9/19.
 */
public class ControlMsgEntity implements Serializable {
    public String messageid;
    public double lat;
    public double lng;
    public double alt;
    public long timestamp;
    public int id;
    public String groupname;//"App监控"
    public String name;//"APP用户"
    public String iconurl;
    public String cellphone;
    public String typename;

    @Override
    public String toString() {
        return "ControlMsgEntity{" +
                "messageid='" + messageid + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", alt=" + alt +
                ", timestamp=" + timestamp +
                ", id=" + id +
                ", groupname='" + groupname + '\'' +
                ", name='" + name + '\'' +
                ", iconurl='" + iconurl + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", typename='" + typename + '\'' +
                '}';
    }

    public ControlMsgEntity(String messageid, double lat, double lng, double alt, long timestamp, int id, String groupname, String name, String iconurl, String cellphone, String typename) {
        this.messageid = messageid;
        this.lat = lat;
        this.lng = lng;
        this.alt = alt;
        this.timestamp = timestamp;
        this.id = id;
        this.groupname = groupname;
        this.name = name;
        this.iconurl = iconurl;
        this.cellphone = cellphone;
        this.typename = typename;
    }

    public ControlMsgEntity() {

    }
}
