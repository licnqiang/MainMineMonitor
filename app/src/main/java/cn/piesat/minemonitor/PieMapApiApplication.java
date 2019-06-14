package cn.piesat.minemonitor;

import android.app.Application;
import android.util.Log;

import pie.core.Workspace;

/**
 * Pie Map Application
 * @author pie
 */
public class PieMapApiApplication extends Application {

	public static Workspace mWorkspace; // 全局的Workspace 最好整个应用只初始化一次

	@Override
	public void onCreate() {
		super.onCreate();
		mWorkspace = null;
	}

	@Override
	public void onTerminate() {
		Log.i("zrc", "onTerminate");
		if (PieMapApiApplication.mWorkspace != null) {
			PieMapApiApplication.mWorkspace.close();
			PieMapApiApplication.mWorkspace = null;
		}
		Log.i("zrc", "onDestroy");
		super.onTerminate();
	}
}
