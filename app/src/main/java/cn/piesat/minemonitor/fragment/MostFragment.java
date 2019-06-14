package cn.piesat.minemonitor.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.HomeActivity;
import cn.piesat.minemonitor.LayerManageActivity;
import cn.piesat.minemonitor.LayerSettingActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.mapdata.utils.SysConstant;
import cn.piesat.minemonitor.util.ToastUtil;
import pie.core.Layer;
import pie.core.LayerSet;
import pie.core.MapView;

/**
 *
 */
public class MostFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static String FRGT_FLAG = "MapInitFrgt";
    private PopupWindow poplayer;
    private ImageView layer;
    private ImageView ic_layer;
    private View popupView3;
    private HomeActivity momeActivity;
    private Boolean mark = false;
    private Boolean mark2 = false;
    private Boolean mark3 = false;
    private ImageView img_icon;
    private ImageView img_icon2;
    private ImageView img_positioning;
    private LayerSet layerSet;
    private LayerSet layerSet1;
    private LayerSet layerSet2;
    private LayerSet layerSet3;
    private LayerSet layerSet4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_most, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layer = view.findViewById(R.id.iv_layer);
        ic_layer = view.findViewById(R.id.iv_ic_layer);
        layer.setOnClickListener(this);
        ic_layer.setOnClickListener(this);
        initPop();
        ToggleButton yjtb = popupView3.findViewById(R.id.tb_jytb);
        yjtb.setOnCheckedChangeListener(this);
        ToggleButton yxsj = popupView3.findViewById(R.id.tb_yxsj);
        yxsj.setOnCheckedChangeListener(this);
        ToggleButton xzqh = popupView3.findViewById(R.id.tb_xzqh);
        xzqh.setOnCheckedChangeListener(this);
    }

    public void setActivity(HomeActivity homeActivity) {
        momeActivity = homeActivity;
    }

    private void initPop() {
        popupView3 = getLayoutInflater().inflate(R.layout.pop_layer, null);
        poplayer = new PopupWindow(popupView3, 400, ActionBar.LayoutParams.MATCH_PARENT, true);
        poplayer.setTouchable(true);
        poplayer.setOutsideTouchable(true);
        poplayer.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        poplayer.setAnimationStyle(R.style.PopupAnimationRight);
        getVeiw();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_layer:
                poplayer.showAtLocation(v.findViewById(R.id.iv_layer), Gravity.RIGHT, 0, 0);
                break;
            case R.id.iv_ic_layer:
                //编辑图斑颜色
                MapView mapView = momeActivity.getMapView();
                List<LayerSet> layerSets = new ArrayList<>();
                int count = mapView.getLayerSetCount();
                for (int i = 0; i < count; i++) {
                    //按图层集索引获取图层集。
                    LayerSet layerSet = mapView.getLayerSet(i);
                    layerSets.add(layerSet);
                }
                Gson gons = new Gson();
                String json = gons.toJson(layerSets);
                Bundle bundle = new Bundle();
                bundle.putString(SysConstant.BUNDLE_KEY.KEY_ALL_LAYERSET,json);
                Intent intent = new Intent(getContext(),LayerManageActivity.class);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                startActivity(intent);
                break;
        }
    }

    public void getVeiw() {
        img_icon = (ImageView) popupView3.findViewById(R.id.img_icon);
        img_icon2 = (ImageView) popupView3.findViewById(R.id.img_icon2);
        img_icon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给控件设置图片
                if (mark == false) {
                    mark2 = false;
                    mark3 = true;
                    img_icon2.setImageResource(R.drawable.map_satellite_on);
                    img_icon.setImageResource(R.drawable.map_flat_ok);
                    momeActivity.switchMapSon(true);
                    mark = true;
                }
            }
        });
        img_icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给控件设置图片
                if(mark3 == true){
                    if(mark2 == false){
                        mark = false;
                        img_icon.setImageResource(R.drawable.map_flat_on);
                        img_icon2.setImageResource(R.drawable.map_satellite_ok);
                        momeActivity.switchMapSon(false);
                        mark2 = true;
                    }
                 }
             }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_jytb:
                layerSet = momeActivity.getMapView().getLayerSet(SysConstant.PropertyName.SLTCJ);
                layerSet3 = momeActivity.getMapView().getLayerSet(SysConstant.PropertyName.TBBH);
                layerSet4 = momeActivity.getMapView().getLayerSet("LabelLayerset");
                if(this.layerSet != null){
                    if (isChecked) {
                        if(this.layerSet.isVisible()){
                            /*this.layerSet.setVisible(false);
                            layerSet3.setVisible(false);*/
                            if(layerSet != null){
                                layerSet.setVisible(false);
                            }
                            if(layerSet3 != null){
                                layerSet3.setVisible(false);
                            }
                            if(layerSet4 != null){
                                layerSet4.setVisible(false);
                            }

                        }
                    } else {
                        if(!this.layerSet.isVisible()){
                            /*this.layerSet.setVisible(true);
                            layerSet3.setVisible(true);
                            layerSet4.setVisible(true);*/
                            if(layerSet != null){
                                layerSet.setVisible(true);
                            }
                            if(layerSet3 != null){
                                layerSet3.setVisible(true);
                            }
                                if(layerSet4 != null){
                                layerSet4.setVisible(true);
                            }
                        }
                    }
                }
                break;
            case R.id.tb_yxsj:
                layerSet1 = momeActivity.getMapView().getLayerSet(SysConstant.PropertyName.YXTC);
                if(layerSet1 != null){
                    if (isChecked) {
                        if(layerSet1.isVisible()){
                            layerSet1.setVisible(false);
                        }
                    } else {
                        if(!layerSet1.isVisible()){
                            layerSet1.setVisible(true);
                        }
                    }
                }
                break;
            case R.id.tb_xzqh:
                layerSet2 = momeActivity.getMapView().getLayerSet(SysConstant.PropertyName.XZQH);
                if(layerSet2 != null){
                    if (isChecked) {
                        if(layerSet2.isVisible()){
                            layerSet2.setVisible(false);
                        }
                    } else {
                        if(!layerSet2.isVisible()){
                            layerSet2.setVisible(true);
                        }
                    }
                }
                break;
        }
       }
    }

