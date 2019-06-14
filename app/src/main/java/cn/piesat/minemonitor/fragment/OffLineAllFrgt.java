package cn.piesat.minemonitor.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.OffLineMapActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.adapter.CitiesAdapter;
import cn.piesat.minemonitor.adapter.HotCitiesAdapter;
import cn.piesat.minemonitor.base.BaseFragment;
import cn.piesat.minemonitor.entitys.ControlMsgEntity;
import cn.piesat.minemonitor.entitys.EventMsgEntity;
import cn.piesat.minemonitor.util.ToastUtil;

public class OffLineAllFrgt extends BaseFragment implements
        MKOfflineMapListener {

    public OffLineAllFrgt() {
        super();
    }

    private MKOfflineMap mOffline;

    @ViewInject(R.id.hotcitylist)
    private ListView hotCityList;
    @ViewInject(R.id.allcitylist)
    private ExpandableListView allCityList;
    @ViewInject(R.id.citylist_layout)
    private LinearLayout cl;
    private OffLineMapActivity m_thisActivity;
    CitiesAdapter m_aAdapter;
    private MKOfflineMap m_offlineMap;

    @Override
    protected int getLayoutView() {

        return R.layout.off_line_all;
    }

    @Override
    protected void initView() {
        m_offlineMap = new MKOfflineMap();
        m_offlineMap.init(this);
    }


    @Override
    protected void initData(Bundle bundle) {
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        initListView();
    }

    public String formatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    List<MKOLSearchRecord> records2;
    ArrayList<MKOLSearchRecord> records1;

    @SuppressWarnings("unused")
    private void initListView() {
        ArrayList<String> hotCities = new ArrayList<String>();
        // 获取热门城市列表
        records1 = mOffline.getHotCityList();
        if (records1 == null) {
            return;
        }
        for (MKOLSearchRecord r : records1) {
            hotCities.add(r.cityName + "(" + r.cityID + ")" + "   --"
                    + this.formatDataSize(r.size));
        }
        HotCitiesAdapter hAdapter = new HotCitiesAdapter(records1,
                m_thisActivity);
        if (hAdapter == null) {
            return;
        }
        hotCityList.setAdapter(hAdapter);

        // 获取所有支持离线地图的城市
        List<String> allCities = new ArrayList<String>();
        records2 = mOffline.getOfflineCityList();
        if (records1 != null) {
            for (MKOLSearchRecord r : records2) {
                allCities.add(r.cityName + "(" + r.cityID + ")" + "   --"
                        + this.formatDataSize(r.size));
            }
            for (int i = 0; i < records2.size(); i++) {
            }
        }
        m_aAdapter = new CitiesAdapter(records2, m_thisActivity);
        allCityList.setAdapter(m_aAdapter);

        cl.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setClickListener() {
        hotCityList.setOnItemClickListener(new OnHotCityItemClickListener());
        allCityList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                if(records2.get(i).childCities==null){
                    mOffline.start(records2.get(i).cityID);

                    String content = records2.get(i).cityName + "已添加到下载在列表";
                    SpannableString spannableString = new SpannableString(content);
                    if (content.length() <= 9) {
                        return false;
                    }
                    spannableString.setSpan(
                            new ForegroundColorSpan(Color.parseColor("#f74224")), 0,
                            content.length() - 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    ToastUtil.show(activity, spannableString.toString());
                }
                return false;

            }
        });
        allCityList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                MKOLSearchRecord mkRecord = (MKOLSearchRecord) m_aAdapter.getChild(i, i1);
                m_offlineMap.start(mkRecord.cityID);
                ToastUtil.show(m_thisActivity, mkRecord.cityName + "已经添加到下载在列表中...");
                return true;
            }
        });
    }

    @Override
    public void addList(List<ControlMsgEntity> entityList) {

    }

    @Override
    public void addEventList(List<EventMsgEntity> entityList) {

    }


    @Override
    public void showFailedError(String error) {

    }

    class OnHotCityItemClickListener implements
            AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            mOffline.start(records1.get(position).cityID);
            ToastUtil.show(activity, records1.get(position).cityName
                    + "已经添加到下载在列表中...");
        }

    }


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        m_thisActivity = (OffLineMapActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                // Log.d("OfflineDemo", String.format("add offlinemap num:%d",
                // state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);

                break;
            default:
                break;
        }

    }

}
