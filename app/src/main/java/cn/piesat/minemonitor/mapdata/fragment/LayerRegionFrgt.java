package cn.piesat.minemonitor.mapdata.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.piesat.minemonitor.LayerSettingActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.mapdata.base.BaseFragment;
import cn.piesat.minemonitor.mapdata.utils.SysConstant;
import cn.piesat.minemonitor.mapdata.widget.ColorPickerDialog;
import cn.piesat.minemonitor.mapdata.widget.EditTextDialog;
import cn.piesat.minemonitor.mapdata.widget.LayerSelectDialog;
import pie.core.Layer;
import pie.core.LayerSet;
import pie.core.Style;

/**
 * Created by Administrator on 2017/5/24.
 */

public class LayerRegionFrgt extends BaseFragment {

    @BindView(R.id.tv_region_fore_color)
    TextView m_tvRegionForeColor;
    @BindView(R.id.tv_region_tm)
    TextView m_tvRegionTm;//透明度
    @BindView(R.id.tv_line_type)
    TextView m_tvLineType;
    @BindView(R.id.tv_line_width)
    TextView m_tvLineWidth;
    @BindView(R.id.tv_line_color)
    TextView m_tvLineColor;

    private Layer m_layer;
    private LayerSettingActivity m_activity;

    public static String FRGT_FLAG = "LayerRegionFrgt";

    @Override
    public int getLayoutId() {
        return R.layout.layer_region_setting;
    }

    @Override
    public void loadingData() {
        m_layer = m_activity.getLayer();
        if (m_layer != null) {
            refreshRegionForeColor();
            refreshRegionTm();
            refreshLineTypeView();
            refreshLineWidth();
            refreshLineColor();
        }
    }

    @Override
    public void onAttach(Context Context) {
        super.onAttach(Context);
        m_activity = (LayerSettingActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRegionColorPickerDialog != null) {
            mRegionColorPickerDialog = null;
        }
        if (mColorPickerDialog != null) {
            mColorPickerDialog = null;
        }
    }

    private void refreshRegionForeColor() {
        Style style = m_layer.getStyle();
        m_tvRegionForeColor.setBackgroundColor(style.fillForeColor);
    }

    private void refreshRegionTm() {
        Style style = m_layer.getStyle();
        int color = style.fillForeColor;
        int alpha = (color & 0xff000000) >>> 24;
        m_tvRegionTm.setText((int) Math.round(alpha * 100 / 255.0) + "%");
        refreshRegionForeColor();
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

    ColorPickerDialog mRegionColorPickerDialog;

    @OnClick(R.id.layout_region_font_color)
    void onClickRegionFontColor() {
        if (mRegionColorPickerDialog == null) {
            mRegionColorPickerDialog = new ColorPickerDialog(m_activity, R.style.common_alert_dialog);
            mRegionColorPickerDialog.setOnButtonClickListener(new ColorPickerDialog.OnColorPickerClickListener() {
                @Override
                public void cancel() {

                }

                @Override
                public void confirm(int color) {
                    Style style = m_layer.getStyle();
                    int colorOld = style.fillForeColor;
                    if (colorOld != color) {
                        int alpha = (colorOld & 0xff000000) >>> 24;
                        int red = (color & 0x00ff0000) >> 16;
                        int green = (color & 0x0000ff00) >> 8;
                        int blue = (color & 0x000000ff);
                        //修改透明色
                        color = Color.argb(alpha, red, green, blue );
                        style.fillForeColor = color;
                        m_layer.setStyle(style);
                        refreshRegionForeColor();
                    }

                }
            });
        }
        mRegionColorPickerDialog.show();
    }

    @OnClick(R.id.layout_region_tm)
    void onClickRegionTm() {
        final Style style = m_layer.getStyle();
        EditTextDialog dialog = new EditTextDialog(m_activity, R.style.common_alert_dialog);
        dialog.setOnButtonClickListener(new EditTextDialog.OnClickListener() {
            @Override
            public void onClick(String num) {
                int value;
                try {
                    value = Integer.valueOf(num);
                }catch (Exception e){
                    value = 0;
                }

                int color = style.fillForeColor;
                int alpha = value * 255 / 100;
                int red = (color & 0x00ff0000) >> 16;
                int green = (color & 0x0000ff00) >> 8;
                int blue = (color & 0x000000ff);
                color = Color.argb(alpha, red, green, blue);
                style.fillForeColor = color;
                m_layer.setStyle(style);
                refreshRegionTm();
            }
        });
        dialog.show();
        int color = style.fillForeColor;
        int alpha = (color & 0xff000000) >>> 24;
        dialog.setNumValue((int) Math.round(alpha * 100 / 255.0));
    }

    @OnClick(R.id.layout_line_type)
    void onClickLineType() {
        final List<String> data = new ArrayList<>();
        data.add("实线");
        data.add("虚线");
        LayerSelectDialog dialog = new LayerSelectDialog(m_activity, R.style.common_alert_dialog);
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
        EditTextDialog dialog = new EditTextDialog(m_activity, R.style.common_alert_dialog);
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

    ColorPickerDialog mColorPickerDialog;

    @OnClick(R.id.layout_line_color)
    void onClickLineColor() {
        if (mColorPickerDialog == null) {
            mColorPickerDialog = new ColorPickerDialog(m_activity, R.style.common_alert_dialog);
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

    private static final class LayerRegionFrgtHandle {

        private static final LayerRegionFrgt NEWINSTANC = new LayerRegionFrgt();
    }

    public static LayerRegionFrgt newInstance() {
        return LayerRegionFrgtHandle.NEWINSTANC;
    }
}
