package cn.piesat.minemonitor.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    public static String ty = "/PIEMapData/projectData/default/data";
    public static String encrypt = Environment.getExternalStorageDirectory().getAbsolutePath()+ty+"/";
    public static String encryptDB = "/nmmvsqlite.db";
    public static String encryptZIP ="/db.zip";
    public static String encryptDBJ = "/nmmvsqlite.db-journal";
    public static String encryptTEX ="/VerifyAnalysis.txt";
    public static String bt = "/B02解译成果数据";
    public static String bs = "/B03行政区划数据";
    private static int count;

    public static void deletefile(List<File> fileName) {
        try {
            // 找到文件所在的路径并删除该文件
            for (int i = 0; i < fileName.size(); i++) {
                File file = new File(String.valueOf(fileName.get(i)));
                file.delete();
            }
            Log.i("fd","删除成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String> getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            Log.e("error","空目录");return null;}
        List<String> s = new ArrayList<>();
        for (File f : files) {
            if (f.isDirectory()) {
                if (f.getName().contains("T")){
                    s.add(f.getAbsolutePath());
                }

            }
        }
        return s;
    }
    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
    public void copyFile(String oldPath, String newPath) {
        try {
//           int  bytesum  =  0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {  //文件存在时
                InputStream inStream = new FileInputStream(oldPath);  //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
//               int  length;
                while ((byteread = inStream.read(buffer)) != -1) {
//                   bytesum  +=  byteread;  //字节数  文件大小
//                   System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }
}
