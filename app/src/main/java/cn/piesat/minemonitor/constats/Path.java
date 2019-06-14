package cn.piesat.minemonitor.constats;

import android.os.Environment;

/**
 * Created by admin on 2017/11/7.
 * 路径常量
 */

public class Path {

    /**
     * PATH_PIE_MAP_RES_DEFAULT 地图资源默认路径
     */
    public static final String PATH_PIE_MAP_RES_DEFAULT = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/PIE/sdk/piesdk" + "/map/";
    public static final String PATH_PIE_DEM_DATA_DEFAULT = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/PIE/sdk/piesdk" + "/map/dem/";

}
