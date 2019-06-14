package cn.piesat.minemonitor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.piesat.minemonitor.mapdata.base.FrgtManager;
import cn.piesat.minemonitor.mapdata.fragment.LayerLabelFrgt;
import cn.piesat.minemonitor.mapdata.fragment.LayerLineFrgt;
import cn.piesat.minemonitor.mapdata.fragment.LayerPointFrgt;
import cn.piesat.minemonitor.mapdata.fragment.LayerRegionFrgt;
import cn.piesat.minemonitor.mapdata.utils.SysConstant;
import cn.piesat.minemonitor.util.ToastUtil;
import pie.core.DatasetType;
import pie.core.Layer;
import pie.core.MapView;

/**
 * 配置图层
 */
public class LayerSettingActivity extends BaseActivity {

    @BindView(R.id.tvCommTitle)
    TextView tvCommTitle;
    @BindView(R.id.img_title_left)
    ImageView imgTitleLeft;

    LayerPointFrgt mPointFrgt;
    LayerLineFrgt mLineFrgt;
    LayerRegionFrgt mRegionFrgt;
    LayerLabelFrgt mLabelFrgt;


    private Layer m_layer;
    private Layer m_labellayer;
    private DatasetType m_datasettype;

    public Layer getLayer(){
        return m_layer;
    }
    public Layer getLabelLayer(){
        return m_labellayer;
    }
    public DatasetType getDatasetType(){
        return m_datasettype;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer_style_setting);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setTitleValue(R.string.layer_setting_title);
        imgTitleLeft.setVisibility(View.VISIBLE);
        initData();
        initfrgt();
    }

    private void initfrgt() {
        if(m_layer == null){
            return;
        }
        mLabelFrgt = LayerLabelFrgt.newInstance();
        FrgtManager.addFragment(this, R.id.frgt_label, mLabelFrgt, LayerLabelFrgt.FRGT_FLAG);
        m_datasettype = DatasetType.valueOf(m_layer.getDataset().getType());
        switch (m_datasettype) {
            case POINT:
                mPointFrgt = LayerPointFrgt.newInstance();
                FrgtManager.addFragment(this, R.id.frgt_type, mPointFrgt, LayerPointFrgt.FRGT_FLAG);
                break;
            case LINE:
                mLineFrgt = LayerLineFrgt.newInstance();
                FrgtManager.addFragment(this, R.id.frgt_type, mLineFrgt, LayerLineFrgt.FRGT_FLAG);
                break;
            case REGION:
                mRegionFrgt = LayerRegionFrgt.newInstance();
                FrgtManager.addFragment(this, R.id.frgt_type, mRegionFrgt, LayerRegionFrgt.FRGT_FLAG);
                break;
            default:
                ToastUtil.show(this,"图层类型错误");
                break;
        }
    }

    private void initData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String json = bundle.getString(SysConstant.BUNDLE_KEY.KEY_LAYER_JSON);
            String labeljson=bundle.getString(SysConstant.BUNDLE_KEY.KEY_LABELLAYER_JSON);
            Gson gson = new Gson();
            m_layer= gson.fromJson(json,Layer.class);
            m_labellayer=gson.fromJson(labeljson,Layer.class);
        }
    }

    public void setTitleValue(int resId) {
        tvCommTitle.setText(resId);
        imgTitleLeft.setBackgroundResource(R.drawable.ic_back);
    }

    @OnClick(R.id.img_title_left)
    void onTitleLeftImgClick() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

