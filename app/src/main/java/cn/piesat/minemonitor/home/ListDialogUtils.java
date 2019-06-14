package cn.piesat.minemonitor.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.adapter.DsetAdapter;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.util.CompressOperate_zip4j;
import cn.piesat.minemonitor.util.FileUtil;
import cn.piesat.minemonitor.util.LoadingDialogTools;
import cn.piesat.minemonitor.util.ToastUtil;


/**
 * 导出数据集列表dialog
 */

public class ListDialogUtils extends Dialog {
    private Activity m_activity;
    private TextView tv_title;
    List<Integer> indexlist;
    private CustomSQLTools s;
    private CompressOperate_zip4j zip4j;
    public ListDialogUtils(Activity activity) {
        super(activity, R.style.common_alert_dialog);
        this.m_activity = activity;
        indexlist = new ArrayList<>();
    }

    public void showDialog(final List<String> dslist) {
        final Dialog dialog = new Dialog(m_activity, R.style.common_alert_dialog);
        dialog.setContentView(R.layout.dialog_exprotpro);
        s = new CustomSQLTools();
        RecyclerView rv_timeinfolists = (RecyclerView) dialog.findViewById(R.id.rv_dslists);
        tv_title = (TextView) dialog.findViewById(R.id.tv_tftitle);
        Button sure = (Button) dialog.findViewById(R.id.confirm_button);
        Button cancel = (Button) dialog.findViewById(R.id.cancel_button);
        rv_timeinfolists.setLayoutManager(new LinearLayoutManager(m_activity));
        rv_timeinfolists.addItemDecoration(new DividerItemDecoration(
                m_activity, DividerItemDecoration.VERTICAL));
        final DsetAdapter adapter = new DsetAdapter(dslist);
        rv_timeinfolists.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb_dscheck);
                cb.toggle();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMounted(m_activity, s.nmSd2)) {
                    dialog.dismiss();
                    LoadingDialogTools.showDialogAndMsg(m_activity);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MediaScannerConnection.scanFile(m_activity, new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()}, null, null);
//                    s.copy(s.copyPath+"nmmvsqlslite.db",s.nmSd);
//                    s.getExtSDCardPathList();
                            Map<Integer, Boolean> maplist = adapter.getM_selectedIndexMap();
                            if (maplist != null && maplist.size() > 0) {
                                for (int index : maplist.keySet()) {
                                    indexlist.add(index);
                                }

                                if (indexlist != null && indexlist.size() > 0) {
                                    for (int i : indexlist) {
                                        String dsetName = dslist.get(i);
//                            File file = new File(s.copyPath+dsetName+"/B06验证相关数据");
//                            boolean bexport = s.copyFile(file, s.copyPath1);
//                                File file = new File(s.copyPath1 + dsetName);
                                        File file = new File(s.unifyPath(1) + dsetName);
                                        if (!file.exists()) {
                                            try {
                                                //按照指定的路径创建文件夹
                                                file.mkdirs();
                                            } catch (Exception e) {
                                                // TODO: handle exception
                                            }
                                        }
//                                int bexport = s.copy(s.copyPath + dsetName + "/B06验证相关数据", s.copyPath1 + dsetName + "/");
                                        int bexport = s.copy(s.unifyPath(0) + dsetName + Constant.B06, s.unifyPath(1) + dsetName + "/");
                                        if (bexport != 0) {
                                            ToastUtil.show(m_activity, "导出失败");
                                            LoadingDialogTools.dismissDialog();
                                        } else {
//                                copy(s.copyPath + dsetName + "/B06验证相关数据",s.copyPath1 + dsetName );
                                            File file1 = new File(s.unifyPath(1));
                                            Uri uri = Uri.fromFile(file1);
                                            zip4j = new CompressOperate_zip4j();
                                            zip4j.compressZip4j(String.valueOf(file1), s.nmSd2+"/", "123456");
                                            FileUtil.deleteDir(String.valueOf(file1));
                                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(s.nmSd2));
                                            m_activity.sendBroadcast(intent);
                                            ToastUtil.show(m_activity, "导出成功");
                                            LoadingDialogTools.dismissDialog();
                                        }
                                    }
                                }
                            } else {
                                ToastUtil.show(m_activity, "无选中数据集");
                                return;
                            }
                            File fileSdCopy = new File(s.unifyPath(0) + Constant.SQLITENAME);
                            s.copyFile(fileSdCopy, s.unifyPath(1));
                        }
                    }, 2000);//3秒后执行Runnable中的run方法


                } else

                {
                    Toast.makeText(m_activity, "请插入SD卡!", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Display defaultDisplay = m_activity.getWindowManager().getDefaultDisplay();
        int width = (int) (defaultDisplay.getWidth() * 0.8);
        int height = (int) (defaultDisplay.getHeight() * 0.5);
        android.view.WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width;
        params.height = height;
        dialog.getWindow().setAttributes(params);
        dialog.show();

    }

    public static boolean checkMounted(Context context, String mountPoint) {
        if (mountPoint == null) {
            return false;
        }
        StorageManager storageManager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);
        try {
            Method getVolumeState = storageManager.getClass().getMethod(
                    "getVolumeState", String.class);
            String state = (String) getVolumeState.invoke(storageManager,
                    mountPoint);
            return Environment.MEDIA_MOUNTED.equals(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
