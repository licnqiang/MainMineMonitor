package cn.piesat.minemonitor.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.piesat.minemonitor.entity.CheckInfoEntiy;

/**
 * @author lq
 * @fileName ImageUtil
 * @data on  2019/6/17 9:53
 * @describe TODO
 */
public class ImageUtil {

    //添加图片水印 并保存
    public static void makePhoto(Context context, String photoName, CheckInfoEntiy checkInfoEntiy) {
        FileOutputStream b = null;
        Bitmap bitmap = PhotoBitmapUtils.amendRotatePhoto(photoName, context);
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        String date = formater.format(curDate);   //获取当前的日期事件信息
        if (bitmap != null) {
            String N=doubleToString(Double.parseDouble(checkInfoEntiy.getFileCreateLocationY()));
            String watermarker = "北纬" + doubleToString(Double.parseDouble(checkInfoEntiy.getFileCreateLocationY()))
                    + "  东经" + doubleToString(Double.parseDouble(checkInfoEntiy.getFileCreateLocationX()));
            Bitmap bitmap1 = WatermarkTool.drawTextToRightBottom(context, bitmap, date, 24, Color.WHITE, 50, 50); //添加坐标水印
            Bitmap bitmap2 = WatermarkTool.drawTextToRightBottom(context, bitmap1, watermarker, 24, Color.WHITE, 50, 20); //添加坐标水印
            try {
                b = new FileOutputStream(photoName);
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把图片数据写入指定的文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (b != null) {
                        b.flush();   //刷新输出流
                        b.close();   //关闭输出流
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.000000").format(num);
    }
}
