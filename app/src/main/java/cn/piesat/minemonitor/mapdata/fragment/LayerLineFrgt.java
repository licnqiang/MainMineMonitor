package cn.piesat.minemonitor.mapdata.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.piesat.minemonitor.LayerSettingActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.mapdata.base.BaseFragment;
import cn.piesat.minemonitor.mapdata.widget.ColorPickerDialog;
import cn.piesat.minemonitor.mapdata.widget.EditTextDialog;
import cn.piesat.minemonitor.mapdata.widget.LayerSelectDialog;
import pie.core.Layer;
import pie.core.Style;

/**
 * Created by Administrator on 2017/5/24.
 */

public class LayerLineFrgt extends BaseFragment {

    @BindView(R.id.tv_line_type)
    TextView m_tvLineType;
    @BindView(R.id.tv_line_width)
    TextView m_tvLineWidth;
    @BindView(R.id.tv_line_color)
    TextView m_tvLineColor;
    @BindView(R.id.layout_line_type)
    LinearLayout m_layoutLineType;
    @BindView(R.id.layout_line_width)
    LinearLayout m_layoutLineWidth;
    @BindView(R.id.layout_line_color)
    LinearLayout m_layoutLineColor;

    public static String FRGT_FLAG = "LayerLineFrgt";
    private Layer m_layer;
    private LayerSettingActivity m_activity;

    @Override
    public int getLayoutId() {
        return R.layout.layer_line_setting;
    }

    @Override
    public void loadingData() {
        m_activity = (LayerSettingActivity) getActivity();
        m_layer = m_activity.getLayer();
        if (m_layer != null) {
            refreshLineTypeView();
            refreshLineWidth();
            refreshLineColor();
        }
    }

    private void refreshLineTypeView() {
        Style lineStyle = m_layer.getStyle();
        int lineType = lineStyle.lineStyle;
        switch (lineType) {
            case 0://实线
                m_tvLineType.setText("实线");
                break;
            case 2://虚线
                m_tvLineType.setText("虚线");
                break;

        }
    }

    private void refreshLineWidth() {
        Style lineStyle = m_layer.getStyle();
        m_tvLineWidth.setText(lineStyle.lineWidth + "");
    }

    private void refreshLineColor() {
        Style lineStyle = m_layer.getStyle();
        m_tvLineColor.setBackgroundColor(lineStyle.lineColor);
    }

    @OnClick(R.id.layout_line_type)
    void onClickLineType() {
        final List<String> data = new ArrayList<>();
        data.add("实线");
        data.add("虚线");
        LayerSelectDialog dialog = new LayerSelectDialog(getContext(), R.style.common_alert_dialog);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Style style = m_layer.getStyle();
                int lineType = style.lineStyle;
                switch (position) {
                    case 0://实线
                        lineType = 0;
                        break;
                    case 1://虚线
                        lineType = 2;
                        break;
                }
                if (style.lineStyle == lineType) {
                    return;
                }
                style.lineStyle = lineType;
                m_layer.setStyle(style);
                refreshLineTypeView();
            }
        });
        dialog.show();
        dialog.setData(data);
    }

    @OnClick(R.id.layout_line_width)
    void onClickLineWidth() {
        final Style style = m_layer.getStyle();
        EditTextDialog dialog = new EditTextDialog(getContext(), R.style.common_alert_dialog);
        dialog.setOnButtonClickListener(new EditTextDialog.OnClickListener() {
            @Override
            public void onClick(String num) {
                int value;
                try {
                    value = Integer.valueOf(num);
                }catch (Exception e){
                    value = 0;
                }
                if (value != 0) {
                    style.lineWidth = value;
                    m_layer.setStyle(style);
                    refreshLineWidth();
                }
            }
        });
        dialog.show();
        dialog.setNumValue(style.lineWidth);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mColorPickerDialog != null) {
            mColorPickerDialog = null;
        }
    }

    ColorPickerDialog mColorPickerDialog;

    @OnClick(R.id.layout_line_color)
    void onClickLineColor() {
        if (mColorPickerDialog == null) {
            mColorPickerDialog = new ColorPickerDialog(getActivity(), R.style.common_alert_dialog);
            mColorPickerDialog.setOnButtonClickListener(new ColorPickerDialog.OnColorPickerClickListener() {
                @Override
                public void cancel() {

                }

                @Override
                public void confirm(int color) {
                    Style style = m_layer.getStyle();
                    int colorOld = style.lineColor;
                    if (colorOld != color) {
                        style.lineColor = color;
                        m_layer.setStyle(style);
                        refreshLineColor();
                    }

                }
            });
        }

        mColorPickerDialog.show();
    }

    private static final class LayerLineFrgtHandle {

        private static final LayerLineFrgt NEWINSTANC = new LayerLineFrgt();
    }

    public static LayerLineFrgt newInstance() {
        return LayerLineFrgtHandle.NEWINSTANC;
    }
}
