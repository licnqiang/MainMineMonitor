package cn.piesat.minemonitor.mapdata.fragment;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.piesat.minemonitor.LayerSettingActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.mapdata.base.BaseFragment;
import cn.piesat.minemonitor.mapdata.utils.FieldListDialogUtils;
import cn.piesat.minemonitor.mapdata.widget.ColorPickerDialog;
import cn.piesat.minemonitor.mapdata.widget.EditTextDialog;
import cn.piesat.minemonitor.mapdata.widget.LabelPositionDialog;
import cn.piesat.minemonitor.rxbus.RxBus;
import cn.piesat.minemonitor.rxbus.event.ThemeLabelEntityEvent;
import cn.piesat.minemonitor.rxbus.event.ThemeLayerEvent;
import cn.piesat.minemonitor.rxbus.event.ThemepostlayerEvent;
import pie.core.DatasetType;
import pie.core.DatasetVector;
import pie.core.FieldInfo;
import pie.core.Layer;
import pie.core.TextStyle;
import pie.map.enums.TextAlignmentType;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 标签专题图
 * Created by Administrator on 2017/5/24.
 */

public class LayerLabelFrgt extends BaseFragment {

    public static String FRGT_FLAG = "LayerLabelFrgt";

    @BindView(R.id.tv_mark_color)
    TextView mTvMarkColor;
    @BindView(R.id.sw_labeltheme)
    Switch m_swLabelTheme;
    @BindView(R.id.tv_field)
    TextView m_labelfield;
    @BindView(R.id.tv_position)
    TextView m_labelpostion;
    @BindView(R.id.tv_fontsize)
    TextView m_labelfontsize;
    @BindView(R.id.ll_themelayout)
    LinearLayout m_themelayout;
    @BindView(R.id.ll_position)
    LinearLayout m_positionlayout;
    @BindView(R.id.ll_mark_color)
    LinearLayout m_colorlayout;
    @BindView(R.id.ll_fontsize)
    LinearLayout m_fontsizelayout;
    @BindView(R.id.ll_field)
    LinearLayout m_fieldlayout;


    ColorPickerDialog mColorPickerDialog;
    private Layer m_layer;
    private Layer m_labelLayer;
    private LayerSettingActivity m_activity;
    private List<String> m_fieldlist;
    DatasetType m_dstype;
    private Subscription m_themeLabel;
    private TextAlignmentType m_type;
    private String m_field;

    @Override
    public int getLayoutId() {
        return R.layout.label_setting_layout;
    }

    /**
     * 获取标注字段列表
     */
    public void getFieldList() {
        DatasetVector dset = (DatasetVector) m_layer.getDataset();
        if (dset != null) {
            int fieldCount = dset.getFieldCount();
            for (int i = 0; i < fieldCount; i++) {
                FieldInfo fieldInfo = dset.getFieldInfoAt(i);
                m_fieldlist.add(fieldInfo.foreignName);
            }
        }
    }

    @Override
    public void loadingData() {
        m_fieldlist = new ArrayList<>();
        m_activity = (LayerSettingActivity) getActivity();
        m_layer = m_activity.getLayer();
        m_labelLayer = m_activity.getLabelLayer();
        m_dstype = m_activity.getDatasetType();
        if (m_labelLayer != null) {
            refreshIsVisable();
            refreshfield();
            refreshlabelposition();
            refreshfontsize();
            refreshfontcolor();
        } else {
            m_themelayout.setVisibility(View.GONE);
        }
        getFieldList();
        setOnCheckedChange();
        themeLabelLayer();
    }

    /**
     * 主界面添加标签专题图成功后返回的图层对象
     */
    private void themeLabelLayer() {
        m_themeLabel = RxBus.getDefault().toObserverable(ThemepostlayerEvent.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                //在主线程进行观察，可做UI更新操作
                .observeOn(AndroidSchedulers.mainThread())
                //观察的对象
                .subscribe(new Action1<ThemepostlayerEvent>() {
                    @Override
                    public void call(ThemepostlayerEvent event) {
                        if (event != null) {
                            m_labelLayer = event.m_layer;
                            if (m_labelLayer != null) {
                                m_themelayout.setVisibility(View.VISIBLE);
                                refreshIsVisable();
                                refreshfield();
                                refreshlabelposition();
                                refreshfontsize();
                                refreshfontcolor();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 标签专题图开关checked事件
     */
    private void setOnCheckedChange() {
        m_swLabelTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (m_labelLayer != null) {
                    if (isChecked) {
                        m_themelayout.setVisibility(View.VISIBLE);
                        m_labelLayer.setVisible(true);
                        refreshIsVisable();
                        refreshfield();
                        refreshlabelposition();
                        refreshfontsize();
                        refreshfontcolor();
                    } else {
                        m_themelayout.setVisibility(View.GONE);
                        m_labelLayer.setVisible(false);
                    }
                } else {
                    RxBus.getDefault().post(new ThemeLayerEvent(m_layer));
//                    m_themelayout.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 标注位置点击事件
     */
    @OnClick(R.id.ll_position)
    void onLabelPositionClick() {
        LabelPositionDialog dialog = new LabelPositionDialog(m_activity, R.style.common_alert_dialog, m_dstype);
        dialog.setOnButtonClickListener(new LabelPositionDialog.OnClickListener() {
            @Override
            public void getPositionvalue(TextAlignmentType type) {
                m_type = type;
                ThemeLabelEntityEvent entity = new ThemeLabelEntityEvent();
                m_labelpostion.setText(entity.getLabelPositionName(type));
                String s = new Gson().toJson(m_layer, Layer.class);
                entity.layerjson = s;
                entity.field = m_field;
                entity.type = type;
                entity.fontsize = m_labelLayer.getTextStyle().width;
                entity.forecolor = m_labelLayer.getTextStyle().foreColor;

                RxBus.getDefault().post(entity);
            }
        });
        dialog.show();

    }

    @OnClick(R.id.ll_mark_color)
    void OnLLMarkColorClick() {
        showColorPickerDialog();
    }

    /**
     * 标注字段点击事件
     */
    @OnClick(R.id.ll_field)
    void onLabelFieldClick() {
        final FieldListDialogUtils dialog = new FieldListDialogUtils(m_activity);
        dialog.showDialog(m_fieldlist, new FieldListDialogUtils.OnItemFieldListener() {
            @Override
            public void getFieldName(String field) {
                m_field = field;
                m_labelfield.setText(field);
                String s = new Gson().toJson(m_layer, Layer.class);
                ThemeLabelEntityEvent entity = new ThemeLabelEntityEvent();
                entity.layerjson = s;
                entity.field = field;
                if (m_type != null) {
                    entity.type = m_type;
                } else {
                    entity.type = TextAlignmentType.Horizontal;
                }
                entity.fontsize = m_labelLayer.getTextStyle().width;
                entity.forecolor = m_labelLayer.getTextStyle().foreColor;
                //去主界面添加标签专题图
                RxBus.getDefault().post(entity);
            }
        });
    }

    /**
     * 标注字体大小点击事件
     */
    @OnClick(R.id.ll_fontsize)
    void onFontSizeClick() {
        if (m_labelLayer != null) {
            EditTextDialog dialog = new EditTextDialog(getContext(), R.style.common_alert_dialog);

            dialog.setOnButtonClickListener(new EditTextDialog.OnClickListener() {
                @Override
                public void onClick(String num) {
                    TextStyle textStyle = m_labelLayer.getTextStyle();
                    int value;
                    try {
                        value = Integer.valueOf(num);
                    } catch (Exception e) {
                        value = 0;
                    }
                    if (value != 0) {
                        if (textStyle == null) {
                            textStyle = new TextStyle();
                        }
                        textStyle.height = Double.valueOf(num);
                        textStyle.width = Double.valueOf(num);
                        m_labelLayer.setTextStyle(textStyle);
                        refreshfontsize();
                    }

                }
            });
            dialog.show();
            dialog.setNumValue(m_labelLayer.getTextStyle().height);
        }
    }

    /**
     * 颜色对话框
     */
    void showColorPickerDialog() {
        if (mColorPickerDialog == null) {
            mColorPickerDialog = new ColorPickerDialog(getActivity(), R.style.common_alert_dialog);
            mColorPickerDialog.setOnButtonClickListener(new ColorPickerDialog.OnColorPickerClickListener() {
                @Override
                public void cancel() {

                }

                @Override
                public void confirm(int color) {
                    TextStyle textStyle = m_labelLayer.getTextStyle();
                    if (textStyle == null) {
                        textStyle = new TextStyle();
                    }
                    int colorOld = textStyle.foreColor;
                    if (colorOld != color) {
                        textStyle.foreColor = color;
                        m_labelLayer.setTextStyle(textStyle);
                    }
                    refreshfontcolor();

                }
            });
        }

        mColorPickerDialog.show();
    }

    /**
     * 刷新标签专题图布局是否可见
     */
    private void refreshIsVisable() {
        if (m_labelLayer.isVisible()) {
            m_swLabelTheme.setChecked(true);
            m_themelayout.setVisibility(View.VISIBLE);
        } else {
            m_swLabelTheme.setChecked(false);
            m_themelayout.setVisibility(View.GONE);
        }
    }

    private void refreshfield() {
    }

    private void refreshlabelposition() {
    }

    /**
     * 刷新标注字体大小
     */

    private void refreshfontsize() {
        TextStyle lineStyle = m_labelLayer.getTextStyle();
        m_labelfontsize.setText(lineStyle.height + "");

    }
    /**
     * 刷新标注字体颜色
     */
    private void refreshfontcolor() {
        TextStyle texttyle = m_labelLayer.getTextStyle();
        mTvMarkColor.setBackgroundColor(texttyle.foreColor);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mColorPickerDialog != null) {
            mColorPickerDialog = null;
        }
        if (m_themeLabel.isUnsubscribed()) {
            m_themeLabel.unsubscribe();
        }
    }

    private static final class LayerLabelFrgtFrgtHandle {

        private static final LayerLabelFrgt NEWINSTANC = new LayerLabelFrgt();
    }

    public static LayerLabelFrgt newInstance() {
        return LayerLabelFrgtFrgtHandle.NEWINSTANC;
    }
}
