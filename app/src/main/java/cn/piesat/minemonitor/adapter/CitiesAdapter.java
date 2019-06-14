package cn.piesat.minemonitor.adapter;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.piesat.minemonitor.R;


public class CitiesAdapter extends BaseExpandableListAdapter implements MKOfflineMapListener {

    private Map<String, integer> map1;
    private Map<String, integer> map2;
    private List<MKOLSearchRecord> m_dataList;
    private Context context;

    private String nameString;
    private MKOLSearchRecord mkChildCityRecord;

    public CitiesAdapter(List<MKOLSearchRecord> m_dataList, Context context) {
        super();
        map1 = new HashMap<String, integer>();
        map2 = new HashMap<String, integer>();
        this.m_dataList = m_dataList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return m_dataList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if (m_dataList.get(i).childCities==null){
            return 0;
        }
        return m_dataList.get(i).childCities.size();
    }

    @Override
    public Object getGroup(int i) {
        return m_dataList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        if (m_dataList.get(i).childCities==null){
            return null;
        }
        return m_dataList.get(i).childCities.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean b, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.city_download_item, null);
            holder.m_tvName = (TextView) convertView
                    .findViewById(R.id.tv_city_name);
            holder.m_tvSize = (TextView) convertView
                    .findViewById(R.id.tv_city_size);
            holder.m_ivChildCity = (ImageView) convertView
                    .findViewById(R.id.img_child_city_arrow);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.m_ivChildCity.setTag(position);
        MKOLSearchRecord mkCityRecord = m_dataList.get(position);
        holder.m_tvName.setText(mkCityRecord.cityName);
        holder.m_tvSize.setText(this.formatDataSize((int)mkCityRecord.dataSize));
        if (mkCityRecord.childCities == null) {
            holder.m_ivChildCity.setImageResource(R.drawable.download);
        } else {
            nameString = mkCityRecord.cityName;
            holder.m_ivChildCity.setImageResource(R.drawable.ic_downarrow_n_2x);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup viewGroup) {
        ViewHolder1 holder1 = null;
        if (convertView == null) {
            holder1 = new ViewHolder1();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.child_city_item, null);
            holder1.m_tvChildCityName = (TextView) convertView
                    .findViewById(R.id.tv_child_city_name);
            holder1.m_tvChildCitySize = (TextView) convertView
                    .findViewById(R.id.tv_child_city_size);
            holder1.m_ivChildCityDownLoad = (ImageView) convertView
                    .findViewById(R.id.offline_child_city_download);
            convertView.setTag(holder1);
        } else {
            holder1 = (ViewHolder1) convertView.getTag();
        }

        holder1.m_ivChildCityDownLoad.setTag(childPosition);// 新加的
        mkChildCityRecord = m_dataList.get(groupPosition).childCities.get(childPosition);

        if (mkChildCityRecord != null) {
            holder1.m_tvChildCityName.setText(mkChildCityRecord.cityName);
            holder1.m_tvChildCitySize.setText(this
                    .formatDataSize((int) mkChildCityRecord.dataSize));
        } else {
            holder1.m_tvChildCityName.setText("");
            holder1.m_tvChildCitySize.setText("");
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void onGetOfflineMapState(int i, int i1) {

    }

    static class ViewHolder {
        TextView m_tvName;
        TextView m_tvSize;
        ImageView m_ivChildCity;
    }

    static class ViewHolder1 {
        TextView m_tvChildCityName;
        TextView m_tvChildCitySize;
        ImageView m_ivChildCityDownLoad;
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

}
