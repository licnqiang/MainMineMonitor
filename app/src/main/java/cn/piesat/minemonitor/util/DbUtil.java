package cn.piesat.minemonitor.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.piesat.minemonitor.entitys.RegisteredEntity;

/**
 * 作者：wangyi
 * <p>
 * 邮箱：wangyi@piesat.cn
 */
public class DbUtil {
    private static final String DATEBASE_NAME = "nmStore_db";
    private static final int DATABASE_VERSION = 1;
    public static String RegisteredTable = "RegisteredTable";//登陆注册表
    private static DbUtil dbUtil;
    private SQLiteDatabase database;
    private MySQLiteHelper mySQLiteHelper;

    public DbUtil(){}

    public static synchronized DbUtil getInstance(){
        if(dbUtil == null){
            dbUtil = new DbUtil();
        }
        return dbUtil;
    }

    //初始化数据库
    public synchronized void openDb(Context context) throws Exception {
        mySQLiteHelper = new MySQLiteHelper(context);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mySQLiteHelper.setWriteAheadLoggingEnabled(true);
        }
        database = mySQLiteHelper.getReadableDatabase();
    }



    public class MySQLiteHelper extends SQLiteOpenHelper {

        public MySQLiteHelper (Context context) {
            super(context, DATEBASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+ RegisteredTable+"(id integer primary key autoincrement,name text,password varchar)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    /**
     * 从数据库读取User信息。
     */
   /* public List<RegisteredEntity> loadUser() {
        List<RegisteredEntity> list = new ArrayList<>();
        Cursor cursor = database
                .query(RegisteredTable, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                RegisteredEntity registeredEntity = new RegisteredEntity();
                registeredEntity.setId(cursor.getInt(cursor.getColumnIndex("id")));
                registeredEntity.setName(cursor.getString(cursor
                        .getColumnIndex("name")));
                registeredEntity.setPassword(cursor.getString(cursor

                        .getColumnIndex("password")));
                list.add(registeredEntity);
            } while (cursor.moveToNext());
        }
        return list;
    }*/


    public int Quer(String pwd,String name)
    {
        Cursor cursor =database.rawQuery("select * from " +RegisteredTable +" where name=?", new String[]{name});

        if (cursor.getCount()>0)
        {
            Cursor pwdcursor =database.rawQuery("select * from "+RegisteredTable+  " where password=? and name=?",new String[]{pwd,name});
            if (pwdcursor.getCount()>0)
            {
                return 1;
            }
            else {
                return -1;
            }
        }
        else {
            return 0;
        }


    }
    /**
     * 将User实例存储到数据库。
     */
   /* public int  saveUser(RegisteredEntity user) {
        if (user != null) {
            Cursor cursor = database.rawQuery("select * from "+RegisteredTable+" where name=?", new String[]{user.getName().toString()});
            if (cursor.getCount() > 0) {
                return -1;
            } else {
                try {
                    database.execSQL("insert into "+RegisteredTable+"(name,password) values(?,?) ", new String[]{user.getName().toString(), user.getPassword().toString()});
                } catch (Exception e) {
                }
                return 1;
            }
        }
        else {
            return 0;
        }
    }
*/
}
