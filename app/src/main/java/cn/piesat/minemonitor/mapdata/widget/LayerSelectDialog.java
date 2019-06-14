package cn.piesat.minemonitor.mapdata.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.R;


/**
 * Created by Administrator on 2017/5/18.
 */

public class LayerSelectDialog extends Dialog implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mlistView;
    private List<String> m_datas;

    private AdapterView.OnItemClickListener m_onItemClickListener;

    public LayerSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
        m_datas = new ArrayList<>();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        m_onItemClickListener = onItemClickListener;
    }

    public void setData(List<String> data){
        m_datas.clear();
        m_datas.addAll(data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(mContext, R.layout.dialog_layer_select_confirm, null);
        setContentView(contentView);
        mlistView = (ListView) contentView.findViewById(R.id.listview);
        mlistView.setOnItemClickListener(this);
        ArrayAdapter<String>  adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1,m_datas);
        mlistView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        m_onItemClickListener.onItemClick(parent,view,position,id);
    }
}
