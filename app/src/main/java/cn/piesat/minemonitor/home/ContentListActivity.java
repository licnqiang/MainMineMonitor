package cn.piesat.minemonitor.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.piesat.minemonitor.BaseActivity;
import cn.piesat.minemonitor.MainActivity;
import cn.piesat.minemonitor.MapRelatedActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.adapter.TBgcdAdapter;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.entity.CheckInfoEntiy;
import cn.piesat.minemonitor.entity.TaskListEntity;
import cn.piesat.minemonitor.home.contentlist.ShowMediaActivity;
import cn.piesat.minemonitor.home.minetype.MineTypeQueryActivity;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;
import cn.piesat.minemonitor.media.DialogUtils;
import cn.piesat.minemonitor.util.ImageUtil;
import cn.piesat.minemonitor.util.NaviWaysUtils;
import cn.piesat.minemonitor.util.ToastUtil;


/**
 * 任务列表详情展示页面
 */
public class ContentListActivity extends BaseActivity implements View.OnClickListener, TBgcdAdapter.InnerItemOnclickListener, AdapterView.OnItemClickListener {
    //顶部title文字
    private TextView topTitle;
    private ImageView menu;
    private TextView changeMap;
    private Intent intent;
    private static final int PIC_PATH = 0;
    private static final int VID_PATH = 1;
    private Uri imgUri;
    private static String PHOTO_FILE_NAME;
    private String mvideo_suoluepic;
    private String imgDesc;
    private Uri vidUri;
    @BindView(R.id.tv_task_number)
    TextView rwbh;
    @BindView(R.id.tv_map_number)
    TextView tbbh;
    @BindView(R.id.tv_ks_name)
    TextView ksmc;
    @BindView(R.id.tv_jylx)
    TextView jylx;
    @BindView(R.id.tv_qxmc)
    TextView qxmc;
    @BindView(R.id.et_note)
    EditText bzNote;
    @BindView(R.id.tv_yzfx)
    TextView tvYzfx;
    private Bundle bundle;
    private String typeBZ;
    private int from;
    private String tbksmc;
    private String tbjylx;
    private String tbqxmc;
    private String tbdllx;
    private String tbphlx;
    private String tbyxdx;
    private String tbzldx;
    private String tbkckz;
    private String tbkcfs;
    private String tbyzdd;
    private int counter = 1;
    private String fileNO1;
    private List<String> trackXY;
    private int gcdChanged = 0;
    private TextView direction;
    private String provice = "";
    private String tbPic;
    private String tbPicLastYear;
    private String thisYearMap = " ";
    private String lastYearMap = " ";
    private File outputImage;
    private CheckInfoEntiy checkInfoEntiy;

    @OnClick({R.id.tv_qxmc, R.id.dllx, R.id.tv_phlx, R.id.tv_yxdx, R.id.tv_zldx, R.id.tv_kckz, R.id.tv_kcfs, R.id.tv_yzfx, R.id.tv_yzdd})
    public void onViewClick(View view) {
        String typeData = "";
        typeBZ = listbean.getDataType();
        switch (view.getId()) {
            case R.id.tv_qxmc:
                intent = new Intent(this, CListActivity.class);
                intent.putExtra("Code", 100);//1： 旗县名称100
                startActivityForResult(intent, 100);
                break;
            case R.id.dllx:
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
                    intent.putExtra("Code", 201);//1： 地类类型200
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
                    intent.putExtra("Code", 202);//1： 地类类型200
                    startActivityForResult(intent, 202);
                }
                break;
            case R.id.tv_phlx:
                if (typeBZ.equals(Constant.ksd) || typeBZ.equals(Constant.ksm)) {
                    intent = new Intent(this, CListActivity.class);
                    bundle = new Bundle();
                    typeData = "3002";
                    bundle.putString("BelongId", typeData);
                    intent.putExtras(bundle);
                    intent.putExtra("Code", 300);//1： 破坏类型300
                    startActivityForResult(intent, 300);
                }
                break;
            case R.id.tv_yxdx:
                break;
            case R.id.tv_zldx:
                if (typeBZ.equals(Constant.kszl)) {
                    intent = new Intent(this, CListActivity.class);
                    bundle = new Bundle();
                    typeData = "3004";
                    bundle.putString("BelongId", typeData);
                    intent.putExtras(bundle);
                    intent.putExtra("Code", 400);//1： 治理对象400
                    startActivityForResult(intent, 400);
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
            case R.id.tv_yzfx:
                intent = new Intent(this, CListActivity.class);
                intent.putExtra("Code", 1000);//1：验证分析
                startActivityForResult(intent, 1000);
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
                }
                break;
        }
    }

    @BindView(R.id.dllx)
    TextView dllx;
    @BindView(R.id.tv_phlx)
    TextView phlx;
    @BindView(R.id.tv_yxdx)
    EditText yxdx;
    @BindView(R.id.tv_zldx)
    TextView zldx;
    @BindView(R.id.tv_kckz)
    TextView kckz;
    @BindView(R.id.tv_kcfs)
    TextView kcfs;
    @BindView(R.id.et_task_note)
    EditText yzfx;
    @BindView(R.id.iv_this_year)
    ImageView tYear;
    @BindView(R.id.iv_last_year)
    ImageView lYear;
    @BindView(R.id.tv_yzdd)
    TextView yzdd;

    @BindView(R.id.img_dllx)
    ImageView imgDllx;
    @BindView(R.id.img_phlx)
    ImageView imgPhlx;
    @BindView(R.id.img_yxdx)
    ImageView imgYxdx;
    @BindView(R.id.img_zldx)
    ImageView imgZldx;


    @BindView(R.id.rb_point)
    RadioButton point;
    @BindView(R.id.rb_line)
    RadioButton line;
    @BindView(R.id.rb_surface)
    RadioButton surface;


    @BindView(R.id.rb_for)
    RadioButton rbfor;
    @BindView(R.id.rb_wrong)
    RadioButton rbwrong;
    private String changedEnd = " ";
    private String des = "";
    private String adds = " ";

    @BindView(R.id.tv_this_year)
    TextView thisYearName;
    @BindView(R.id.tv_last_year)
    TextView lastYearName;

    @BindView(R.id.tv_mapSee)
    TextView mapSee;

    @BindView(R.id.ll_layout)
    LinearLayout ll_layout;
    private SharedPreferences.Editor edit;
    private LinearLayout over;
    private String gcdx;
    private String gcdy;
    private String savePicPath;
    private String saveDivPath;

    @OnClick(R.id.ll_update_info)
    public void updateInfo(LinearLayout layout) {
        //点击更新信息，判断该图斑是否已经添加观察点，且判断是否对图斑详情进行了更改。且更改成功。
        //判断该列是否最后一个状态变为验证中的图斑，如果是，改变该任务状态为已验证，如果不是则为验证中。
        String tbksmc = ksmc.getText().toString();//矿山名称
        String tbjylx = jylx.getText().toString();//解译类型
        String tbqxmc = qxmc.getText().toString();//旗县名称
        String tbdllx = dllx.getText().toString();//地类类型
        String tbphlx = phlx.getText().toString();//破坏类型
        String tbyxdx = yxdx.getText().toString();//影响对象
        String tbzldx = zldx.getText().toString();//治理对象
        String tbkckz = kckz.getText().toString();//开采矿种
        String tbkcfs = kcfs.getText().toString();//开采矿种
        adds = yzdd.getText().toString().replace("", "");
        des = yzfx.getText().toString().replace(" ", "");
        if (tbjylx.equals(Constant.ksd) || tbjylx.equals(Constant.ksm)) {
            if (tbdllx.equals("")) {
                Toast.makeText(this, "地类类型不为空！", Toast.LENGTH_SHORT).show();
            } else if (tbphlx.equals("")) {
                Toast.makeText(this, "破坏类型不为空！", Toast.LENGTH_SHORT).show();
            }
        } else if (tbjylx.equals(Constant.kszhx) || tbjylx.equals(Constant.kszhm)) {
            if (tbdllx.equals("")) {
                Toast.makeText(this, "地类类型不为空！", Toast.LENGTH_SHORT).show();
            } else if (tbyxdx.equals("")) {
                Toast.makeText(this, "影响对象不为空", Toast.LENGTH_SHORT).show();
            }
        } else if (tbjylx.equals(Constant.kszl)) {
            if (tbzldx.equals("")) {
                Toast.makeText(this, "治理对象不为空", Toast.LENGTH_SHORT).show();
            }
        }
        if (s.getTBgcd(ContentListActivity.this, listbean.getCheckNo()).size() == 0) {
            Toast.makeText(this, "请至少添加一个观察点并添加图片！", Toast.LENGTH_SHORT).show();
        } else if (tbksmc.equals("")) {
            Toast.makeText(this, "矿山名称不为空！", Toast.LENGTH_SHORT).show();
        } else if (tbqxmc.equals("")) {
            Toast.makeText(this, "旗县名称不为空！", Toast.LENGTH_SHORT).show();
        } else if (tbkckz.equals("")) {
            Toast.makeText(this, "开采矿种不为空", Toast.LENGTH_SHORT).show();
        } else if (tbkcfs.equals("")) {
            Toast.makeText(this, "开采方式不为空", Toast.LENGTH_SHORT).show();
        } else if (adds.equals("")) {
            Toast.makeText(this, "请添写验证地点！", Toast.LENGTH_SHORT).show();
        } else if (changedEnd.equals(" ")) {
            Toast.makeText(this, "请输入解译对比结果和描述信息", Toast.LENGTH_SHORT).show();
        } else if (des.equals("") && !changedEnd.equals(Constant.FOR)) {
            Toast.makeText(this, "请填写验证分析描述！", Toast.LENGTH_SHORT).show();
        } else if (!adds.equals("") && !changedEnd.equals(" ") && !tbksmc.equals("") && !tbqxmc.equals("") && !tbkckz.equals("") && !tbkcfs.equals("")) {
            mapDetails = new ArrayList<>();
            mapDetails.add(adds);//验证地点
            mapDetails.add(changedEnd);//同解译结果比对
            mapDetails.add(des);//验证描述
            mapDetails.add(Constant.IMPLEMENT);//验证状态
            mapDetails.add(tbksmc);//矿山名称
            mapDetails.add(tbqxmc);//旗县名称
            mapDetails.add(tbdllx);//地类类型
            mapDetails.add(tbphlx);//破坏类型
            mapDetails.add(tbyxdx);//影响对象
            mapDetails.add(tbzldx);//治理对象
            mapDetails.add(tbkckz);//开采矿种
            mapDetails.add(tbkcfs);//开采方式
            mapDetails.add(bzNote.getText().toString());//备注
            if (!tbqxmc.equals("") && !tbqxmc.equals(listbean.getXzqName())) {
                mapDetails.add(Constant.YES);
                mapDetails.add(s.getQXcode(tbqxmc, ContentListActivity.this));
            } else {
                mapDetails.add(Constant.NO);
                mapDetails.add(s.getQXcode(tbqxmc, ContentListActivity.this));
            }
            mapDetails.add(SpHelper.getStringValue("USERNAME"));

            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            //清除数据
            edit = sper.edit();
            edit.putString("TASK_ID", listbean.getTaskNumber());
            edit.remove("Than");
            edit.commit();
            s.update(ContentListActivity.this, listbean.getCheckNo(), mapDetails.get(0), mapDetails.get(1), mapDetails.get(2), mapDetails.get(3)
                    , mapDetails.get(4), mapDetails.get(5), mapDetails.get(6), mapDetails.get(7), mapDetails.get(8), mapDetails.get(9), mapDetails.get(10),
                    mapDetails.get(11), mapDetails.get(12), mapDetails.get(13), mapDetails.get(14), mapDetails.get(15));
            int num = s.getAllState(listbean.getTaskNumber(), ContentListActivity.this, Constant.IMPLEMENT).size();
            if (num == s.getAllPoints(listbean.getTaskNumber(), ContentListActivity.this).size()) {
                s.updateTastState(listbean.getTaskNumber(), ContentListActivity.this, Constant.VERIFIED);
                s.updateEnd(ContentListActivity.this, listbean.getTaskNumber(), Constant.VERIFIED);
            } else if (num > 0) {
                s.updateTastState(listbean.getTaskNumber(), ContentListActivity.this, Constant.IMPLEMENT);
            }
            Log.d("update", "updateInfo: " + s.getAllState(listbean.getTaskNumber(), ContentListActivity.this, Constant.IMPLEMENT).size());
        }

    }

    private LinearLayout gcdsz;
    private CustomSQLTools s;
    private CheckInfoEntiy startinfo;
    private String uniqueId;
    private UUID uuid;
    private String gcdCode;
    private String picpath;
    private String fileNO;
    private ListView gcdlb;
    private TBgcdAdapter adapter1;
    private List<CheckInfoEntiy> cie;
    private List<CheckInfoEntiy> gcdlist = new ArrayList<>();
    private String str;
    private int count = 1;
    private List<String> gcdcf;
    private List<CheckInfoEntiy> cieChoose;
    private List<CheckInfoEntiy> cieChooseShow;
    private List<String> mapDetails;
    private LinearLayout tbSee;
    private LinearLayout tbSeeName;
    //声明Sharedpreferenced对象
    private SharedPreferences sper;
    private TaskListEntity listbean;
    public static final String[] provices = new String[]{"东向", "南向", "西向", "北向", "东北", "东南", "西北", "西南"};

    @OnCheckedChanged({R.id.rb_for, R.id.rb_wrong})
    public void OnCheckedChangeListener2(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb_for:
                if (ischanged) {//注意：这里一定要有这个判断，只有对应该id的按钮被点击了，ischanged状态发生改变，才会执行下面的内容
                    //这里写你的按钮变化状态的UI及相关逻辑
                    changedEnd = Constant.FOR;
                    edit = sper.edit();
                    //通过editor对象写入数据
                    edit.putString("Than", Constant.FOR);
                    //提交数据存入到xml文件中
                    edit.commit();
                }
                break;
            case R.id.rb_wrong:
                if (ischanged) {
                    changedEnd = Constant.WRONG;
                    edit = sper.edit();
                    //通过editor对象写入数据
                    edit.putString("Than", Constant.WRONG);
                    //提交数据存入到xml文件中
                    edit.commit();
                }
                break;
            default:
                break;
        }
    }

    private String taskNumber;
    private String mapNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        sper = getSharedPreferences("User", Context.MODE_PRIVATE);
        ButterKnife.bind(this);
        Spinner sp = new Spinner(this);
        s = new CustomSQLTools();
        //如果观察点列表不为空
        gcdsz = findViewById(R.id.ll_gcd_sz);
        gcdlb = findViewById(R.id.lv_gcz_lb);
        tbSee = findViewById(R.id.ll_tb_see);
        tbSeeName = findViewById(R.id.ll_tb_see_name);
        over = findViewById(R.id.ll_over);
        cie = new ArrayList<>();

        //创建数据库
        Intent intent = getIntent();
        from = intent.getIntExtra("from", 0);
        switch (from) {
            case 1:
                break;
            case 2:
                listbean = (TaskListEntity) this.getIntent().getSerializableExtra("item");
                edit = sper.edit();
                //通过editor对象写入数据
                edit.putString("CHECK", listbean.getCheckNo());
                edit.putString("TASK", listbean.getTaskNumber());
                //提交数据存入到xml文件中
                edit.commit();
                add();
                break;
            case 3:
                listbean = (TaskListEntity) this.getIntent().getSerializableExtra("item");
                edit = sper.edit();
                //通过editor对象写入数据
                edit.putString("CHECK", listbean.getCheckNo());
                edit.putString("TASK", listbean.getTaskNumber());
                //提交数据存入到xml文件中
                edit.commit();
                add();
                break;
        }
        //进行观察点编号编辑操作
        initView();
        onClickListener();
        //该图斑下不重复的观察点集合。
        gtbcf();
        typeBZ = listbean.getDataType();
        if (typeBZ.equals(Constant.kszl)) {
            imgYxdx.setImageResource(R.drawable.pig_red);
            imgDllx.setImageResource(R.drawable.pig_red);
            imgPhlx.setImageResource(R.drawable.pig_red);
        } else if (typeBZ.equals(Constant.kszhx) || typeBZ.equals(Constant.kszhm)) {
            imgZldx.setImageResource(R.drawable.pig_red);
            imgPhlx.setImageResource(R.drawable.pig_red);
        } else if (typeBZ.equals(Constant.ksd) || typeBZ.equals(Constant.ksm)) {
            imgYxdx.setImageResource(R.drawable.pig_red);
            imgZldx.setImageResource(R.drawable.pig_red);
        }
        if (typeBZ.equals(Constant.kszhx) || typeBZ.equals(Constant.kszhm)) {
            yxdx.setInputType(InputType.TYPE_CLASS_TEXT);//影响对象
        } else {
            yxdx.setKeyListener(null);
        }
    }

    public void gtbcf() {
        if (gcdChanged == 21) {
            cieChoose = new ArrayList<>();
            cieChoose.addAll(bcfgcd());
            if (cieChoose.size() > 1) {
                List<String> nolist = new ArrayList<>();
                for (int i = 0; i < cieChoose.size(); i++) {
                    nolist.add(cieChoose.get(i).getFileCreateLocationNo());
                }
                String[] strs = new String[nolist.size()];
                nolist.toArray(strs);
                Set<String> set = new HashSet<String>();
                set.addAll(Arrays.asList(strs));
                if (nolist.size() != set.size()) {
                    cieChoose.remove(cieChoose.size() - 1);
                }
            }
            adapter1 = new TBgcdAdapter(ContentListActivity.this, cieChoose);
        } else {
            adapter1 = new TBgcdAdapter(ContentListActivity.this, bcfgcd());
        }
        adapter1.setOnInnerItemOnClickListener(this);
        gcdlb.setAdapter(adapter1);
        gcdlb.setOnItemClickListener(this);

    }

    public List<CheckInfoEntiy> bcfgcd() {
        gcdcf = new ArrayList<>();

        if (listbean == null) {
//取出数据,第一个参数是写入是的键，第二个参数是如果没有获取到数据就默认返回的值。
            String value = sper.getString("CHECK", "Null");
            String taskid = sper.getString("TASK", "Null");
            List<TaskListEntity> tast = new ArrayList<>();
            if (!value.equals("Null")) {
                tast.addAll(s.getOnePoints(taskid, ContentListActivity.this, value));
                listbean = tast.get(0);
                add();
                gcdcf.addAll(s.gcdcf(ContentListActivity.this, value));
                gcdcf.size();
                gcdCode = s.getTBgcdXZBM(listbean.getCheckNo(), ContentListActivity.this);
                gcd();
                edit = sper.edit();
                edit.remove("CHECK");
                edit.remove("TASK");
                edit.commit();
            } else {
                gtbcf();
            }

        } else {
            gcdcf.addAll(s.gcdcf(ContentListActivity.this, listbean.getCheckNo()));
        }


        //获取该观察点对应的数据。
        cieChoose = new ArrayList<>();
        cieChoose.clear();
        if (gcdcf != null && gcdcf.size() > 0) {
            for (int i = 0; i < gcdcf.size(); i++) {
                cie = new ArrayList<>();
                Log.d("----", "观察点编号重复: " + gcdcf.get(i));
                cie.addAll(s.thereIsA(ContentListActivity.this, gcdcf.get(i)));
                Log.d("----", "bcfgcd:观察点集合 " + cie.size());
                cieChoose.add(cie.get(0));
            }
        }
        cieChoose.addAll(gcdlist);
        return cieChoose;
    }

    private void gcd() {
        //初始化观察点编详情，生成观察点编号，观察点坐标，以及观察点唯一标识。
        gcdlist.clear();
        initList();
        adapter1 = new TBgcdAdapter(ContentListActivity.this, bcfgcd());
        adapter1.setOnInnerItemOnClickListener(this);
        gcdlb.setAdapter(adapter1);
        gcdlb.setOnItemClickListener(this);
        adapter1.notifyDataSetChanged();
        //ListView item的点击事件
    }

    /**
     * 初始化数据
     */
    private void initList() {
        if (gcdx.equals("-1") || gcdy.equals("-1") || gcdx.equals("Null")) {
            Toast.makeText(this, "暂未定位成功请重新定位", Toast.LENGTH_SHORT).show();
        }
        List<String> xyList = new ArrayList<>();
        xyList.addAll(s.getXY(gcdx, gcdy));
        uuid = UUID.randomUUID();
        uniqueId = uuid.toString();
        startinfo = new CheckInfoEntiy();
        if (s.getTBgcd(ContentListActivity.this, listbean.getCheckNo()).size() == 0 && s.getTBgcdAll(ContentListActivity.this).size() == 0) {
            gcdCode = s.getTBgcdXZBM(listbean.getCheckNo(), ContentListActivity.this);
            startinfo.setFileCreateLocationNo("Y" + gcdCode + "001");
        } else if (s.getTBgcd(ContentListActivity.this, listbean.getCheckNo()).size() == 0 && s.getTBgcdAll(ContentListActivity.this).size() > 0) {
            gcdCode = s.getTBgcdXZBM(listbean.getCheckNo(), ContentListActivity.this);
            if (s.getgcdNum(ContentListActivity.this, gcdCode).size() > 0) {
                startinfo.setFileCreateLocationNo(s.getgcdLastOne(ContentListActivity.this, gcdCode));
            } else {
                startinfo.setFileCreateLocationNo("Y" + gcdCode + "001");
            }
        } else if (s.getTBgcd(ContentListActivity.this, listbean.getCheckNo()).size() > 0) {
            gcdCode = s.getTBgcdXZBM(listbean.getCheckNo(), ContentListActivity.this);
            startinfo.setFileCreateLocationNo(s.getgcdLastOne(ContentListActivity.this, gcdCode));
        }
        startinfo.setCheckItemId(uniqueId);
        startinfo.setCheckNO(listbean.getCheckNo());
        //纬度
        startinfo.setFileCreateLocationX(xyList.get(0));
        //经度
        startinfo.setFileCreateLocationY(xyList.get(1));
        gcdlist.add(startinfo);
    }

    private void add() {
        taskNumber = listbean.getTaskNumber();
        mapNumber = listbean.getMapNumber();
        rwbh.setText(taskNumber);
        tbbh.setText(mapNumber);
        ksmc.setText(listbean.getMineNumber());//矿山名称
        jylx.setText(listbean.getDataType());//解译类型
        qxmc.setText(listbean.getXzqName());//旗县名称
        dllx.setText(listbean.getKsDLMC());//地类类型
        phlx.setText(listbean.getKsPHLX());//破坏类型
        yxdx.setText(listbean.getKsYXDX());//影响对象
        zldx.setText(listbean.getKsZLDX());//治理对象
        kckz.setText(listbean.getMineType());//开采矿种
        kcfs.setText(listbean.getKsKCFS());//开采方式
        yzdd.setText(listbean.getCheckAdressName());//验证地点
        bzNote.setText(listbean.getRemark());
        String note1 = sper.getString("NOTE", "");
        if (!note1.isEmpty()) {
            yzfx.setText(note1);
        } else {
            yzfx.setText(listbean.getDes());
        }
        //获取图斑属性
        if (listbean.getKsFieldso().equals(Constant.POINT)) {
            line.setEnabled(false);
            surface.setEnabled(false);
            point.setChecked(true);
        } else if (listbean.getKsFieldso().equals(Constant.LINE)) {
            point.setEnabled(false);
            surface.setEnabled(false);
            line.setChecked(true);
        } else if (listbean.getKsFieldso().equals(Constant.SURFACE)) {
            line.setEnabled(false);
            point.setEnabled(false);
            surface.setChecked(true);
        }
        String than = sper.getString("Than", "Null");
        //与解译结果比对
        if (listbean.getKsTBCheckRES() != null) {
            if (listbean.getKsTBCheckRES().equals(Constant.FOR)) {
                rbfor.setChecked(true);
            } else if (listbean.getKsTBCheckRES().equals(Constant.WRONG)) {
                rbwrong.setChecked(true);
            }
        } else if (!than.isEmpty()) {
            if (than.equals(Constant.FOR)) {
                rbfor.setChecked(true);
            } else if (than.equals(Constant.WRONG)) {
                rbwrong.setChecked(true);
            }
        }
        //获取图斑图片编号
        //获取图斑图片所在数据文件夹名
        CustomSQLTools s = new CustomSQLTools();
        tbPic = s.getTBPath(taskNumber, listbean.getMapNumber() + ".jpg");
        tbPicLastYear = s.getTBPathLastYear(taskNumber, listbean.getMapNumber() + ".jpg");
        List<String> yearList = new ArrayList<>();
        yearList.addAll(s.getYear(taskNumber, ContentListActivity.this));
        if (yearList.size() > 0) {
            thisYearMap = yearList.get(0);
            lastYearMap = yearList.get(1);
        }
        tYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Uri.fromFile(new File(tbPic)));
            }
        });
        lYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Uri.fromFile(new File(tbPicLastYear)));
            }
        });
        if (fileIsExists(tbPic) == true && fileIsExists(tbPicLastYear) == true) {
            Glide.with(this)
                    .load(Uri.fromFile(new File(tbPic)))
                    .override(255, 320)
                    .into(tYear);
//            tYear.setImageURI(Uri.fromFile(new File(tbPic)));
            thisYearName.setText(thisYearMap);
            lastYearName.setText(lastYearMap);
            Glide.with(this)
                    .load(Uri.fromFile(new File(tbPicLastYear)))
                    .override(255, 320)
                    .into(lYear);
//            lYear.setImageURI(Uri.fromFile(new File(tbPicLastYear)));
        } else if (fileIsExists(tbPic) == true) {
            Glide.with(this)
                    .load(Uri.fromFile(new File(tbPic)))
                    .override(255, 320)
                    .into(tYear);
//            tYear.setImageURI(Uri.fromFile(new File(tbPic)));
            thisYearName.setText(thisYearMap);
        } else if (fileIsExists(tbPicLastYear) == true) {
            lastYearName.setText(lastYearMap);
//            lYear.setImageURI(Uri.fromFile(new File(tbPicLastYear)));
            Glide.with(this)
                    .load(Uri.fromFile(new File(tbPicLastYear)))
                    .override(255, 320)
                    .into(lYear);
        } else if (fileIsExists(tbPic) == false && fileIsExists(tbPicLastYear) == false) {
            if (from == 3) {
            } else {
                Toast.makeText(this, "暂无解译图片请检查导入图片！", Toast.LENGTH_SHORT).show();
            }

            mapSee.setText("图斑预览:暂无图片");
            tbSee.setVisibility(View.GONE);
            tbSeeName.setVisibility(View.GONE);
        } else if (fileIsExists(tbPic) == false) {
            tYear.setVisibility(View.INVISIBLE);
        } else if (fileIsExists(tbPicLastYear) == false) {
            lYear.setVisibility(View.INVISIBLE);
        }
        //设置验证描述信息
    }

    private Dialog dia;

    public void showDialog(Uri path) {
        dia = new Dialog(this, R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.activity_dialog1);
        final ImageView imageView = (ImageView) dia.findViewById(R.id.start_img);
        imageView.setImageURI(path);
        dia.show();
        Window window = dia.getWindow();
        dia.setCanceledOnTouchOutside(true);//点击可以退出
        android.view.WindowManager.LayoutParams params = dia.getWindow().getAttributes();
        Display display = getWindowManager().getDefaultDisplay();
        params.width = (int) (display.getWidth());
        params.height = (int) (display.getHeight() * 2 / 3);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
//        GestureImageView gs= new GestureImageView(this);
//        gs.GestureImageViewInit();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
            }
        });
    }

    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void onClickListener() {
        menu.setOnClickListener(this);
        changeMap.setOnClickListener(this);
        gcdsz.setOnClickListener(this);
        ll_layout.setOnClickListener(this);
        over.setOnClickListener(this);
    }

    private void initView() {
        topTitle = findViewById(R.id.tv_setting_title);
        if (from == 3) {
            topTitle.setText("图斑添加 ");
        } else {
            topTitle.setText("图斑详情 ");
        }
        menu = findViewById(R.id.iv_setting_children_menu);
        changeMap = findViewById(R.id.btn_change_map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_children_menu:
                popupWindow.showAtLocation(findViewById(R.id.iv_setting_children_menu), Gravity.RIGHT, 0, 0);
                break;
            case R.id.btn_change_map:
                intent = new Intent(ContentListActivity.this, MapRelatedActivity.class);
                intent.putExtra("Code", 23);//图斑编辑
                intent.putExtra("CX", listbean.getKsCenterCoordX());
                intent.putExtra("CY", listbean.getKsCenterCoordY());
                intent.putExtra("PlaquesNum", listbean.getMapNumber());
                intent.putExtra("TASKID", listbean.getTaskNumber());
                intent.putExtra("DataType", listbean.getShpName());
                startActivityForResult(intent, 23);
                break;
            case R.id.ll_gcd_sz:
//                //矿山名称
//                tbksmc = ksmc.getText().toString();
//                //解译类型
//                tbjylx = jylx.getText().toString();
//                //旗县名称
//                tbqxmc = qxmc.getText().toString();
//                //地类类型
//                tbdllx = dllx.getText().toString();
//                //破坏类型
//                tbphlx = phlx.getText().toString();
//                //影响对象
//                tbyxdx = yxdx.getText().toString();
//                //治理对象
//                tbzldx = zldx.getText().toString();
//                //开采矿种
//                tbkckz = kckz.getText().toString();
//                //开采矿种
//                tbkcfs = kcfs.getText().toString();
//                //验证地点
//                tbyzdd = yzdd.getText().toString();
                intent = new Intent(ContentListActivity.this, MapRelatedActivity.class);
                intent.putExtra("Code", 20);//20观察点设置
                intent.putExtra("CX", listbean.getKsCenterCoordX());
                intent.putExtra("CY", listbean.getKsCenterCoordY());
                intent.putExtra("PlaquesNum", listbean.getMapNumber());
//                //获取到edit对象
//                edit = sper.edit();
//                //通过editor对象写入数据
//                edit.putString("CHECK", listbean.getCheckNo());
//                edit.putString("TASK", listbean.getTaskNumber());
//                des = yzfx.getText().toString().replace(" ", "");
//                edit.putString("CX", listbean.getKsCenterCoordX());
//                edit.putString("CY", listbean.getKsCenterCoordY());
//                edit.putString("NOTE", des);
//                edit.putString("ksmc", tbksmc);
//                edit.putString("jylx", tbjylx);
//                edit.putString("qxmc", tbqxmc);
//                edit.putString("dllx", tbdllx);
//                edit.putString("phlx", tbphlx);
//                edit.putString("yxdx", tbyxdx);
//                edit.putString("zldx", tbzldx);
//                edit.putString("kckz", tbkckz);
//                edit.putString("kcfs", tbkcfs);
//                edit.putString("yzdd", tbyzdd);
//                edit.putString("yzfx", des);
//                edit.putString("bz", bzNote.getText().toString());
//                //提交数据存入到xml文件中
//                edit.commit();
                counter++;
                startActivityForResult(intent, 20);
                break;

            case R.id.ll_layout:
                Log.d("///", "onClick: " + listbean.getKsCenterCoordX());
                Log.d("///", "onClick: " + listbean.getKsCenterCoordY());
                double lonX = Double.parseDouble(listbean.getKsCenterCoordX());
                double lonY = Double.parseDouble(listbean.getKsCenterCoordY());
                if (lonX > 0 || lonY > 0) {
                    NaviWaysUtils.setUpBaiduAPPNavi(this, lonX, lonY);
                } else {
                    NaviWaysUtils.setUpBaiduAPPNavi(this, 116.124, 39.12);
                }
                //导航
                break;
            case R.id.ll_over:
                //矿山名称
                tbksmc = ksmc.getText().toString();
                //解译类型
                tbjylx = jylx.getText().toString();
                //旗县名称
                tbqxmc = qxmc.getText().toString();
                //地类类型
                tbdllx = dllx.getText().toString();
                //破坏类型
                tbphlx = phlx.getText().toString();
                //影响对象
                tbyxdx = yxdx.getText().toString();
                //治理对象
                tbzldx = zldx.getText().toString();
                //开采矿种
                tbkckz = kckz.getText().toString();
                //开采矿种
                tbkcfs = kcfs.getText().toString();
                if (tbjylx.equals(Constant.ksd) || tbjylx.equals(Constant.ksm)) {
                    if (tbdllx.equals("")) {
                        Toast.makeText(this, "地类类型不为空！", Toast.LENGTH_SHORT).show();
                    } else if (tbphlx.equals("")) {
                        Toast.makeText(this, "破坏类型不为空！", Toast.LENGTH_SHORT).show();
                    }
                } else if (tbjylx.equals(Constant.kszhx) || tbjylx.equals(Constant.kszhm)) {
                    if (tbdllx.equals("")) {
                        Toast.makeText(this, "地类类型不为空！", Toast.LENGTH_SHORT).show();
                    } else if (tbyxdx.equals("")) {
                        Toast.makeText(this, "影响对象不为空", Toast.LENGTH_SHORT).show();
                    }
                } else if (tbjylx.equals(Constant.kszl)) {
                    if (tbzldx.equals("")) {
                        Toast.makeText(this, "治理对象不为空", Toast.LENGTH_SHORT).show();
                    }
                }
                des = yzfx.getText().toString().replace(" ", "");
                adds = yzdd.getText().toString().replace(" ", "");
                if (s.getTBgcd(ContentListActivity.this, listbean.getCheckNo()).size() == 0) {
                    Toast.makeText(this, "请先执行更新信息操作!", Toast.LENGTH_SHORT).show();
                } else if (s.getTBgcd(ContentListActivity.this, listbean.getCheckNo()).size() == 0) {
                    Toast.makeText(this, "请至少添加一个观察点并添加图片！", Toast.LENGTH_SHORT).show();
                } else if (tbksmc.equals("")) {
                    Toast.makeText(this, "矿山名称不为空！", Toast.LENGTH_SHORT).show();
                } else if (tbqxmc.equals("")) {
                    Toast.makeText(this, "旗县名称不为空！", Toast.LENGTH_SHORT).show();
                } else if (tbkckz.equals("")) {
                    Toast.makeText(this, "开采矿种不为空", Toast.LENGTH_SHORT).show();
                } else if (tbkcfs.equals("")) {
                    Toast.makeText(this, "开采方式不为空", Toast.LENGTH_SHORT).show();
                } else if (adds.equals("")) {
                    Toast.makeText(this, "请添写验证地点！", Toast.LENGTH_SHORT).show();
                } else if (changedEnd.equals(" ")) {
                    Toast.makeText(this, "请输入解译对比结果和描述信息", Toast.LENGTH_SHORT).show();
                } else if (des.equals("") && !changedEnd.equals(Constant.FOR)) {
                    Toast.makeText(this, "请填写验证分析描述！", Toast.LENGTH_SHORT).show();
                } else if (s.getTBgcd(ContentListActivity.this, listbean.getCheckNo()).size() > 0) {
                    if (s.queryStatus(ContentListActivity.this, listbean.getCheckNo()).equals(Constant.IMPLEMENT)) {
                        int number = 0;
                        Toast.makeText(this, "验证完成", Toast.LENGTH_SHORT).show();
                        s.update(ContentListActivity.this, listbean.getCheckNo(), Constant.VERIFIED);
                        int num = s.getAllState(listbean.getTaskNumber(), ContentListActivity.this, Constant.VERIFIED).size();
                        if (num == s.getAllPoints(listbean.getTaskNumber(), ContentListActivity.this).size()) {
                            s.updateTastState(listbean.getTaskNumber(), ContentListActivity.this, Constant.VERIFIED);
                            s.updateEnd(ContentListActivity.this, listbean.getTaskNumber(), Constant.VERIFIED);
                            number = 3;
                        } else if (num > 0) {
                            s.updateTastState(listbean.getTaskNumber(), ContentListActivity.this, Constant.IMPLEMENT);
                            number = 1;
                        }
                        edit = sper.edit();
                        edit.putString("TASK_ID", listbean.getTaskNumber());
                        edit.commit();
                        intent = new Intent(this, VerNoActivity.class);
                        intent.putExtra("from", number);
                        startActivity(intent);
                    } else if (s.queryStatus(ContentListActivity.this, listbean.getCheckNo()).equals(Constant.VERIFIED)) {
                        Toast.makeText(this, "已经验证完成，请勿重复操作", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "请先执行更新信息操作!", Toast.LENGTH_SHORT).show();
                    }

                }


                break;


        }
    }

    Dialog videoDia;

    public void showVideoDialog(final String gcdId) {
        videoDia = DialogUtils.showAreaDialog(this, "拍照", "录像", "取消", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Camera(gcdId);
                videoDia.dismiss();

            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                recordVideo(gcdId);
                videoDia.dismiss();

            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogUtils.getInstance(ContentListActivity.this).hideProgressDialog();
                videoDia.dismiss();
            }
        });
    }

    private List<CheckInfoEntiy> exist;

    //传入观察点值
    private void Camera(String gcdId) {
        exist = new ArrayList<>();
        CheckInfoEntiy existCie = new CheckInfoEntiy();
        exist.addAll(s.thereIsA(ContentListActivity.this, gcdId));
        if (exist.size() != 0) {
            uuid = UUID.randomUUID();
            uniqueId = uuid.toString();
            str = exist.get(0).getFileCreateLocationNo();
            String num = str.substring(1, 8);
            fileNO = "Z" + num;
            ++count;
            fileNO1 = "Z" + num + "-" + count;
            picpath = fileNO + "-" + count + ".jpg";
            existCie.setCheckItemId(uniqueId);
            existCie.setCheckNO(exist.get(0).getCheckNO());  //相同作业单号
            existCie.setFileCreateLocationY(gcdy);
            existCie.setFileCreateLocationX(gcdx);   //相同经纬度
            existCie.setFileCreateLocationNo(exist.get(0).getFileCreateLocationNo());     //相同观察点编号
            exist.clear();
            exist.add(existCie);
        } else {
            str = gcdlist.get(0).getFileCreateLocationNo();
            String num = str.substring(1, 8);
            fileNO = "Z" + num + "-1";
            fileNO1 = "Z" + num + "-1";
            picpath = fileNO + ".jpg";
        }
        String path = s.unifyPath(0) + taskNumber + Constant.B06;
        //储存数据库路径
        savePicPath = s.unifyPath(2) + taskNumber + Constant.B06_1 + picpath;
        File path1 = new File(path);
        if (!path1.exists()) {
            path1.mkdirs();
        }
        outputImage = new File(path, picpath);
        if (outputImage.exists()) {
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imgUri = FileProvider.getUriForFile(ContentListActivity.this, "cn.piesat.minemonitor.fileprovider", outputImage);
        } else {
            imgUri = Uri.fromFile(outputImage);
        }
        intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, PIC_PATH);
    }

    /**
     * 录视频
     */
    public void recordVideo(String gcdId) {
        File file = new File(s.unifyPath(0) + taskNumber + Constant.B06);
        if (!file.exists()) {
            file.mkdirs();
        }
        exist = new ArrayList<>();
        CheckInfoEntiy existCie = new CheckInfoEntiy();
        exist.addAll(s.thereIsA(ContentListActivity.this, gcdId));
        if (exist.size() != 0) {
            uuid = UUID.randomUUID();
            uniqueId = uuid.toString();
            str = exist.get(0).getFileCreateLocationNo();
            String num = str.substring(1, 8);
            ++count;
            fileNO = "Z" + num + "-" + count;
            fileNO1 = "Z" + num + "-" + count + ".mp4";
            PHOTO_FILE_NAME = s.unifyPath(0) + taskNumber + Constant.B06 + File.separator + fileNO1;
            existCie.setCheckItemId(uniqueId);
            existCie.setCheckNO(exist.get(0).getCheckNO());  //相同作业单号
            existCie.setFileCreateLocationY(gcdy);
            existCie.setFileCreateLocationX(gcdx);   //相同经纬度
            existCie.setFileCreateLocationNo(exist.get(0).getFileCreateLocationNo());     //相同观察点编号
            exist.clear();
            exist.add(existCie);
        } else {
            String str = gcdlist.get(0).getFileCreateLocationNo();
            String num = str.substring(1, 8);
            fileNO = "Z" + num + "-" + count;
            fileNO1 = "Z" + num + "-1.mp4";
            PHOTO_FILE_NAME = s.unifyPath(0) + taskNumber + Constant.B06 + File.separator + fileNO1;
            mvideo_suoluepic = s.unifyPath(0) + taskNumber + Constant.B06 + File.separator + fileNO + ".jpg";
        }
        //储存视频数据库路径
        saveDivPath = s.unifyPath(2) + taskNumber + Constant.B06 + File.separator + fileNO1;
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File imageFile = new File(PHOTO_FILE_NAME);
        vidUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            //如果是7.0或以上
            vidUri = FileProvider.getUriForFile(this, getApplication().getPackageName() + ".fileprovider", imageFile);
        } else {
            vidUri = Uri.fromFile(imageFile);
        }
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);// 设置录制品质
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3 * 60);//限制时长(秒为单
        intent.putExtra(MediaStore.EXTRA_OUTPUT, vidUri);// 设置路径位)
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10 * 1024 * 1024);// 设置录像大小(单位:字节)
        startActivityForResult(intent, VID_PATH);
    }

    public boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    Dialog descDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PIC_PATH) {
            if (resultCode == RESULT_OK) {
                descDialog = DialogUtils.showCameraDesDialog(ContentListActivity.this, "确定", "取消", "方位", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hasSdcard()) {
                            EditText m_etDesc = descDialog.findViewById(R.id.et_camera_desc);
                            String descContent = m_etDesc.getText().toString();
                            imgDesc = (TextUtils.isEmpty(descContent) ? "暂无描述内容" : descContent);
                            if (!provice.equals("")) {
                                for (int i = 0; i < gcdlist.size(); i++) {
                                    if (gcdlist.size() == 1) {
                                        gcdlist.get(i).setFileType(Constant.PIC);
                                        gcdlist.get(i).setFileCreateTime(s.getCurrTime());
                                        gcdlist.get(i).setSensorDirection(provice);
                                        gcdlist.get(i).setFilePath(savePicPath);
                                        gcdlist.get(i).setCheckDes(imgDesc.replace(" ", ""));
                                        gcdlist.get(i).setFileName(picpath);
                                        gcdlist.get(i).setFileNo(fileNO1);
                                    }
                                }
                                for (int i = 0; i < exist.size(); i++) {
                                    if (exist.size() == 1) {
                                        exist.get(i).setFileType(Constant.PIC);
                                        exist.get(i).setFileCreateTime(s.getCurrTime());
                                        exist.get(i).setSensorDirection(provice);
                                        exist.get(i).setFilePath(savePicPath);
                                        exist.get(i).setCheckDes(imgDesc.replace(" ", ""));
                                        exist.get(i).setFileName(picpath);
                                        exist.get(i).setFileNo(fileNO1);
                                    }
                                }
                                if (exist.size() != 0) {
                                    if (exist.get(0).getFileCreateLocationNo() != null) {
                                        s.tbgcdAdd(ContentListActivity.this,
                                                exist.get(0).getCheckItemId(),
                                                listbean.getCheckNo(),
                                                exist.get(0).getCheckDes(),
                                                exist.get(0).getFileNo(),
                                                exist.get(0).getFileName(),
                                                exist.get(0).getFilePath(),
                                                exist.get(0).getFileCreateTime(),
                                                exist.get(0).getSensorDirection(),
                                                exist.get(0).getFileType(),
                                                exist.get(0).getFileCreateLocationNo(),
                                                SpHelper.getStringValue("GCDX"),
                                                SpHelper.getStringValue("GCDY"));
                                    }
                                } else {
                                    s.tbgcdAdd(ContentListActivity.this,
                                            gcdlist.get(0).getCheckItemId(),
                                            listbean.getCheckNo(),
                                            gcdlist.get(0).getCheckDes(),
                                            gcdlist.get(0).getFileNo(),
                                            gcdlist.get(0).getFileName(),
                                            gcdlist.get(0).getFilePath(),
                                            gcdlist.get(0).getFileCreateTime(),
                                            gcdlist.get(0).getSensorDirection(),
                                            gcdlist.get(0).getFileType(),
                                            gcdlist.get(0).getFileCreateLocationNo(),
                                            SpHelper.getStringValue("GCDX"),
                                            SpHelper.getStringValue("GCDY"));
                                }
                                if (s.getTBgcd(ContentListActivity.this, listbean.getCheckNo()).size() != 0) {
                                    gcdlb.setAdapter(adapter1);
                                    adapter1.notifyDataSetChanged();
                                    trackXY = new ArrayList<>();
                                    trackXY.addAll(xY());
                                    s.AddTrack(ContentListActivity.this, s.getUUID(), listbean.getCheckNo(), devinceNo, trackXY.get(0), trackXY.get(1),
                                            Constant.TRACK_EVENT_PIC, s.getCurrTime(), SpHelper.getStringValue("USERNAME"), listbean.getTaskNumber(), Constant.GPS);
                                    Toast.makeText(ContentListActivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                                    File file1 = new File(s.unifyPath(0));
                                    Uri uri = Uri.fromFile(file1);
                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                                    sendBroadcast(intent);
                                    hideKeyboard(ContentListActivity.this);
                                } else {
                                    Toast.makeText(ContentListActivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                                }
                                if (null != outputImage && null != checkInfoEntiy) {
                                    ImageUtil.makePhoto(getBaseContext(), outputImage.getPath(), checkInfoEntiy);
                                }
                                if (descDialog != null) {
                                    descDialog.dismiss();
                                    provice = "";
                                }
                            } else {
                                Toast.makeText(ContentListActivity.this, "请选择方位!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ToastUtil.show(ContentListActivity.this, "没有存储卡！");
                            if (descDialog != null) {
                                descDialog.dismiss();
                            }
                        }

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (descDialog != null) {
                            descDialog.dismiss();
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (descDialog != null) {
                            direction = descDialog.findViewById(R.id.tv_direction);
                            AlertDialog.Builder builder = new AlertDialog.Builder(ContentListActivity.this);
                            builder.setTitle("镜头指向：");
                            builder.setItems(provices, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    provice = provices[arg1];
                                    direction.setText("方位:" + provices[arg1]);
                                }
                            });
                            builder.create().show();
                        }
                    }
                });

            }
        } else if (requestCode == VID_PATH) {
            if (resultCode == RESULT_OK) {
                descDialog = DialogUtils.showCameraDesDialog(ContentListActivity.this, "确定", "取消", "方位", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hasSdcard()) {
                            EditText m_etDesc = descDialog.findViewById(R.id.et_camera_desc);
                            String descContent = m_etDesc.getText().toString();
                            imgDesc = (TextUtils.isEmpty(descContent) ? "暂无描述内容" : descContent);
                            if (!provice.equals("")) {
                                for (int i = 0; i < gcdlist.size(); i++) {
                                    if (gcdlist.size() == 1) {
                                        gcdlist.get(i).setFileType(Constant.DIV);
                                        gcdlist.get(i).setFileCreateTime(s.getCurrTime());
                                        gcdlist.get(i).setSensorDirection(provice);
                                        gcdlist.get(i).setFilePath(saveDivPath);
                                        gcdlist.get(i).setCheckDes(imgDesc.replace(" ", ""));
                                        gcdlist.get(i).setFileName(fileNO1);
                                        gcdlist.get(i).setFileNo(fileNO);
                                    }
                                }
                                for (int i = 0; i < exist.size(); i++) {
                                    if (exist.size() == 1) {
                                        exist.get(i).setFileType(Constant.DIV);
                                        exist.get(i).setFileCreateTime(s.getCurrTime());
                                        exist.get(i).setSensorDirection(provice);
                                        exist.get(i).setFilePath(saveDivPath);
                                        exist.get(i).setCheckDes(imgDesc.replace(" ", ""));
                                        exist.get(i).setFileName(fileNO1);
                                        exist.get(i).setFileNo(fileNO);
                                    }
                                }
                                if (exist.size() != 0) {
                                    if (exist.get(0).getFileCreateLocationNo() != null) {
                                        s.tbgcdAdd(ContentListActivity.this,
                                                exist.get(0).getCheckItemId(),
                                                listbean.getCheckNo(),
                                                exist.get(0).getCheckDes(),
                                                exist.get(0).getFileNo(),
                                                exist.get(0).getFileName(),
                                                exist.get(0).getFilePath(),
                                                exist.get(0).getFileCreateTime(),
                                                exist.get(0).getSensorDirection(),
                                                exist.get(0).getFileType(),
                                                exist.get(0).getFileCreateLocationNo(),
                                                SpHelper.getStringValue("GCDX"),
                                                SpHelper.getStringValue("GCDY"));
                                    }
                                } else {
                                    s.tbgcdAdd(ContentListActivity.this,
                                            gcdlist.get(0).getCheckItemId(),
                                            listbean.getCheckNo(),
                                            gcdlist.get(0).getCheckDes(),
                                            gcdlist.get(0).getFileNo(),
                                            gcdlist.get(0).getFileName(),
                                            gcdlist.get(0).getFilePath(),
                                            gcdlist.get(0).getFileCreateTime(),
                                            gcdlist.get(0).getSensorDirection(),
                                            gcdlist.get(0).getFileType(),
                                            gcdlist.get(0).getFileCreateLocationNo(),
                                            SpHelper.getStringValue("GCDX"),
                                            SpHelper.getStringValue("GCDY"));
                                }
                                if (s.getTBgcd(ContentListActivity.this, listbean.getCheckNo()).size() != 0) {
                                    gcdlb.setAdapter(adapter1);
                                    adapter1.notifyDataSetChanged();
                                    trackXY = new ArrayList<>();
                                    trackXY.addAll(xY());
                                    s.AddTrack(ContentListActivity.this, s.getUUID(), listbean.getCheckNo(), devinceNo, trackXY.get(0), trackXY.get(1),
                                            Constant.TRACK_EVENT_PIC, s.getCurrTime(), SpHelper.getStringValue("USERNAME"), listbean.getTaskNumber(), Constant.GPS);
                                    Toast.makeText(ContentListActivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                                    File file1 = new File(s.unifyPath(0));
                                    Uri uri = Uri.fromFile(file1);
                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                                    sendBroadcast(intent);
                                    hideKeyboard(ContentListActivity.this);
                                } else {
                                    Toast.makeText(ContentListActivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                                }
                                if (descDialog != null) {
                                    descDialog.dismiss();
                                    provice = "";
                                }
                            } else {
                                Toast.makeText(ContentListActivity.this, "请选择方位!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ToastUtil.show(ContentListActivity.this, "没有存储卡！");
                            if (descDialog != null) {
                                descDialog.dismiss();
                            }
                        }

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (descDialog != null) {
                            descDialog.dismiss();
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (descDialog != null) {
                            direction = descDialog.findViewById(R.id.tv_direction);
                            AlertDialog.Builder builder = new AlertDialog.Builder(ContentListActivity.this);
                            builder.setTitle("镜头指向：");
                            builder.setItems(provices, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    provice = provices[arg1];
                                    direction.setText("方位:" + provices[arg1]);
                                }
                            });
                            builder.create().show();
                        }
                    }
                });
            }

        } else if (requestCode == 100 && resultCode == CListActivity.RESULT_OK) {
            String noChanged = qxmc.getText().toString().trim();
            String changedQXMC = data.getStringExtra("DataName");
            if (!noChanged.equals(changedQXMC)) {
                String codeXZQ = s.getQXcode(changedQXMC, ContentListActivity.this);
                s.updateQXMC(ContentListActivity.this, listbean.getCheckNo(), changedQXMC, codeXZQ);
                s.deleteAllGCD(ContentListActivity.this, listbean.getCheckNo());
                gcdlist.clear();
                adapter1 = new TBgcdAdapter(ContentListActivity.this, bcfgcd());
                adapter1.setOnInnerItemOnClickListener(this);
                gcdlb.setAdapter(adapter1);
                gcdlb.setOnItemClickListener(this);
                adapter1.notifyDataSetChanged();
            }
            qxmc.setText(changedQXMC);
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
        } else if (requestCode == 1000 && resultCode == CListActivity.RESULT_OK) {
            yzfx.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 1002 && resultCode == CListActivity.RESULT_OK) {
            yzdd.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 1003 && resultCode == CListActivity.RESULT_OK) {
            yzdd.setText(data.getStringExtra("DataName"));
        } else if (requestCode == 20 && resultCode == MapRelatedActivity.RESULT_OK) {
            gcdx = data.getStringExtra("mapGCDX");
            gcdy = data.getStringExtra("mapGCDY");
            SpHelper.setStringValue("GCDX", gcdx);
            SpHelper.setStringValue("GCDY", gcdy);
            gcd();
        } else if (requestCode == 21 && resultCode == MapRelatedActivity.RESULT_OK) {
            gcdChanged = data.getIntExtra("mapGCDXG", -1);
            Toast.makeText(this, "修改成功!", Toast.LENGTH_SHORT).show();
            gtbcf();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void itemClick(View v) {
        final int position;

        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.ll_tb_gcd_pz:
                checkInfoEntiy = cieChoose.get(position);
                showVideoDialog(cieChoose.get(position).getFileCreateLocationNo());
                break;
            case R.id.ll_tb_gcd_tpck:
                cieChoose.get(position);
                intent = new Intent(this, ShowMediaActivity.class);
                intent.putExtra("taskId", cieChoose.get(position).getFileCreateLocationNo());
                intent.putExtra("mapId", mapNumber);
                startActivity(intent);
                break;
            case R.id.ll_tb_gcd_dw:
                if (cieChoose.get(position).getFilePath() == null) {
                    Toast.makeText(this, "请重新获取!进行保存", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(ContentListActivity.this, MapRelatedActivity.class);
                    intent.putExtra("Code", 21);//观察点修改
                    cieChooseShow = new ArrayList<>();
                    cieChooseShow.clear();
                    cieChooseShow.addAll(s.thereIsA(ContentListActivity.this, cieChoose.get(position).getFileCreateLocationNo()));
                    if (cieChooseShow.size() > 0) {
                        intent.putExtra("CX", cieChooseShow.get(0).getFileCreateLocationX());
                        intent.putExtra("CY", cieChooseShow.get(0).getFileCreateLocationY());
                    } else {
                        intent.putExtra("CX", cieChoose.get(position).getFileCreateLocationX());
                        intent.putExtra("CY", cieChoose.get(position).getFileCreateLocationY());
                    }
                    intent.putExtra("CN", cieChoose.get(position).getFileCreateLocationNo());
                    intent.putExtra("PlaquesNum", listbean.getMapNumber());
                    startActivityForResult(intent, 21);
                }


                break;
            case R.id.ll_tb_gcd_sc:
                AlertDialog.Builder builder = new AlertDialog.Builder(ContentListActivity.this);
                builder.setMessage("确定删除吗？");
                builder.setTitle("观察点删除");
                builder.setIcon(R.drawable.ic_alert);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s.deleteGCD(ContentListActivity.this, cieChoose.get(position).getFileCreateLocationNo());
                        if (s.thereIsA(ContentListActivity.this, cieChoose.get(position).getFileCreateLocationNo()).size() > 0) {
                            Toast.makeText(ContentListActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        } else {
                            if (cieChoose.size() > 0) {
                                cieChoose.remove(cieChoose.get(position));
                                adapter1.notifyDataSetChanged();
                                Toast.makeText(ContentListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                break;
            default:
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("整体item----->", position + "");
    }


    @Override
    protected void onResume() {
//        if (counter >= 2 && !sper.getString("qxmc", "null").equals("null")) {
//            ksmc.setText(sper.getString("ksmc", "null"));
//            jylx.setText(sper.getString("jylx", "null"));
//            qxmc.setText(sper.getString("qxmc", "null"));
//            dllx.setText(sper.getString("dllx", "null"));
//            phlx.setText(sper.getString("phlx", "null"));
//            yxdx.setText(sper.getString("yxdx", "null"));
//            zldx.setText(sper.getString("zldx", "null"));
//            kckz.setText(sper.getString("kckz", "null"));
//            kcfs.setText(sper.getString("kcfs", "null"));
//            yzfx.setText(sper.getString("yzfx", "null"));
//            yzdd.setText(sper.getString("yzdd", "null"));
//            bzNote.setText(sper.getString("bz", "null"));
//            counter = 1;
//        }

        super.onResume();
    }

    private void hideKeyboard(Context context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    //
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {}

    @Override
    protected void onPause() {
        super.onPause();
    }
}
