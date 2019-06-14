package cn.piesat.minemonitor.mapdata.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    /**
     * 获取指定文件夹下的所有文件名称
     *
     * @param filePath
     * @return
     */
    public static List<String> getFileNames(String filePath) {
        List<String> filesList = new ArrayList<String>();
        File file = new File(filePath);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                filesList.add(tempFile.getName());
            }
            return filesList;
        }
        return null;
    }

    /**
     * 获取指定文件夹下的所有子文件夹名称
     *
     * @param filePath
     * @return
     */
    public static List<String> getDirectoryNames(String filePath) {
        List<String> filesList = new ArrayList<String>();
        File file = new File(filePath);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                if (tempFile.isDirectory()) {
                    filesList.add(tempFile.getName());
                }
            }
            return filesList;
        }

        return null;
    }

    public static String hasFileAndCreate(String filePath) {
        if (filePath != null && !filePath.equals("")) {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return filePath;
    }

    /**
     * 判断文件是否存在通过文件路径
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean hasFileByPath(String filePath) {
        if (filePath != null && !filePath.equals("")) {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某文件夹中是否存在某文件名的文件
     *
     * @param directory 文件夹路径
     * @param fileName  文件名
     * @return
     */
    public static String hasFileByName(String directory, String fileName) {
        File file = new File(directory);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                String tempFileName = tempFile.getName().toLowerCase();
                fileName = fileName.toLowerCase();
                if (tempFile.isFile() && tempFileName.equals(fileName)) {
                    return tempFile.getAbsolutePath();
                } else if (tempFile.isDirectory()) {
                    String path = hasFileByName(tempFile.getAbsolutePath(), fileName);
                    if (!TextUtils.isEmpty(path)) {
                        return path;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断某文件夹中是否存在某格式的文件
     *
     * @param directory  文件夹路径
     * @param fileSuffix 文件后缀名
     * @return
     */
    public static String hasFileBySuffix(String directory, String fileSuffix) {
        File file = new File(directory);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                if (tempFile.isFile() && tempFile.getName().endsWith(fileSuffix)) {
                    return tempFile.getAbsolutePath();
                } else if (tempFile.isDirectory()) {
                    String path = hasFileBySuffix(tempFile.getAbsolutePath(), fileSuffix);
                    if (!TextUtils.isEmpty(path)) {
                        return path;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断某文件夹中是否存在某格式的文件
     *
     * @param directory  文件夹路径
     * @param except     不包含这些文件夹
     * @param fileSuffixs 文件后缀名
     * @return
     */
    public static List<File> hasFileBySuffix(String directory, List<String> except, List<String> fileSuffixs) {
        List<File> goalFiles = new ArrayList<>();
        File file = new File(directory);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                if (except == null || !except.contains(tempFile.getName())) {
                    if (tempFile.isFile()) {
                        String fileName = tempFile.getName();
                        try {
                            if(fileName.contains(".")) {
                                String hz = fileName.substring(fileName.lastIndexOf("."));
                                if (fileSuffixs.contains(hz)) {
                                    goalFiles.add(tempFile);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else if (tempFile.isDirectory()) {
                        List<File> childFiles = hasFileBySuffix(tempFile.getAbsolutePath(), null, fileSuffixs);
                        if (childFiles != null) {
                            goalFiles.addAll(childFiles);
                        }
                    }
                }
            }
        }
        return goalFiles;
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }
    }

    public static void deleteAll(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                if (tempFile.isFile()) {
                    tempFile.delete();
                } else {
                    deleteAll(tempFile.getAbsolutePath());
                }
            }
            file.delete();
        }
    }

    public static void deleteDirectoryFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                if (tempFile.isFile()) {
                    tempFile.delete();
                }
            }
        }
    }

}
