package cn.piesat.minemonitor.util;

import com.baidu.location.BDLocation;

import java.util.Observable;

/**
 * Created by Administrator on 2017/6/12.
 */

public class LocationWatcher extends Observable {

    public void setLocation(BDLocation location) {
        if (location != null) {
            this.setChanged();
            notifyObservers(location);
        }
    }
}
