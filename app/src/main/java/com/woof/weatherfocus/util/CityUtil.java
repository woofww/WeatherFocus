package com.woof.weatherfocus.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.woof.weatherfocus.model.entity.City;
import com.woof.weatherfocus.model.entity.Province;
import com.woof.weatherfocus.model.entity.Zone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Woof on 3/1/2017.
 */

public class CityUtil {

    public static List<Province> loadProvince(SQLiteDatabase db) {
        List<Province> list = new ArrayList<>();

        // 通过Cursor对Province进行查找, 填入对应的table name, 在china_city对应存在三个表,
        // 此处应填如T_Province
        Cursor cursor = db.query("T_Province", null, null, null, null, null, null);

        // 查找部分
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.ProSort = cursor.getInt(cursor.getColumnIndex("ProSort"));
                province.ProName = cursor.getString(cursor.getColumnIndex("ProName"));
                //将Province对应表进行遍历，存入到list当中
                list.add(province);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static List<City> loadCity(SQLiteDatabase db, int ProId) {
        List<City> list = new ArrayList<>();

        Cursor cursor = db.query("T_City", null, "ProId = ?", new String[] {String.valueOf(ProId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.CitySort = cursor.getInt(cursor.getColumnIndex("CitySort"));
                city.ProID = ProId;
                city.CityName = cursor.getString(cursor.getColumnIndex("CityName"));
                list.add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static List<Zone> loadZone(SQLiteDatabase db, int CityId) {
        List<Zone> list = new ArrayList<>();

        Cursor cursor = db.query("T_Zone", null, "CityID = ?", new String[] {String.valueOf(CityId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Zone zone = new Zone();
                zone.ZoneID = cursor.getInt(cursor.getColumnIndex("ZoneID"));
                zone.CityID = CityId;
                zone.ZoneName = cursor.getString(cursor.getColumnIndex("ZoneName"));
                list.add(zone);
            } while (cursor.moveToNext());
        }
        return list;
    }
}

