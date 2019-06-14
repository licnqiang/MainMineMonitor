package cn.piesat.minemonitor.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;


public class LoadingDialogTools {

    private static LodingDialog lodingDialog = null;

    private LoadingDialogTools() {
    }

    public static void showDialog(Context context) {
		if (lodingDialog == null) {
        lodingDialog = new LodingDialog(context);
		}
        lodingDialog.show();
        lodingDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dismissDialog();
            }
        });
        lodingDialog.setCanceledOnTouchOutside(false);
    }

    public static void showDialogAndMsg(Context context) {
        if (lodingDialog == null) {
            lodingDialog = new LodingDialog(context);
        }
        lodingDialog.setMessage("Loading").show();
        lodingDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dismissDialog();
            }
        });
        lodingDialog.setCanceledOnTouchOutside(false);
    }

    public static void setMessage(String text) {
        if (lodingDialog != null) {
            lodingDialog.setMessage(text);
        }
    }

    public static void cancelable(boolean isCancleable) {
        if (lodingDialog != null) {
            lodingDialog.setCancelable(isCancleable);
        }
    }

    public static void dismissDialog() {
        if (lodingDialog != null) {
            lodingDialog.dismiss();
            lodingDialog = null;
        }

    }

}
