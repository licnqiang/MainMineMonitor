package cn.piesat.minemonitor.adapter.impl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.entity.CheckInfoEntiy;
import cn.piesat.minemonitor.home.AddMapActivity;
import cn.piesat.minemonitor.home.ContentListActivity;

/**
 * Created by yjl on 2018/4/13.
 */

public class AddTBgcdAdapter extends BaseAdapter implements View.OnClickListener {
    List<CheckInfoEntiy> checkInfoEntiys = new ArrayList<>();
    private AddMapActivity mContext;
    private InnerItemOnclickListener mListener;

    public AddTBgcdAdapter(AddMapActivity context, List<CheckInfoEntiy> list) {
        mContext = context;
        checkInfoEntiys = list;
    }

    @Override
    public int getCount() {
        return checkInfoEntiys.size();
    }

    @Override
    public Object getItem(int position) {
        return checkInfoEntiys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_tbgcd, null);
            viewHolder.gcdbh = (TextView) view.findViewById(R.id.tv_gcd_bh);
            viewHolder.gcdwzjd = (TextView) view.findViewById(R.id.tv_gcd_wz_jd);
            viewHolder.gcdwzwd = (TextView) view.findViewById(R.id.tv_gcd_wz_wd);
            viewHolder.gcdpz = (ImageView) view.findViewById(R.id.iv_gcd_pz);
            viewHolder.gcdtpck = (ImageView) view.findViewById(R.id.iv_gcd_tpck);
            viewHolder.gcddw = (ImageView) view.findViewById(R.id.iv_gcd_dw);
            viewHolder.gcdsc = (ImageView) view.findViewById(R.id.iv_gcd_sc);
            viewHolder.llgcdpz = (LinearLayout) view.findViewById(R.id.ll_tb_gcd_pz);
            viewHolder.llgcdtpck = (LinearLayout) view.findViewById(R.id.ll_tb_gcd_tpck);
            viewHolder.llgcddw = (LinearLayout) view.findViewById(R.id.ll_tb_gcd_dw);
            viewHolder.llgcdsc = (LinearLayout) view.findViewById(R.id.ll_tb_gcd_sc);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
//        viewHolder.gcdwzjd.setText(new DecimalFormat("0.00").format(checkInfoEntiys.get(position).getFileCreateLocationX()));
//        viewHolder.gcdwzwd.setText(new DecimalFormat("0.00").format(checkInfoEntiys.get(position).getFileCreateLocationY()));
        double x = Double.parseDouble(checkInfoEntiys.get(position).getFileCreateLocationX());
        double y = Double.parseDouble(checkInfoEntiys.get(position).getFileCreateLocationY());
        viewHolder.gcdbh.setText((CharSequence) checkInfoEntiys.get(position).getFileCreateLocationNo());
        viewHolder.gcdwzjd.setText(doubleToString(x) );
        viewHolder.gcdwzwd.setText(doubleToString(y));
        viewHolder.llgcdpz.setTag(position);
        viewHolder.llgcdpz.setOnClickListener(this);
//                new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "进行拍照操作", Toast.LENGTH_SHORT).show();
//                mContext.showVideoDialog();
//            }
//        }

        viewHolder.llgcdtpck.setTag(position);
        viewHolder.llgcdtpck.setOnClickListener(this);
//                new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "进行图片查看操作", Toast.LENGTH_SHORT).show();
//            }
//        }

        viewHolder.llgcddw.setTag(position);
        viewHolder.llgcddw.setOnClickListener(this);
//                new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "进行观察点定位操作", Toast.LENGTH_SHORT).show();
//            }
//        }
        viewHolder.llgcdsc.setTag(position);
        viewHolder.llgcdsc.setOnClickListener(this
//                new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnItemDeleteListener.onDeleteClick(position);
//            }
//        }
        );
        return view;
    }
    public static String doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    /**
     * 删除按钮的监听接口
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }

    class ViewHolder {
        TextView gcdbh;
        TextView gcdwzjd;
        TextView gcdwzwd;
        ImageView gcdpz;
        ImageView gcdtpck;
        ImageView gcddw;
        ImageView gcdsc;
        LinearLayout llgcdpz;
        LinearLayout llgcdtpck;
        LinearLayout llgcddw;
        LinearLayout llgcdsc;

    }

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}
