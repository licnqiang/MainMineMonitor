package cn.piesat.minemonitor.home.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.home.Constant;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;

public class LongRunningService extends Service {

    private AlarmManager manager;
    private PendingIntent pi;
    private CustomSQLTools s;
    private String runXy;

    public LongRunningService() {
    }

    private MyBinder mBinder = new MyBinder();

    class MyBinder extends Binder {

        public void startDownload() {
            Log.d("TAG", "startDownload() executed");
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onDestroy() {
        manager.cancel(pi);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        s = new CustomSQLTools();
        final List<String> trackXY = new ArrayList<>();
        if (null==BaseActivity.instance||BaseActivity.instance.xY().isEmpty()){

        }else{
            trackXY.addAll(BaseActivity.instance.xY());
        }
        final String lon = SpHelper.getStringValue("LON");
        final String lat = SpHelper.getStringValue("LAT");
        if (trackXY.size() > 0) {
            runXy = SpHelper.getStringValue("RUNXY","1");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!runXy.equals("1")){

                    }else{
                        if (!lon.equals("0") && !lat.equals("0")) {
                            s.AddTrack(getApplication(), s.getUUID(), "", BaseActivity.instance.devinceNo, lon, lat,
                                    Constant.TRACK_EVENT_HEARTBEAT, s.getCurrTime(), SpHelper.getStringValue("USERNAME"), "", Constant.GPS);
                        } else {
                            s.AddTrack(getApplication(), s.getUUID(), "", BaseActivity.instance.devinceNo, trackXY.get(0), trackXY.get(1),
                                    Constant.TRACK_EVENT_HEARTBEAT, s.getCurrTime(), SpHelper.getStringValue("USERNAME"), "", Constant.GPS);
                        }
                    }


                }
            }).start();
        }


        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 3600 * 1000; // 10秒
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
