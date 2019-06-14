package cn.piesat.minemonitor.mapdata.utils;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.mapdata.adapter.FieldListAdapter;
import pie.core.DataSource;


/**
 * Created by Administrator on 2017/5/24.
 */

public class FieldListDialogUtils extends Dialog {
    private Activity m_activity;
    private TextView tv_title;
    List<Integer> indexlist;
    private DataSource m_dataSource;
    private String m_proPath;

    public FieldListDialogUtils(Activity activity) {
        super(activity, R.style.common_alert_dialog);
        this.m_activity = activity;
        indexlist = new ArrayList<>();
    }

    public interface OnItemFieldListener {
        void getFieldName(String field);
    }

    public void showDialog(final List<String> fieldlist, final OnItemFieldListener listener) {
        final Dialog dialog = new Dialog(m_activity, R.style.common_alert_dialog);
        dialog.setContentView(R.layout.dialog_fieldlist);
        RecyclerView rv_timeinfolists = (RecyclerView) dialog.findViewById(R.id.rv_fieldlists);
        rv_timeinfolists.setLayoutManager(new LinearLayoutManager(m_activity));
        rv_timeinfolists.addItemDecoration(new DividerItemDecoration(
                m_activity, DividerItemDecoration.VERTICAL));
        final FieldListAdapter adapter = new FieldListAdapter(fieldlist);
        rv_timeinfolists.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String field = fieldlist.get(position);
                listener.getFieldName(field);
                dialog.dismiss();
            }
        });

        Display defaultDisplay = m_activity.getWindowManager().getDefaultDisplay();
        int width = (int) (defaultDisplay.getWidth() * 0.8);
        int height = (int) (defaultDisplay.getHeight() * 0.4);
        android.view.WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width;
        params.height = height;
        dialog.getWindow().setAttributes(params);
        dialog.show();

    }

    public void showDialog1(final List<String> fieldlist, final OnItemFieldListener listener) {
        final Dialog dialog = new Dialog(m_activity, R.style.common_alert_dialog);
        dialog.setContentView(R.layout.dialog_fieldlist);
        TextView tds = dialog.findViewById(R.id.tv_tftitle);
        tds.setText("请选择需要切换的数据");
        RecyclerView rv_timeinfolists = (RecyclerView) dialog.findViewById(R.id.rv_fieldlists);
        rv_timeinfolists.setLayoutManager(new LinearLayoutManager(m_activity));
        rv_timeinfolists.addItemDecoration(new DividerItemDecoration(
                m_activity, DividerItemDecoration.VERTICAL));
        final FieldListAdapter adapter = new FieldListAdapter(fieldlist);
        rv_timeinfolists.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String field = fieldlist.get(position);
                listener.getFieldName(field);
                dialog.dismiss();
            }
        });

        Display defaultDisplay = m_activity.getWindowManager().getDefaultDisplay();
        int width = (int) (defaultDisplay.getWidth() * 0.8);
        int height = (int) (defaultDisplay.getHeight() * 0.4);
        android.view.WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width;
        params.height = height;
        dialog.getWindow().setAttributes(params);
        dialog.show();

    }
}
