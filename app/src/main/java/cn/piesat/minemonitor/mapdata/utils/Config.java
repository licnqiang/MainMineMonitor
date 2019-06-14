package cn.piesat.minemonitor.mapdata.utils;

import android.os.Environment;

import java.io.File;

public class Config {

	private static final String ROOT_FILE_NAME = "PIEMapData";
	private static final String PROJECT_FILE_NAME = "projectData";
	private static final String CONFIG_FILE_NAME = "config";
	private static final String MAP_CONFIG_FILE_NAME = "map";
	private static final String WORKSPACE_FILE_NAME = "workspace.xml";
	private static String STR_SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

	public static String hasFileAndCreate(String filePath) {
		if (filePath != null && !filePath.equals("")) {
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		return filePath;
	}

	public static String getRootPath(){
		StringBuilder sb = new StringBuilder();
		sb.append(STR_SDCARD_ROOT).append(File.separator);
		sb.append(ROOT_FILE_NAME);
		return hasFileAndCreate(sb.toString());
	}

	public static String getProjectPath(){
		StringBuilder sb = new StringBuilder();
		sb.append(getRootPath()).append(File.separator);
		sb.append(PROJECT_FILE_NAME);
		return hasFileAndCreate(sb.toString());
	}

	public static String getConfgiPath(){
		StringBuilder sb = new StringBuilder();
		sb.append(getMapResoucePath()).append(File.separator);
		sb.append(CONFIG_FILE_NAME);
		return hasFileAndCreate(sb.toString());
	}

	public static String getMapResoucePath(){
		StringBuilder sb = new StringBuilder();
		sb.append(getRootPath()).append(File.separator);
		sb.append(MAP_CONFIG_FILE_NAME);
		return hasFileAndCreate(sb.toString());
	}


}
