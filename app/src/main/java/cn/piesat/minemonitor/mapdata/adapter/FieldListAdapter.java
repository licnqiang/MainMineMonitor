package cn.piesat.minemonitor.mapdata.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.piesat.minemonitor.R;


public class FieldListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public FieldListAdapter(List<String> data) {
        super(R.layout.item_fieldlist, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_filed, item);

    }
}
