package cn.piesat.minemonitor;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.piesat.minemonitor.mapdata.adapter.LayerItemDragAdapter;
import cn.piesat.minemonitor.mapdata.utils.SysConstant;
import cn.piesat.minemonitor.rxbus.RxBus;
import cn.piesat.minemonitor.rxbus.event.LayerSetEvent;
import cn.piesat.minemonitor.rxbus.event.RefreProject;
import cn.piesat.minemonitor.rxbus.event.ThemepostlayerEvent;
import cn.piesat.minemonitor.util.ToastUtil;
import pie.core.DatasetType;
import pie.core.Layer;
import pie.core.LayerSet;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class LayerManageActivity extends BaseActivity implements OnItemSwipeListener, BaseQuickAdapter.OnItemClickListener, OnItemDragListener,
        BaseQuickAdapter.OnItemChildClickListener {

    private List<Layer> mLayersList;
    private List<Layer> mLableLayerList;
    private LayerItemDragAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    private Layer mBaseLayer;
    private int itemMoveStarPostion = -1;
    private int itemMoveEndPostion = -1;
    private LayerSet mVectorLayerSet;
    private Subscription m_subscription;
    private final String Layerset_Name = "矢量图层集";
    private LayerSet m_layerSet;
    String strDelete = "右滑删除";
    int mTextWidth;
    int mTextHeight;
    Paint paint;
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tvCommTitle)
    TextView tvCommTitle;
    @BindView(R.id.tv_layer_name)
    TextView tvBaseMap;
    @BindView(R.id.img_title_left)
    ImageView imgTitleLeft;
    @BindView(R.id.img_title_right)
    ImageView imgTitleRight;
    @BindView(R.id.iv_layer_visibility)
    ImageView imgBaseLayerVisible;
    @BindView(R.id.layout_base_map)
    LinearLayout baseMapLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer_manage);
        ButterKnife.bind(this);
        initTitle();
        initView();
        subscribe();
        initPaint();
    }


    private void initTitle() {
        tvCommTitle.setText(R.string.layer_setting_title);
        imgTitleLeft.setVisibility(View.VISIBLE);
        imgTitleRight.setVisibility(View.VISIBLE);
        imgTitleLeft.setBackgroundResource(R.drawable.ic_back);
        //imgTitleRight.setBackgroundResource(R.drawable.ic_layer_refresh);
        imgTitleRight.setVisibility(View.GONE);
    }

    @OnClick(R.id.img_title_left)
    void onTitleLeftImgClick() {
        onBackPressed();
    }

    @OnClick(R.id.img_title_right)
    void onTitleRightClick() {
//        setResult(MainActivity.class, RESULT_OK);
        RxBus.getDefault().post(new RefreProject());
    }

    @OnClick(R.id.iv_layer_visibility)
    void onBaseLayerVisibleClick() {
       /* if (mBaseLayer != null) {
            mBaseLayer.setVisible(!mBaseLayer.isVisible());
            if (mBaseLayer.isVisible()) {
                imgBaseLayerVisible.setBackgroundResource(R.drawable.ic_eye_on);
            } else {
                imgBaseLayerVisible.setBackgroundResource(R.drawable.ic_eye_off);
            }
        }*/
    }


    private void initView() {
        mLayersList = new ArrayList<>();
        mLableLayerList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initData();
        mAdapter = new LayerItemDragAdapter(mLayersList);
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        //mAdapter.enableDragItem(mItemTouchHelper);
        mAdapter.setOnItemDragListener(this);
//        mAdapter.disableSwipeItem();
        // mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(this);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String json = bundle.getString(SysConstant.BUNDLE_KEY.KEY_ALL_LAYERSET);
            List<LayerSet> layerSets = new Gson().fromJson(json, new TypeToken<List<LayerSet>>() {
            }.getType());
            initLayers(layerSets);
        }
    }

    private void initLayers(List<LayerSet> layerSets) {
        if (layerSets == null) {
            return;
        }
        if (mLayersList != null) {
            mLayersList.clear();
        }
        if (mLayersList == null) {
            mLayersList = new ArrayList<>();
        }
        if (layerSets.size() > 0) {
            //第一个图层即为底图
            LayerSet baseMapLayerSet = layerSets.get(0);
            if (baseMapLayerSet.getLayerCount() > 0) {
                mBaseLayer = baseMapLayerSet.getLayer(0);
                if (mBaseLayer.getDataset().getType() == DatasetType.MBIMAGE.getValue()) {
                    //设置底图图层名称
                    tvBaseMap.setText(mBaseLayer.getName());
                    //显示底图显示或隐藏状态
                    if (mBaseLayer.isVisible()) {
                        imgBaseLayerVisible.setBackgroundResource(R.drawable.ic_eye_on);
                    } else {
                        imgBaseLayerVisible.setBackgroundResource(R.drawable.ic_eye_off);
                    }
                } else {
                    //如果没有底图则不显示底图view
                    tvBaseMap.setVisibility(View.GONE);
                    baseMapLayout.setVisibility(View.GONE);
                }
            } else {
                //如果没有底图则不显示底图view
                tvBaseMap.setVisibility(View.GONE);
                baseMapLayout.setVisibility(View.GONE);
            }
            for (LayerSet layerset : layerSets) {
                if (layerset.getLayerSetName().equals(Layerset_Name) || layerset.getLayerSetName().equals(SysConstant.PropertyName.XZQH)) {
                    mVectorLayerSet = layerset;
                    List<Layer> layers = new ArrayList<>();
                    int count = layerset.getLayerCount();
                    for (int i = 0; i < count; i++) {
                        Layer layer = layerset.getLayer(i);
                        layers.add(layer);

                    }
                    mLayersList.addAll(layers);
                } else if (layerset.getLayerSetName().equals("LabelLayerset")) {
                    List<Layer> lableLayer = new ArrayList<>();
                    int count = layerset.getLayerCount();
                    for (int i = 0; i < count; i++) {
                        Layer layer = layerset.getLayer(i);
                        lableLayer.add(layer);
                    }
                    mLableLayerList.addAll(lableLayer);

                }
            }

        }
    }

    private Layer getLableByLayer(Layer ly) {
        Layer temp = null;
        if (mLableLayerList != null && mLableLayerList.size() > 0) {
            for (int i=0;i<mLableLayerList.size();i++){
                Layer layer=mLableLayerList.get(i);
                if (layer != null && ly != null) {
                    String layername = layer.getName();
                    String ln = ly.getName();
                    if (layer.getName().startsWith(ly.getName())) {
                        temp = layer;
                        break;
                    }
                }
            }
        }
        return temp;
    }

    private void subscribe() {
        m_subscription = RxBus.getDefault().toObserverable(Object.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                //在主线程进行观察，可做UI更新操作
                .observeOn(AndroidSchedulers.mainThread())
                //观察的对象
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object obj) {
                        if (obj instanceof LayerSetEvent) {
                            LayerSetEvent event = (LayerSetEvent) obj;
                            initLayers(event.layerSets);
                            mAdapter.notifyDataSetChanged();
                            ToastUtil.show(LayerManageActivity.this, "刷新完成");
                        } else if (obj instanceof ThemepostlayerEvent) {//当添加标签专题图时，如果不存在，则去主界面添加themelay,并记得刷新图层管理界面的labellayerset集合
//                            String json = (String) obj;
//                            List<LayerSet> layerSets = new Gson().fromJson(json, new TypeToken<List<LayerSet>>() {
//                            }.getType());
//                            initLayers(layerSets);

                            ThemepostlayerEvent themelayer = ((ThemepostlayerEvent) obj);
                            if (mLableLayerList != null ) {
                                if (mLableLayerList.size() > 0) {
                                    List<String> lynamelist = new ArrayList<>();
                                    for (int i = 0; i < mLableLayerList.size(); i++) {
                                        Layer ly = mLableLayerList.get(i);
                                        if (ly != null) {
                                            lynamelist.add(ly.getName());
                                        }

                                    }
                                    if (lynamelist.contains(themelayer.m_layer.getName())) {
                                        mLableLayerList.remove(themelayer.m_layer);
                                        mLableLayerList.add(themelayer.m_layer);
                                    } else {
                                        mLableLayerList.add(themelayer.m_layer);
                                    }
                                } else {
                                    mLableLayerList.add(themelayer.m_layer);
                                }
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

    @Override
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
        Log.d("yyh", "开始" + pos);
        itemMoveStarPostion = pos;
    }

    @Override
    public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
        Log.d("yyh", "move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());

    }

    @Override
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
        Log.d("yyh", "结束" + pos);
        itemMoveEndPostion = pos;
        if (itemMoveStarPostion != -1 && itemMoveEndPostion != -1) {
            LayerSet layerSet = mVectorLayerSet;
            if (layerSet != null) {
                layerSet.moveTo(itemMoveStarPostion, itemMoveEndPostion);
            }
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_layer_visibility:
               /* Layer layer = (Layer) adapter.getItem(position);
                layer.setVisible(!layer.isVisible());
                if (layer.isVisible()) {
                    view.setBackgroundResource(R.drawable.ic_eye_on);
                } else {
                    view.setBackgroundResource(R.drawable.ic_eye_off);
                }*/
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Layer layer = (Layer) adapter.getItem(position);
        Layer lableLayer = getLableByLayer(layer);
        Gson gson = new Gson();
        String json = gson.toJson(layer, Layer.class);
        String labeljson = gson.toJson(lableLayer, Layer.class);
        Bundle bundle = new Bundle();
        bundle.putString(SysConstant.BUNDLE_KEY.KEY_LAYER_JSON, json);
        bundle.putString(SysConstant.BUNDLE_KEY.KEY_LABELLAYER_JSON, labeljson);
        Intent intent = new Intent(this, LayerSettingActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        if (m_subscription.isUnsubscribed()) {
            m_subscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
    }

    @Override
    public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
    }

    @Override
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
        if (m_layerSet != null) {
            int count = m_layerSet.getLayerCount();
            if (count > pos) {
                String name = m_layerSet.getLayer(pos).getName();
                m_layerSet.removeLayer(name);
            }
        }
    }

    @Override
    public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
        canvas.drawColor(ContextCompat.getColor(LayerManageActivity.this, R.color.colorPrimary));
        canvas.drawText(strDelete, (dX - mTextWidth) / 2, canvas.getClipBounds().centerY() + mTextHeight / 2, paint);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);
        Rect rect = new Rect();
        paint.getTextBounds(strDelete, 0, strDelete.length(), rect);
        mTextWidth = rect.width();
        mTextHeight = rect.height();
    }
}
