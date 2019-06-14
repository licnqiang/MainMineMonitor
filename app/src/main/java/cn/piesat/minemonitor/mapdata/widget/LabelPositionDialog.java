package cn.piesat.minemonitor.mapdata.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import cn.piesat.minemonitor.R;
import pie.core.DatasetType;
import pie.map.enums.TextAlignmentType;

/**
 * Created by Administrator on 2017/5/18.
 */

public class LabelPositionDialog extends Dialog implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private Context mContext;
    private OnClickListener mListener;
    TextAlignmentType m_postion;
    private DatasetType m_type;

    public LabelPositionDialog(Context context, int themeResId, DatasetType type) {
        super(context, themeResId);
        mContext = context;
        this.m_type=type;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                this.dismiss();
                break;
            case R.id.confirm_button:
                this.dismiss();
                if (mListener != null) {
                    mListener.getPositionvalue(m_postion);
                }
                break;
        }
    }


    public void setOnButtonClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        void getPositionvalue(TextAlignmentType type);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(mContext, R.layout.dialog_labelposition, null);
        setContentView(contentView);
        RadioGroup m_labelpos_line = (RadioGroup) contentView.findViewById(R.id.rg_labelpos_l);
        m_labelpos_line.setOnCheckedChangeListener(this);
        RadioGroup m_labelpos_pntregin = (RadioGroup) contentView.findViewById(R.id.rg_labelpos_pr);
        m_labelpos_pntregin.setOnCheckedChangeListener(this );
        if (m_type== DatasetType.POINT||m_type== DatasetType.REGION){
            m_labelpos_line.setVisibility(View.GONE);
            m_labelpos_pntregin.setVisibility(View.VISIBLE);
        }else if (m_type== DatasetType.LINE){
            m_labelpos_line.setVisibility(View.VISIBLE);
            m_labelpos_pntregin.setVisibility(View.GONE);
        }
        Button cancel = (Button) contentView.findViewById(R.id.cancel_button);
        Button confirm = (Button) contentView.findViewById(R.id.confirm_button);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (group.getId()==R.id.rg_labelpos_pr) {
            switch (checkedId) {
                case R.id.rb_horizontal:
                    m_postion = TextAlignmentType.Horizontal;
                    break;
                case R.id.rb_vertical:
                    m_postion = TextAlignmentType.Perpendicular;
                    break;
                default:
                    m_postion = TextAlignmentType.Horizontal;
                    break;
            }
        }else if (group.getId()==R.id.rg_labelpos_l){
            switch (checkedId) {
                case R.id.rb_shuiping:
                    m_postion = TextAlignmentType.Horizontal;
                    break;
                case R.id.rb_chuizhi:
                    m_postion = TextAlignmentType.Perpendicular;
                    break;
                case R.id.rb_yxpx:
                    m_postion = TextAlignmentType.Parallel;
                    break;
                case R.id.rb_yxwq:
                    m_postion = TextAlignmentType.Curve;
                    break;
                default:
                    m_postion = TextAlignmentType.Horizontal;
                    break;
            }
        }
    }
}
