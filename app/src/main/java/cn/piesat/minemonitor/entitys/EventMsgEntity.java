package cn.piesat.minemonitor.entitys;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by licancan on 2016/9/19.
 */
public class EventMsgEntity implements Serializable, Parcelable {
    public int id;
    public String enterpryid;
    public String remark;
    public String time;
    public String title;
    public String typeid;
    public String content;
    public String eventtime;
    public String status;
    public String lat;
    public String lng;
    public String alt;
    public String counts;
    public String level;
    public String usercode;
    public String messageid;

    public EventMsgEntity() {
    }

    public long getTime(String time) {
        this.time = time;
        if (!TextUtils.isEmpty(time)) {
            return Long.parseLong(time);
        }
        return 0;
    }

    public long getEventTime(String eventtime) {
        this.eventtime = eventtime;
        if (!TextUtils.isEmpty(eventtime) && !"null".equals(eventtime)) {
            return Long.parseLong(eventtime.trim());
        }
        return 0;
    }

    public double getLat(String lat) {
        this.lat = lat;
        if (!TextUtils.isEmpty(lat)) {
            return Double.parseDouble(lat);
        }
        return 0.0;
    }

    public double getLng(String lng) {
        this.lng = lng;
        if (!TextUtils.isEmpty(lng)) {
            return Double.parseDouble(lng);
        }
        return 0.0;
    }

    public int getCounts(String counts) {
        this.counts = counts;
        if (!TextUtils.isEmpty(counts)) {
            return Integer.parseInt(counts);
        }
        return 0;
    }


    public EventMsgEntity(int id, String enterpryid, String remark, String time, String title, String typeid, String content, String eventtime, String status, String lat, String lng, String alt, String counts, String level, String usercode, String messageid) {
        this.id = id;
        this.enterpryid = enterpryid;
        this.remark = remark;
        this.time = time;
        this.title = title;
        this.typeid = typeid;
        this.content = content;
        this.eventtime = eventtime;
        this.status = status;
        this.lat = lat;
        this.lng = lng;
        this.alt = alt;
        this.counts = counts;
        this.level = level;
        this.usercode = usercode;
        this.messageid = messageid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "EventMsgEntity{" +
                "id=" + id +
                ", enterpryid='" + enterpryid + '\'' +
                ", remark='" + remark + '\'' +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", typeid='" + typeid + '\'' +
                ", content='" + content + '\'' +
                ", eventtime='" + eventtime + '\'' +
                ", status='" + status + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", alt='" + alt + '\'' +
                ", counts='" + counts + '\'' +
                ", level='" + level + '\'' +
                ", usercode='" + usercode + '\'' +
                ", messageid='" + messageid + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.enterpryid);
        parcel.writeString(this.remark);
        parcel.writeString(this.time);
        parcel.writeString(this.title);
        parcel.writeString(this.typeid);
        parcel.writeString(this.content);
        parcel.writeString(this.eventtime);
        parcel.writeString(this.status);
        parcel.writeString(this.lat);
        parcel.writeString(this.lng);
        parcel.writeString(this.alt);
        parcel.writeString(this.counts);
        parcel.writeString(this.level);
        parcel.writeString(this.usercode);
        parcel.writeString(this.messageid);
    }

    public static final Creator<EventMsgEntity> CREATOR = new Creator<EventMsgEntity>() {

        @Override
        public EventMsgEntity[] newArray(int i) {
            return new EventMsgEntity[i];
        }

        @Override
        public EventMsgEntity createFromParcel(Parcel parcel) {
            // TODO Auto-generated method stub
            return new EventMsgEntity(parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString());
        }
    };
}
