package cn.piesat.minemonitor.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import java.util.ArrayList;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.util.DialogUtils;

/**
 * 离线地图管理列表适配器
 */
public class LocalMapAdapter extends BaseAdapter implements
        MKOfflineMapListener {

    //	private TextView stateView;
    private Context context;
    private MKOfflineMap mOffline;
    private ArrayList<MKOLUpdateElement> localMapList;

    @SuppressWarnings("deprecation")
    public LocalMapAdapter(Context context) {
        super();
        this.context = context;
        initOffLine();
        getListData();
    }

    private void initOffLine() {
        // TODO Auto-generated method stub
        mOffline = new MKOfflineMap();
        mOffline.init(this);
    }

    private void getListData() {
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
    }

    @Override
    public int getCount() {
        return localMapList.size();
    }

    @Override
    public Object getItem(int index) {
        return localMapList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View view, ViewGroup arg2) {
        MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
        view = LayoutInflater.from(context).inflate(
                R.layout.offline_localmap_list, null);
        initViewItem(view, e);
        return view;
    }

    public void initViewItem(View view,
                             final MKOLUpdateElement mkolUpdateElement) {
        Button remove = (Button) view.findViewById(R.id.remove);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView update = (TextView) view.findViewById(R.id.update);
        TextView ratio = (TextView) view.findViewById(R.id.ratio);
        ratio.setText(mkolUpdateElement.ratio + "%");
        title.setText(mkolUpdateElement.cityName);
        if (mkolUpdateElement.update) {
            update.setText("可更新");
        } else {
            update.setText("最新");
        }

        remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogUtils.showAlertDialog(context, "确定要删除该文件", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        localMapList.remove(mkolUpdateElement);
                        mOffline.remove(mkolUpdateElement.cityID);
                        LocalMapAdapter.this.notifyDataSetChanged();
                    }
                });

            }
        });
    }

    /**
     * 更新状态显示
     */
    public void updateView() {
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        this.notifyDataSetChanged();
    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
                    updateView();
                    this.notifyDataSetChanged();
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
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
