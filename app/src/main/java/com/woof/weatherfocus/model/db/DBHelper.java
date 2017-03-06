package com.woof.weatherfocus.model.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.woof.weatherfocus.R;
import com.woof.weatherfocus.base.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 获取数据库，如果data/data/packagename/ 如对应位置不存在，则将raw位置数据库导入到目录当中
 * Created by Woof on 2/27/2017.
 */

public class DBHelper {

    private static final String DB_NAME = "china_city.db"; //应用内用于城市查询的数据库
    static final String PACKAGE_NAME = "com.woof.weatherfocus";
    static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" +
            PACKAGE_NAME; //该部分设置城市数据库的存放位置
    private SQLiteDatabase mSQLiteDatabase;

    private DBHelper(){} //阻止外部进行直接实例化，仅能通过类内部访问，维护访问的安全性

    public static DBHelper getInstance(){return DBHelperHolder.sInstance;}

    private static class DBHelperHolder {
        /**
         * 该部分用于获取帮助类的实例，进行操作
         */
        static final DBHelper sInstance = new DBHelper();
    }

    public SQLiteDatabase getDataBase(){return mSQLiteDatabase;}

    public void openDataBase() throws FileNotFoundException {
        this.mSQLiteDatabase = this.openDataBase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDataBase(String dbFile) {
        /**
         * 该方法用于判断对应位置是否存在数据库，如果不存在则通过raw文件夹进行导入，如果需要数据库随安装包安装到
         * 设备道中，应该将db文件置于raw文件夹当中
         */
        try{
            if (!(new File(dbFile).exists())){
                InputStream is = BaseApplication.getAppContext().getResources().openRawResource(R.raw.china_city);//不存在直接导入
                FileOutputStream fos = new FileOutputStream(dbFile);
                int BUFFER_SIZE = 500000;
                byte[] buffer = new byte[BUFFER_SIZE];
                int count;
                while((count = is.read(buffer)) > 0){
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            return SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void dataBaseClose(){
        if (this.mSQLiteDatabase != null){
            this.mSQLiteDatabase.close();
        }
    }
}
