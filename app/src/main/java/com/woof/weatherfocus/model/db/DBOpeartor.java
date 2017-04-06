package com.woof.weatherfocus.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Woof on 2/28/2017.
 */

public class DBOpeartor extends SQLiteOpenHelper{

    private Context mContext;
    private static final String CREATE_MULTYCITY = "create table if not exists MultiCity("
            + "id integer primary key autoincrement,"
            + "city_name text)";
    public DBOpeartor(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MULTYCITY);
        Toast.makeText(mContext, "数据库创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
