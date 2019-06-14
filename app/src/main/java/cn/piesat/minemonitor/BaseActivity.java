package cn.piesat.minemonitor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.home.ListDialogUtils;
import cn.piesat.minemonitor.util.ActivityCollector;
import cn.piesat.minemonitor.util.LocationUtil2;

/**
 * Created by yjl on 2018/3/2.
 * 判断点击图标跳转的activity
 */

public class BaseActivity extends AppCompatActivity {
    public PopupWindow popupWindow;
    private Intent intent;
    public double latitude = 0.0;
    public double longitude = 0.0;
    private List<String> list;
    public String devinceNo;
    private CustomSQLTools s;
    private List<String> list1 = new ArrayList<String>();
    String provider = "";
    public static BaseActivity instance = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        instance = this;
        Log.d("yjl", getClass().getSimpleName());
        s = new CustomSQLTools();
//        initData();
        ActivityCollector.addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View popupView3 = getLayoutInflater().inflate(R.layout.pop, null);
        LinearLayout button = popupView3.findViewById(R.id.ll_sz);
        LinearLayout task = popupView3.findViewById(R.id.ll_rw);
        ImageView ivback = popupView3.findViewById(R.id.iv_pop_back);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogUtils dialogUtils = new ListDialogUtils(BaseActivity.this);
                list1 = s.taskDataExportQuery(BaseActivity.this);
                dialogUtils.showDialog(list1);
//                // 此处为伪代码，实际为一个真实存在的文件，即你想复制的文件。
//                if (s.copyFile(file, s.copyPath1)==true){
//                    Toast.makeText(BaseActivity.this,"文件拷贝成功！！！",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(BaseActivity.this,"文件拷贝失败！！！",Toast.LENGTH_SHORT).show();
//                }
//                if (s.copy(s.copyPath, s.copyPath1) == 0) {
//                    Toast.makeText(BaseActivity.this, "文件拷贝成功！！！", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(BaseActivity.this, "文件拷贝失败！！！", Toast.LENGTH_SHORT).show();
//                }
//                intent = new Intent(BaseActivity.this, HomeActivity.class);
//                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(BaseActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        popupWindow = new PopupWindow(popupView3, 400, ActionBar.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setAnimationStyle(R.style.PopupAnimationRight);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        devinceNo = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        //序列号（sn）tm.getSimSerialNumber();
        Log.d("aaaa", "onCreate: " +tm.getSimSerialNumber());
//        Log.d("aaaa", "fdasdf: "+ tm.getSimSerialNumber());
//        Log.d("aaaa", "获取设备基板名称: "+ android.os.Build.BOARD);
//        Log.d("aaaa", "获取设备引导程序版本号: "+ android.os.Build.BOOTLOADER);
//        Log.d("aaaa", "获取设备品牌: "+android.os.Build.BRAND);
//        Log.d("aaaa", "获取设备指令集名称（CPU的类型）: "+android.os.Build.CPU_ABI);
//        Log.d("aaaa", "获取第二个指令集名称: "+android.os.Build.CPU_ABI2);
//        Log.d("aaaa", "获取设备驱动名称: "+android.os.Build.DEVICE);
//        Log.d("aaaa", "获取设备显示的版本包（在系统设置中显示为版本号）和ID一样: "+android.os.Build.DISPLAY);
//        Log.d("aaaa", "设备的唯一标识。由设备的多个信息拼接合成。: "+android.os.Build.FINGERPRINT);
//        Log.d("aaaa", "设备硬件名称,一般和基板名称一样（BOARD）: "+android.os.Build.HARDWARE);

    }


    public List<String> xY() {
        list = new ArrayList<>();
        list.clear();
        Location location = LocationUtil2.getInstance(this).showLocation();
        if(location!=null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        list.add(latitude + "");
        list.add(longitude + "");
        return list;
    }
}
