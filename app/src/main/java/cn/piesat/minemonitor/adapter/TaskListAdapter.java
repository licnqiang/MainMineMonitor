package cn.piesat.minemonitor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.entity.TaskListEntity;
import cn.piesat.minemonitor.home.Constant;

/**
 * Created by yjl on 2018/3/7.
 */

public class TaskListAdapter extends ArrayAdapter<TaskListEntity> {
    private int resourceId;

    public TaskListAdapter(@NonNull Context context, int resource, List<TaskListEntity> textViewResourceId) {
        super(context, resource, textViewResourceId);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TaskListEntity listEntity = getItem(position);
        View view;
        ViewHolder vh;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            vh = new ViewHolder();
            vh.tv_mapNumber = view.findViewById(R.id.tv_map_number);
            vh.tv_taskNumber = view.findViewById(R.id.tv_task_number);
            vh.tv_mineName = view.findViewById(R.id.tv_mine_name);
            vh.tv_dataType = view.findViewById(R.id.tv_data_type);
            vh.tv_mineType = view.findViewById(R.id.tv_mine_type);
            vh.img_state = view.findViewById(R.id.iv_state);
            vh.img_contetn = view.findViewById(R.id.iv_content);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }

        if (position % 2 == 0) {
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            view.setBackgroundColor(Color.parseColor("#F3F3F3"));
        }

        vh.tv_mapNumber.setText(listEntity.getMapNumber());
        vh.tv_taskNumber.setText(listEntity.getMineNumber());
        vh.tv_mineName.setText(listEntity.getMineType());
        vh.tv_dataType.setText(listEntity.getKsKCFS());
        String dataType  = listEntity.getDataType();
        vh.tv_mineType.setText(dataType.substring(0,dataType.indexOf("_")));
        if (listEntity.getState().equals(Constant.IMPLEMENT)) {
            vh.img_state.setImageResource(R.drawable.dot_orange);
//            vh.img_state.setImageResource(R.drawable.dot_green);
        }
        if (listEntity.getState().equals(Constant.VERIFIED)) {
            vh.img_state.setImageResource(R.drawable.dot_green);
        }
        if (listEntity.getState().equals(Constant.UNVERIFIED)) {
            vh.img_state.setImageResource(R.drawable.dot_red);
        }
        vh.img_contetn.setImageResource(listEntity.getContent());
        return view;
    }

    class ViewHolder {
        TextView tv_mapNumber;
        TextView tv_taskNumber;
        TextView tv_mineName;
        TextView tv_dataType;
        TextView tv_mineType;
        ImageView img_state;
        ImageView img_contetn;
    }
}
