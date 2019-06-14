package cn.piesat.minemonitor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.home.Constant;
import cn.piesat.minemonitor.mapdata.utils.Config;
import cn.piesat.minemonitor.mapdata.utils.FieldListDialogUtils;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;
import cn.piesat.minemonitor.util.CompressOperate_zip4j;
import cn.piesat.minemonitor.util.DrawableSwitch;
import cn.piesat.minemonitor.util.FileUtil;
import cn.piesat.minemonitor.util.LoadingDialogTools;
import cn.piesat.minemonitor.util.RarDecompressionUtil;
import cn.piesat.minemonitor.util.ToastUtil;

/**
 * 登录页面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    String[] mPerms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int mRequestCode = 0x1;
    private Button login;
    private Intent intent;
    @BindView(R.id.tv_registered)
    TextView tv_registered;
    @BindView(R.id.et_user_name)
    EditText user_name;
    @BindView(R.id.et_password)
    EditText password;
    private DrawableSwitch ds_all_clean;
    private SharedPreferences sp;
    private CustomSQLTools s;
    private SharedPreferences.Editor editor;
    private CompressOperate_zip4j zip4j;
    private List<File> list;
    public static String encryptDir = "VerifyData";
    public static String encrypt2 = encryptDir;
    private int tag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpHelper.setStringValue("FIELD", "VerifyData");
        s = new CustomSQLTools();
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sp.getBoolean("LOGINTAG", true)) {
            editor = sp.edit();
            editor.putBoolean("LOGINTAG", false);
            editor.putString("FIELD1", "0");
            editor.commit();
        }
        ButterKnife.bind(this);
        initView();
        onClickListener();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, mPerms, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    showPermiDialog();
                }
            }
        }
    }

    /**
     * 提示手动开启权限
     */
    private void showPermiDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("提示")
                .setCancelable(false)
                .setMessage("需要获取相应权限，否则您的应用将无法正常使用! 请手动开启权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        // Start for result
                        MainActivity.this.startActivityForResult(intent, mRequestCode);
                    }
                })
                .create().show();
    }


    private void onClickListener() {
        login.setOnClickListener(this);
    }

    private void initView() {
        user_name.setText("");
        password.setText("");
        login = findViewById(R.id.btn_login);
        ds_all_clean = findViewById(R.id.ds_all_clean);
        tv_registered.setOnClickListener(this);
        if (sp.getBoolean("isSwitchOn", false)) {
            user_name.setText(sp.getString("username", ""));
            password.setText(sp.getString("password", ""));
            ds_all_clean.setSwitchOn(true);
        }
       /* if(ds_all_clean.isSwitchOn()){

        }*/

        ds_all_clean.setListener(new DrawableSwitch.MySwitchStateChangeListener() {
            @Override
            public void mySwitchStateChanged(boolean isSwitchOn) {
                if (!isSwitchOn) {
//                    user_name.setText("");
                    password.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_registered:
                List<String> setFileName = new ArrayList<>();
                List<String> setFileName1 = new ArrayList<>();
                setFileName.clear();
                setFileName1.clear();
                setFileName.addAll(s.getFileName());
                for (int i = 0; i < setFileName.size(); i++) {
                    if (setFileName.get(i).contains("/VerifyData")) {
                        setFileName1.add(setFileName.get(i).substring(setFileName.get(i).indexOf("/V") + 1));
                    }

                }
                final FieldListDialogUtils dialog = new FieldListDialogUtils(MainActivity.this);
                dialog.showDialog1(setFileName1, new FieldListDialogUtils.OnItemFieldListener() {
                    @Override
                    public void getFieldName(String field) {
                        String path = Config.getProjectPath() + "/default/config";
                        String sqlData = Config.getProjectPath() + "/default/data/" + field + "/nmmvsqlite.db";
                        File file = new File(sqlData);
                        if (!file.exists()) {
                            Toast.makeText(MainActivity.this, "数据未切换成功!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            deleteFile(new File(path));
                            SpHelper.setStringValue("FIELD", field);
                            Toast.makeText(MainActivity.this, field + "数据切换成功!", Toast.LENGTH_SHORT).show();
                        }
                        //deleteAllFiles(new File(path));

                    }
                });
                break;
            case R.id.btn_login:
                LoadingDialogTools.showDialog(this);
                saveData(new DataCallBack() {
                    @Override
                    public void succeed() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LoadingDialogTools.dismissDialog();
                                intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void failed(final String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LoadingDialogTools.dismissDialog();
                                ToastUtil.show(MainActivity.this, msg);
                            }
                        });
                    }
                });

                break;
            default:
                break;
        }

    }


    private void saveData(final DataCallBack dataCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpHelper.setStringValue("RUNXY", "2");
                encryptDir = SpHelper.getStringValue("FIELD");
                if (encryptDir.equals("")) {
                    encryptDir = "VerifyData";
                }
                editor = sp.edit();
                editor.putString("FIELD1", encryptDir);
                editor.commit();
                list = new ArrayList<>();
                String name = user_name.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                try {
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
                        String employee_name = new CustomSQLTools().getPwdName(name, pwd, MainActivity.this);
                        if (!TextUtils.isEmpty(employee_name)) {
                            SpHelper.setStringValue("USERNAME", employee_name);
                            SpHelper.setStringValue("PASSWORD", pwd);
                            if (ds_all_clean.isSwitchOn()) {
                                editor = sp.edit();
                                editor.putString("username", name);
                                editor.putString("password", pwd);
                                editor.putBoolean("isSwitchOn", true);
                                editor.commit();
                            }
                            final List<String> trackXY = new ArrayList<>();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    trackXY.addAll(xY());
                                    if (trackXY.size() > 0) {
                                        s.AddTrack(MainActivity.this, s.getUUID(), "", "", trackXY.get(0), trackXY.get(1),
                                                Constant.TRACK_EVENT_LOGIN, s.getCurrTime(), SpHelper.getStringValue("USERNAME"), "", Constant.GPS);
                                    }
                                }
                            });
                            List<String> jiedir = new ArrayList<>();
                            jiedir.addAll(FileUtil.getFilesAllName(FileUtil.encrypt + encryptDir));
                            for (int i = 0; i < jiedir.size(); i++) {
                                if (new File(jiedir.get(i) + FileUtil.bt + ".rar").exists()) {
                                    Log.e("----------", "-----------" + jiedir.get(i) + FileUtil.bt + ".rar");
                                    RarDecompressionUtil.unrar(jiedir.get(i) + FileUtil.bt + ".rar", jiedir.get(i), "hangtianhongtu");
                                }
                                if (new File(jiedir.get(i) + FileUtil.bs + ".rar").exists()) {
                                    RarDecompressionUtil.unrar(jiedir.get(i) + FileUtil.bs + ".rar", jiedir.get(i), "hangtianhongtu");
                                }
                            }
                            dataCallBack.succeed();
                        } else {
                            dataCallBack.failed("用户名或密码错误");
                        }
                    } else {
                        dataCallBack.failed("用户名或密码不能为空");
                    }
                } catch (final Exception e) {
                    dataCallBack.failed(e.getMessage());
                }
            }
        }).start();
    }

    interface DataCallBack {
        void succeed();

        void failed(String msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!ds_all_clean.isSwitchOn()) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isSwitchOn", false);
            editor.commit();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!ds_all_clean.isSwitchOn()) {
            user_name.setText("");
            password.setText("");
        }

    }

    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }
}
