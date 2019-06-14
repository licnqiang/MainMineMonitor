package cn.piesat.minemonitor.util;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by licancan on 2017/5/23.
 */

public class NaviWaysUtils {

//    static double longtitude_zhongdian = 116.124;
//    static double latitude_zhongdian = 39.12;
    public static void setUpBaiduAPPNavi(Context context,double x ,double y){
        try {

            Intent intent = Intent.getIntent("intent://map/direction?origin=我的位置&destination=latlng:"+y+","+x+"|name&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if(isAvilible(context, "com.baidu.BaiduMap")){
                context.startActivity(intent);
            }else {
               ToastUtil.show(context,"您尚未安装百度地图");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static void setUpGaodeAppNavi(Context context,double x ,double y   ){
        try {
            Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname="+"我的位置"+"&dlat="+y+"&dlon="+x+"&dname="+"终点"+"&dev=0&m=0&t=1");
            if(isAvilible(context,"com.autonavi.minimap")){
                context.startActivity(intent);
            }else {
                ToastUtil.show(context,"您尚未安装高德地图");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAvilible(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo>  pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if(pinfo != null){
            for(int i = 0; i < pinfo.size(); i++){
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }
}
