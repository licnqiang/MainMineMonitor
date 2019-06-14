package cn.piesat.minemonitor.media;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SDCardUtil {
	private static String mSDCardPath = null;
	private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
	/**
	 * 获取存储卡个数。
	 * 
	 * @param context
	 * @return
	 */
//	public static int getSDCount(Context context) {
//		return getSDPath(context).length;
//	}

	/**
	 * 获取sd卡路径。
	 * 
	 * @param context
	 * 
	 * @return
	 */
//	@SuppressLint("InlinedApi")
//	public static String[] getSDPath(Context context) {
//		StorageManager sm = (StorageManager) context
//				.getSystemService(Context.STORAGE_SERVICE);
//		try {
//			return (String[]) sm.getClass().getMethod("getVolumePaths", null)
//					.invoke(sm, null);
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		}==
//		return new String[] {};
//	}

	/**
	 * 获取当前路径的剩余大小。
	 * 
	 * @param sdcardPath
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public static long getSDCardAvailSize(String sdcardPath) {
		// 取得SDCard当前的状态
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			android.os.StatFs statfs = new android.os.StatFs(sdcardPath);
			// 获取SDCard上BLOCK总数
			long nTotalBlocks = statfs.getBlockCount();
			// 获取SDCard上每个block的SIZE
			long nBlocSize = statfs.getBlockSize();

			// 获取可供程序使用的Block的数量
			long nAvailaBlock = statfs.getAvailableBlocks();
			// 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
			long nFreeBlock = statfs.getFreeBlocks();
			// 计算SDCard 总容量大小MB
			long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;
			// 计算 SDCard 剩余大小MB
			long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;
			return nSDFreeSize;
		}
		return 0;
	}

	/**
	 * 获取当前路径的总大小。
	 * 
	 * @param sdcardPath
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public static long getSDCardTotalSize(String sdcardPath) {
		// 取得SDCard当前的状态
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			android.os.StatFs statfs = new android.os.StatFs(sdcardPath);
			// 获取SDCard上BLOCK总数
			long nTotalBlocks = statfs.getBlockCount();
			// 获取SDCard上每个block的SIZE
			long nBlocSize = statfs.getBlockSize();
			// 获取可供程序使用的Block的数量
			long nAvailaBlock = statfs.getAvailableBlocks();
			// 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
			long nFreeBlock = statfs.getFreeBlocks();
			// 计算SDCard 总容量大小MB
			long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;
			// 计算 SDCard 剩余大小MB
			long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;
			return nSDTotalSize;
		}
		return 0;
	}

	/**
	 * 
	 * 功能:获取标准的存储路径，有内置卡就返回内存卡路径；
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static String getStandardStoragePath() {
		String path = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			path = getSecondStoragePath();
			if (path != null && new File(path).exists()) {
				File secondFile = new File(path);
				String externalPath = getExternalSDCardStoragePath();
				if (externalPath != null) {
					File externalFile = new File(externalPath);
					if (externalFile.exists()) {
						if (secondFile.getTotalSpace() < externalFile
								.getTotalSpace()) {
							path = externalPath;
						}
					}
				}
			} else {
				path = getExternalSDCardStoragePath();
			}
		} else {
			path = getExternalSDCardStoragePath();
		}
		// LogSuperUtil.i(SysConstant.Log.testxc, "sdcard path="+path);
		return path;
	}

	/**
	 * 
	 * 功能:获取可移除的外置存储卡路径
	 */
	public static String getSecondStoragePath() {
		String secondPath = null;
		secondPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		// LogSuperUtil.i(SysConstant.Log.testxc, "secondPath="+secondPath);
		return secondPath;
	}

	private static String getExternalSDCardStoragePath() {
		File file = new File("/system/etc/vold.fstab");
		FileReader fr = null;
		BufferedReader br = null;
		String path = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
		}
		try {
			if (fr != null) {
				br = new BufferedReader(fr);
				String s = br.readLine();
				while (s != null) {
					if (s.startsWith("dev_mount")) {
						String[] tokens = s.split("\\s");
						path = tokens[2];
						if (!Environment.getExternalStorageDirectory()
								.getAbsolutePath().equals(path)) {
							break;
						}
					}
					s = br.readLine();
				}
			}
		} catch (IOException e) {
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
			}
		}
		return path;
	}

	public static boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	public static String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}


}
