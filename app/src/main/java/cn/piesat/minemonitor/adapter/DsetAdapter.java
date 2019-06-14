package cn.piesat.minemonitor.adapter;


import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.piesat.minemonitor.R;


public class DsetAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public DsetAdapter(List<String> data) {
        super(R.layout.item_dsetname, data);
        m_selectedIndexMap=new HashMap<Integer, Boolean>();
    }

    //用来存放所有选中的实体
    private Map<Integer,Boolean> m_selectedIndexMap;


    public Map<Integer, Boolean> getM_selectedIndexMap() {
        return m_selectedIndexMap;
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {

        if(m_selectedIndexMap.containsKey(helper.getAdapterPosition()))
        {
            helper.setChecked(R.id.cb_dscheck,true);
        }
        else
        {
            helper.setChecked(R.id.cb_dscheck,false);
        }
        helper.setOnCheckedChangeListener(R.id.cb_dscheck, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    m_selectedIndexMap.put(helper.getAdapterPosition(), isChecked);
                }else{
                    m_selectedIndexMap.remove(helper.getAdapterPosition());
                }
            }
        });
        helper.setText(R.id.tv_dsname, item);

    }
}
