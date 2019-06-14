package cn.piesat.minemonitor.media;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import cn.piesat.minemonitor.R;

/**
 * Created by yjl on 2018/3/19.
 */

public class DialogUtils {
    static Dialog m_dialog;
    private static DialogUtils dialogUtils;
    private static ProgressDialog progressDialog;

    public static DialogUtils getInstance(Context context) {
        progressDialog = new ProgressDialog(context);
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        return dialogUtils;
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    // 显示对话框界面
    @SuppressWarnings("deprecation")
    public static Dialog showAreaDialog(Activity activity, String firstName, String secondName, String thirdName, View.OnClickListener fristClick, View.OnClickListener secondClick, View.OnClickListener cancleClick) {
        m_dialog = new Dialog(activity, R.style.common_alert_dialog);
        m_dialog.setContentView(R.layout.dialog_changearea);
        TextView china = (TextView) m_dialog.findViewById(R.id.tv_areachina);
        TextView singpo = (TextView) m_dialog.findViewById(R.id.tv_areasingpo);
        TextView cancel = (TextView) m_dialog.findViewById(R.id.tv_areacancel);
        china.setText(firstName);
        singpo.setText(secondName);
        cancel.setText(thirdName);
        Window window = m_dialog.getWindow();
        android.view.WindowManager.LayoutParams params = m_dialog.getWindow().getAttributes();
        Display display = activity.getWindowManager().getDefaultDisplay();
        params.width = (int) (display.getWidth());
        params.height = (int) (display.getHeight() * 1 / 3);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        m_dialog.show();
        china.setOnClickListener(fristClick);
        singpo.setOnClickListener(secondClick);
        cancel.setOnClickListener(cancleClick);
        return m_dialog;
    }

    /**
     * Dec: 显示拍照和视频的描述对话框界面
     *
     * @param activity    上下文
     * @param firstName   第一个按钮名称
     * @param secondName  第二个按钮名称
     * @param fristClick  第一个点击事件
     * @param secondClick 第二个点击事件
     * @return 返回一个Dialog
     */
    public static Dialog showCameraDesDialog(Activity activity, String firstName, String secondName, String threeName, View.OnClickListener fristClick, View.OnClickListener secondClick, View.OnClickListener threeClick) {
        m_dialog = new Dialog(activity, R.style.common_alert_dialog);
        m_dialog.setContentView(R.layout.dialog_desc_for_camera);
        Window window = m_dialog.getWindow();
        EditText mEtDesc = (EditText) window.findViewById(R.id.et_camera_desc);
        TextView cancelTV = (TextView) window.findViewById(R.id.tv_sure);
        TextView sureTV = (TextView) window.findViewById(R.id.tv_cancel);
        TextView direction = (TextView) window.findViewById(R.id.tv_direction);
        sureTV.setText(firstName);
        cancelTV.setText(secondName);
        Log.e("------","--threeName----"+threeName);
        direction.setText(threeName);
        android.view.WindowManager.LayoutParams params = m_dialog.getWindow().getAttributes();
        Display display = activity.getWindowManager().getDefaultDisplay();
        params.width = (int) (display.getWidth());
        params.height = (int) (display.getHeight() * 2 / 3);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        m_dialog.show();
        sureTV.setOnClickListener(fristClick);
        cancelTV.setOnClickListener(secondClick);
        direction.setOnClickListener(threeClick);
        return m_dialog;
    }
}
