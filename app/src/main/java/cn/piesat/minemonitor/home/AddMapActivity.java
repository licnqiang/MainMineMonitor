package cn.piesat.minemonitor.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.HomeActivity;
import cn.piesat.minemonitor.MapRelatedActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.adapter.impl.AddTBgcdAdapter;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.entity.CheckInfoEntiy;
import cn.piesat.minemonitor.entity.TaskListEntity;
import cn.piesat.minemonitor.home.minetype.MineTypeQueryActivity;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;
import cn.piesat.minemonitor.setting.CleanActivity;

public class AddMapActivity extends BaseActivity implements AddTBgcdAdapter.InnerItemOnclickListener, AdapterView.OnItemClickListener {
    private CustomSQLTools s;
    private String tasknum;//任务编号
    private String jybh1;//解译编号
    private List<String> m_dataList;//所有的数据集合
    private Intent intent;
    private TaskListEntity listbean;
    private String typeBZ;
    private String yjlxShow;
    private Bundle bundle;
    private SharedPreferences sper;
    private SharedPreferences.Editor edit;
    private String cx = "null";
    private String cy = "null";
    private String tbType = "";
    private int num = -1;
    //观察点列表
    private CheckInfoEntiy startinfo;
    private UUID uuid;
    private String uniqueId;
    private List<CheckInfoEntiy> gcdlist = new ArrayList<>();
    private AddTBgcdAdapter adapter1;
    private ListView gcdlb;
    private List<CheckInfoEntiy> cieChoose;
    private String rwzt;
    private String getKsmc;
    private String getJylx;
    private String getQxmc;
    private String getDllx;
    private String getPhlx;
    private String getYxdx;
    private String getZldx;
    private String getKckz;
    private String getKcfs;
    private String getYzdd;
    private String getYzfx;
    private String getBz;
    private String addX = "null";
    private String addY = "null";
    private String seeX = "111.6911";
    private String seeY = "40.8027";


    @OnClick(R.id.ll_back)
    public void back() {
        finish();
    }

    @BindView(R.id.tv_task_number)
    TextView taskNum;//任务编号
    @BindView(R.id.tv_map_number)
    TextView yjbh;//解译编号
    @BindView(R.id.tv_ks_name)
    TextView ksName;//矿山名称
    @BindView(R.id.tv_jylx)
    TextView yjlx;//解译类型
    @BindView(R.id.tv_qxmc)
    TextView qxmc;//旗县名称
    @BindView(R.id.tv_dllx)
    TextView dllx;//地类类型
    @BindView(R.id.tv_phlx)
    TextView phlx;//破坏类型
    @BindView(R.id.tv_yxdx)
    TextView yxdx;//影响对象
    @BindView(R.id.tv_zldx)
    TextView zldx;//治理对象
    @BindView(R.id.tv_kckz)
    TextView kckz;//开采矿种
    @BindView(R.id.tv_kcfs)
    TextView kcfs;//开采方式
    @BindView(R.id.et_yzfx)
    TextView yzfx;//验证分析
    @BindView(R.id.et_note)
    TextView note;//备注
    @BindView(R.id.tv_yzdd)
    TextView yzdd;//备注
    @BindView(R.id.tv_yzfxms)
    TextView yzfxms;//备注
    @BindView(R.id.btn_change_map)
    TextView tbdw;//图斑定位
    //隐藏显示
    @BindView(R.id.ll_dllx)
    LinearLayout LLdllx;//地类类型
    @BindView(R.id.ll_phlx)
    LinearLayout LLphlx;//破坏类型
    @BindView(R.id.ll_yxdx)
    LinearLayout LLyxdx;//影响对象
    @BindView(R.id.ll_zldx)
    LinearLayout LLzldx;//治理对象
    @BindView(R.id.LL_yxdx_zldx)
    LinearLayout LLyxdxzldx;//影响对象治理对象
    @BindView(R.id.ll_dllx_phlx)
    LinearLayout LLdllxphlx;//地类类型破坏类型
    @BindView(R.id.ll_addtb)
    LinearLayout addtb;//添加图斑


    @OnClick({R.id.ll_addtb, R.id.ll_update_info, R.id.ll_layout, R.id.ll_gcd_sz, R.id.ll_over, R.id.tv_jylx, R.id.tv_qxmc, R.id.tv_dllx, R.id.tv_phlx, R.id.tv_yxdx, R.id.tv_zldx, R.id.tv_kckz, R.id.tv_kcfs, R.id.tv_yzdd, R.id.et_yzfx, R.id.et_note, R.id.btn_change_map, R.id.tv_yzfxms})
    public void onViewClick(View view) {
        String typeData = "";
        Calendar a = Calendar.getInstance();
        //矿山名称
        getKsmc = ksName.getText().toString().replace(" ", "");
        //解译类型
        getJylx = yjlx.getText().toString().replace(" ", "");
        //旗县名称
        getQxmc = qxmc.getText().toString().replace(" ", "");
        //地类类型
        getDllx = dllx.getText().toString().replace(" ", "");
        //破坏类型
        getPhlx = phlx.getText().toString().replace(" ", "");
        //影响对象
        getYxdx = yxdx.getText().toString().replace(" ", "");
        //治理对象
        getZldx = zldx.getText().toString().replace(" ", "");
        //开采矿种
        getKckz = kckz.getText().toString().replace(" ", "");
        //开采矿种
        getKcfs = kcfs.getText().toString().replace(" ", "");
        //验证地点
        getYzdd = yzdd.getText().toString().replace(" ", "");
        //验证分析
        getYzfx = yzfx.getText().toString().replace(" ", "");
        //备注
        getBz = note.getText().toString().replace(" ", "");

        switch (view.getId()) {
            case R.id.ll_addtb://添加图斑
                if (!getJylx.equals("")) {
                    uuid = UUID.randomUUID();
                    //
                    uniqueId = uuid.toString();
                    listbean.setCheckNo(uniqueId);
                    addX = sper.getString("GCDX", "111.6911");
                    addY = sper.getString("GCDY", "40.8027");
                    listbean.setKsCenterCoordX(addX);
                    listbean.setKsCenterCoordY(addY);
                    listbean.setState(Constant.UNVERIFIED);
                    listbean.setShpName((yjlx.getText().toString() + "_" + a.get(Calendar.YEAR)));
                    if (getJylx.equals(Constant.ksm) || getJylx.equals(Constant.ksd)) {
                        if (getDllx.equals("")) {
                            Toast.makeText(this, "地类类型不为空!", Toast.LENGTH_SHORT).show();
                        } else if (getPhlx.equals("")) {
                            Toast.makeText(this, "破坏类型不为空!", Toast.LENGTH_SHORT).show();
                        } else if (!getDllx.equals("") && !getDllx.equals("")) {
                            listbean.setKsDLMC(getDllx);//地类类型
                            listbean.setKsPHLX(getPhlx);//破坏类型
                        }
                    } else if (getJylx.equals(Constant.kszhm) || getJylx.equals(Constant.kszhx)) {
                        if (getDllx.equals("")) {
                            Toast.makeText(this, "地类类型不为空!", Toast.LENGTH_SHORT).show();
                        } else if (getYxdx.equals("")) {
                            Toast.makeText(this, "影响对象不为空!", Toast.LENGTH_SHORT).show();
                        } else if (!getDllx.equals("") && !getYxdx.equals("")) {
                            listbean.setKsDLMC(getDllx);//地类类型
                            listbean.setKsYXDX(getYxdx);//影响对象
                        }
                    } else if (getJylx.equals(Constant.kszl)) {
                        if (getZldx.equals("")) {
                            Toast.makeText(this, "治理对象不为空!", Toast.LENGTH_SHORT).show();
                        } else {
                            listbean.setKsZLDX(getZldx);//治理对象
                        }
                    }
                    if (tasknum.equals("")) {
                        Toast.makeText(this, "任务编号不为空!", Toast.LENGTH_SHORT).show();
                    } else if (jybh1.equals("")) {
                        Toast.makeText(this, "解译编号不为空，请重新获取!", Toast.LENGTH_SHORT).show();
                    } else if (getKsmc.equals("")) {
                        Toast.makeText(this, "矿山名称不为空!", Toast.LENGTH_SHORT).show();
                    } else if (getQxmc.equals("")) {
                        Toast.makeText(this, "旗县名称不为空!", Toast.LENGTH_SHORT).show();
                    } else if (getKckz.equals("")) {
                        Toast.makeText(this, "开采矿种不为空!", Toast.LENGTH_SHORT).show();
                    } else if (getKcfs.equals("")) {
                        Toast.makeText(this, "开采方式不为空!", Toast.LENGTH_SHORT).show();
                    } else if (getYzdd.equals("")) {
                        Toast.makeText(this, "验证地点不为空!", Toast.LENGTH_SHORT).show();
                    } else if (tbType.equals("")) {
                        Toast.makeText(this, "图斑属性不为空!", Toast.LENGTH_SHORT).show();
                    } else if (addX.equals("null") || addY.equals("null")) {
                        Toast.makeText(this, "定位点坐标不为空", Toast.LENGTH_SHORT).show();
                    } else if (getYzfx.equals("")) {
                        Toast.makeText(this, "验证分析不为空", Toast.LENGTH_SHORT).show();
                    } else if (!tasknum.equals("") && !jybh1.equals("") && !getKsmc.equals("") && !getQxmc.equals("")
                            && !getKckz.equals("") && !getKcfs.equals("") && !tbType.equals("") && !getYzdd.equals("")
                            && !getYzfx.equals("")) {
                        //查询所选行政区保存编码
                        listbean.setXzqCode(s.getQXcode(getQxmc, AddMapActivity.this));//旗县编码
                        listbean.setXzqChange(Constant.NO);//旗县是否变更
                        listbean.setIsnew(Constant.YES);//是否为新增图斑
                        listbean.setRecoredUser(SpHelper.getStringValue("USERNAME"));//操作人信息
                        //以显示部分
                        listbean.setTaskNumber(tasknum);//任务编号
                        listbean.setMapNumber(jybh1);//解译编号
                        listbean.setMineNumber(getKsmc);//矿山名称
                        listbean.setDataType(getJylx);//解译类型
                        listbean.setXzqName(getQxmc);//旗县名称
                        listbean.setKsDLMC(getDllx);//地类类型
                        listbean.setKsPHLX(getPhlx);//破坏类型
                        listbean.setKsYXDX(getYxdx);//影响对象
                        listbean.setKsZLDX(getZldx);//治理对象
                        listbean.setMineType(getKckz);//开采矿种
                        listbean.setKsKCFS(getKcfs);//开采方式
                        listbean.setKsFieldso(tbType);//图斑属性
                        listbean.setCheckAdressName(getYzdd);//验证地点
                        listbean.setKsTBCheckRES(Constant.HOLE);//与解译结果比对
                        listbean.setDes(getYzfx);//验证分析
                        listbean.setRemark(getBz);//备注
                        s.tbAdd(AddMapActivity.this, listbean.getCheckNo(), listbean.getTaskNumber(), listbean.getXzqName(), listbean.getXzqCode(), listbean.getState(), listbean.getMineNumber()
                                , listbean.getKsKCFS(), listbean.getMineType(), listbean.getKsDLMC(), listbean.getKsPHLX(), listbean.getKsYXDX(), listbean.getKsZLDX(), listbean.getMapNumber(), listbean.getKsFieldso(), listbean.getKsTBCheckRES(), listbean.getKsCenterCoordX()
                                , listbean.getKsCenterCoordY(), listbean.getRecoredUser(), listbean.getShpName(), listbean.getDataType(), listbean.getDes(), listbean.getRemark(), listbean.getIsnew(), listbean.getXzqChange(), listbean.getCheckAdressName());
                        if (rwzt.equals(Constant.EXPORT)) {
                            s.updateTastState(listbean.getTaskNumber(), AddMapActivity.this, Constant.EXPORT);
                        } else if (rwzt.equals(Constant.IMPLEMENT)) {
                            s.updateTastState(listbean.getTaskNumber(), AddMapActivity.this, Constant.IMPLEMENT);
                        } else if (rwzt.equals(Constant.VERIFIED)) {
                            s.updateTastState(listbean.getTaskNumber(), AddMapActivity.this, Constant.IMPLEMENT);
                        }

                        addtb.setVisibility(View.GONE);
                        bundle = new Bundle();
                        intent = new Intent(this, ContentListActivity.class);
                        bundle.putSerializable("item", listbean);
                        intent.putExtra("from", 3);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "请选择解译类型", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.ll_update_info://更新信息
                if (addtb.getVisibility() == View.VISIBLE) {
                    Toast.makeText(this, "请先执行添加图斑操作", Toast.LENGTH_SHORT).show();
                } else if (addtb.getVisibility() == View.GONE) {
                    num = s.getTBgcd(AddMapActivity.this, listbean.getCheckNo()).size();
                }
                if (num == 0) {
                    Toast.makeText(this, "请至少添加一个观察点并添加图片！", Toast.LENGTH_SHORT).show();
                } else if (addtb.getVisibility() == View.GONE && num > 0) {
                    if (num == s.getAllPoints(listbean.getTaskNumber(), AddMapActivity.this).size()) {
                        s.updateTastState(listbean.getTaskNumber(), AddMapActivity.this, Constant.VERIFIED);
                        s.updateEnd(AddMapActivity.this, listbean.getTaskNumber(), Constant.VERIFIED);
                    } else if (num > 0) {
                        s.updateTastState(listbean.getTaskNumber(), AddMapActivity.this, Constant.IMPLEMENT);
                    }
                }

                break;
            case R.id.btn_change_map://图版添加
                intent = new Intent(this, MapRelatedActivity.class);
                intent.putExtra("Code", 22);//1： 添加图版
                startActivityForResult(intent, 22);
                break;
            case R.id.ll_layout://导航
                if (addtb.getVisibility() == View.VISIBLE) {
                    Toast.makeText(this, "请先执行添加图斑操作", Toast.LENGTH_SHORT).show();
                } else if (addtb.getVisibility() == View.GONE) {
                    num = s.getTBgcd(AddMapActivity.this, listbean.getCheckNo()).size();
                }
                if (num == 0) {
                    Toast.makeText(this, "请至少添加一个观察点并添加图片！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_gcd_sz://观察点设置
                if (addtb.getVisibility() == View.VISIBLE) {
                    Toast.makeText(this, "请先执行添加图斑操作", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(AddMapActivity.this, HomeActivity.class);
                    intent.putExtra("key", "settingGCD");
                    //获取到edit对象
                    edit = sper.edit();
                    //通过editor对象写入数据
                    edit.putString("CHECK", listbean.getCheckNo());
                    edit.putString("TASK", listbean.getTaskNumber());
                    edit.putString("CX", listbean.getKsCenterCoordX());
                    edit.putString("CY", listbean.getKsCenterCoordY());
                    edit.putString("AddTBBH", listbean.getMapNumber());
                    //提交数据存入到xml文件中
                    edit.commit();
                    startActivityForResult(intent, 12);
                }

                break;
            case R.id.ll_over://验证完成
                if (addtb.getVisibility() == View.VISIBLE) {
                    Toast.makeText(this, "请先执行添加图斑操作", Toast.LENGTH_SHORT).show();
                } else if (addtb.getVisibility() == View.GONE) {
                    num = s.getTBgcd(AddMapActivity.this, listbean.getCheckNo()).size();
                }

                if (num == 0) {
                    Toast.makeText(this, "请至少添加一个观察点并添加图片！", Toast.LENGTH_SHORT).show();
                } else if (addtb.getVisibility() == View.GONE && num > 0) {
                    if (num == s.getAllPoints(listbean.getTaskNumber(), AddMapActivity.this).size()) {
                        s.updateTastState(listbean.getTaskNumber(), AddMapActivity.this, Constant.VERIFIED);
                        s.updateEnd(AddMapActivity.this, listbean.getTaskNumber(), Constant.VERIFIED);
                    } else if (num > 0) {
                        s.updateTastState(listbean.getTaskNumber(), AddMapActivity.this, Constant.IMPLEMENT);
                    }
                }
                break;
            case R.id.tv_jylx:
                intent = new Intent(this, CListActivity.class);
                intent.putExtra("Code", 700);//1： 旗县名称100
                startActivityForResult(intent, 700);
                break;
            case R.id.tv_qxmc:
                intent = new Intent(this, CListActivity.class);
                intent.putExtra("Code", 100);//1： 旗县名称100
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_dllx:
                if (!typeBZ.equals("")) {
                    if (typeBZ.equals(Constant.ksm)) {
                        typeData = "3001";
                        intent = new Intent(this, CListActivity.class);
                        bundle = new Bundle();
                        bundle.putString("BelongId", typeData);
                        intent.putExtras(bundle);
                        intent.putExtra("Code", 200);//1： 地类类型200
                        startActivityForResult(intent, 200);
                    } else if (typeBZ.equals(Constant.ksd)) {
                        intent = new Intent(this, CListActivity.class);
                        intent.putExtra("Code", 201);//1： 地类类型井口点
                        startActivityForResult(intent, 201);
                    } else if (typeBZ.equals(Constant.kszhm)) {
                        typeData = "3003";
                        intent = new Intent(this, CListActivity.class);
                        bundle = new Bundle();
                        bundle.putString("BelongId", typeData);
                        intent.putExtras(bundle);
                        intent.putExtra("Code", 200);//1： 地类类型200
                        startActivityForResult(intent, 200);
                    } else if (typeBZ.equals(Constant.kszhx)) {
                        intent = new Intent(this, CListActivity.class);
                        intent.putExtra("Code", 202);//1： 地类类型矿山线
                        startActivityForResult(intent, 202);
                    }
                } else {
                    Toast.makeText(this, "请首先填写解译类型", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_phlx:
                if (!typeBZ.equals("")) {
                    if (typeBZ.equals(Constant.ksd) || typeBZ.equals(Constant.ksm)) {
                        intent = new Intent(this, CListActivity.class);
                        bundle = new Bundle();
                        typeData = "3002";
                        bundle.putString("BelongId", typeData);
                        intent.putExtras(bundle);
                        intent.putExtra("Code", 300);//1： 破坏类型300
                        startActivityForResult(intent, 300);
                    }
                } else {
                    Toast.makeText(this, "请首先填写解译类型", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_yxdx:
                if (!typeBZ.equals("")) {
                } else {
                    Toast.makeText(this, "请首先填写解译类型", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_zldx:
                if (!typeBZ.equals("")) {
                    if (typeBZ.equals(Constant.kszl)) {
                        intent = new Intent(this, CListActivity.class);
                        bundle = new Bundle();
                        typeData = "3004";
                        bundle.putString("BelongId", typeData);
                        intent.putExtras(bundle);
                        intent.putExtra("Code", 400);//1： 治理对象400
                        startActivityForResult(intent, 400);
                    }
                } else {
                    Toast.makeText(this, "请首先填写解译类型", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_kckz:
                intent = new Intent(this, MineTypeQueryActivity.class);
                bundle = new Bundle();
                typeData = "1005";
                bundle.putString("BelongId", typeData);
                intent.putExtras(bundle);
                intent.putExtra("Code", 500);//1：开采矿种 500
                startActivityForResult(intent, 500);
                break;
            case R.id.tv_kcfs:
                intent = new Intent(this, CListActivity.class);
                bundle = new Bundle();
                typeData = "1001";
                bundle.putString("BelongId", typeData);
                intent.putExtras(bundle);
                intent.putExtra("Code", 600);//1：开采方式 600
                startActivityForResult(intent, 600);
                break;
            case R.id.tv_yzdd:
                switch (typeBZ) {
                    case Constant.ksd:
                        typeData = "3007";
                        intent = new Intent(this, CListActivity.class);
                        bundle = new Bundle();
                        bundle.putString("BelongId", typeData);
                        intent.putExtras(bundle);
                        intent.putExtra("Code", 1002);//1： 地类类型200
                        startActivityForResult(intent, 1002);
                        break;
                    case Constant.ksm:
                        typeData = "3006";
                        intent = new Intent(this, CListActivity.class);
                        bundle = new Bundle();
                        bundle.putString("BelongId", typeData);
                        intent.putExtras(bundle);
                        intent.putExtra("Code", 1002);//1： 地类类型200
                        startActivityForResult(intent, 1002);
                        break;
                    case Constant.kszhx:
                        typeData = "3010";
                        intent = new Intent(this, CListActivity.class);
                        bundle = new Bundle();
                        bundle.putString("BelongId", typeData);
                        intent.putExtras(bundle);
                        intent.putExtra("Code", 1002);//1： 地类类型200
                        startActivityForResult(intent, 1002);
                        break;
                    case Constant.kszhm:
                        typeData = "3009";
                        intent = new Intent(this, CListActivity.class);
                        bundle = new Bundle();
                        bundle.putString("BelongId", typeData);
                        intent.putExtras(bundle);
                        intent.putExtra("Code", 1002);//1： 地类类型200
                        startActivityForResult(intent, 1002);
                        break;
                    case Constant.kszl:
                        intent = new Intent(this, CListActivity.class);
                        bundle = new Bundle();
                        typeData = "3008";
                        bundle.putString("BelongId", typeData);
                        intent.putExtras(bundle);
                        intent.putExtra("Code", 1003);//1： 治理对象400
                        startActivityForResult(intent, 1003);
                        break;
                    default:
                }
                break;
            case R.id.tv_yzfxms:
                intent = new Intent(this, CListActivity.class);
                intent.putExtra("Code", 1000);//验证分析
                startActivityForResult(intent, 1000);
                break;
            case R.id.et_note:
                break;
            default:
        }
    }

    @OnCheckedChanged({R.id.rb_point, R.id.rb_line, R.id.rb_surface})
    public void rbPointLineSurface(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb_point:
                if (ischanged) {//注意：这里一定要有这个判断，只有对应该id的按钮被点击了，ischanged状态发生改变，才会执行下面的内容
                    //这里写你的按钮变化状态的UI及相关逻辑
                    tbType = Constant.POINT;
//                    Toast.makeText(this, "" + tbType, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_line:
                if (ischanged) {
                    tbType = Constant.LINE;
//                    Toast.makeText(this, "" + tbType, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_surface:
                if (ischanged) {
                    tbType = Constant.SURFACE;
//                    Toast.makeText(this, "" + tbType, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //矿山名称
        getKsmc = ksName.getText().toString().replace(" ", "");
        //解译类型
        getJylx = yjlx.getText().toString().replace(" ", "");
        //旗县名称
        getQxmc = qxmc.getText().toString().replace(" ", "");
        //地类类型
        getDllx = dllx.getText().toString().replace(" ", "");
        //破坏类型
        getPhlx = phlx.getText().toString().replace(" ", "");
        //影响对象
        getYxdx = yxdx.getText().toString().replace(" ", "");
        //治理对象
        getZldx = zldx.getText().toString().replace(" ", "");
        //开采矿种
        getKckz = kckz.getText().toString().replace(" ", "");
        //开采矿种
        getKcfs = kcfs.getText().toString().replace(" ", "");
        //验证地点
        getYzdd = yzdd.getText().toString().replace(" ", "");
        //验证分析
        getYzfx = yzfx.getText().toString().replace(" ", "");
        //备注
        getBz = note.getText().toString().replace(" ", "");
        if (!getKsmc.equals("")) {
            outState.putString("ksmc", getKsmc);
        }
        if (!getJylx.equals("")) {
            outState.putString("jylx", getJylx);
        }
        if (!getQxmc.equals("")) {
            outState.putString("qxmc", getQxmc);
        }
        if (!getDllx.equals("")) {
            outState.putString("dllx", getDllx);
        }
        if (!getPhlx.equals("")) {
            outState.putString("phlx", getPhlx);
        }
        if (!getYxdx.equals("")) {
            outState.putString("yxdx", getYxdx);
        }
        if (!getZldx.equals("")) {
            outState.putString("zldx", getZldx);
        }
        if (!getKckz.equals("")) {
            outState.putString("kckz", getKckz);
        }
        if (!getKcfs.equals("")) {
            outState.putString("kcfs", getKcfs);
        }
        if (!getYzdd.equals("")) {
            outState.putString("yzdd", getYzdd);
        }
        if (!tbType.equals("")) {
            outState.putString("tbsx", tbType);
        }
        if (!addX.equals("0.0") && !addY.equals("null")) {
            outState.putDouble("cx", Double.parseDouble(seeX));
            outState.putDouble("cy", Double.parseDouble(seeY));
        }
        if (!getYzfx.equals("")) {
            outState.putString("yzfx", getYzfx);
        }
        if (!getBz.equals("")) {
            outState.putString("bz", getBz);
        }
        Log.d("test++++++++++", "onSaveInstanceState无参: 销毁时候");

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d("test++++++++++", "onSaveInstanceState: 销毁时候");
//        if (!getKsmc.equals("")) {
//            outState.putString("ksmc", getKsmc);
//        }
//        if (!getJylx.equals("")) {
//            outState.putString("jylx", getJylx);
//        }
//        if (!getQxmc.equals("")) {
//            outState.putString("qxmc", getQxmc);
//        }
//        if (!getDllx.equals("")) {
//            outState.putString("dllx", getDllx);
//        }
//        if (!getPhlx.equals("")) {
//            outState.putString("phlx", getPhlx);
//        }
//        if (!getYxdx.equals("")) {
//            outState.putString("yxdx", getYxdx);
//        }
//        if (!getZldx.equals("")) {
//            outState.putString("zldx", getZldx);
//        }
//        if (!getKckz.equals("")) {
//            outState.putString("kckz", getKckz);
//        }
//        if (!getKcfs.equals("")) {
//            outState.putString("kcfs", getKcfs);
//        }  if (!getYzdd.equals("")) {
//            outState.putString("yzdd", getYzdd);
//        }
//        if (!tbType.equals("")) {
//            outState.putString("tbsx", tbType);
//        }
//        if (!changedEnd.equals("")) {
//            outState.putString("jybd", changedEnd);
//        }
//        if (!cx.equals("null") && !cy.equals("null")) {
//            outState.putString("cx", cx);
//            outState.putString("cy", cy);
//        }
//        if (!getYzfx.equals("")) {
//            outState.putString("yzfx", getYzfx);
//        }
//        if (!getBz.equals("")) {
//            outState.putString("bz", getBz);
//        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("test++++++++++", "onRestoreInstanceState: 重新恢复");
        ksName.setText(savedInstanceState.getString("ksmc", ""));
        yjlx.setText(savedInstanceState.getString("jylx", ""));
        qxmc.setText(savedInstanceState.getString("qxmc", ""));
        dllx.setText(savedInstanceState.getString("dllx", ""));
        phlx.setText(savedInstanceState.getString("phlx", ""));
        yxdx.setText(savedInstanceState.getString("yxdx", ""));
        zldx.setText(savedInstanceState.getString("zldx", ""));
        kckz.setText(savedInstanceState.getString("kckz", ""));
        kcfs.setText(savedInstanceState.getString("kcfs", ""));
        tbType = savedInstanceState.getString("tbsx", "");
        yzdd.setText(savedInstanceState.getString("yzdd", ""));
        cx = savedInstanceState.getString("cx", "");
        cy = savedInstanceState.getString("cy", "");
        yzfx.setText(savedInstanceState.getString("yzfx", ""));
        note.setText(savedInstanceState.getString("bz", ""));
        typeBZ = savedInstanceState.getString("jylx", "");
        if (!cy.equals("null") && !cx.equals("null")) {
            tbdw.setText(sper.getString("SEEX", "111.6911") + "   " + sper.getString("SEEY", "40.80270"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_map);
        if (savedInstanceState != null) {

        }
        ButterKnife.bind(this);
        s = new CustomSQLTools();
        listbean = new TaskListEntity();
        gcdlb = findViewById(R.id.lv_gcz_lb);
        tasknum = getIntent().getStringExtra("rwbh");
        rwzt = getIntent().getStringExtra("rwzt");
        jybh1 = s.getBigMap(tasknum, this);
        typeBZ = yjlx.getText().toString();
        sper = getSharedPreferences("User", Context.MODE_PRIVATE);
//        String dexX = sper.getString("demX", "null");
//        String dexY = sper.getString("demY", "null");
//        if (!dexX.equals("null") && !dexY.equals("null")) {
//            edit = sper.edit();
//            edit.remove("demX");
//            edit.remove("demY");
//            edit.commit();
//        }

        addtb.setVisibility(View.VISIBLE);
        showData();
    }

    @Override
    protected void onRestart() {
        if (!addX.equals("null") && !addY.equals("null")) {
            tbdw.setText(seeX + "   " + seeY);
        }
        super.onRestart();
    }

    public void showData() {
        taskNum.setText(tasknum);
        yjbh.setText(jybh1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 700 && resultCode == CleanActivity.RESULT_OK) {
            dllx.setText("");
            yzdd.setText("");
            yjlx.setText(data.getStringExtra("DataName"));
            typeBZ = data.getStringExtra("DataName");
            if (typeBZ.equals(Constant.ksd) || typeBZ.equals(Constant.ksm)) {
                LLdllx.setVisibility(View.VISIBLE);
                LLphlx.setVisibility(View.VISIBLE);
                LLdllxphlx.setVisibility(View.VISIBLE);
                LLyxdxzldx.setVisibility(View.GONE);
            } else if (typeBZ.equals(Constant.kszhx) || typeBZ.equals(Constant.kszhm)) {
                LLdllxphlx.setVisibility(View.VISIBLE);
                LLphlx.setVisibility(View.INVISIBLE);
                LLdllx.setVisibility(View.VISIBLE);
                LLyxdx.setVisibility(View.VISIBLE);
                LLyxdxzldx.setVisibility(View.VISIBLE);
                LLzldx.setVisibility(View.INVISIBLE);
            } else if (typeBZ.equals(Constant.kszl)) {
                LLyxdx.setVisibility(View.GONE);
                LLdllxphlx.setVisibility(View.GONE);
                LLzldx.setVisibility(View.VISIBLE);
                LLyxdxzldx.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == 100 && resultCode == CListActivity.RESULT_OK) {
            qxmc.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 200 && resultCode == CListActivity.RESULT_OK) {
            dllx.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 201 && resultCode == CListActivity.RESULT_OK) {
            dllx.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 202 && resultCode == CListActivity.RESULT_OK) {
            dllx.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 300 && resultCode == CListActivity.RESULT_OK) {
            phlx.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 400 && resultCode == CListActivity.RESULT_OK) {
            zldx.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 500 && resultCode == MineTypeQueryActivity.RESULT_OK) {
            kckz.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 600 && resultCode == CListActivity.RESULT_OK) {
            kcfs.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 800 && resultCode == CListActivity.RESULT_OK) {
            yzdd.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 1000 && resultCode == CListActivity.RESULT_OK) {
            yzfx.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 12 && resultCode == 12) {
        } else if (requestCode == 1002 && resultCode == CListActivity.RESULT_OK) {
            yzdd.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 1003 && resultCode == CListActivity.RESULT_OK) {
            yzdd.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 22 && resultCode == CListActivity.RESULT_OK) {
            addX = data.getStringExtra("mapGCDX");
            addY = data.getStringExtra("mapGCDY");
            seeX = s.getXY(addX, addY).get(0);
            seeY = s.getXY(addX, addY).get(1);
            tbdw.setText(seeX + seeY);
            //获取到edit对象
            edit = sper.edit();
            edit.putString("SEEX", seeX);
            edit.putString("SEEY", seeY);
            edit.putString("GCDX", addX);
            edit.putString("GCDY", addY);
            edit.commit();


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void itemClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
