package cn.piesat.minemonitor.mapdata.fragment;

import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.piesat.minemonitor.LayerSettingActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.mapdata.base.BaseFragment;
import cn.piesat.minemonitor.mapdata.widget.ColorPickerDialog;
import cn.piesat.minemonitor.mapdata.widget.EditTextDialog;
import pie.core.Layer;
import pie.core.Style;

/**
 * Created by Administrator on 2017/5/24.
 */

public class LayerPointFrgt extends BaseFragment {

    public static String FRGT_FLAG = "LayerPointFrgt";

    @BindView(R.id.tv_point_size)
    TextView m_tvPointSize;
    @BindView(R.id.tv_point_color)
    TextView m_tvPointColor;
    ColorPickerDialog mColorPickerDialog;

    private Layer m_layer;
    private LayerSettingActivity m_activity;

    @Override
    public int getLayoutId() {
        return R.layout.layer_point_setting;
    }

    @Override
    public void loadingData() {
        m_activity = (LayerSettingActivity) getActivity();
        m_layer = m_activity.getLayer();
        if (m_layer != null) {
            refreshPointSize();
            refreshPointColor();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mColorPickerDialog != null) {
            mColorPickerDialog = null;
        }
    }

    private void refreshPointSize() {
        Style style = m_layer.getStyle();
        m_tvPointSize.setText(style.markerSize + "");
    }

    private void refreshPointColor() {
        Style style = m_layer.getStyle();
        m_tvPointColor.setBackgroundColor(style.lineColor);
    }

    @OnClick(R.id.layout_point_size)
    void onPointSizeClick() {
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
                    style.markerType = Style.MarkerType_Null;
                    style.markerSize = value;
                    style.markerHeight = value;
                    style.markerWidth = value;
                    m_layer.setStyle(style);
                    refreshPointSize();
                }
            }
        });
        dialog.show();
        dialog.setNumValue(style.markerSize);
    }

    @OnClick(R.id.layout_point_color)
    void onPointColorClick() {
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
                    if (color != colorOld) {
                        style.markerType = Style.MarkerType_Null;
                        style.lineColor = color;
                        m_layer.setStyle(style);
                        refreshPointColor();
                    }
                }
            });
        }
        mColorPickerDialog.show();
    }

    private static final class LayerPointFrgtHandle {

        private static final LayerPointFrgt NEWINSTANC = new LayerPointFrgt();
    }

    public static LayerPointFrgt newInstance() {
        return LayerPointFrgtHandle.NEWINSTANC;
    }
}
