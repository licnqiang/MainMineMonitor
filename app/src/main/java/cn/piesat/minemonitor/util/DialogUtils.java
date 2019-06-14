package cn.piesat.minemonitor.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import cn.piesat.minemonitor.R;


public class DialogUtils {

	private static ProgressDialog progressDialog;

	@SuppressWarnings("unused")
	private ProgressDialog AlertDialog;
	private static DialogUtils dialogUtils;
	// private static AlertDialog.Builder builder;
	static Dialog m_dialog;

	public static DialogUtils getInstance(Context context) {
		progressDialog = new ProgressDialog(context);
		if (dialogUtils == null) {
			dialogUtils = new DialogUtils();
		}
		return dialogUtils;
	}

	public DialogUtils() {
		// TODO Auto-generated constructor stub
	}

	public static void showAlertDialog(Context context, String msg, DialogInterface.OnClickListener dialogListener) {
		// context = context.getApplicationContext();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("操作提示").setMessage(msg).setCancelable(false).setPositiveButton("确定", dialogListener).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public ProgressDialog showProgressDialog(Context context, String msg) {
		// ProgressDialog progressDialog = new ProgressDialog(context);
		int style = ProgressDialog.STYLE_SPINNER;
		progressDialog.setProgressStyle(style);
		progressDialog.setCanceledOnTouchOutside(false);
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setCancelable(false);
		// dialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage(msg);
		// showOrClose(dialog,true);
		progressDialog.show();
		return progressDialog;
	}

	public static ProgressDialog showProgressDialog2(Context context, String msg) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		int style = ProgressDialog.STYLE_SPINNER;
		progressDialog.setProgressStyle(style);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setMessage(msg);
		progressDialog.show();
		return progressDialog;

	}

	public void hideProgressDialog() {
		progressDialog.dismiss();
	}

	// public static void showOrClose(boolean flag) {
	// // TODO Auto-generated method stub
	// if (flag) {
	// progressDialog.show();
	// } else {
	// progressDialog.cancel();
	// }
	// }

	// 显示对话框界面
	@SuppressWarnings("deprecation")
	public static Dialog showAreaDialog(Activity activity, String firstName, String secondName, String thirdName, OnClickListener fristClick, OnClickListener secondClick, OnClickListener cancleClick) {
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
		params.height = (int) (display.getHeight() * 1 / 4);
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);
		m_dialog.show();
		china.setOnClickListener(fristClick);
		singpo.setOnClickListener(secondClick);
		cancel.setOnClickListener(cancleClick);
		return m_dialog;
	}

	/**
	 * Dec: 显示事件上报对话框界面
	 * @param activity 上下文
	 * @param firstName 第一个按钮名称
	 * @param secondName 第二个按钮名称
	 * @param fristClick 第一个点击事件
	 * @param secondClick 第二个点击事件
	 * @return 返回一个Dialog
	 */
	public static Dialog showReportDialog(Activity activity, String firstName, String secondName,OnClickListener fristClick, OnClickListener secondClick) {
		m_dialog = new Dialog(activity, R.style.common_alert_dialog);
		m_dialog.setContentView(R.layout.dialog_area_for_resport);
		TextView onLineTV = (TextView) m_dialog.findViewById(R.id.tv_report_online);
		TextView offLineTV = (TextView) m_dialog.findViewById(R.id.tv_resport_offline);
		onLineTV.setText(firstName);
		offLineTV.setText(secondName);
		Window window = m_dialog.getWindow();
		android.view.WindowManager.LayoutParams params = m_dialog.getWindow().getAttributes();
		Display display = activity.getWindowManager().getDefaultDisplay();
		params.width = (int) (display.getWidth());
		params.height = (int) (display.getHeight() * 1 / 4);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		m_dialog.show();
		onLineTV.setOnClickListener(fristClick);
		offLineTV.setOnClickListener(secondClick);
		return m_dialog;
	}

	/**
	 * Dec: 显示拍照和视频的描述对话框界面
	 * @param activity 上下文
	 * @param firstName 第一个按钮名称
	 * @param secondName 第二个按钮名称
	 * @param fristClick 第一个点击事件
	 * @param secondClick 第二个点击事件
	 * @return 返回一个Dialog
	 */
	public static Dialog showCameraDesDialog(Activity activity, String firstName, String secondName,OnClickListener fristClick, OnClickListener secondClick) {
		m_dialog = new Dialog(activity, R.style.common_alert_dialog);
		m_dialog.setContentView(R.layout.dialog_desc_for_camera);
		EditText mEtDesc = (EditText) m_dialog.findViewById(R.id.et_camera_desc);
		TextView sureTV = (TextView) m_dialog.findViewById(R.id.tv_sure);
		TextView cancelTV = (TextView) m_dialog.findViewById(R.id.tv_cancel);
		sureTV.setText(firstName);
		cancelTV.setText(secondName);
		Window window = m_dialog.getWindow();
		android.view.WindowManager.LayoutParams params = m_dialog.getWindow().getAttributes();
		Display display = activity.getWindowManager().getDefaultDisplay();
		params.width = (int) (display.getWidth());
		params.height = (int) (display.getHeight() * 1 / 3);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		m_dialog.show();
		sureTV.setOnClickListener(fristClick);
		cancelTV.setOnClickListener(secondClick);
		return m_dialog;
	}

	/**
	 * Dec: 显示长按删除的对话框界面
	 * @param activity 上下文
	 * @param firstName 第一个按钮名称
	 * @param secondName 第二个按钮名称
	 * @param fristClick 第一个点击事件
	 * @param secondClick 第二个点击事件
	 * @return 返回一个Dialog
	 */
	public static Dialog showDeleteDialog(Activity activity, String firstName, String secondName,OnClickListener fristClick, OnClickListener secondClick) {
		m_dialog = new Dialog(activity, R.style.common_alert_dialog);
		m_dialog.setContentView(R.layout.dialog_delete_for_onekey);

		TextView sureTV = (TextView) m_dialog.findViewById(R.id.tv_sure);
		TextView cancelTV = (TextView) m_dialog.findViewById(R.id.tv_cancel);
		sureTV.setText(firstName);
		cancelTV.setText(secondName);
		Window window = m_dialog.getWindow();
		android.view.WindowManager.LayoutParams params = m_dialog.getWindow().getAttributes();
		Display display = activity.getWindowManager().getDefaultDisplay();
		params.width = (int) (display.getWidth())*2/3;
		params.height = (int) (display.getHeight() * 1 / 3);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		m_dialog.show();
		sureTV.setOnClickListener(fristClick);
		cancelTV.setOnClickListener(secondClick);
		return m_dialog;
	}



	/**
	 * Dec: 显示长按删除的对话框界面
	 * @param activity 上下文
	 * @param firstName 第一个按钮名称
	 * @param secondName 第二个按钮名称
	 * @param fristClick 第一个点击事件
	 * @param secondClick 第二个点击事件
	 * @return 返回一个Dialog
	 */
	public static Dialog showOutDialog(Activity activity, String firstName, String secondName,OnClickListener fristClick, OnClickListener secondClick) {
		m_dialog = new Dialog(activity, R.style.common_alert_dialog);
		m_dialog.setContentView(R.layout.dialog_out_for_onekey);

		TextView sureTV = (TextView) m_dialog.findViewById(R.id.tv_sure);
		TextView cancelTV = (TextView) m_dialog.findViewById(R.id.tv_cancel);
		sureTV.setText(firstName);
		cancelTV.setText(secondName);
		Window window = m_dialog.getWindow();
		android.view.WindowManager.LayoutParams params = m_dialog.getWindow().getAttributes();
		Display display = activity.getWindowManager().getDefaultDisplay();
		params.width = (int) (display.getWidth())*2/3;
		params.height = (int) (display.getHeight() * 1 / 3);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		m_dialog.show();
		sureTV.setOnClickListener(fristClick);
		cancelTV.setOnClickListener(secondClick);
		return m_dialog;
	}

//	public static Dialog showBeSureDialog(Context context) {
//		m_dialog = new Dialog(context, R.style.to_besure_dialog);
//		m_dialog.setContentView(R.layout.event_sure_layout);
//		return m_dialog;
//	}
//
//	public static Dialog showSetGestureDialog(Context context) {
//		m_dialog = new Dialog(context, R.style.to_besure_dialog);
//		m_dialog.setContentView(R.layout.gesture_setting_layout);
//		return m_dialog;
//	}
//
//	public static Dialog showCacheCleanDialog(Context context) {
//		m_dialog = new Dialog(context, R.style.to_besure_dialog);
//		m_dialog.setContentView(R.layout.cache_clean_setting_layout);
//		return m_dialog;
//	}
//
//	public static Dialog showLogOutDialog(Context context) {
//		m_dialog = new Dialog(context, R.style.to_besure_dialog);
//		m_dialog.setContentView(R.layout.log_out_setting_layout);
//		// Window window = m_dialog.getWindow();
//		// android.view.WindowManager.LayoutParams params =
//		// window.getAttributes();
//		// Display display = ((Activity) context).getWindowManager()
//		// .getDefaultDisplay();
//		// params.width = (int) (display.getWidth());
//		// params.height = (int) (display.getHeight());
//		// params.gravity = Gravity.CENTER_VERTICAL;
//		// window.setAttributes(params);
//		return m_dialog;
//	}
	public interface CameraDescCallBack{
		void setDesc(String desc);
	}
}
