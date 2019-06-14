package cn.piesat.minemonitor.media;

import java.io.File;

/**
 * Created by yjl on 2018/3/19.
 */

public class Config {
    private static String STR_SDCARD_ROOT = SDCardUtil.getStandardStoragePath();
    private static final String DATA = "data";
    private static final String ROOT_PATH = "Android";
    private static String USER_NAME = "mine";
    private static final String RECORD = "record";
    private static final String IMAGE = "image";
    /**
     * 获取数据根目录。
     *
     * @return 数据根目录。
     */
    public static String getRootPath() {
        return new StringBuffer().append(STR_SDCARD_ROOT).append(File.separator).append(ROOT_PATH).toString();
    }
    /**
     * 功能:获取当前用户的数据目录 /sdcard/Location/data/username
     */
    public static String getDataPath() {
        return new StringBuffer().append(getRootPath()).append(File.separator).append(DATA).toString();
    }

    public static String getDataRecordPath() {
        String imagePath = new StringBuffer().append(getDataPath()).append(File.separator).append(USER_NAME).append(File.separator).append(RECORD).toString();
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return imagePath;
    }


    public static String getDataImgPath() {
        String imagePath = new StringBuffer().append(getDataPath()).append(File.separator).append(USER_NAME).append(File.separator).append(IMAGE).toString();
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return imagePath;
    }
}
