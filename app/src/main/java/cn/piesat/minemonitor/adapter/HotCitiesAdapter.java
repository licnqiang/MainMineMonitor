package cn.piesat.minemonitor.adapter;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.piesat.minemonitor.R;


public class HotCitiesAdapter extends BaseAdapter implements
        MKOfflineMapListener {

	private Map<String, integer> map1;
	private Map<String, integer> map2;
	private List<MKOLSearchRecord> m_dataList;
	private Context context;
	private MKOfflineMap offlineMap;

	/**
	 * 开始下载
	 */
	public void start(int cityid) {
		// int cityid = Integer.parseInt(cidView.getText().toString());
		offlineMap.start(cityid);
	}

	/**
	 * 暂停下载
	 */
	public void stop(int cityid) {
		offlineMap.pause(cityid);
	}

	public HotCitiesAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HotCitiesAdapter(List<MKOLSearchRecord> m_dataList, Context context) {
		super();

		map1 = new HashMap<String, integer>();
		map2 = new HashMap<String, integer>();
		offlineMap = new MKOfflineMap();
		offlineMap.init(this);
		this.m_dataList = m_dataList;
		this.context = context;
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
	public int getCount() {
		return m_dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return m_dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hot_city_download_item, null);
			holder.m_tvName = (TextView) convertView
					.findViewById(R.id.tv_hot_city_name);
			holder.m_tvSize = (TextView) convertView
					.findViewById(R.id.tv_hot_city_size);
			holder.m_ivDawnLoad = (ImageView) convertView
					.findViewById(R.id.img_child_city_arrow);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final MKOLSearchRecord cityRecord = m_dataList.get(position);
		holder.m_tvName.setText(cityRecord.cityName);
		holder.m_tvSize.setText(this.formatDataSize((int) cityRecord.dataSize));
		return convertView;
	}

	static class ViewHolder {
		TextView m_tvName;
		TextView m_tvSize;
		ImageView m_ivDawnLoad;

	}

	@Override
	public void onGetOfflineMapState(int type, int state) {
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
			MKOLUpdateElement update = offlineMap.getUpdateInfo(state);
			// 处理下载进度更新提示
			if (update != null) {
				// stateView.setText(String.format("%s : %d%%", update.cityName,
				// update.ratio));
				// updateView();
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
