package cn.piesat.minemonitor.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cn.piesat.minemonitor.entity.CheckInfoEntiy;
import cn.piesat.minemonitor.entity.CheckTrackEntiy;
import cn.piesat.minemonitor.entity.TaskEntity;
import cn.piesat.minemonitor.entity.TaskListEntity;
import cn.piesat.minemonitor.entitys.RegisteredEntity;
import cn.piesat.minemonitor.home.Constant;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;


/**
 * Created by yjl on 2018/4/8.
 */

public class CustomSQLTools {
    //数据库存储路径
    private String filePath;
    private SQLiteDatabase database;
    private String uid_pfix;
    private int count;
    private List<CheckInfoEntiy> checkbean;

    public String chooseFileName = SpHelper.getStringValue("FIELD");
    public String pathlib = "/PIEMapData/projectData/default/data/VerifyData/";
    private String employee_name;
    private UUID uuid;
    public String export = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PIEMapData/projectData/default/";
    public String copyPath = Environment.getExternalStorageDirectory().getAbsolutePath() + pathlib;
    public String nmSd = "/storage/sdcard1/VerifyData/";
    public String nmSd2 = "/storage/sdcard1";
    private String thisYear;
    private String lastYear;
    private List<String> fileNameList;

    public String unifyPath(int i) {
        String unifyPath = "";
        switch (i) {
            case 0://选择shp导出路径以及选择刷新数据路径
                if (chooseFileName.equals("0")) {
                    copyPath = Environment.getExternalStorageDirectory().getAbsolutePath() + pathlib;
                } else {
                    copyPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PIEMapData/projectData/default/data/" + chooseFileName + "/";
                }
                unifyPath = copyPath;
                break;
            case 1://选择SD卡备份路径
                if (chooseFileName.equals("0")) {
                    nmSd = "/storage/sdcard1/VerifyData/";
                } else {
                    nmSd = "/storage/sdcard1/" + chooseFileName + "/";
                }
                unifyPath = nmSd;
                break;
            case 2://储存数据库路径
                if (chooseFileName.equals("0")) {
                    copyPath = pathlib;
                } else {
                    copyPath = "/PIEMapData/projectData/default/data/" + chooseFileName + "/";
                }
                unifyPath = copyPath;
                break;
        }

        return unifyPath;
    }

    public SQLiteDatabase openDatabase(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        chooseFileName = sharedPreferences.getString("FIELD1", "VerifyData");
        if (chooseFileName.equals("0")) {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + pathlib + "nmmvsqlite.db";
        } else {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PIEMapData/projectData/default/data/" + chooseFileName + "/nmmvsqlite.db";
        }
        Log.d("filename", "openDatabase: " + filePath);
        File jhPath = new File(filePath);
        //查看数据库文件是否存在
        if (jhPath.exists()) {
            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        } else {
            Toast.makeText(context, "数据库文件不存在", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 登陆表查询
     */
    public String getPwdName(String userName, String pwd, Context context) {
        String sql = "select employee_name,loginname,password from EMPLOYEE where loginname=? and password=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase database = s.openDatabase(context);
        Cursor cursor = database.rawQuery(sql, new String[]{userName, pwd});
        RegisteredEntity entity = null;
        while (cursor.moveToNext()) {
            entity = new RegisteredEntity();
            employee_name = cursor.getString(cursor
                    .getColumnIndex("employee_name"));
        }
        return employee_name;
    }

    /**
     * 查询设备文件下图斑路径
     * 参数说明：taskID 为子路径
     * tbpath为子路径本年图斑名称
     */
    public String getTBPath(String taskId, String tbpath) {
        filePath = unifyPath(0) + taskId + "/B05任务相关数据/" + tbpath;
//        T062120180425001
        return filePath;
    }

    /**
     * 查询设备文件下图斑路径
     * 参数说明：taskID 为子路径
     * tbpath为子路径去年图斑名称
     */
    public String getTBPathLastYear(String taskId, String tbpath) {
        filePath = unifyPath(0) + taskId + "/B04历年成果数据/" + tbpath;
        return filePath;
    }


    /**
     * 1
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 查询指定的任务子项数据
     */
    public List<TaskListEntity> getAllPoints(String id, Context context) {
        String sql = "select * from TB_DATA_CHECK_ITEM where TASK_ID=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase database = s.openDatabase(context);
        List<TaskListEntity> pointList = new ArrayList<TaskListEntity>();
        TaskListEntity point = null;
        Cursor cursor = database.rawQuery(sql, new String[]{id});
        String state;
        while (cursor.moveToNext()) {
            point = new TaskListEntity();
            point.setTaskNumber(cursor.getString(cursor
                    .getColumnIndex("TASK_ID")));
            point.setMapNumber(cursor.getString(cursor
                    .getColumnIndex("KS_TBBH")));
            point.setMineNumber(cursor.getString(cursor
                    .getColumnIndex("KS_NAME")));
            point.setDataType(cursor.getString(cursor
                    .getColumnIndex("DATATYPE")));
            point.setXzqName(cursor.getString(cursor
                    .getColumnIndex("XZQNAME")));
            point.setKsDLMC(cursor.getString(cursor
                    .getColumnIndex("KS_DLMC")));
            point.setKsPHLX(cursor.getString(cursor
                    .getColumnIndex("KS_PHLX")));
            point.setKsYXDX(cursor.getString(cursor
                    .getColumnIndex("KS_YXDX")));
            point.setKsZLDX(cursor.getString(cursor
                    .getColumnIndex("KS_ZLDX")));


            point.setKsCenterCoordX(cursor.getString(cursor
                    .getColumnIndex("KS_CENTER_COOR_X")));
            point.setKsCenterCoordY(cursor.getString(cursor
                    .getColumnIndex("KS_CENTER_COOR_Y")));
            point.setMineType(cursor.getString(cursor
                    .getColumnIndex("KS_KCKZ")));
            point.setKsKCFS(cursor.getString(cursor
                    .getColumnIndex("KS_KCFS")));
            point.setDes(cursor.getString(cursor
                    .getColumnIndex("KS_TBCHECK_RESDESC")));
            point.setKsFieldso(cursor.getString(cursor
                    .getColumnIndex("KS_FIELDS0")));
            point.setKsTBCheckRES(cursor.getString(cursor
                    .getColumnIndex("KS_TBCHECK_RES")));
            point.setState(cursor.getString(cursor
                    .getColumnIndex("CHECK_STATUS")));
            point.setDataType(cursor.getString(cursor
                    .getColumnIndex("DATATYPE")));
            point.setKsTBJT(cursor.getString(cursor
                    .getColumnIndex("KS_TBJT")));

            point.setCheckNo(cursor.getString(cursor
                    .getColumnIndex("CHECK_NO")));
            point.setCheckAdressName(cursor.getString(cursor.
                    getColumnIndex("CHECK_ADRESS_NANE")));
            point.setRemark(cursor.getString(cursor.
                    getColumnIndex("REMARK")));
            point.setShpName(cursor.getString(cursor.
                    getColumnIndex("SHPNAME")));


            pointList.add(point);
        }
        database.close();
        return pointList;


    }

    /**
     * 1
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 查询一条数据
     */
    public List<TaskListEntity> getOnePoints(String id, Context context, String checkNo) {
        String sql = "select * from TB_DATA_CHECK_ITEM where TASK_ID=? and  CHECK_NO=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase database = s.openDatabase(context);
        List<TaskListEntity> pointList = new ArrayList<TaskListEntity>();
        TaskListEntity point = null;
        Cursor cursor = database.rawQuery(sql, new String[]{id, checkNo});
        while (cursor.moveToNext()) {
            point = new TaskListEntity();
            point.setTaskNumber(cursor.getString(cursor
                    .getColumnIndex("TASK_ID")));
            point.setMapNumber(cursor.getString(cursor
                    .getColumnIndex("KS_TBBH")));
            point.setMineNumber(cursor.getString(cursor
                    .getColumnIndex("KS_NAME")));
            point.setDataType(cursor.getString(cursor
                    .getColumnIndex("DATATYPE")));
            point.setXzqName(cursor.getString(cursor
                    .getColumnIndex("XZQNAME")));
            point.setKsDLMC(cursor.getString(cursor
                    .getColumnIndex("KS_DLMC")));
            point.setKsPHLX(cursor.getString(cursor
                    .getColumnIndex("KS_PHLX")));
            point.setKsYXDX(cursor.getString(cursor
                    .getColumnIndex("KS_YXDX")));
            point.setKsZLDX(cursor.getString(cursor
                    .getColumnIndex("KS_ZLDX")));


            point.setKsCenterCoordX(cursor.getString(cursor
                    .getColumnIndex("KS_CENTER_COOR_X")));
            point.setKsCenterCoordY(cursor.getString(cursor
                    .getColumnIndex("KS_CENTER_COOR_Y")));
            point.setMineType(cursor.getString(cursor
                    .getColumnIndex("KS_KCKZ")));
            point.setKsKCFS(cursor.getString(cursor
                    .getColumnIndex("KS_KCFS")));
            point.setDes(cursor.getString(cursor
                    .getColumnIndex("KS_TBCHECK_RESDESC")));
            point.setKsFieldso(cursor.getString(cursor
                    .getColumnIndex("KS_FIELDS0")));
            point.setKsTBCheckRES(cursor.getString(cursor
                    .getColumnIndex("KS_TBCHECK_RES")));
            point.setState(cursor.getString(cursor
                    .getColumnIndex("CHECK_STATUS")));
            point.setDataType(cursor.getString(cursor
                    .getColumnIndex("DATATYPE")));
            point.setKsTBJT(cursor.getString(cursor
                    .getColumnIndex("KS_TBJT")));

            point.setCheckNo(cursor.getString(cursor
                    .getColumnIndex("CHECK_NO")));
            point.setCheckAdressName(cursor.getString(cursor.
                    getColumnIndex("CHECK_ADRESS_NANE")));


            pointList.add(point);
        }
        database.close();
        return pointList;


    }

    /**
     * 1
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 查询该任务下某状态的数据。
     */
    public List<TaskListEntity> getAllState(String id, Context context, String state) {
        String sql = "select * from TB_DATA_CHECK_ITEM where TASK_ID=? and  CHECK_STATUS=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase database = s.openDatabase(context);
        List<TaskListEntity> pointList = new ArrayList<TaskListEntity>();
        TaskListEntity point = null;
        Cursor cursor = database.rawQuery(sql, new String[]{id, state});
        while (cursor.moveToNext()) {
            point = new TaskListEntity();
            point.setTaskNumber(cursor.getString(cursor
                    .getColumnIndex("TASK_ID")));
            point.setMapNumber(cursor.getString(cursor
                    .getColumnIndex("KS_TBBH")));
            point.setMineNumber(cursor.getString(cursor
                    .getColumnIndex("KS_NAME")));
            point.setDataType(cursor.getString(cursor
                    .getColumnIndex("DATATYPE")));
            point.setXzqName(cursor.getString(cursor
                    .getColumnIndex("XZQNAME")));
            point.setKsDLMC(cursor.getString(cursor
                    .getColumnIndex("KS_DLMC")));
            point.setKsPHLX(cursor.getString(cursor
                    .getColumnIndex("KS_PHLX")));
            point.setKsYXDX(cursor.getString(cursor
                    .getColumnIndex("KS_YXDX")));
            point.setKsZLDX(cursor.getString(cursor
                    .getColumnIndex("KS_ZLDX")));


            point.setKsCenterCoordX(cursor.getString(cursor
                    .getColumnIndex("KS_CENTER_COOR_X")));
            point.setKsCenterCoordY(cursor.getString(cursor
                    .getColumnIndex("KS_CENTER_COOR_Y")));
            point.setMineType(cursor.getString(cursor
                    .getColumnIndex("KS_KCKZ")));
            point.setKsKCFS(cursor.getString(cursor
                    .getColumnIndex("KS_KCFS")));
            point.setDes(cursor.getString(cursor
                    .getColumnIndex("KS_TBCHECK_RESDESC")));
            point.setKsFieldso(cursor.getString(cursor
                    .getColumnIndex("KS_FIELDS0")));
            point.setKsTBCheckRES(cursor.getString(cursor
                    .getColumnIndex("KS_TBCHECK_RES")));
            point.setState(cursor.getString(cursor
                    .getColumnIndex("CHECK_STATUS")));
            point.setDataType(cursor.getString(cursor
                    .getColumnIndex("DATATYPE")));
            point.setKsTBJT(cursor.getString(cursor
                    .getColumnIndex("KS_TBJT")));

            point.setCheckNo(cursor.getString(cursor
                    .getColumnIndex("CHECK_NO")));
            point.setCheckAdressName(cursor.getString(cursor.
                    getColumnIndex("CHECK_ADRESS_NANE")));


            pointList.add(point);
        }
        database.close();
        return pointList;


    }

    /**
     * 野外验证任务信息表TB_DATA_CHECK_TASK
     * 查询所有分配任务数据。
     */
    public List<TaskEntity> getAllocated(String taskState, Context context) {
        List<TaskEntity> taskbean;
        String sql = "select * from TB_DATA_CHECK_TASK where TASK_STATUS=?";
        taskbean = new ArrayList<TaskEntity>();
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        TaskEntity task = null;
        Cursor cursor = db.rawQuery(sql, new String[]{taskState});
//        db.execSQL("insert into TB_DATA_CHECK_TASK (TASK_ID,TASK_NAME,TASK_CREATE_DATE,TASK_XZQ,TASK_STATUS) values (?,?,?,?,?)", new String[]{"T010020180327008", "2018呼和浩特市野外验证测试1", "2018-03-27 17:02:40", "0100", "已分配"});
        String state;
        while (cursor.moveToNext()) {
            task = new TaskEntity();
            task.setTaskNumber(cursor.getString(cursor
                    .getColumnIndex("TASK_ID")));
            task.setTaskName(cursor.getString(cursor
                    .getColumnIndex("TASK_NAME")));
            task.setTaskCreateDate(cursor.getString(cursor
                    .getColumnIndex("TASK_CREATE_DATE")));
            task.setTaskXZQ(cursor.getString(cursor
                    .getColumnIndex("TASK_XZQ")));
            task.setTaskContend(cursor.getString(cursor
                    .getColumnIndex("TASK_CONETND")));
            task.setTaskNote(cursor.getString(cursor
                    .getColumnIndex("TASK_NOTE")));
            task.setState(cursor.getString(cursor
                    .getColumnIndex("TASK_STATUS")));
            taskbean.add(task);
        }
        db.close();
        return taskbean;
    }

    /**
     * 野外验证任务信息表TB_DATA_CHECK_TASK
     * 查询所有分配任务数据。
     */
    public List<String> taskDataExportQuery(Context context) {
        List<String> taskbean;
        String sql = "select * from TB_DATA_CHECK_TASK";
        taskbean = new ArrayList<>();
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        String task = null;
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {
            task = cursor.getString(cursor
                    .getColumnIndex("TASK_ID"));
            taskbean.add(task);
        }
        db.close();
        return taskbean;
    }

    /**
     * 野外验证任务信息表TB_DATA_CHECK_TASK
     * 修改某任务状态
     */
    public void updateTastState(String taskId, Context context, String state) {
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL("update TB_DATA_CHECK_TASK set TASK_STATUS=? where TASK_ID=?", new Object[]{state, taskId});
        db.close();
    }

    /**
     * 野外验证任务信息表TB_DATA_CHECK_TASK
     * 返回本期及其历史年份
     */
    public List<String> getYear(String taskNum, Context context) {
        List<String> taskbean;
        String sql = "select * from TB_DATA_CHECK_TASK where TASK_ID=?";
        taskbean = new ArrayList<String>();
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, new String[]{taskNum});
        while (cursor.moveToNext()) {
            thisYear = cursor.getString(cursor
                    .getColumnIndex("TASK_BEGINE_DATE")).substring(0, 4);
            lastYear = cursor.getString(cursor
                    .getColumnIndex("TASK_HISTORY_YEAR"));
            taskbean.add(thisYear);
            if (null != lastYear && !lastYear.equals("null") && !lastYear.equals("")) {
                taskbean.add(lastYear);
            } else {
                taskbean.add(" ");
            }

        }
        return taskbean;
    }

    /**
     * 行政区编码表TB_XZQ_CODE
     * 查询指定行政区编码返回行政区名称。
     */

    public String getXZQName(String xzqCode, Context context) {
        String sql = "select * from TB_XZQ_CODE where PROV_CODE=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, new String[]{xzqCode});
        String xzqName = null;
        while (cursor.moveToNext()) {
            xzqName = cursor.getString(cursor.getColumnIndex("PROV_NAME"));
        }
        db.close();
        return xzqName;
    }

    /**
     * 行政区编码表TB_XZQ_CODE
     * 查询指定行政区名称行政区编码返回。
     */
    public String getQXcode(String name, Context context) {
        String sql = "select * from TB_XZQ_CODE where PROV_NAME=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        String xzqName = null;
        while (cursor.moveToNext()) {
            xzqName = cursor.getString(cursor.getColumnIndex("PROV_CODE"));
        }
        db.close();
        return xzqName;
    }

    /**
     * 地类代码信息表TB_DLDM_INFO
     * 查询所有的验证地点类型。
     */
    public List<String> getDLDX(Context context) {
        String sql = "select * from TB_DLDM_INFO";
        CustomSQLTools s = new CustomSQLTools();
        List<String> dldmbean = new ArrayList<>();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, null);
        String dldx = null;
        while (cursor.moveToNext()) {
            dldx = cursor.getString(cursor.getColumnIndex("DAIMAMINGCHENG"));
            dldmbean.add(dldx);
        }
        db.close();
        return dldmbean;


    }

    /**
     * 地类代码信息表TB_DLDM_INFO
     * 查询所有的验证地点类型。
     */
    public List<String> getDLLX(Context context, String code) {
        String sql = "select * from TB_DICTIONARY_CODE t where t.belongs_id=?";
        CustomSQLTools s = new CustomSQLTools();
        List<String> dldmbean = new ArrayList<>();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, new String[]{code});
        String dldx = null;
        while (cursor.moveToNext()) {
            dldx = cursor.getString(cursor.getColumnIndex("code_name_ch"));
            dldmbean.add(dldx);
        }
        db.close();
        return dldmbean;


    }


    /**
     * 野外验证信息表TB_DATA_CHECK_INFO
     * 查询所有的该任务下图斑详情下图斑观察点的详情
     */
    public List<CheckInfoEntiy> getTBgcd(Context context, String checkNO) {
        List<CheckInfoEntiy> checkbean = new ArrayList<>();
        String sql = "select * from TB_DATA_CHECK_INFO where CHECK_NO=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        CheckInfoEntiy cie = null;
        Cursor cursor = db.rawQuery(sql, new String[]{checkNO});
        while (cursor.moveToNext()) {
            cie = new CheckInfoEntiy();
            cie.setCheckItemId(cursor
                    .getString(cursor.getColumnIndex("CHECK_ITEM_ID")));
            cie.setCheckNO(cursor.getString(cursor
                    .getColumnIndex("CHECK_NO")));
            cie.setCheckDes(cursor.getString(cursor
                    .getColumnIndex("CHECK_DES")));
            cie.setFileNo(cursor.getString(cursor
                    .getColumnIndex("FILE_NO")));
            cie.setFileName(cursor.getString(cursor
                    .getColumnIndex("FILE_NAME")));
            cie.setFilePath(cursor.getString(cursor
                    .getColumnIndex("FILE_PATH")));
            cie.setFileCreateTime(cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_TIME")));
            cie.setFileCreateLocationX(cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_X")));
            cie.setFileCreateLocationY(cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_Y")));
            cie.setSensorDirection(cursor.getString(cursor
                    .getColumnIndex("SENSOR_DIRECTION")));
            cie.setFileType(cursor.getString(cursor
                    .getColumnIndex("FLIE_TYPE")));
            cie.setFileCreateLocationNo(cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_NO")));
            checkbean.add(cie);
        }
        db.close();
        return checkbean;
    }

    /**
     * 野外验证任务信息表TB_DATA_CHECK_INFO
     * 修改某任务经纬度
     */
    public void updateXYNO(String NO, Context context, String X, String Y) {
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL("update TB_DATA_CHECK_INFO set FILE_CREATE_LOCATION_X=? where FILE_CREATE_LOCATION_NO=?", new Object[]{X, NO});
        db.execSQL("update TB_DATA_CHECK_INFO set FILE_CREATE_LOCATION_Y=? where FILE_CREATE_LOCATION_NO=?", new Object[]{Y, NO});
        db.close();
    }

    /**
     * 野外验证信息表TB_DATA_CHECK_INFO
     * 查询所有的该任务下该图斑详情下图斑观察点的详情
     */
    public List<CheckInfoEntiy> getTBgcdAll(Context context) {
        List<CheckInfoEntiy> checkbean = new ArrayList<>();
        String sql = "select * from TB_DATA_CHECK_INFO";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        CheckInfoEntiy cie = null;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            cie = new CheckInfoEntiy();
            cie.setCheckItemId(cursor
                    .getString(cursor.getColumnIndex("CHECK_ITEM_ID")));
            cie.setCheckNO(cursor.getString(cursor
                    .getColumnIndex("CHECK_NO")));
            cie.setCheckDes(cursor.getString(cursor
                    .getColumnIndex("CHECK_DES")));
            cie.setFileNo(cursor.getString(cursor
                    .getColumnIndex("FILE_NO")));
            cie.setFileName(cursor.getString(cursor
                    .getColumnIndex("FILE_NAME")));
            cie.setFilePath(cursor.getString(cursor
                    .getColumnIndex("FILE_PATH")));
            cie.setFileCreateTime(cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_TIME")));
            cie.setFileCreateLocationX(cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_X")));
            cie.setFileCreateLocationY(cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_Y")));
            cie.setSensorDirection(cursor.getString(cursor
                    .getColumnIndex("SENSOR_DIRECTION")));
            cie.setFileType(cursor.getString(cursor
                    .getColumnIndex("FLIE_TYPE")));
            cie.setFileCreateLocationNo(cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_NO")));
            checkbean.add(cie);
        }
        db.close();
        return checkbean;
    }

    /**
     * 野外验证信息表TB_DATA_CHECK_INFO
     * 返回所有不重复的观察点值。
     */
    public List<String> gcdcf(Context context, String checkNO) {
        List<String> checkbean = new ArrayList<>();
        if (checkNO != null) {
            String sql = "select * from TB_DATA_CHECK_INFO where CHECK_NO=?";
            CustomSQLTools s = new CustomSQLTools();
            SQLiteDatabase db = s.openDatabase(context);
            Cursor cursor = db.rawQuery(sql, new String[]{checkNO});
            while (cursor.moveToNext()) {
                String info = cursor.getString(cursor
                        .getColumnIndex("FILE_CREATE_LOCATION_NO"));
                checkbean.add(info);
            }
            Set<String> set = new HashSet<String>(checkbean);
            checkbean.clear();
            checkbean.addAll(set);
            db.close();
        } else {
            Toast.makeText(context, "数据有误", Toast.LENGTH_SHORT).show();
        }

        return checkbean;
    }

    /**
     * 野外验证信息表TB_DATA_CHECK_INFO
     * 删除该观察点所有数据。
     */
    public void deleteGCD(Context context, String gcdId) {
        checkbean = new ArrayList<>();
        String sql = "delete from TB_DATA_CHECK_INFO where FILE_CREATE_LOCATION_NO  like '%" + gcdId + "%'";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL(sql);
        db.close();
    }

    /**
     * 野外验证信息表TB_DATA_CHECK_INFO
     * 删除该图斑所有观察点所有数据。
     */
    public void deleteAllGCD(Context context, String gcdId) {
        checkbean = new ArrayList<>();
        String sql = "delete from TB_DATA_CHECK_INFO where CHECK_NO  =?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL(sql, new String[]{gcdId});
        db.close();
    }

    /**
     * 野外验证信息表TB_DATA_CHECK_INFO
     * 查询该观察点是否存在
     */

    public List<CheckInfoEntiy> thereIsA(Context context, String gcdId) {
        checkbean = new ArrayList<>();
        String sql = "select * from TB_DATA_CHECK_INFO where FILE_CREATE_LOCATION_NO= ?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        CheckInfoEntiy cie = null;
        Cursor cursor = db.rawQuery(sql, new String[]{gcdId});
        while (cursor.moveToNext()) {
            cie = new CheckInfoEntiy();
            cie.setCheckItemId(cursor
                    .getString(cursor.getColumnIndex("CHECK_ITEM_ID")));
            cie.setCheckNO(cursor.getString(cursor
                    .getColumnIndex("CHECK_NO")));
            cie.setCheckDes(cursor.getString(cursor
                    .getColumnIndex("CHECK_DES")));
            cie.setFileNo(cursor.getString(cursor
                    .getColumnIndex("FILE_NO")));
            cie.setFileName(cursor.getString(cursor
                    .getColumnIndex("FILE_NAME")));
            cie.setFilePath(cursor.getString(cursor
                    .getColumnIndex("FILE_PATH")));
            cie.setFileCreateTime(cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_TIME")));
            cie.setFileCreateLocationX(String.valueOf(cursor.getDouble(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_X"))));
            cie.setFileCreateLocationY(String.valueOf(cursor.getDouble(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_Y"))));
            cie.setSensorDirection(cursor.getString(cursor
                    .getColumnIndex("SENSOR_DIRECTION")));
            cie.setFileType(cursor.getString(cursor
                    .getColumnIndex("FLIE_TYPE")));
            cie.setFileCreateLocationNo(cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_NO")));
            checkbean.add(cie);
        }
        Log.d("-------", "thereIsA:返回重复的数据集合 " + checkbean.size());
        db.close();
        return checkbean;
    }

    /**
     * 野外验证信息表TB_DATA_CHECK_INFO
     * 添加一条观察点。
     */
    public void tbgcdAdd(Context context, String checkItemId, String checkNo, String checkDes, String fileNo, String fileName, String filePath, String fileTime, String sensor, String fileType, String gcdNo, String gcdX, String gcdY) {
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL("insert into TB_DATA_CHECK_INFO(CHECK_ITEM_ID,CHECK_NO,CHECK_DES,FILE_NO,FILE_NAME,FILE_PATH,FILE_CREATE_TIME,SENSOR_DIRECTION,FLIE_TYPE,FILE_CREATE_LOCATION_NO,FILE_CREATE_LOCATION_X,FILE_CREATE_LOCATION_Y)values (?,?,?,?,?,?,?,?,?,?,?,?)",
                new String[]{checkItemId, checkNo, checkDes, fileNo, fileName, filePath, fileTime, sensor, fileType, gcdNo, gcdX, gcdY});
        db.close();
    }

    /**
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 通过CHECK_NO
     * 修改字段
     * CHECK_ADRESS_NAME
     * KS_TBCHECK_RES
     * KS_TBCHECK_RESDESC
     * mapDetails.add(adds);//验证地点
     * mapDetails.add(changedEnd);//同解译结果比对
     * mapDetails.add(des);//验证描述
     * mapDetails.add("验证中");//验证状态
     * mapDetails.add(tbksmc);//矿山名称
     * mapDetails.add(tbqxmc);//旗县名称
     * mapDetails.add(tbdllx);//地类类型
     * mapDetails.add(tbphlx);//破坏类型
     * mapDetails.add(tbyxdx);//影响对象
     * mapDetails.add(tbzldx);//治理对象
     * mapDetails.add(tbkckz);//开采矿种
     * mapDetails.add(tbkcfs);//开采方式
     * mapDetails.add(bzNote.getText().toString());//备注
     * if (!tbqxmc.equals("") && !tbqxmc.equals(listbean.getXzqName())) {
     * mapDetails.add("是");
     * } else {
     * mapDetails.add("否");
     * }
     * mapDetails.add(SpHelper.getStringValue("USERNAME"));
     */
    public void update(Context context, String checkNO, String addrs, String results, String des, String state, String tbksmc,
                       String tbqxmc, String tbdllx, String tbphlx, String tbyxdx, String tbzldx, String tbkckz, String tbkcfs,
                       String bzNote, String XZQCHANGE, String xzqnum, String user) {
        String sql = "UPDATE TB_DATA_CHECK_ITEM SET CHECK_ADRESS_NAME = " + addrs + ",KS_TBCHECK_RES = " + results + ",KS_TBCHECK_RESDESC = " + des + ",CHECK_STATUS = " + state + " where = " + checkNO;
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
//        db.execSQL("update TB_DATA_CHECK_ITEM set CHECK_ADRESS_NAME=? KS_TBCHECK_RES=? KS_TBCHECK_RESDESC=? CHECK_STATUS = ? where = ?", new String[]{
//                addrs, results, des, state,checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set CHECK_ADRESS_NANE=? where CHECK_NO=?", new Object[]{addrs, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set KS_TBCHECK_RES=? where CHECK_NO=?", new Object[]{results, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set KS_TBCHECK_RESDESC=? where CHECK_NO=?", new Object[]{des, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set CHECK_STATUS=? where CHECK_NO=?", new Object[]{state, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set KS_NAME=? where CHECK_NO=?", new Object[]{tbksmc, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set XZQNAME=? where CHECK_NO=?", new Object[]{tbqxmc, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set KS_DLMC=? where CHECK_NO=?", new Object[]{tbdllx, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set KS_PHLX=? where CHECK_NO=?", new Object[]{tbphlx, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set KS_YXDX=? where CHECK_NO=?", new Object[]{tbyxdx, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set KS_ZLDX=? where CHECK_NO=?", new Object[]{tbzldx, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set KS_KCKZ=? where CHECK_NO=?", new Object[]{tbkckz, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set KS_KCFS=? where CHECK_NO=?", new Object[]{tbkcfs, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set REMARK=? where CHECK_NO=?", new Object[]{bzNote, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set XZQCHANGE=? where CHECK_NO=?", new Object[]{XZQCHANGE, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set XZQCODE=? where CHECK_NO=?", new Object[]{xzqnum, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set RECORED_USER=? where CHECK_NO=?", new Object[]{user, checkNO});
        db.close();
    }

    /**
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 通过CHECK_NO
     */
    public void update(Context context, String checkNO, String state) {
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL("update TB_DATA_CHECK_ITEM set CHECK_STATUS=? where CHECK_NO=?", new Object[]{state, checkNO});
        db.close();
    }

    /**
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 通过QXMC
     */
    public void updateQXMC(Context context, String checkNO, String qxmc, String xcqCode) {
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL("update TB_DATA_CHECK_ITEM set XZQNAME=? where CHECK_NO=?", new Object[]{qxmc, checkNO});
        db.execSQL("update TB_DATA_CHECK_ITEM set XZQCODE=? where CHECK_NO=?", new Object[]{xcqCode, checkNO});
        db.close();
    }

    /**
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 查询某条任务的状态
     */
    public String queryStatus(Context context, String checkNO) {
        String sql = "select * from TB_DATA_CHECK_ITEM where CHECK_NO= ?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        String tag = "";
        Cursor cursor = db.rawQuery(sql, new String[]{checkNO});
        while (cursor.moveToNext()) {
            String info = cursor.getString(cursor
                    .getColumnIndex("CHECK_STATUS"));
            if (info.equals(Constant.IMPLEMENT)) {
                tag = info;
            } else if (info.equals(Constant.VERIFIED)) {
                tag = info;
            } else if (info.equals(Constant.UNVERIFIED)) {
                tag = info;
            }
        }
        db.close();
        return tag;
    }

    /**
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 通过CHECK_NO
     * 修改字段所有验证中数据为已验证
     * CHECK_ADRESS_NAME
     * KS_TBCHECK_RES
     * KS_TBCHECK_RESDESC
     */
    public void updateEnd(Context context, String taskId, String state) {
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL("update TB_DATA_CHECK_ITEM set CHECK_STATUS=? where TASK_ID=?", new Object[]{state, taskId});
        db.close();
    }

    /**
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 通过CHECK_NO
     * 行政区编号可以从图斑编号中获取：验证图斑任务信息表TB_DATA_CHECK_ITEM的XZQCODE字段获取
     * 获取该任务图斑详情四位行政编码。
     * 生成规则：G+四位行政区编号+三位序号
     */
    public String getTBgcdXZBM(String CHECK_NO, Context context) {
        String sql = "select * from TB_DATA_CHECK_ITEM where CHECK_NO=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, new String[]{CHECK_NO});
        String xzqName = null;
        while (cursor.moveToNext()) {
            xzqName = cursor.getString(cursor.getColumnIndex("XZQCODE"));
        }
        String num = "Y" + xzqName;
        db.close();
        return xzqName;
    }

    /**
     * 验证图斑任务信息表TB_DATA_CHECK_TASK
     * 通过TaskID
     */
    public String getTaskXZQ(String taskID, Context context) {
        String sql = "select * from TB_DATA_CHECK_TASK where TASK_ID=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, new String[]{taskID});
        String xzqName = null;
        while (cursor.moveToNext()) {
            xzqName = cursor.getString(cursor.getColumnIndex("TASK_XZQ"));
        }
        db.close();
        return xzqName;
    }

    /**
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 任务区域设置
     */
    public TaskListEntity taskWork(String taskId, Context context) {
        String sql = "select * from TB_DATA_CHECK_ITEM where TASK_ID=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, new String[]{taskId});
        List<TaskListEntity> pointList = new ArrayList<TaskListEntity>();
        TaskListEntity taskListEntity;
        List<String> list = new ArrayList<>();
        String lat = null;
        String lon = null;
        while (cursor.moveToNext()) {
            taskListEntity = new TaskListEntity();
            lat = cursor.getString(cursor.getColumnIndex("KS_CENTER_COOR_X"));
            lon = cursor.getString(cursor.getColumnIndex("KS_CENTER_COOR_Y"));
            if (!lat.equals("") && !lat.equals(null)) {
                taskListEntity.setKsCenterCoordX(lat);
                taskListEntity.setKsCenterCoordY(lon);
            }
            pointList.add(taskListEntity);
        }
        taskListEntity = pointList.get(0);
        return taskListEntity;
    }

    /**
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 获取观察点编号，截取后三位，进行自增
     */
    public String getgcdLastOne(Context context, String code) {
        List<Integer> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        list1.clear();
        list.clear();
        //获取数据库id最大的一条。
        String sql = "select * from TB_DATA_CHECK_INFO";
        checkbean = new ArrayList<>();
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, null);
        String Orderno = null;
        while (cursor.moveToNext()) {
            String info = cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_NO")).substring(1, 8);
            String maxOrderno = info.substring(0, 4);
            if (maxOrderno.equals(code)) {
                list.add(Integer.valueOf(info));
            }
        }
        Collections.sort(list);
        if (list.size() > 0) {
            int DownA = list.get(list.size() - 1) + 1;
            int mAB = Integer.parseInt(code);
            if (mAB > 1000) {
                Orderno = "Y" + DownA;
            } else {
                Orderno = "Y0" + DownA;
            }

        }
        db.close();
        return Orderno;
    }

    /**
     * 验证图斑任务信息表TB_DATA_CHECK_ITEM
     * 获取编号
     */
    public List<String> getgcdNum(Context context, String code) {
        List<String> list1 = new ArrayList<>();
        list1.clear();
        //获取数据库id最大的一条。
        String sql = "select * from TB_DATA_CHECK_INFO";
        checkbean = new ArrayList<>();
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String info = cursor.getString(cursor
                    .getColumnIndex("FILE_CREATE_LOCATION_NO")).substring(1, 5);
            if (info.equals(code)) {
                list1.add(info);
            }
        }
        db.close();
        return list1;
    }

    public String getBigMap(String taskNum, Context context) {
        String sql = "select * from TB_DATA_CHECK_ITEM where TASK_ID=?";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, new String[]{taskNum});
        int tasknum = 0;
        List<Integer> list = new ArrayList();
        while (cursor.moveToNext()) {
            tasknum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("KS_TBBH")).substring(1));
            list.add(tasknum);
        }
        Collections.sort(list);
        Collections.reverse(list);
        tasknum = list.get(0) + 1;
        String show = "";
        if (tasknum / 100 >= 1) {
            show = "J" + tasknum;
        } else if (tasknum / 10 >= 1) {
            show = "J0" + tasknum;
        } else if (tasknum / 10 < 1) {
            show = "J00" + tasknum;
        }

        db.close();
        return show;
    }

    /**
     * 野外图斑表
     * 添加一条图版信息。
     */
    public void tbAdd(Context context, String checkNo, String taskID, String qxName, String qxCode, String checkStatus, String ksName,
                      String kcfs, String kckz, String dlmc, String phlx, String yxdx, String zldx,
                      String tbnum, String tbType, String tbCheckRes, String cx, String cy, String user, String shpName, String dataType,
                      String tbCheckDes, String note, String isNew, String xzqCheange, String yzdd) {
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL("insert into TB_DATA_CHECK_ITEM" +
                        "(CHECK_NO," +
                        "TASK_ID," +
                        "XZQNAME" +
                        ",XZQCODE" +
                        ",CHECK_STATUS" +
                        ",KS_NAME" +
                        ",KS_KCFS" +
                        ",KS_KCKZ" +
                        ",KS_DLMC" +
                        ",KS_PHLX" +
                        ",KS_YXDX," +
                        "KS_ZLDX" +
                        ",KS_TBBH" +
                        ",KS_FIELDS0" +
                        ",KS_TBCHECK_RES" +
                        ",KS_CENTER_COOR_X" +
                        ",KS_CENTER_COOR_Y" +
                        ",RECORED_USER" +
                        ",SHPNAME" +
                        ",DATATYPE" +
                        ",KS_TBCHECK_RESDESC" +
                        ",REMARK" +
                        ",ISNEW" +
                        ",XZQCHANGE,CHECK_ADRESS_NANE)values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new String[]{checkNo, taskID, qxName, qxCode, checkStatus, ksName, kcfs, kckz, dlmc, phlx, yxdx, zldx, tbnum,
                        tbType, tbCheckRes, cx, cy, user, shpName, dataType, tbCheckDes, note, isNew, xzqCheange, yzdd});
        Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
        db.close();
    }

    public List<String> Txt() {
        //将读出来的一行行数据使用List存储
        String filePath = unifyPath(0) + "VerifyAnalysis.txt";

        List newList = new ArrayList<String>();
        try {
            File file = new File(filePath);
            int count = 0;//初始化 key值
            if (file.isFile() && file.exists()) {//文件存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        String reds = lineTxt.split("\\+")[0];  //java 正则表达式
                        newList.add(count, reds);
                        count++;
                    }
                }
                isr.close();
                br.close();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newList;
    }

    /**
     * 1.1.野外验证轨迹信息表TB_DATA_CHECK_TRACK  *
     * 添加一条轨迹点。
     */
    public void AddTrack(Context context, String trackNo, String checkNo, String devinceNo, String placeX, String placeY, String eventType, String eventTime, String userId, String taskJobid, String locationType) {
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL("insert into TB_DATA_CHECK_TRACK(TRACK_NO,CHECK_NO,DEVINCE_NO,X,Y,EVENT_TYPE,EVENT_TIME,USER_NAME,TASK_JOB_ID,LOCATION_TYPE)values (?,?,?,?,?,?,?,?,?,?)",
                new String[]{trackNo, checkNo, devinceNo, placeX, placeY, eventType, eventTime, userId, taskJobid, locationType,});
        db.close();
    }

    //生成唯一标识
    public String getUUID() {
        uuid = UUID.randomUUID();
        String uniqueId = String.valueOf(uuid);
        return uniqueId;
    }//获取当前时间

    public String getCurrTime() {
        long m_lstartTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(m_lstartTime);
        return dateString;
    }

    public List<String> getXY(String x, String y) {
        List<String> xy = new ArrayList<>();
        xy.clear();
        BigDecimal bg = new BigDecimal(x);
        BigDecimal bg1 = new BigDecimal(y);
        String gcdx1 = String.valueOf(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        String gcdy1 = String.valueOf(bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        xy.add(gcdx1);
        xy.add(gcdy1);
        return xy;
    }

    /**
     * 查询所有的轨迹点TB_DATA_CHECK_TRACK
     */
    public List<LatLng> getAllTrackPoint(Context context) {
        List<LatLng> points = new ArrayList<LatLng>();
        String sql = "select * from TB_DATA_CHECK_TRACK";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            double x = Double.parseDouble(cursor.getString(cursor.getColumnIndex("X")));
            double y = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Y")));
            points.add(new LatLng(x, y));
        }
        db.close();
        return points;
    }

    /**
     * 查询所有的轨迹点TB_DATA_CHECK_TRACK
     */
    public List<CheckTrackEntiy> getAllTrackInfo(Context context) {
        List<CheckTrackEntiy> checkTrackEntiyList = new ArrayList<>();
        CheckTrackEntiy checkInfoEntiy;
        String sql = "select * from TB_DATA_CHECK_TRACK";
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            checkInfoEntiy = new CheckTrackEntiy();
            checkInfoEntiy.setPlaceX(String.valueOf(cursor.getString(cursor.getColumnIndex("X"))));
            checkInfoEntiy.setPlaceY(String.valueOf(cursor.getString(cursor.getColumnIndex("Y"))));
            checkInfoEntiy.setEventTime(cursor.getString(cursor.getColumnIndex("EVENT_TIME")));
            checkInfoEntiy.setEventType(cursor.getString(cursor.getColumnIndex("EVENT_TYPE")));
            if (!checkInfoEntiy.getPlaceX().equals("0") || !checkInfoEntiy.getPlaceY().equals("0")) {
                checkTrackEntiyList.add(checkInfoEntiy);
            }
        }
        db.close();
        return checkTrackEntiyList;
    }

    /**
     * 修改观察点图片描述
     */
    public void updatePicContent(Context context, String taskId, String des) {
        CustomSQLTools s = new CustomSQLTools();
        SQLiteDatabase db = s.openDatabase(context);
        db.execSQL("update TB_DATA_CHECK_INFO set CHECK_DES=? where CHECK_ITEM_ID=?", new Object[]{des, taskId});
        db.close();
    }

    public int copy(String fromFile, String toFile) {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!root.exists()) {
            return -1;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();

        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            } else//如果当前项为文件则进行文件拷贝
            {
                CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return 0;
    }

    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public int CopySdcardFile(String fromFile, String toFile) {

        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            return -1;
        }
    }

    private final static String FileName = "nmmvsqlite.db";

    public boolean copyFile(File src, String destPath) {
        boolean result = false;
        if ((src == null) || (destPath == null)) {
            return result;
        }
        File dest = new File(destPath + FileName);
        if (dest != null && dest.exists()) {
            dest.delete(); // delete file
        }
        try {
            dest.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileChannel srcChannel = null;
        FileChannel dstChannel = null;

        try {
            srcChannel = new FileInputStream(src).getChannel();
            dstChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        try {
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getFileName() {
        String path = export + "data/";        //要遍历的路径
        File file = new File(path);        //获取其file对象
        File[] fs = file.listFiles();    //遍历path下的文件和目录，放在File数组中
        fileNameList = new ArrayList<>();
        for (File f : fs) {                    //遍历File[]数组
            if (f.isDirectory()) {       //若目录(即文件夹)，则打印
                fileNameList.add(String.valueOf(f));
            }

        }
        return fileNameList;
    }
}
